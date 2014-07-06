package com.gaoshin.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Post {
    private Long id;
    private User author;
    private Group group;
    private Post parent;
    private String title;
    private String content;
    private boolean html = false;
    private Calendar createTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private List<Post> children = new ArrayList<Post>();
    private int thumbup;
    private int thumbdown;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Post getParent() {
        return parent;
    }

    public void setParent(Post parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public List<Post> getChildren() {
        return children;
    }

    public void setChildren(List<Post> children) {
        this.children = children;
    }

    public void setThumbup(int thumbup) {
        this.thumbup = thumbup;
    }

    public int getThumbup() {
        return thumbup;
    }

    public void setThumbdown(int thumbdown) {
        this.thumbdown = thumbdown;
    }

    public int getThumbdown() {
        return thumbdown;
    }
}
