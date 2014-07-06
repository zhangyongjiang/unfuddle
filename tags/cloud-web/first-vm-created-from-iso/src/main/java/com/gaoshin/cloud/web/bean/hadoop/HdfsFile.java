package com.gaoshin.cloud.web.bean.hadoop;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HdfsFile {
    private boolean exists;
    private boolean dir;
    private String parent;
    private String name;
    private Long size;
    private long modificationTime;
    private String owner;
    private String group;
    private List<HdfsFile> children = new ArrayList<HdfsFile>();
    private long blockSize;
    private short replication;
    private String permission;
    private String content;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }
    
    public String getPath() {
        if(parent == null) {
            return "/" + name;
        }
        else if (parent.equals("/")){
            return parent + name;
        }
        else {
            return parent + "/" + name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<HdfsFile> getChildren() {
        return children;
    }

    public void setChildren(List<HdfsFile> children) {
        this.children = children;
    }

    public long getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(long modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String user) {
        this.owner = user;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setBlockSize(long blockSize) {
        this.blockSize = blockSize;
    }

    public void setReplication(short replication) {
        this.replication = replication;
    }

    public long getBlockSize() {
        return blockSize;
    }

    public short getReplication() {
        return replication;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
