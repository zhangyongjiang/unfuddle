package com.gaoshin.cloud.web.resource.hadoop;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.cloud.web.bean.GenericResponse;
import com.gaoshin.cloud.web.bean.hadoop.HdfsFile;
import com.gaoshin.cloud.web.service.HdfsService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import common.util.web.JerseyBaseResource;

@Path("/hadoop/hdfs")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class HdfsResource extends JerseyBaseResource {
    @Autowired
    private HdfsService hdfsService;

    @Path("info")
    @GET
    public HdfsFile info(@QueryParam("path") String path, @QueryParam("size") Long size) throws IOException {
        if (path == null || "null".equals(path) || path.trim().length() == 0) {
            path = "/";
        }

        return hdfsService.info(path, size);
    }

    @Path("download")
    @GET
    public Response download(@QueryParam("path") String path) throws IOException {
        hdfsService.download(path);
        return Response.ok().build();
    }

    @Path("upload/{path: .+}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@PathParam("path") String path,
            @FormDataParam("file") InputStream inputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail
            )  throws IOException {
        String filePath = "/" + path + "/" + fileDetail.getFileName();
        hdfsService.upload(filePath, inputStream);
        return Response.ok().build();
    }
    
    @Path("mkdir/{path: .+}")
    @POST
    public GenericResponse mkdir(@PathParam("path") String path) throws IOException {
        hdfsService.mkdir("/" + path);
        return new GenericResponse();
    }
    
    @Path("rm")
    @GET
    public Response rm(@QueryParam("path") String path) throws IOException {
        hdfsService.rm(path);
        return Response.ok().build();
    }
}
