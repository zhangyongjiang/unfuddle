package com.gaoshin.business;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.beans.Group;
import com.gaoshin.beans.Post;
import com.gaoshin.beans.PostList;
import com.gaoshin.beans.User;
import com.gaoshin.dao.GroupDao;
import com.gaoshin.dao.UserDao;
import com.gaoshin.entity.GroupEntity;
import com.gaoshin.entity.PostEntity;
import com.gaoshin.entity.UserEntity;
import common.util.Misc;

@Service("groupService")
@Transactional
public class GroupServiceImpl extends BaseServiceImpl implements GroupService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Override
    public Group createGroup(Group group) {
        UserEntity owner = userDao.getUser(group.getOwner());
        GroupEntity entity = new GroupEntity(group);
        entity.setOwner(owner);
        userDao.saveEntity(entity);
        return entity.getBean(Group.class);
    }

    @Override
    public Group getGroup(Long id) {
        return userDao.getEntity(GroupEntity.class, id).getBean(Group.class);
    }

    @Override
    public Group getGroupByName(String name) {
        GroupEntity entity = userDao.getFirstEntityBy(GroupEntity.class, "name", name);
        if (entity == null)
            return null;
        else
            return entity.getBean(Group.class);
    }

    @Override
    public Post createPost(User user, Long groupId, Post post) {
        UserEntity userEntity = userDao.getUser(user);

        if (!userEntity.isAdminUser()) {
            post.setTitle(Misc.removeTag(post.getTitle()));
            post.setContent(Misc.removeTag(post.getContent()));
        }
        post.setCreateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
        GroupEntity groupEntity = userDao.getEntity(GroupEntity.class, groupId);
        PostEntity entity = new PostEntity(post);
        entity.setAuthor(userEntity);
        entity.setGroup(groupEntity);
        userDao.saveEntity(entity);
        return entity.getBean(Post.class);
    }

    @Override
    public Post replyPost(User user, Long postId, Post post) {
        UserEntity userEntity = userDao.getUser(user);
        PostEntity postEntity = userDao.getEntity(PostEntity.class, postId);
        GroupEntity groupEntity = userDao.getEntity(GroupEntity.class, postEntity.getGroup().getId());
        PostEntity entity = new PostEntity(post);
        entity.setAuthor(userEntity);
        entity.setGroup(groupEntity);
        entity.setParent(postEntity);
        userDao.saveEntity(entity);
        return entity.getBean(Post.class);
    }

    @Override
    public PostList listLatestPosts(Long groupId, Long beforeId, int size) {
        List<PostEntity> entities = groupDao.listLatestGroupPosts(groupId, beforeId, size);
        PostList list = new PostList();
        for (PostEntity entity : entities) {
            Post post = entity.getBean(Post.class);
            if (entity.getAuthor() != null)
                post.setAuthor(entity.getAuthor().getBean(User.class));
            list.getList().add(post);
        }
        return list;
    }

    @Override
    public PostList listUserPosts(User assertUser, Long beforeId, int size) {
        UserEntity userEntity = userDao.getUser(assertUser);
        List<PostEntity> entities = groupDao.listLatestUserPosts(userEntity.getId(), beforeId, size);
        PostList list = new PostList();
        for (PostEntity entity : entities) {
            Post post = entity.getBean(Post.class);
            post.setGroup(entity.getGroup().getBean(Group.class));
            list.getList().add(post);
        }
        return list;
    }

    @Override
    public Post randomPost(Long groupId) {
        String sql = "select count(*) from posts where group_id = " + groupId;
        int count = Integer.parseInt(userDao.nativeQuerySelectRow(sql).toString());
        if (count == 0)
            return null;
        int random = new Random(System.currentTimeMillis()).nextInt(count);
        PostEntity entity = userDao.findEntityBy(PostEntity.class, "group.id", groupId, random, 1).get(0);
        Post post = entity.getBean(Post.class);
        if (entity.getAuthor() != null)
            post.setAuthor(entity.getAuthor().getBean(User.class));
        if (entity.getGroup() != null)
            post.setGroup(entity.getGroup().getBean(Group.class));
        return post;
    }

    @Override
    public Post getPost(Long postId) {
        PostEntity entity = userDao.getEntity(PostEntity.class, postId);
        Post post = entity.getBean(Post.class);
        if (entity.getAuthor() != null)
            post.setAuthor(entity.getAuthor().getBean(User.class));
        if (entity.getGroup() != null)
            post.setGroup(entity.getGroup().getBean(Group.class));

        for (PostEntity childEntity : entity.getChildren()) {
            Post child = childEntity.getBean(Post.class);
            if (childEntity.getAuthor() != null)
                child.setAuthor(childEntity.getAuthor().getBean(User.class));
            if (childEntity.getGroup() != null)
                child.setGroup(childEntity.getGroup().getBean(Group.class));
            post.getChildren().add(child);
        }

        PostEntity parentEntity = entity.getParent();
        if (parentEntity != null) {
            Post parent = parentEntity.getBean(Post.class);
            post.setParent(parent);
            parent.getChildren().add(post);
            if (parentEntity.getAuthor() != null)
                parent.setAuthor(parentEntity.getAuthor().getBean(User.class));
            if (parentEntity.getGroup() != null)
                parent.setGroup(parentEntity.getGroup().getBean(Group.class));
        }
        return post;
    }

    @Override
    public void deletePost(Long postId) {
        PostEntity entity = userDao.getEntity(PostEntity.class, postId);
        userDao.removeEntity(entity);
    }

    @Override
    public PostList listByScores(Long groupId, int start, int size) {
        List<PostEntity> entities = groupDao.listByScores(groupId, start, size);
        PostList list = new PostList();
        for (PostEntity entity : entities) {
            Post post = entity.getBean(Post.class);
            if (entity.getAuthor() != null)
                post.setAuthor(entity.getAuthor().getBean(User.class));
            list.getList().add(post);
        }
        return list;
    }

    @Override
    public Post thumbdown(User assertUser, Long postId) {
        PostEntity entity = userDao.getEntity(PostEntity.class, postId);
        entity.setThumbup(entity.getThumbup() + 1);
        userDao.saveEntity(entity);
        return entity.getBean(Post.class);
    }

    @Override
    public Post thumbup(User assertUser, Long postId) {
        PostEntity entity = userDao.getEntity(PostEntity.class, postId);
        entity.setThumbdown(entity.getThumbdown() + 1);
        userDao.saveEntity(entity);
        return entity.getBean(Post.class);
    }

}
