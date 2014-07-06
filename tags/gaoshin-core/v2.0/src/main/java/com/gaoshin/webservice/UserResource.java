package com.gaoshin.webservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.DimensionValueList;
import com.gaoshin.beans.GenericResponse;
import com.gaoshin.beans.Review;
import com.gaoshin.beans.ReviewTarget;
import com.gaoshin.beans.StringList;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserAndDistanceList;
import com.gaoshin.beans.UserDevice;
import com.gaoshin.beans.UserFileList;
import com.gaoshin.beans.UserFileType;
import com.gaoshin.beans.UserList;
import com.gaoshin.beans.UserRole;
import com.gaoshin.business.ReviewService;
import com.gaoshin.business.UserService;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.inject.Inject;
import common.util.DesEncrypter;
import common.util.ImageUtil;
import common.web.BusinessException;
import common.web.JerseyBaseResource;
import common.web.ServiceError;

@Path("/user")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json" })
public class UserResource extends JerseyBaseResource {
    private static final Logger logger = Logger.getLogger(UserResource.class
            .getName());

    private static final String SessionKeyUser = "userInSession";
    private static final String SessionKeyUserId = "userIdInSession";
    private static final String SessionDeviceInfo = "userDeviceInfoInSession";
    private static final String CookieNameUser = "u";
    private static final String CookieNamePhone = "p";
    public static final Long ROOT_USER_ID = -1l;

    public static final String FOLDER_ICON = "/";
    public static final String FOLDER_ALBUM = "/album";

    private UserService userService;
    private User root = null;

    private List<Long> bannedUserList = new ArrayList<Long>();

    @Inject
    private ReviewService reviewService;

    public User getRootUser() {
        return root;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;

        root = new User();
        root.setName("ROOT");
        User db = userService.checkUser(root);
        if (db == null) {
            root.setPhone("666666");
            root = userService.signup(root);
        } else {
            root = db;
        }
    }

    @POST
    @Path("/signup")
    public User signup(User user) {
        return userService.signup(user);
    }

    @POST
    @Path("/update")
    public User update(User user) {
        User current = assertCurrentUser();
        user.setId(current.getId());
        user.setPhone(current.getPhone());
        return userService.update(user);
    }

    @POST
    @Path("/login")
    public User login(User user) {
        user = userService.login(user);
        setSessionAttribute(SessionKeyUser, user);
        setSessionAttribute(SessionKeyUserId, user.getId());

        UserDevice ud = userService.getUserDeviceInfo(user.getId());
        if (ud == null)
            ud = new UserDevice();
        setSessionAttribute(SessionDeviceInfo, user.getId());

        // setCookie(CookieNameUser, user.getName());
        return user;
    }

    @POST
    @Path("icon")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public GenericResponse uploadIcon(
            @FormDataParam("file") List<FormDataBodyPart> filePart
            ) throws Exception {
        String name = upload(filePart, UserFileType.icon);
        User user = assertCurrentUser();
        userService.setIcon(user, name);
        return new GenericResponse("success");
    }

    @GET
    @Path("icon/{userId}")
    @Produces("image/*")
    public Response downloadPhotoByUserId(@PathParam("userId") Long userId,
            @DefaultValue("0") @QueryParam("w") int width, @DefaultValue("0") @QueryParam("h") int height)
            throws IOException {
        User user = new User();
        user.setId(userId);
        user = userService.checkUser(user);
        if (user == null)
            throw new BusinessException(ServiceError.NotFound, "user not found " + userId);
        String photo = user.getIcon();
        if (photo == null)
            throw new BusinessException(ServiceError.NotFound);

        if (width == 0 && height == 0) {
            int pos = photo.lastIndexOf(".");
            String ext = photo.substring(pos + 1).toLowerCase();
            String contentType = "image/" + ext;
            responseInvoker.get().setContentType(contentType);
            ServletOutputStream outputStream = responseInvoker.get().getOutputStream();
            userService.download(user, UserFileType.icon, user.getIcon(), outputStream);
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            userService.download(user, UserFileType.icon, user.getIcon(), baos);
            OutputStream outputStream = responseInvoker.get().getOutputStream();
            responseInvoker.get().setContentType("image/png");
            ImageUtil.resize(new ByteArrayInputStream(baos.toByteArray()), outputStream, width, height);
        }

        return Response.ok().build();
    }

    @POST
    @Path("album")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public GenericResponse uploadPhoto(
            @FormDataParam("file") List<FormDataBodyPart> filePart
            ) throws Exception {
        String name = upload(filePart, UserFileType.album);
        return new GenericResponse("success");
    }

    @POST
    @Path("album/star")
    public GenericResponse star(Review review) throws Exception {
        User me = assertCurrentUser();
        review.setAuthor(me);
        review.setTargetType(ReviewTarget.album);
        review.setThumbsup(1);
        reviewService.create(review);

        return new GenericResponse("success");
    }

    @GET
    @Path("album/{userId}")
    public StringList getAlbumFileList(@PathParam("userId") Long userId) throws Exception {
        User user = new User();
        user.setId(userId);
        String folder = "/album";
        return userService.listUserFiles(user, folder);
    }

    @GET
    @Path("resources/{resType}/{userId}")
    public UserFileList getResources(@PathParam("resType") UserFileType resType,
            @PathParam("userId") Long userId)
            throws Exception {
        return userService.listUserResources(resType, userId);
    }

    @GET
    @Path("resources/{fid}")
    public Response getResources(@PathParam("fid") Long fid)
            throws Exception {
        ServletOutputStream outputStream = responseInvoker.get().getOutputStream();
        try {
            userService.download(fid, outputStream);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("download-album/{fid}")
    @Produces("image/*")
    public Response download(@PathParam("fid") Long resId,
            @DefaultValue("0") @QueryParam("width") int width) throws Exception {
        try {
            if (width > 0) {
                // String contentType = "image/png";
                // responseInvoker.get().setContentType(contentType);
                ServletOutputStream outputStream = responseInvoker.get().getOutputStream();
                userService.downloadPhoto(resId, outputStream, width);
            } else {
                ServletOutputStream outputStream = responseInvoker.get().getOutputStream();
                userService.download(resId, outputStream);
            }
            return Response.ok().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    public String upload(List<FormDataBodyPart> filePart, UserFileType resType) throws Exception {
        User user = assertCurrentUser();
        for (FormDataBodyPart bp : filePart) {
            InputStream is = bp.getEntityAs(InputStream.class);
            String name = bp.getContentDisposition().getFileName();
            userService.upload(user, is, name, resType);
            is.close();
            return name;
        }
        return null;
    }

    @POST
    @Path("logout")
    public GenericResponse logout() {
        requestInvoker.get().getSession().invalidate();
        // setCookie(CookieNamePhone, null);
        // setCookie(CookieNameUser, null);
        return new GenericResponse();
    }

    @GET
    @Path("current")
    public User me() {
        User user = getCurrentUser();
        if (user == null) {
            user = new User();
        } else if (user.getId() == null) {
            User db = userService.checkUser(user);
            if (db != null) {
                user = db;
                setSessionAttribute(SessionKeyUser, user);
                setSessionAttribute(SessionKeyUserId, user.getId());
                UserDevice ud = userService.getUserDeviceInfo(user.getId());
                if (ud == null)
                    ud = new UserDevice();
                setSessionAttribute(SessionDeviceInfo, user.getId());
            }
        }
        return user;
    }

    @GET
    @Path("dimensions")
    public DimensionValueList getUserDimensions() {
        User user = assertCurrentUser();
        return userService.listUserDimensions(user);
    }

    @GET
    @Path("dimensions-by-phone/{phone}")
    public DimensionValueList getUserDimensions(@PathParam("phone") String phone) {
        User user = new User();
        user.setPhone(phone);
        return userService.listUserDimensions(user);
    }

    @POST
    @Path("dimensions")
    public GenericResponse setDimensions(DimensionValueList dimensionValues) {
        User user = assertCurrentUser();
        userService.setUserDimensions(user, dimensionValues);
        return new GenericResponse();
    }

    @GET
    @Path("dimensions-by-id/{userId}")
    public DimensionValueList getUserDimensions(@PathParam("userId") Long userId) {
        User user = new User();
        user.setId(userId);
        return userService.listUserDimensions(user);
    }

    @GET
    @Path("search")
    public UserAndDistanceList search(
            @QueryParam("lat") Float latitude, @QueryParam("lng") Float longitude,
            @QueryParam("lat1") Float latitude1, @QueryParam("lng1") Float longitude1,
            @QueryParam("age0") Long minAge, @QueryParam("age1") Long maxAge,
            @QueryParam("gender") String gender,
            @DefaultValue("true") @QueryParam("nome") boolean selfExcluded,
            @DefaultValue("3000") @QueryParam("miles") Float miles,
            @DefaultValue("0") @QueryParam("offset") int offset, @DefaultValue("100") @QueryParam("size") int size) {
        if (latitude == null || longitude == null) {
            try {
                String center = getCookie("center");
                if (center != null) {
                    String[] latlng = center.split(",");
                    latitude = Float.parseFloat(latlng[0]);
                    longitude = Float.parseFloat(latlng[1]);
                }
            } catch (Exception e) {
                System.out.println("cannot get lat/lng from cookie");
            }
        }

        User user = getCurrentUser();
        if (latitude == null || longitude == null && user != null) {
            if (user.getCurrentLocation() != null) {
                latitude = user.getCurrentLocation().getLatitude();
                longitude = user.getCurrentLocation().getLongitude();
            }
        }
        
        if (latitude == null || longitude == null) {
            latitude = 40.7493946f;
            longitude = 73.968265f;
        }

        if (latitude == null || longitude == null || latitude < -180f || latitude > 180f || longitude < -180f || longitude > 180f || (miles != null && miles < 0)) {
            throw new BusinessException(ServiceError.InvalidInput);
        }

        // if (miles > 100 || miles <= 0)
        // miles = 30f;

        UserAndDistanceList list = userService.search((selfExcluded ? user : null), latitude, longitude, latitude1, longitude1, miles, minAge, maxAge, gender, offset, size);
        return list;
    }

    public User assertCurrentUser() {
        User user = getCurrentUser();
        if (user == null)
            throw new BusinessException(ServiceError.NoGuest);
        return user;
    }

    public User getCurrentUser() {
        User user = (User) getSessionAttribute(SessionKeyUser);
        boolean found = false;
        if (user == null) {
            user = new User();

            Long userId = (Long) getSessionAttribute(SessionKeyUserId);
            if (userId != null) {
                user.setId(userId);
                found = true;
            }

            String userName = getUserNameInCookie();
            if (userName != null) {
                user.setName(userName);
                found = true;
            }

            String phone = getUserPhoneInCookie();
            if (phone != null) {
                user.setPhone(phone);
                found = true;
            }
        } else {
            found = true;
        }

        if (found && user != null && user.getId() != null && bannedUserList.contains(user.getId())) {
            if (requestInvoker != null && requestInvoker.get() != null && requestInvoker.get().getSession() != null) {
                requestInvoker.get().getSession().invalidate();
            }
        }

        return found ? user : null;
    }

    @GET
    @Path("profile")
    public User getProfile() {
        User user = assertCurrentUser();
        return userService.getUser(user);
    }

    @GET
    @Path("profile-by-id")
    public User getProfile(@QueryParam("id") Long userId) {
        User user = new User();
        user.setId(userId);
        return userService.getUser(user);
    }

    @POST
    @Path("add-friend/{friendId}")
    public GenericResponse addFriend(@PathParam("friendId") Long friendId) {
        User user = assertCurrentUser();
        userService.addFriend(user, friendId);
        return new GenericResponse();
    }

    @POST
    @Path("ban/{userId}")
    public GenericResponse banUser(@PathParam("userId") Long userId) {
        User user = assertCurrentUser();
        UserRole role = user.getRole();
        if (UserRole.ADMIN.equals(role) || UserRole.SUPER.equals(role)) {
            userService.banUser(userId);
            bannedUserList.add(userId);
            return new GenericResponse();
        } else {
            throw new BusinessException(ServiceError.Forbidden);
        }
    }

    @POST
    @Path("unban/{userId}")
    public GenericResponse unbanUser(@PathParam("userId") Long userId) {
        User user = assertCurrentUser();
        UserRole role = user.getRole();
        if (UserRole.ADMIN.equals(role) || UserRole.SUPER.equals(role)) {
            userService.unbanUser(userId);
            bannedUserList.remove(userId);
            return new GenericResponse();
        } else {
            throw new BusinessException(ServiceError.Forbidden);
        }
    }

    @POST
    @Path("delete/{userId}")
    public GenericResponse deleteUser(@PathParam("userId") Long userId) {
        User user = assertCurrentUser();
        UserRole role = user.getRole();
        if (user.getId().equals(userId) || UserRole.ADMIN.equals(role) || UserRole.SUPER.equals(role)) {
            userService.deleteUser(userId);
            return new GenericResponse();
        } else {
            throw new BusinessException(ServiceError.Forbidden);
        }
    }

    @POST
    @Path("confirm-deletion")
    public GenericResponse confirmDeletion() {
        User user = assertCurrentUser();
        userService.confirmDeletion(user);
        return new GenericResponse();
    }

    @GET
    @Path("my-friends")
    public UserList getFriends() {
        User user = assertCurrentUser();
        return userService.getFriends(user);
    }

    @GET
    @Path("most-recents")
    public UserList getMostRecents() {
        return userService.getMostRecents();
    }

    @GET
    @Path("album-upgrade")
    public GenericResponse upgradeAlbum() {
        User user = assertCurrentUser();
        if (!user.isSuperUser()) {
            throw new BusinessException(ServiceError.Forbidden);
        }
        userService.upgradeAlbum();
        return new GenericResponse();
    }

    protected String getUserNameInCookie() {
        try {
            String cookie = getCookie(CookieNameUser);
            String ip = requestInvoker.get().getRemoteAddr();
            return Security.decrypt64(cookie, ip);
        } catch (Exception e) {
            return null;
        }
    }

    protected String getUserPhoneInCookie() {
        String cookie = getCookie(CookieNamePhone);
        if (cookie == null)
            return null;
        try {
            String ip = requestInvoker.get().getRemoteAddr();
            String phoneNumber = Security.decrypt64(cookie, ip);
	        if(phoneNumber != null)
	        return phoneNumber;
        } catch (Throwable t) {}

        try {
	        String phoneNumber = DesEncrypter.selfdecrypt(cookie).get(0);
	        return phoneNumber;
        } catch (Throwable t) {}
        return null;
    }

}
