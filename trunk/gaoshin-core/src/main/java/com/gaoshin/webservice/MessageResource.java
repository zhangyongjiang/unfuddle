package com.gaoshin.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.Message;
import com.gaoshin.beans.MessageList;
import com.gaoshin.beans.User;
import com.gaoshin.business.MessageService;
import com.sun.jersey.spi.inject.Inject;

@Path("/message")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class MessageResource extends GaoshinResource {
	
    @Inject
    private MessageService messageService;

    @POST
    @Path("send")
    public Message send(Message msg) {
        User user = assertUser();
        Message send = messageService.send(user, msg);
        return send;
    }

    @GET
    @Path("list")
    public MessageList list(@QueryParam("who") Long userId2, @DefaultValue("0") @QueryParam("since") Long since,
            @DefaultValue("0") @QueryParam("start") int start, @DefaultValue("10") @QueryParam("size") int size) {
        User user1 = assertUser();
        return messageService.list(user1, userId2, since, start, size);
    }
}
