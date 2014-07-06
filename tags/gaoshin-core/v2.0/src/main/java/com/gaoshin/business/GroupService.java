package com.gaoshin.business;

import com.gaoshin.beans.Group;
import com.gaoshin.beans.Post;
import com.gaoshin.beans.PostList;
import com.gaoshin.beans.User;

public interface GroupService {
    Group createGroup(Group group);

    Group getGroup(Long id);

    Group getGroupByName(String name);

    Post createPost(User assertUser, Long groupId, Post post);

    Post replyPost(User assertUser, Long postId, Post post);

    PostList listLatestPosts(Long groupId, Long before, int size);

    PostList listUserPosts(User assertUser, Long beforeId, int size);

    Post randomPost(Long id);

    Post getPost(Long postId);

    void deletePost(Long postId);

    PostList listByScores(Long id, int start, int size);

    Post thumbdown(User assertUser, Long postId);

    Post thumbup(User assertUser, Long postId);
}
