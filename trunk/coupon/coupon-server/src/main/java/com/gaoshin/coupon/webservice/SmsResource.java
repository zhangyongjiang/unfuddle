package com.gaoshin.coupon.webservice;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.gaoshin.authone.service.SmsMessage;
import com.gaoshin.authone.service.SmsService;
import com.sun.jersey.spi.inject.Inject;
import common.util.web.GenericResponse;
import common.util.web.JerseyBaseResource;

@Path("/ws/sms")
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class SmsResource extends JerseyBaseResource {
    @Inject SmsService smsService;
    
    @Path("/register")
    @POST
    public GenericResponse reigster() throws IOException {
        String url = requestInvoker.get().getRequestURL().toString();
        int pos = url.indexOf("/register");
        smsService.nofifyServer(url.substring(0, pos) + "/received");
        return new GenericResponse();
    }
    
    @Path("/send")
    @POST
    public GenericResponse send(SmsMessage msg) throws IOException {
        smsService.send(msg);
        return new GenericResponse();
    }
    
    @Path("/send/{phone}/{msg}")
    @POST
    public GenericResponse send(@PathParam("phone") String phone, @PathParam("msg") String msg) throws IOException {
        SmsMessage sm = new SmsMessage();
        sm.setPhone(phone);
        sm.setMsg(msg);
        return send(sm);
    }
    
    @Path("/received")
    @POST
    public GenericResponse received(SmsMessage msg) {
        smsService.received(msg);
        return new GenericResponse();
    }
    
}
