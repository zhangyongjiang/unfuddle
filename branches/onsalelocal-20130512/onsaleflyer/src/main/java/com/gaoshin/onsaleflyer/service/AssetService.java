package com.gaoshin.onsaleflyer.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gaoshin.onsaleflyer.beans.UserAsset;

public interface AssetService {

	void upload(String userId, UserAsset ua, InputStream uploadedInputStream) throws IOException;

	void rename(String userId, UserAsset ua, String newName) throws IOException;

	void remove(String userId, UserAsset ua);

	void get(String userId, UserAsset ua, OutputStream outputStream) throws IOException;
}
