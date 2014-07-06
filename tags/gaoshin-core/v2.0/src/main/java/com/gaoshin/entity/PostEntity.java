package com.gaoshin.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gaoshin.beans.Post;

@Entity
@Table(name = "posts")
public class PostEntity extends GenericEntity {
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;
    
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @JoinColumn(name = "parent_id")
    private PostEntity parent;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;
    
    @Column
    private boolean html = false;
    
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createTime;

    @OneToMany(mappedBy = "parent")
    private List<PostEntity> children = new ArrayList<PostEntity>();

    @Column
    private int thumbup;

    @Column
    private int thumbdown;

    public PostEntity() {
    }

    public PostEntity(Post bean) {
        super(bean);
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setParent(PostEntity parent) {
        this.parent = parent;
    }

    public PostEntity getParent() {
        return parent;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public boolean isHtml() {
        return html;
    }

    public void setChildren(List<PostEntity> children) {
        this.children = children;
    }

    public List<PostEntity> getChildren() {
        return children;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
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
