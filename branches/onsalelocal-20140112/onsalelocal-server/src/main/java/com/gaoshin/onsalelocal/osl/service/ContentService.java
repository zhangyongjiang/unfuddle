package com.gaoshin.onsalelocal.osl.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gaoshin.onsalelocal.osl.entity.UserFile;

public interface ContentService {

	UserFile save(String userId, InputStream stream) throws IOException;

	UserFile save(String userId, InputStream stream, String id) throws IOException;

	InputStream getInputStream(String id) throws IOException;

	void read(String id, OutputStream out) throws IOException;

	UserFile getByFileId(String fileId);

}
