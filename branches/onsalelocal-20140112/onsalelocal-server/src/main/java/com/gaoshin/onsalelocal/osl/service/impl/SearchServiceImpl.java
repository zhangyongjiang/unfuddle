package com.gaoshin.onsalelocal.osl.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.osl.beans.OfferSearchRequest;
import com.gaoshin.onsalelocal.osl.beans.OfferSearchResponse;
import com.gaoshin.onsalelocal.osl.beans.SearchOrder;
import com.gaoshin.onsalelocal.osl.dao.OslDao;
import com.gaoshin.onsalelocal.osl.service.SearchService;
import common.db.dao.City;
import common.db.dao.GeoDao;
import common.geo.Geocode;

@Service("searchService")
public class SearchServiceImpl implements SearchService {
    static final Logger logger = Logger.getLogger(SearchServiceImpl.class);

    @Autowired @Qualifier("offerSearchServer")
    private SolrServer offerSearchServer;

    @Autowired @Qualifier("storeSearchServer")
    private CommonsHttpSolrServer storeSearchServer;

    @Autowired
    private GeoDao geoDao;
    
    @Autowired
    private OslDao oslDao;
    
    @Override
    public OfferSearchResponse search(final OfferSearchRequest req) throws Exception {
        long queryBuildTime = System.currentTimeMillis();
        SolrQuery query = new SolrQuery();
        query.setStart(req.getOffset());
        query.setRows(req.getSize());
        query.setTimeAllowed(1000);
        
        StringBuilder sb = new StringBuilder();
        boolean hasFilter = false;
        
        if(req.getMerchant() != null) {
        	String[] merchants = req.getMerchant().split(";");
        	sb.append((!hasFilter) ? " " : " AND ").append(getKeyValue("merchant", exactMatch(merchants)));
        	hasFilter = true;
        }
        
        if(req.getSubcategory() != null) {
        	sb.append((!hasFilter) ? " " : " AND ").append(getKeyValue("subcategory", exactMatch(req.getSubcategory())));
        	hasFilter = true;
        }
        
        if(req.getKeywords() != null && req.getKeywords().trim().length() > 0) {
            String keywords = req.getKeywords().replaceAll("[^a-zA-Z0-9 ]+", " ");
            sb.append((!hasFilter) ? " " : " AND ").append(getKeyValue("text", keywords));
            hasFilter = true;
        }

        if(req.getCategory() != null && req.getCategory().trim().length() > 0) {
        	String[] categories = req.getCategory().split(";");
            sb.append((!hasFilter) ? " " : " AND ").append(getKeyValue("category", exactMatch(categories)));
            hasFilter = true;
        }

        if(req.getSource() != null) {
            sb.append((!hasFilter) ? " " : " AND ").append(getKeyValue("source", req.getSource()));
        	hasFilter = true;
        }
        else {
        	if(req.getLocalService() != null) {
        		if(req.getLocalService()) {
                    sb.append((!hasFilter) ? " " : " AND ").append(getKeyValue("source", "yipit"));
                	hasFilter = true;
        		}
        		else {
                    sb.append((!hasFilter) ? " !" : " AND !").append(getKeyValue("source", "yipit"));
                	hasFilter = true;
        		}
        	}
        }
        
        if(!hasFilter){
            sb.append(getAllKeyValue("*"));
        }
        
        if(req.getGroup() != null) {
        	query.set(GroupParams.GROUP, true);
        	query.set(GroupParams.GROUP_FIELD, req.getGroup());
        	query.set(GroupParams.GROUP_LIMIT, req.getGroupLimit());
        	query.set(GroupParams.GROUP_MAIN, true);
        }
        
        if(req.getLat() != null && req.getLng() != null) {
	        query.addFilterQuery("{!geofilt}"); 
	        query.set("sfield", ("location")); 
	        query.set("pt", req.getLat()+","+req.getLng());
	        query.set("d", String.valueOf(req.getRadius()*1.6));
        }
        
    	if(SearchOrder.DistanceAsc.equals(req.getOrder())) {
        	if(req.getLat() != null) {
        		query.set("sort", "geodist() asc");
        	}
    	}
    	else if(SearchOrder.DistanceDesc.equals(req.getOrder())) {
        	if(req.getLat() != null) {
        		query.set("sort", "geodist() desc");
        	}
    	}
    	else if(SearchOrder.UpdatedDesc.equals(req.getOrder())) {
    		query.set("sort", "updated desc");
    	}
    	else if(SearchOrder.UpdatedAsc.equals(req.getOrder())) {
    		query.set("sort", "updated asc");
    	}
    	else if(req.getOrder() == null) {
	        if(req.getKeywords() == null){
	        	if(req.getLat() != null) {
	        		query.set("sort", "geodist() asc");
	        	}
	        }else{
	            query.set("sort", "score desc"); 
	        }
    	}
    	
    	if(req.getExclude() != null) {
    		sb.append(" -(id:").append(req.getExclude()).append(")");
    	}
        
    	if(req.getNostore() != null) {
    		sb.append(" -(merchant:").append(exactMatch(req.getNostore())).append(")");
    	}
        
        String solrQuery = sb.toString();
        query.setQuery(solrQuery);
        query.setFields("score, *");
        String qstr = query.toString();
		logger.info(qstr);
        System.out.println("Query: " + qstr);
        QueryResponse resp = offerSearchServer.query(query);
        logger.info("search done");
        OfferSearchResponse list = new OfferSearchResponse();
        SolrDocumentList result = resp.getResults();
        list.setOffset(result.getStart());
        list.setTotal(result.getNumFound());
        list.setSize(result.size());
        
        boolean hasSlocalOffer = false;
        if(req.getSource()!=null && !com.gaoshin.onsalelocal.api.DataSource.Shoplocal.getValue().equals(req.getSource())) {
        	hasSlocalOffer = true;
        }
        
        for(SolrDocument doc : result) {
        	Float score = (Float) doc.getFieldValue("score");
        	if(score<0.88f) break;
            OfferDetails offer = new OfferDetails();
            list.getItems().add(offer);
            offer.setAddress((String)doc.getFieldValue("address"));
            offer.setTags(getTagsFromSolrDoc(doc));
            offer.setCity((String)doc.getFieldValue("city"));
            offer.setState((String)doc.getFieldValue("state"));
            offer.setEnd((Long)doc.getFieldValue("end"));
            offer.setDiscount((String)doc.getFieldValue("discount"));
            offer.setPrice((String)doc.getFieldValue("price"));
            offer.setHighlights((String)doc.getFieldValue("highlights"));
            offer.setId((String)doc.getFieldValue("id"));
            offer.setMerchant((String)doc.getFieldValue("merchant"));
            offer.setSource((String)doc.getFieldValue("source"));
            offer.setRootSource((String)doc.getFieldValue("rootSource"));
            offer.setSmallImg((String)doc.getFieldValue("smallImg"));
            offer.setLargeImg((String)doc.getFieldValue("largeImg"));
            offer.setTitle((String)doc.getFieldValue("title"));
            offer.setUrl((String)doc.getFieldValue("url"));
            offer.setPhone((String)doc.getFieldValue("phone"));
            offer.setUpdated((Long) doc.getFieldValue("updated"));
            
            Integer popularity = (Integer)doc.getFieldValue("popularity");
			offer.setPopularity(popularity);
			
            if(com.gaoshin.onsalelocal.api.DataSource.Shoplocal.getValue().equalsIgnoreCase(offer.getSource()))
            	hasSlocalOffer = true;
	        try {
	            offer.setLatitude(Float.parseFloat(doc.getFieldValue("location_0_coordinate").toString()));
	            offer.setLongitude(Float.parseFloat(doc.getFieldValue("location_1_coordinate").toString()));
                offer.setDistance((float) Geocode.distance(req.getLat(), req.getLng(), offer.getLatitude(), offer.getLongitude()));
	        }
	        catch (Exception e) {
	        }
        }
        
//        if(!hasSlocalOffer && req.getLat() != null && req.getLng() != null && req.getKeywords() != null && req.getRadius()>=10) {
        if(!hasSlocalOffer && req.getLat() != null && req.getLng() != null) {
        	logger.info("find nearby cities");
			List<City> cities = geoDao.nearbyCities(req.getLat(), req.getLng(), 20);
			for(City city : cities) {
	        	logger.info("ondemandShoplocal " + city.getCity() + "," + city.getState());
				oslDao.ondemandShoplocal(city);
				break;
			}
        }

        return list;
    }
    
    public static String getNotNullValue(){
        return getRangeValue("\"\"","*");
    }
    
    public static String getRangeValue(String i, String j){
        return "["+i+" TO "+j+"]";
    }

    public StringBuilder appendAND(StringBuilder sb) {
        if(sb.length() > 0)
            sb.append(AND);
        return sb;
    }
    
    public StringBuilder appendOR(StringBuilder sb) {
        if(sb.length() > 0)
            sb.append(OR);
        return sb;
    }
    
    public StringBuilder appendNotNull(StringBuilder sb, String key){
        return appendQuery(sb, key,getNotNullValue());
    }
    
    public StringBuilder appendRange(StringBuilder sb, String key, String i, String j) {
        return appendQuery(sb,key,getRangeValue(i,j));
    }

    public StringBuilder appendQuery(StringBuilder sb, String key, String value) {
        sb.append(" ").append(getKeyValue(key,value));
        return sb;
    }

    public static String getKeyValue(String key, String value){
        return key+":("+value+")";
    }

    public static String exactMatch(String s){
        return "\""+s+"\"";
    }

    public static String exactMatch(String[] s){
    	StringBuilder sb = new StringBuilder();
    	for(int i=0; i<s.length; i++) {
    		if(i>0)
    			sb.append(" ");
    		sb.append("\"").append(s[i]).append("\"");
    	}
        return sb.toString();
    }

    public static String getAllKeyValue(String key){
        return getKeyValue(key, "*");
    }
    public static String getMultivalue(String[] value){
        return "("+join(" ",value)+")";
    }
    
    public static String join(String delimiter, String...input){
        String ret = null;
        if(input.length>0){
            StringBuilder sb = new StringBuilder();
            for(String value : input) {
                sb.append(value+delimiter);
            }
            ret = sb.substring(0,sb.length()-delimiter.length());
        }
        return ret;
    }
    
    public static String getNoCacheKeyValue(String key, String value){
        return NOCACHE+getKeyValue(key, value);
    }
    
    public String getSolrFieldName(String objFieldName) {
        return getSolrFieldName(objFieldName,true);
    }
    
    public String getSolrFieldName(String objFieldName, boolean plus) {
        return (plus?"":"-") + objFieldName;
    }
    
    private static final String AND = " AND ";
    private static final String OR = " OR ";
    public static final String NOCACHE = "{!cache=false}";
    public static final String NOCACHE(int i){return "{!cache=false cost="+i+"}";}

	@Override
    public void searchStore(String query, OutputStream out) throws IOException {
		String link = storeSearchServer.getBaseURL() + "/select?" + query;
		URL url = new URL(link);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream in = connection.getInputStream();
		byte[] buff = new byte[8192];
		while(true) {
			int len = in.read(buff);
			if(len < 0)
				break;
			out.write(buff, 0, len);
		}
		in.close();
		connection.disconnect();
    }
	
    private String getTagsFromSolrDoc(SolrDocument doc) {
        Object value = doc.getFieldValue("tags");
        if(value == null)
            return null;
        if(value instanceof List) {
            List list = (List) value;
            if(list.size() == 0)
                return null;
            value = list.get(0);
            if(value == null) return null;
            return value.toString();
        }

        return value.toString();
    }
	
	public static void main(String[] args) throws IOException {
	    SearchServiceImpl ssi = new SearchServiceImpl();
	    ssi.storeSearchServer = new CommonsHttpSolrServer("http://localhost:9999/solr/merchant");
	    ssi.searchStore("q=text:food+thai+noodle+mountain+view&wt=json&indent=1", System.out);
    }
}
