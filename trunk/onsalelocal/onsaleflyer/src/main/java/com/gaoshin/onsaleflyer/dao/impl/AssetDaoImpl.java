package com.gaoshin.onsaleflyer.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.gaoshin.onsaleflyer.beans.UserAsset;
import com.gaoshin.onsaleflyer.dao.AssetDao;
import common.db.dao.impl.GenericDaoImpl;

@Repository
public class AssetDaoImpl extends GenericDaoImpl implements AssetDao {
	private static final Logger log = Logger.getLogger(AssetDaoImpl.class);

	@Value("${asset.base}")
	private String base;

	@Override
	public boolean assetExists(UserAsset ua) {
		String path = ua.getPath();
		File file = new File(path);
		return file.exists();
	}

	@Override
	public void create(UserAsset ua, InputStream uploadedInputStream) throws IOException {
		String path = base + File.separator + ua.getPath();
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] buff = new byte[8192];
		while(true) {
			int len = uploadedInputStream.read(buff);
			if(len < 0)
				break;
			fos.write(buff, 0, len);
		}
		fos.close();
		uploadedInputStream.close();
	}

	@Override
	public void rename(UserAsset ua, String newName) {
		String path = ua.getRealPath(base);
		File file = new File(path);
		
		UserAsset newAsset = new UserAsset(ua.getPosterOwner(), newName);
		File newFile = new File(newAsset.getPath());
		
		file.renameTo(newFile);
	}

	@Override
	public void remove(UserAsset ua) {
		String path = ua.getRealPath(base);
		File file = new File(path);
		file.delete();
	}

	@Override
	public void get(UserAsset ua, OutputStream outputStream) throws IOException {
		String path = ua.getRealPath(base);
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		byte[] buff = new byte[8192];
		while(true) {
			int len = fis.read(buff);
			if(len < 0)
				break;
			outputStream.write(buff, 0, len);
		}
		fis.close();
	}
	
}
