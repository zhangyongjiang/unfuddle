package com.gaoshin.onsalelocal.osl.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ContentService {

	String save(InputStream stream) throws IOException;

	void save(InputStream stream, String id) throws IOException;

	InputStream getInputStream(String userId) throws IOException;

	void read(String id, OutputStream out) throws IOException;

}
