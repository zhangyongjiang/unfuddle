package com.gaoshin.onsalelocal.osl.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import common.db.dao.ConfigDao;
import common.util.web.JerseyBaseResource;

@Path("/ws/misc")
@Component
public class MiscResource extends JerseyBaseResource {
	@Autowired
	private ConfigDao configDao;

	@Path("versions")
	@GET
	public SupportedVersions getVersions() {
		SupportedVersions ver = new SupportedVersions();
		String min = configDao.get("version.min", "0");
		String max = configDao.get("version.max", "999999");
		try {
			ver.setMin(Integer.parseInt(min));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ver.setMax(Integer.parseInt(max));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ver;
	}

	public static class SupportedVersions {
		private int min;
		private int max;

		public int getMin() {
			return min;
		}

		public void setMin(int min) {
			this.min = min;
		}

		public int getMax() {
			return max;
		}

		public void setMax(int max) {
			this.max = max;
		}
	}
}
