package com.gaoshin.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import com.gaoshin.beans.Group;
import com.gaoshin.beans.Post;
import com.gaoshin.beans.PostList;
import com.gaoshin.business.GroupService;
import com.sun.jersey.spi.inject.Inject;

public class PredefinedGroupResource extends GaoshinResource {
    protected String groupName;
    protected GroupService groupService;
    protected Group group;

    public PredefinedGroupResource(String groupName) {
        this.groupName = groupName;
    }

    @Inject
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
        group = groupService.getGroupByName(groupName);
        if (group == null) {
            group = new Group();
            group.setName(groupName);
            group = groupService.createGroup(group);
        }
    }

    @POST
    @Path("submit")
    public Post submit(Post post) {
        return groupService.createPost(assertUser(), group.getId(), post);
    }

    @GET
    @Path("list")
    public PostList listLatestPosts(
            @DefaultValue("0") @QueryParam("start") int start,
            @DefaultValue("50") @QueryParam("size") int size
            ) {
        return groupService.listByScores(group.getId(), start, size);
    }

    @GET
    @Path("latest")
    public PostList listLatestPosts(
            @QueryParam("before") Long before,
            @DefaultValue("50") @QueryParam("size") int size
            ) {
        return groupService.listLatestPosts(group.getId(), before, size);
    }

    @POST
    @Path("reply/{postId}")
    public Post reply(@PathParam("postId") Long postId, Post post) {
        return groupService.replyPost(assertUser(), postId, post);
    }
}
