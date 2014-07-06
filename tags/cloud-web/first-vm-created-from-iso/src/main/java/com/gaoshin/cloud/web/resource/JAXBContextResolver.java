package com.gaoshin.cloud.web.resource;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.gaoshin.cloud.web.bean.GenericResponse;
import com.gaoshin.cloud.web.bean.StringList;
import com.gaoshin.cloud.web.bean.hadoop.HdfsFile;
import com.gaoshin.cloud.web.job.bean.JobConfList;
import com.gaoshin.cloud.web.job.bean.JobDependencyList;
import com.gaoshin.cloud.web.job.bean.JobDetails;
import com.gaoshin.cloud.web.job.bean.JobExecutionDetails;
import com.gaoshin.cloud.web.job.bean.JobExecutionDetailsList;
import com.gaoshin.cloud.web.job.bean.JobExecutionList;
import com.gaoshin.cloud.web.job.bean.JobList;
import com.gaoshin.cloud.web.job.bean.TaskList;
import com.gaoshin.cloud.web.xen.bean.VirtualBlockDeviceList;
import com.gaoshin.cloud.web.xen.bean.VirtualDiskImageList;
import com.gaoshin.cloud.web.xen.bean.VmDetails;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
    private JAXBContext context;
    private Class[] types = {
            GenericResponse.class,
            StringList.class,
            HdfsFile.class,
            JobList.class,
            JobExecutionList.class,
            JobExecutionDetailsList.class,
            JobExecutionDetails.class,
            JobConfList.class,
            TaskList.class,
            JobDependencyList.class,
            JobDetails.class,
            VmDetails.class,
            VirtualDiskImageList.class,
            VirtualBlockDeviceList.class
            };

    public JAXBContextResolver() throws Exception {
        this.context = new JSONJAXBContext(JSONConfiguration
                .mapped()
                .arrays("list", "children", "items", "scenes", "msgs",
                        "values", "ucmList", "members", "attrNames", "consoleList").build(),
                types);
    }

    public JAXBContext getContext(Class<?> objectType) {
        for (Class type : types) {
            if (type.equals(objectType)) {
                return context;
            }
        }
        return null;
    }
}
