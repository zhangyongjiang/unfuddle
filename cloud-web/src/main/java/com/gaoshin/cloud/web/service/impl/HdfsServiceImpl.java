package com.gaoshin.cloud.web.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.protocol.DirectoryListing;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.cloud.web.bean.hadoop.HdfsFile;
import com.gaoshin.cloud.web.service.HdfsService;

@Service("hdfsService")
@Transactional
public class HdfsServiceImpl extends HadoopBaseService implements HdfsService {

    @Override
    public HdfsFile ls(String path) throws IOException {
        DFSClient dfsClient = getDfsClient();
        
        if (!dfsClient.exists(path)) {
            HdfsFile hdfsFile = new HdfsFile();
            hdfsFile.setExists(false);
            return hdfsFile;
        }
        
        Path p = new Path(path);
        HdfsFileStatus fileInfo = dfsClient.getFileInfo(path);
        HdfsFile hdfsFile = fromStatus(fileInfo);
        hdfsFile.setName(p.getName());
        if(p.getParent() != null) {
            hdfsFile.setParent(p.getParent().toString());
        }
        if(fileInfo.isDir()) {
            DirectoryListing thisListing = dfsClient.listPaths(path, HdfsFileStatus.EMPTY_NAME);
            HdfsFileStatus[] files = thisListing.getPartialListing();
            for(HdfsFileStatus status : files) {
                HdfsFile file = fromStatus(status);
                file.setParent(path);
                hdfsFile.getChildren().add(file);
            }
        }
        else {
            hdfsFile.setSize(fileInfo.getLen());
        }
        
        return hdfsFile;
    }
    
    public static HdfsFile fromStatus(HdfsFileStatus status) {
        HdfsFile file = new HdfsFile();
        file.setExists(true);
        file.setDir(status.isDir());
        file.setName(status.getLocalName());
        file.setSize(status.getLen());
        file.setModificationTime(status.getModificationTime());
        file.setOwner(status.getOwner());
        file.setGroup(status.getGroup());
        file.setBlockSize(status.getBlockSize());
        file.setReplication(status.getReplication());
        file.setPermission(status.getPermission().toString());
        return file;
    }

    @Override
    public HdfsFile info(String path, Long size) throws IOException {
        HdfsFile file = ls(path);
        if(file.isExists() && !file.isDir() && file.getSize() > 0) {
            if(size == null || size > 8192) {
                size = 8192l;
            }
            if(size > file.getSize()) {
                size = file.getSize();
            }
            byte[] buff = new byte[size.intValue()];
            Path pt = new Path(path);
            FileSystem fs = FileSystem.get(getConf());
            FSDataInputStream inputStream = fs.open(pt);
            try {
                inputStream.read(buff);
                file.setContent(new String(buff, "UTF-8"));
            }
            finally{
                if(inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return file;
    }

    @Override
    public void upload(String path, InputStream inputStream) throws IOException {
        DFSClient dfsClient = getDfsClient();
        OutputStream outputStream = dfsClient.create(path, false);
        try {
            byte[] buff = new byte[32768];
            while(true) {
                int len = inputStream.read(buff);
                if(len < 0) {
                    break;
                }
                outputStream.write(buff, 0, len);
            }
        }
        finally {
            if(outputStream != null) {
                outputStream.close();
            }
        }
    }

    @Override
    public void download(String path) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mkdir(String path) throws IOException {
        DFSClient dfsClient = getDfsClient();
        dfsClient.mkdirs(path);
    }

    @Override
    public void rm(String path) throws IOException {
        DFSClient dfsClient = getDfsClient();
        dfsClient.delete(path);
    }

}
