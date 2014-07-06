package com.gaoshin.onsalelocal.osl.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaoshin.onsalelocal.osl.dao.UserDao;
import com.gaoshin.onsalelocal.osl.entity.UserFile;
import com.gaoshin.onsalelocal.osl.service.ContentService;

@Service("contentService")
@Transactional(readOnly=true)
public class ContentServiceImpl extends ServiceBaseImpl implements ContentService {
    static final Logger logger = Logger.getLogger(ContentServiceImpl.class);

    @Value("${contentPath:/tmp/osl/content}")
    private String rootPath;
    
    @Autowired UserDao userDao;
    
	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public UserFile save(String userId, InputStream stream) throws IOException {
		String fileId = UUID.randomUUID().toString();
		return save(userId, stream, fileId);
    }
    
	@Override
    @Transactional(readOnly=false, rollbackFor=Throwable.class)
    public UserFile save(String userId, InputStream stream, String fileId) throws IOException {
		String path = getPath(fileId);
		File file = new File(path);
		if(!file.exists())
			file.mkdirs();
		String imgFile = getFile(fileId);
		FileOutputStream fos = new FileOutputStream(imgFile);
		byte[] buff = new byte[8192];
		int total = 0;
		while(true) {
			int len = stream.read(buff);
			if(len < 0)
				break;
			total += len;
			fos.write(buff, 0, len);
		}
		fos.close();
		
		if(total == 0) {
			logger.info("!!!! ========= file length is 0 ======== !!!!");
			new File(imgFile).delete();
			return null;
		}
		
		BufferedImage bimg = ImageIO.read(new File(imgFile));
		if(bimg.getWidth()>800 || bimg.getHeight()>800) {
			float scale = 0;
			if(bimg.getWidth()>bimg.getHeight()) {
				scale = (float)bimg.getWidth() / (float)800;
			}
			else {
				scale = (float)bimg.getHeight() / (float)800;
			}
			int w = (int) (((float)bimg.getWidth()) / scale);
			int h = (int) (((float)bimg.getHeight()) / scale);
			Image thumbnail = bimg.getScaledInstance(w, h, Image.SCALE_FAST);
			
			BufferedImage bufferedThumbnail = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
			fos = new FileOutputStream(imgFile);
			ImageIO.write(bufferedThumbnail, "jpeg", fos);
			fos.close();
		}
		
		UserFile uf = new UserFile();
		uf.setUserId(userId);
		uf.setFileId(fileId);
		uf.setWidth(bimg.getWidth());
		uf.setHeight(bimg.getHeight());
		logger.info("!!!! ========= file saved ======== !!!!" + imgFile);
		
		userDao.insert(uf);
		return uf;
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

	@Override
    public UserFile getByFileId(String fileId) {
	    String sql = "select * from UserFile where fileId=:fileId";
		return userDao.queryUniqueBySql(UserFile.class, Collections.singletonMap("fileId", fileId), sql);
    }
}
