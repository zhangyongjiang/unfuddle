package com.gaoshin.cloud.web.resource.hadoop;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.cloud.web.bean.hadoop.HdfsFile;
import com.gaoshin.cloud.web.service.HdfsService;
import common.util.web.JerseyBaseResource;

@Path("/hadoop/job")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class HadoopApplicationResource extends JerseyBaseResource {
    @Autowired private HdfsService hdfsService;

    @GET
    @Path("ls")
    public HdfsFile ls(@QueryParam("path") String path) throws IOException {
        if(path == null || "null".equals(path) || path.trim().length() == 0) {
            path = "/";
        }
        
        return hdfsService.ls(path);
    }
}
