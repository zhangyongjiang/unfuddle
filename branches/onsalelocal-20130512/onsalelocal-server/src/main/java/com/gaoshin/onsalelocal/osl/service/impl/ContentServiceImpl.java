package com.gaoshin.onsalelocal.osl.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.osl.service.ContentService;

@Service("oslService")
@Transactional(readOnly=true)
public class ContentServiceImpl extends ServiceBaseImpl implements ContentService {
    static final Logger logger = Logger.getLogger(ContentServiceImpl.class);

    @Value("${contentPath:/tmp/osl/content}")
    private String rootPath;
    
	@Override
    public String save(InputStream stream) throws IOException {
		String id = UUID.randomUUID().toString();
		save(stream, id);
	    return id;
    }
    
	@Override
    public void save(InputStream stream, String id) throws IOException {
		String path = getPath(id);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		FileOutputStream fos = new FileOutputStream(getFile(id));
		byte[] buff = new byte[8192];
		while(true) {
			int len = stream.read(buff);
			if(len < 0)
				break;
			fos.write(buff, 0, len);
		}
		fos.close();
    }
    
	private String getPath(String id) {
		return rootPath + "/" + id.substring(0, 3) + "/" + id.substring(3, 6) + "/";
	}

	private String getFile(String id) {
		return getPath(id) + id;
	}

	@Override
    public InputStream getInputStream(String id) throws IOException {
		String file = getFile(id);
	    return new FileInputStream(file);
    }

	@Override
    public void read(String id, OutputStream out) throws IOException {
	    InputStream stream = getInputStream(id);
		byte[] buff = new byte[8192];
		while(true) {
			int len = stream.read(buff);
			if(len < 0)
				break;
			out.write(buff, 0, len);
		}
		stream.close();
		out.close();
    }
}
