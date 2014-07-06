package com.gaoshin.onsalelocal.osl.resource;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.onsalelocal.api.Offer;
import com.gaoshin.onsalelocal.api.OfferDetails;
import com.gaoshin.onsalelocal.api.OfferDetailsList;
import com.gaoshin.onsalelocal.api.StoreList;
import com.gaoshin.onsalelocal.osl.beans.OfferSearchRequest;
import com.gaoshin.onsalelocal.osl.beans.OfferSearchResponse;
import com.gaoshin.onsalelocal.osl.beans.SearchOrder;
import com.gaoshin.onsalelocal.osl.entity.Bookmark;
import com.gaoshin.onsalelocal.osl.entity.BookmarkType;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.SearchService;
import common.util.StringUtil;
import common.util.reflection.ReflectionUtil;
import common.util.web.GenericResponse;

@Path("/ws/offer")
@Component
public class OslResource extends OslBaseResource {
    @Autowired private OslService oslService;
    @Autowired private SearchService searchService;

    @Path("home")
    @GET
    public OfferSearchResponse home() throws Exception {
    	OfferSearchRequest req = new OfferSearchRequest();
    	req.setOffset(0);
    	req.setSize(10);
    	req.setSource(com.gaoshin.onsalelocal.api.DataSource.Yipit.getValue());
    	req.setOrder(SearchOrder.UpdatedDesc);
    	req.setGroup("category");
    	OfferSearchResponse resp1 = searchService.search(req);
    	
    	req.setSource(com.gaoshin.onsalelocal.api.DataSource.Shoplocal.getValue());
    	OfferSearchResponse resp2 = searchService.search(req);
    	
    	req.setSource(com.gaoshin.onsalelocal.api.DataSource.Safeway.getValue());
    	OfferSearchResponse resp3 = searchService.search(req);
    	
    	resp1.getItems().addAll(resp2.getItems());
    	resp1.getItems().addAll(resp3.getItems());
    	
    	Collections.sort(resp1.getItems(), new Comparator<OfferDetails>() {
			@Override
			public int compare(OfferDetails o1, OfferDetails o2) {
				if(o1.getUpdated() < o2.getUpdated())
					return 1;
				if(o1.getUpdated() > o2.getUpdated())
					return -1;
				return 0;
			}
		});
    	return resp1;
    }

    @Path("search")
    @GET
    public OfferSearchResponse searchOffer(
    		@QueryParam("subcategory") String subcategory, 
    		@QueryParam("category") String category, 
    		@QueryParam("keywords") String keywords, 
    		@QueryParam("lat") Float lat, 
    		@QueryParam("lng") Float lng, 
    		@QueryParam("merchant") String merchant, 
    		@QueryParam("radius") @DefaultValue("10") float radius,
    		@QueryParam("offset") @DefaultValue("0") int offset,
    		@QueryParam("size") @DefaultValue("200") int size,
    		@QueryParam("order") String order,
    		@QueryParam("group") String group,
    		@QueryParam("exclude") String exclude,
    		@QueryParam("nostore") String nostore,
    		@QueryParam("gl") @DefaultValue("1") int groupLimit
		  , @QueryParam("ls") Boolean localService
    		) throws Exception {
    	subcategory = StringUtil.nullIfEmpty(subcategory);
    	order = StringUtil.nullIfEmpty(order);
    	group = StringUtil.nullIfEmpty(group);
    	exclude = StringUtil.nullIfEmpty(exclude);
    	nostore = StringUtil.nullIfEmpty(nostore);
    	if(lat != null && lng != null) {
    		saveLocationToCookie(lat, lng);
    	}
    	if(size > 1000)
    		size = 50;
    	
    	OfferSearchRequest req = new OfferSearchRequest();
    	req.setSubcategory(subcategory);
    	if(order != null)
    		req.setOrder(SearchOrder.valueOf(order));
    	req.setKeywords(StringUtil.nullIfEmpty(keywords));
    	req.setCategory(StringUtil.nullIfEmpty(category));
    	req.setLat(lat);
    	req.setLng(lng);
    	req.setMerchant(StringUtil.nullIfEmpty(merchant));
    	req.setRadius(radius > 50 ? 50 : radius);
    	req.setOffset(offset);
    	req.setSize(size);
    	req.setGroup(group);
    	req.setGroupLimit(groupLimit);
    	req.setExclude(exclude);
    	req.setNostore(nostore);
    	req.setLocalService(localService);
    	
    	if(group != null) {
			OfferSearchResponse resp = searchService.search(req);
			return resp;
    	}
    	else {
	    	if(req.getMerchant() == null && req.getCategory() == null && req.getKeywords() == null) {
	    		group = "merchant";
	    		groupLimit = 3;
	    	}
	    	else {
	    		group = "title";
	    		groupLimit = 1;
	    	}
	    	req.setGroup(group);
	    	req.setGroupLimit(groupLimit);
			OfferSearchResponse resp = searchService.search(req);
			
//			if(resp.getTotal()<size && group != null && req.getCategory() == null) {
//				group = "category";
//		    	req.setGroup(group);
//		    	req.setGroupLimit(groupLimit);
//				resp = searchService.search(req);
//			}
	    	
			return resp;
    	}
    }
    
    private Float getLatFromCookie() {
    	String value = getCookie("clat");
    	if(value == null)
    		return null;
    	return Float.parseFloat(value) / 10000f;
	}

	private Float getLngFromCookie() {
    	String value = getCookie("clng");
    	if(value == null)
    		return null;
    	return Float.parseFloat(value) / 10000f;
	}

	private void saveLocationToCookie(Float lat, Float lng) {
    	int ilat = (int) (lat * 10000);
    	int ilng = (int) (lng * 10000);
    	setCookie("clat", String.valueOf(ilat));
    	setCookie("clng", String.valueOf(ilng));
	}

	@Path("query")
    @GET
    public OfferDetailsList queryOffer(
    		@QueryParam("lat") float lat, 
    		@QueryParam("lng") float lng, 
    		@QueryParam("radius") int radius,
    		@QueryParam("offset") @DefaultValue("0") int offset,
    		@QueryParam("size") @DefaultValue("200") int size
    		) throws Exception {
    	OfferDetailsList list = oslService.searchOffer(lat, lng, radius, offset, size);
    	
    	String userId = getRequesterUserId();
    	if(userId != null) {
    		BookmarkResource bookmarkResource = getResource(BookmarkResource.class);
    		List<Bookmark> bklist = bookmarkResource.list(null);
    		Set<String> offerids = new HashSet<String>();
    		for(Bookmark bm : bklist) {
    			if(BookmarkType.Offer.equals(bm.getType()))
    			offerids.add(bm.getOfferId());
    		}
    		for(OfferDetails od : list.getItems()) {
    			if(offerids.contains(od.getId()))
    				od.setBookmarked(true);
    		}
    	}
    	
		return list;
    }
    
    @Path("harvest/{schema}")
    @GET
    public GenericResponse harvest(@PathParam("schema") String schema) {
    	int howmany = oslService.harvest(schema);
    	return new GenericResponse(String.valueOf(howmany));
    }
    
    @GET
    @Path("details")
    public OfferDetails getOffer(@QueryParam("offerId") String offerId) {
    	Offer offer = oslService.getOffer(offerId);
    	OfferDetails od = ReflectionUtil.copy(OfferDetails.class, offer);
    	String userId = getRequesterUserId();
    	if(userId != null) {
    		BookmarkResource bookmarkResource = getResource(BookmarkResource.class);
    		List<Bookmark> bklist = bookmarkResource.list(null);
    		for(Bookmark bm : bklist) {
    			if(bm.getOfferId().equals(offer.getId())) {
        			if(BookmarkType.Offer.equals(bm.getType())) {
	    				od.setBookmarked(true);
	    				break;
        			}
    			}
    		}
    	}
    	return od;
    }

	@Path("nearby-stores")
	@GET
	public StoreList nearbyStores(
			@QueryParam("lat") Float latitude
			, @QueryParam("lng") Float longitude
			, @QueryParam("radius") Float radius
			, @QueryParam("category") String category
			, @QueryParam("ls") Boolean serviceOnly
			, @QueryParam("subcategory") String subcategory
			) {
		category = StringUtil.nullIfEmpty(category);
		subcategory = StringUtil.nullIfEmpty(subcategory);
		return oslService.nearbyStores(latitude, longitude, radius, category, subcategory, serviceOnly);
	}
}
