package com.gaoshin.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.GenericResponse;
import com.gaoshin.beans.Location;
import com.gaoshin.beans.MsgType;
import com.gaoshin.beans.Notification;
import com.gaoshin.beans.NotificationList;
import com.gaoshin.beans.User;
import com.gaoshin.business.ClientLogService;
import com.gaoshin.business.NotificationService;
import com.sun.jersey.spi.inject.Inject;
import common.android.log.LogList;
import common.util.DesEncrypter;
import common.util.JsonUtil;
import common.web.BusinessException;
import common.web.ServiceError;

@Path("/notification")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class NotificationResource extends GaoshinResource {
	
    @Inject
    private NotificationService notificationService;

    @Inject
    private ClientLogService clientLogService;

    @GET
    @Path("after")
    public NotificationList after(
            @DefaultValue("0") @QueryParam("after") Long after,
            @DefaultValue("true") @QueryParam("unread") boolean unreadOnly,
            @DefaultValue("false") @QueryParam("mark") boolean markRead,
            @DefaultValue("10") @QueryParam("size") int size) {
        if (size > 100)
            size = 100;
        User user = assertUser();
        return notificationService.after(user, after, size, markRead, unreadOnly);
    }

    @GET
    @Path("before")
    public NotificationList before(
            @DefaultValue("0") @QueryParam("before") Long before,
            @DefaultValue("10") @QueryParam("size") int size) {
        if (size > 100)
            size = 100;
        User user = assertUser();
        return notificationService.before(user, before, size);
    }

    @POST
    @Path("report")
    public GenericResponse report(Notification data) {
        User user = getUser();
        try {
            String json = DesEncrypter.selfdecrypt(data.getMsg()).get(0);
            if (MsgType.Location.equals(data.getType())) {
                LocationResource locRes = getResource(LocationResource.class);
                Location location = JsonUtil.toJavaObject(json, Location.class);
                locRes.save(location);
            } else if (MsgType.Log.equals(data.getType())) {
                LogList logs = JsonUtil.toJavaObject(json, LogList.class);
                clientLogService.save(user, logs);
            }
            return new GenericResponse();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ServiceError.Unknown, e.getMessage());
        }
    }

    @GET
    @Path("report/list")
    public NotificationList getUserLog(
            @QueryParam("phone") String phone,
            @QueryParam("uid") String suid,
            @DefaultValue("0") @QueryParam("start") String sstart,
            @DefaultValue("100") @QueryParam("size") String ssize
            ) {
        User current = assertUser();
        if (!current.isSuperUser())
            throw new BusinessException(ServiceError.Forbidden);

        Long uid = null;
        try {
            uid = Long.parseLong(suid);
        } catch (NumberFormatException e) {
        }

        Integer start = 0;
        try {
            start = Integer.parseInt(sstart);
        } catch (NumberFormatException e) {
        }

        Integer size = 100;
        try {
            size = Integer.parseInt(ssize);
        } catch (NumberFormatException e) {
        }

        if (phone == null || uid == null) {
            return clientLogService.list(start, size);
        } else {
            User user = new User();
            user.setPhone(phone);
            user.setId(uid);
            return clientLogService.list(user, start, size);
        }
    }

    @POST
    @Path("notify")
    public GenericResponse notify(Notification data) {
        User user = getUser();
        if (!user.isSuperUser())
            throw new BusinessException(ServiceError.Forbidden);
        return null;
    }
}
