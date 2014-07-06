package com.gaoshin.onsalelocal.osl.service.impl;

import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;
import org.cyberneko.html.parsers.DOMParser;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.entity.EmailOffer;
import com.gaoshin.onsalelocal.osl.service.EmailService;
import common.api.email.MailClient;
import common.api.email.MailMessage;
import common.crawler.CrawlerBase;

@Service("emailService")
@Transactional(readOnly=true)
public class EmailServiceImpl extends ServiceBaseImpl implements EmailService {
    static final Logger logger = Logger.getLogger(EmailServiceImpl.class);
    
	@Autowired private OslDao oslDao;
	@Autowired private MailClient mailClient;
	
	private boolean running = false;
	private int threshold = 4;
	
	@Override
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
    public synchronized void process(boolean delete, int threshold) {
		this.threshold = threshold;
		if(running) return;
		running = true;
		try {
	        processMail(delete);
        } finally {
        	running = false;
        }
    }

	private void processMail(boolean delete) {
		ArrayList<MailMessage> msgs = mailClient.read(delete);
		for(MailMessage mm : msgs) {
			try {
	            processMail(mm);
            } catch (Exception e) {
	            e.printStackTrace();
            }
		}
    }

	private void processMail(MailMessage mm) throws Exception {
		EmailOffer eo = new EmailOffer();
		eo.setContent(mm.getContent());
		eo.setSender(mm.getFrom());
		StringBuilder sb = new StringBuilder();
		for(String to : mm.getTo()) {
			sb.append(to).append(";");
		}
		if(sb.length()>0) {
			eo.setReceivers(sb.substring(0, sb.length()-1));
		}
		eo.setSubject(mm.getSubject());
		try {
	        String main = getMainContent(mm.getContent());
	        eo.setOffer(main);
        } catch (Exception e) {
	        e.printStackTrace();
        }
		
		oslDao.insert(eo);
    }
	
	private String getMainContent(String content) throws Exception {
    	String lowerCase = content.toLowerCase();
		int pos = lowerCase.indexOf("<html");
    	int pos1 = lowerCase.indexOf("</html>");
    	String html = content.substring(pos==-1? 0 : pos, pos1==-1? content.length() : pos1);
        DOMParser parser = new DOMParser();
        parser.setFeature("http://xml.org/sax/features/namespaces", false);
        parser.parse(new InputSource(new StringReader(html)));
        Document document = parser.getDocument();
        DOMReader reader = new DOMReader();
        org.dom4j.Document doc = reader.read(document);
        List<Element> elements = CrawlerBase.selectElements(doc, "IMG");
        List<ImgElement> imgs = new ArrayList<ImgElement>(); 
        Set<String> set = new HashSet<String>();
        for(Element ele : elements) {
        	String src = CrawlerBase.getImg(ele);
        	if(src==null || src.length()==0 || !src.startsWith("http") || set.contains(src))
        		continue;
        	set.add(src);
        	BufferedImage bi = ImageIO.read(new URL(src));
        	ImgElement ie = new ImgElement();
        	ie.elem = ele;
        	ie.src = src;
        	ie.w = bi.getWidth();
        	ie.h = bi.getHeight();
        	imgs.add(ie);
        }
        
        List<ImgElement> main = new ArrayList<ImgElement>();
        Collections.sort(imgs);
        if(imgs.size() > 1) {
        	ImgElement ie = imgs.get(0);
			int size = ie.w * ie.h;
			main.add(ie);
			for(int i=1; i<imgs.size(); i++) {
	        	ImgElement ie1 = imgs.get(i);
				int s1 = ie1.w * ie1.h;
				if(size > s1 * getThreshold()) {
					break;
				}
				main.add(ie1);
				size = s1;
			}
        }
        
        Map<Element, MutableInt> parents = new HashMap<Element, MutableInt>();
        for(ImgElement ie : main) {
        	markParents(parents, ie.elem);
        }
        
        int max = 0;
        List<Element> top = new ArrayList<Element>();
        for(Entry<Element, MutableInt> entry : parents.entrySet()) {
        	MutableInt m = entry.getValue();
        	if(m.intValue() > max) {
        		max = m.intValue();
        		top.clear();
        		top.add(entry.getKey());
        	}
        	else if (m.intValue() == max) {
        		top.add(entry.getKey());
        	}
        }
        
        Collections.sort(top, new Comparator<Element>() {
			@Override
            public int compare(Element arg0, Element arg1) {
	            return isParentOf(arg0, arg1) ? 1 : -1;
            }
		});
        
        if(top.size() == 0)
        	return null;
        
        Element mainContent = top.get(0);
        StringBuilder sb = new StringBuilder();
        StringBuilder match = new StringBuilder();
        if("TD".equals(mainContent.getName())) {
            sb.append("<table><tr>");
            match.append("</tr></table>");
        }
        if("TR".equals(mainContent.getName())) {
            sb.append("<table>");
            match.append("</table>");
        }
        sb.append(mainContent.asXML());
        sb.append(match.toString());
        
        return sb.toString();
    }

	private boolean isParentOf(Element e0, Element e1) {
		if(e1 == null) return false;
		if(e0.equals(e1.getParent()))
			return true;
		return isParentOf(e0, e1.getParent());
	}
	
	private void markParents(Map<Element, MutableInt> parents, Element ele) {
		Element parent = ele.getParent();
		if(parent == null)
			return;
		MutableInt cnt = parents.get(parent);
		if(cnt == null) {
			cnt = new MutableInt(0);
			parents.put(parent, cnt);
		}
		cnt.increment();
		markParents(parents, parent);
	}

	@Override
    public List<EmailOffer> list(int offset, int size) {
		Map params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("size", size);
		String sql = "select * from EmailOffer order by updated desc limit :offset, :size";
		return oslDao.queryBySql(EmailOffer.class, params , sql );
    }

	@Override
    public EmailOffer getEmailOfferDetails(String id) {
	    return oslDao.getEntity(EmailOffer.class, id);
    }

	public int getThreshold() {
	    return threshold;
    }

	public void setThreshold(int threshold) {
	    this.threshold = threshold;
    }
}
