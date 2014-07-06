package com.gaoshin.cj.resource;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gaoshin.cj.service.CjBackgroundService;
import common.util.web.JerseyBaseResource;

@Path("/ws/cjb")
@Component
public class CjBackgroundResource extends JerseyBaseResource {
    @Autowired private CjBackgroundService cjBackgroundService;

    @PostConstruct
    public void startBackgroundThread() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
//                      cjBackgroundService.fetchLinks();
//                      cjBackgroundService.fetchJoinedAdvertiser();
//                        int fetched = cjBackgroundService.fetchAdvertisers();
//                        if(fetched == 0) {
//                            Thread.sleep(60000);
//                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
    
    @GET
    public String helloWorld() {
        return "Hello World! Now is " + new Date();
    }
}
