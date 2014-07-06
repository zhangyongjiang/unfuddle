package com.gaoshin.onsalelocal.osl.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.gaoshin.onsalelocal.osl.service.ContentService;
import com.gaoshin.onsalelocal.osl.service.OslService;
import com.gaoshin.onsalelocal.osl.service.SearchService;
import com.gaoshin.onsalelocal.osl.service.UserService;
import com.nextshopper.api.Company;
import com.nextshopper.api.CompanyList;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.JerseyBaseResource;

@Path("/ws/company")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class CompanyResource extends JerseyBaseResource {
	private static final Logger logger = Logger.getLogger(CompanyResource.class.getName());

    @Inject private OslService oslService;
    @Inject private ContentService contentService;
    @Inject private UserService userService;
    @Inject private SearchService searchService;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Company createCompany(Company company) throws Exception{
    	String userId = assertRequesterUserId();
    	company.setCreated(System.currentTimeMillis());
    	return oslService.createCompany(userId, company);
    }
    
    @POST
    @Path("/update")
    public Company updateCompany(Company company) throws Exception{
    	String userId = assertRequesterUserId();
    	return oslService.updateCompany(userId, company);
    }
    
    @GET
    public Company get(@QueryParam("companyId")String companyId) throws Exception{
    	return oslService.getCompany(companyId);
    }
    
    @GET
    @Path("/list")
    public CompanyList list(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("150") int size) throws Exception{
    	List<Company> list = oslService.listCompanies(offset, size);
    	CompanyList cl = new CompanyList();
    	cl.setItems(list);
    	return cl;
    }
    
    @GET
    @Path("/search")
    public CompanyList search(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("50") int size, @QueryParam("keywords") String keywords) throws Exception{
    	List<Company> list = oslService.searchCompanies(keywords, offset, size);
    	CompanyList cl = new CompanyList();
    	cl.setItems(list);
    	return cl;
    }
    
}
