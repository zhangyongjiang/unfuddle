package com.gaoshin.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.gaoshin.beans.GenericResponse;
import com.gaoshin.beans.Group;
import com.gaoshin.beans.Post;
import com.gaoshin.beans.PostList;
import com.gaoshin.beans.User;
import com.gaoshin.beans.UserRole;
import com.gaoshin.business.GroupService;
import com.sun.jersey.spi.inject.Inject;
import common.web.BusinessException;
import common.web.ServiceError;

@Path("/group")
@Component
@Produces({ "text/html;charset=utf-8", "text/xml;charset=utf-8", "application/json;charset=utf-8" })
public class GroupResource extends GaoshinResource {
    @Inject
    private GroupService groupService;

    @POST
    @Path("create")
    public Group create(Group group) {
        group.setOwner(assertUser());
        return groupService.createGroup(group);
    }

    @GET
    @Path("{id}")
    public Group getGroup(@PathParam("id") long groupId) {
        return groupService.getGroup(groupId);
    }

    @POST
    @Path("post/{groupId}")
    public Post post(@PathParam("groupId") Long groupId, Post post) {
        return groupService.createPost(assertUser(), groupId, post);
    }

    @GET
    @Path("post/{postId}")
    public Post getPost(@PathParam("postId") Long postId) {
        return groupService.getPost(postId);
    }

    @POST
    @Path("reply/{postId}")
    public Post reply(@PathParam("postId") Long postId, Post post) {
        return groupService.replyPost(assertUser(), postId, post);
    }

    @POST
    @Path("thumbup/{postId}")
    public Post thumbup(@PathParam("postId") Long postId) {
        return groupService.thumbup(assertUser(), postId);
    }

    @POST
    @Path("thumbdown/{postId}")
    public Post thumbdown(@PathParam("postId") Long postId) {
        return groupService.thumbdown(assertUser(), postId);
    }

    @POST
    @Path("post/delete/{postId}")
    public GenericResponse delete(@PathParam("postId") Long postId) {
        User user = assertUser();
        if (user.getRole() == null || user.getRole().ordinal() > UserRole.ADMIN.ordinal())
            throw new BusinessException(ServiceError.Forbidden);
        groupService.deletePost(postId);
        return new GenericResponse();
    }

    @GET
    @Path("latest-posts/{groupId}")
    public PostList listLatestPosts(
            @PathParam("groupId") Long groupId,
            @QueryParam("before") Long before,
            @DefaultValue("10") @QueryParam("size") int size
            ) {
        return groupService.listLatestPosts(groupId, before, size);
    }

    @GET
    @Path("my-posts")
    public PostList listMyPosts(
            @QueryParam("before") Long beforeId,
            @DefaultValue("10") @QueryParam("size") int size
            ) {
        return groupService.listUserPosts(assertUser(), beforeId, size);
    }
}
