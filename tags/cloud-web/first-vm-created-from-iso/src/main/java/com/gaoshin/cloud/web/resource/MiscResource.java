package com.gaoshin.cloud.web.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import common.util.web.JerseyBaseResource;

@Path("/misc")
@Component
@Produces({
        "text/html;charset=utf-8",
        "text/xml;charset=utf-8",
        "application/json;charset=utf-8" })
public class MiscResource extends JerseyBaseResource {

    @POST
    @Path("echo")
    public Response echo(InputStream in) throws IOException {
        ServletOutputStream outputStream = responseInvoker.get().getOutputStream();
        byte[] buff = new byte[8192];
        while (true) {
            int len = in.read(buff);
            if (len < 0) {
                break;
            }
            outputStream.write(buff, 0, len);
        }
        outputStream.close();
        return Response.ok().build();
    }

    @GET
    @Path("hello")
    public String hello() {
        return new Date().toString();
    }
}
