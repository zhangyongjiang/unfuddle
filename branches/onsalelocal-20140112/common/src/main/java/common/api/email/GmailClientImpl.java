package common.api.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.mutable.MutableInt;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.mail.pop3.POP3SSLStore;

@Service("mailClient")
public class GmailClientImpl implements MailClient {
    private static final Logger logger = LoggerFactory.getLogger(GmailClientImpl.class);

    @Value ("${mail.smtpHost}") 		private String smtpHost = "smtp.gmail.com";
    @Value ("${mail.pop3Host}") 		private String pop3Host = "pop.gmail.com";
    @Value ("${mail.inboxUserName}") 	private String inboxUserName = "kevin@onsalelocal.com";
    @Value ("${mail.inboxPassword}") 	private String inboxPassword = "r3dmine!";
    @Value ("${mail.outboxUserName}") 	private String outboxUserName = "kevin@onsalelocal.com";
    @Value ("${mail.outboxPassword}") 	private String outboxPassword = "r3dmine!";
    @Value ("${mail.defaultFrom}") 		private String defaultFrom = "kevin@onsalelocal.com";
    @Value ("${mail.smtp.port}") 		private int smtpport = 587;
    @Value ("${mail.pop3.port}") 		private int popport = 995;

    public boolean send(MailMessage mail) {
        boolean sent = false;
        try {
            String from = mail.getFrom();
            if ((from == null) || (from.length() == 0)) {
                from = defaultFrom;
            }
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", getSmtpHost());
            properties.setProperty("mail.smtp.submitter", inboxUserName);
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.debug", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.port", smtpport + "");
            Session session = Session.getInstance(properties, new Authenticator(outboxUserName, outboxPassword));

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            for (String to : mail.getTo()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }

            message.setSubject(mail.getSubject());

            if (mail.isHtml()) {
                message.setContent(mail.getContent(), "text/html");
            } else {
                message.setContent(mail.getContent(), "text/plain");
            }

            // Send message
            Transport.send(message);

            sent = true;
        } catch (Exception e) {
            logger.error("can't send mail - " + e.getMessage());
        }

        return sent;
    }

    private class Authenticator extends javax.mail.Authenticator {
        private final PasswordAuthentication authentication;

        public Authenticator(String userName, String password) {
            authentication = new PasswordAuthentication(userName, password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }

    public void setDefaultFrom(String defaultFrom) {
        this.defaultFrom = defaultFrom;
    }

    public String getDefaultFrom() {
        return defaultFrom;
    }

    public String getUserName() {
        return inboxUserName;
    }

    public void setUserName(String userName) {
        this.inboxUserName = userName;
    }

    public String getPassword() {
        return inboxPassword;
    }

    public void setPassword(String password) {
        this.inboxPassword = password;
    }

    private String getPlainText(Message msg) throws IOException, MessagingException {
        Object content = msg.getContent();
        if (content instanceof MimeMultipart) {
            return getPlainText((MimeMultipart) content);
        } else {
            return content.toString();
        }

    }

    private String getPlainText(MimeMultipart mmp) throws IOException, MessagingException {
        StringBuilder sb = new StringBuilder();
        getPlainText(sb, mmp);
        return sb.toString();
    }

    private void getPlainText(StringBuilder sb, MimeMultipart mmp) throws IOException, MessagingException {
        for (int i = 0; i < mmp.getCount(); i++) {
            BodyPart body = mmp.getBodyPart(i);
            String contentType = body.getContentType();
            if (contentType.indexOf("multipart") != -1) {
                getPlainText(sb, (MimeMultipart) body.getContent());
            }
//            if (contentType.indexOf("text/plain") != -1) 
            else {
                if (sb.length() > 0) {
                    sb.append("\n------------- multipart -------------\n");
                }
                sb.append(body.getContent().toString());
            } 
        }
    }

    private MailMessage buildMailMessage(Message msg) throws MessagingException, IOException {
        MailMessage mm = new MailMessage();
        InternetAddress addr;
        try {
	        addr = (InternetAddress) msg.getFrom()[0];
	        mm.setFrom(addr.getAddress());
	        mm.setUserName(addr.getPersonal());
        } catch (Exception e) {
	        e.printStackTrace();
        }
        try {
	        InternetAddress to = (InternetAddress) msg.getRecipients(RecipientType.TO)[0];
	        mm.setToString(to.getAddress());
        } catch (Exception e) {
	        e.printStackTrace();
        }
        mm.setSubject(msg.getSubject());
        mm.setContent(getPlainText(msg));
        mm.setHtml(mm.getContent().contains("html") || mm.getContent().contains("div"));
        return mm;
    }
    
    @Override
    public ArrayList<MailMessage> read(boolean delete) {
        ArrayList<MailMessage> mailList = new ArrayList<MailMessage>();
		String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

		Properties pop3Props = new Properties();
        
		pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
		pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
		pop3Props.setProperty("mail.pop3.port", String.valueOf(popport));
		pop3Props.setProperty("mail.pop3.socketFactory.port", String.valueOf(popport));
		
		URLName url = new URLName("pop3", pop3Host, popport, "", inboxUserName, inboxPassword);

		Session session = Session.getInstance(pop3Props, null);
		Store store = new POP3SSLStore(session, url);
		
        try {
    		store.connect();
    		Folder folder = store.getDefaultFolder();
    		folder = folder.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
            Message[] messageList = folder.getMessages();

    		FetchProfile fp = new FetchProfile();
    		fp.add(FetchProfile.Item.ENVELOPE);
    		folder.fetch(messageList, fp);
            
            for (Message msg : messageList) {
                MailMessage mm = buildMailMessage(msg);
                mailList.add(mm);
                if(delete) {
                	msg.setFlag(Flag.DELETED, true);
                }

                logger.trace("email received from " + mm.getFrom() + " - " + mm.getSubject() + " -- to: "
                        + mm.getToString());
            }
            folder.close(true);
            store.close();
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("cannot read email. " + e.getMessage());
        }
        return mailList;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setPop3Host(String pop3Host) {
        this.pop3Host = pop3Host;
    }

    public String getPop3Host() {
        return pop3Host;
    }

    public static void main(String[] args) throws Exception {
        GmailClientImpl client = new GmailClientImpl();
//        ArrayList<MailMessage> msgList = client.read();
//        for (MailMessage msg : msgList) {
//            System.out.println(msg.getUserName());
//            System.out.println(msg.getFrom());
//            System.out.println(msg.getSubject());
//            System.out.println(msg.getContent());
//        }
        MailMessage mm = new MailMessage("kevin@raved.com", "test msg from msg server", "hello world");
        client.send(mm);
        Thread.sleep(30000);
        
//        ArrayList<MailMessage> read = client.read(false);
//        for(MailMessage mm : read) {
//        	System.out.println("<html><body>");
//        	int pos = mm.getContent().toLowerCase().indexOf("<html");
//        	int pos1 = mm.getContent().toLowerCase().indexOf("</html>");
//        	String html = mm.getContent().substring(pos==-1? 0 : pos, pos1==-1? mm.getContent().length() : pos1);
//            DOMParser parser = new DOMParser();
//            parser.setFeature("http://xml.org/sax/features/namespaces", false);
//            parser.parse(new InputSource(new StringReader(html)));
//            Document document = parser.getDocument();
//            DOMReader reader = new DOMReader();
//            org.dom4j.Document doc = reader.read(document);
//            List<Element> elements = CrawlerBase.selectElements(doc, "IMG");
//            List<ImgElement> imgs = new ArrayList<ImgElement>(); 
//            Set<String> set = new HashSet<String>();
//            for(Element ele : elements) {
//            	String src = CrawlerBase.getImg(ele);
//            	if(src==null || src.length()==0 || !src.startsWith("http") || set.contains(src))
//            		continue;
//            	set.add(src);
//            	BufferedImage bi = ImageIO.read(new URL(src));
//            	ImgElement ie = new ImgElement();
//            	ie.elem = ele;
//            	ie.src = src;
//            	ie.w = bi.getWidth();
//            	ie.h = bi.getHeight();
//            	imgs.add(ie);
//            }
//            
//            List<ImgElement> main = new ArrayList<ImgElement>();
//            Collections.sort(imgs);
//            if(imgs.size() > 1) {
//            	ImgElement ie = imgs.get(0);
//    			int size = ie.w * ie.h;
//    			main.add(ie);
//    			for(int i=1; i<imgs.size(); i++) {
//    	        	ImgElement ie1 = imgs.get(i);
//    				int s1 = ie1.w * ie1.h;
//    				if(size > s1 * 4) {
//    					break;
//    				}
//    				main.add(ie1);
//    				size = s1;
//    			}
//            }
//            
//            Map<Element, MutableInt> parents = new HashMap<Element, MutableInt>();
//            for(ImgElement ie : main) {
//            	markParents(parents, ie.elem);
//            }
//            
//            int max = 0;
//            List<Element> top = new ArrayList<Element>();
//            for(Entry<Element, MutableInt> entry : parents.entrySet()) {
//            	MutableInt m = entry.getValue();
//            	if(m.intValue() > max) {
//            		max = m.intValue();
//            		top.clear();
//            		top.add(entry.getKey());
//            	}
//            	else if (m.intValue() == max) {
//            		top.add(entry.getKey());
//            	}
//            }
//            
//            Collections.sort(top, new Comparator<Element>() {
//    			@Override
//                public int compare(Element arg0, Element arg1) {
//    	            return isParentOf(arg0, arg1) ? 1 : -1;
//                }
//    		});
//        	
//            for(Element ele : top) {
//            	System.out.println(ele.asXML());
//            	break;
//            }
//        	System.out.println("</body></html>");
//        }
    }

	private static boolean isParentOf(Element e0, Element e1) {
		if(e1 == null) return false;
		if(e0.equals(e1.getParent()))
			return true;
		return isParentOf(e0, e1.getParent());
	}
	
	private static void markParents(Map<Element, MutableInt> parents, Element ele) {
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
}
