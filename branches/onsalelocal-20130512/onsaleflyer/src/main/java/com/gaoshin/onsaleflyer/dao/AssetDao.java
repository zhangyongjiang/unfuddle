package com.gaoshin.onsaleflyer.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gaoshin.onsaleflyer.beans.UserAsset;
import common.db.dao.GenericDao;

public interface AssetDao extends GenericDao {

	boolean assetExists(UserAsset ua);

	void create(UserAsset ua, InputStream uploadedInputStream) throws IOException;

	void rename(UserAsset ua, String newName);

	void remove(UserAsset ua);

	void get(UserAsset ua, OutputStream outputStream) throws IOException;

}
