package common.util.web;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class ResourceTester {
	protected static EmbeddedJetty server = null;
	protected static final String contextPath = "/bdserver";
	protected static WebResource resource = null;
	protected static final String Password = "password";
	private static int serverRefCount = 0;
	private String userHeader;
	
	@BeforeClass
	public synchronized static void setup() throws Exception {
		initializeExternalDB();
		if(server != null) {
			serverRefCount++;
			return;
		}
		server = new EmbeddedJetty();
		JettyWebAppContext jwac = new JettyWebAppContext();
		jwac.setResourceBase("./src/test/webapp");
		jwac.setContextPath(contextPath);
		jwac.setExtraClassPath("./src/test/resources,./src/main/resources");
		server.addWebAppContext(jwac.getWebAppContext());
		server.start();
		System.out.println("Started embedded Jetty. " + server);

		// Configure Logging
		String logging = "org.apache.commons.logging";
		System.setProperty(logging + ".Log", logging + ".impl.SimpleLog");
//		System.setProperty(logging + ".logging.simplelog.showdatetime", "true");
		System.setProperty(logging + ".simplelog.log.httpclient.wire", "debug");
//		System.setProperty(logging + ".simplelog.log.org.apache.commons.httpclient", "debug");

		DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
		config.getProperties().put(
				DefaultApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
		Client client = ApacheHttpClient.create(config);
		String uri = server.getBaseUri(contextPath);
		resource = client.resource(uri);
		
		serverRefCount ++;
	}

	@AfterClass
	public synchronized static void shutdown() throws Exception {
		dropExternalDB();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				serverRefCount--;
				if(serverRefCount <= 0) {
					try {
						server.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
					server = null;
					serverRefCount = 0;
				}
			}
		});
		thread.start();
	}

	protected void print(String msg) {
		System.out.println("====================" + msg);
	}
	
	protected WebResource getWebResource(String path) {
        WebResource webResource = resource.path(path);
        if(getUserHeader() != null) {
            webResource.header("hu", userHeader);
        }
        return webResource;
	}

	protected Builder getBuilder(String path, String... queryParameters) {
		WebResource webResource = getWebResource(path);
		if(queryParameters != null) {
			for(int i=0; i<queryParameters.length; i=i+2) {
				webResource = webResource.queryParam(queryParameters[i], queryParameters[i+1]);
			}
		}
		return webResource.header("Content-type", "application/json;charset=utf-8").accept("application/json;charset=utf-8");
	}
	
	protected Builder getBuilder(String path) {
        WebResource webResource = getWebResource(path);
		return webResource.header("Content-type", "application/json;charset=utf-8").accept("application/json;charset=utf-8");
	}
	
	protected void post(String path) {
		getBuilder(path).post(" ");
	}
	
	protected <T> T post(String path, Class<T> respClass) {
		return getBuilder(path).post(respClass, " ");
	}
	
	protected String getCurrentTimeMillisString() {
		return System.currentTimeMillis() + "";
	}
	
	private static void initializeExternalDB() {
		Connection c = null;
		Connection c1 = null;
		try {
		      Class.forName("org.hsqldb.jdbcDriver" );
		  } catch (Exception e) {
		      System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
		      e.printStackTrace();
		      return;
		  }

		  try {
			c = DriverManager.getConnection("jdbc:hsqldb:mem:photodb", "sa", "");
			Statement stmt = c.createStatement();
			int row = stmt.executeUpdate(CREATE_NPANXX_TABLE_SQL);
			stmt = c.createStatement();
			row = stmt.executeUpdate(CREATE_PHOTO_TABLE);
			stmt = c.createStatement();
			row = stmt.executeUpdate(CREATE_NPANXX_PHOTO_MAP_TABLE_SQL);
			stmt = c.createStatement();
			
			c1 = DriverManager.getConnection("jdbc:hsqldb:mem:merchantdb", "sa", "");
			Statement stmt1 = c1.createStatement();

			row = stmt1.executeUpdate(CREATE_MERCHANT_TABLE_SQL);

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				c.close(); c1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	private static void dropExternalDB() {
		Connection c = null;
		Connection c1 = null;
		try {
		      Class.forName("org.hsqldb.jdbcDriver" );
		  } catch (Exception e) {
		      System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
		      e.printStackTrace();
		      return;
		  }

		  try {
			c = DriverManager.getConnection("jdbc:hsqldb:mem:photodb", "sa", "");
			Statement stmt = c.createStatement();
			int row = stmt.executeUpdate(DROP_NPANXX_PHOTO_MAP_TABLE_SQL);
			stmt = c.createStatement();
			row = stmt.executeUpdate(DROP_PHOTO_TABLE);
			stmt = c.createStatement();
			row = stmt.executeUpdate(DROP_NPANXX_TABLE_SQL);
			
			c1 = DriverManager.getConnection("jdbc:hsqldb:mem:merchantdb", "sa", "");
			Statement stmt1 = c1.createStatement();
			row = stmt1.executeUpdate(DROP_MERCHANT_TABLE_SQL);

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				c.close(); c1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

		
	}
	
	public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    private static final String CREATE_NPANXX_TABLE_SQL = "CREATE TABLE npanxx (" +
	" npanxx int NOT NULL, " +
	" lata int DEFAULT NULL, " +
	" state varchar(2) DEFAULT NULL," +
	" country varchar(2) DEFAULT NULL," +
	" msa int DEFAULT NULL," +
	" rc varchar(100) DEFAULT NULL," +
	" ocn_type varchar(1) DEFAULT NULL," +
	" lat float DEFAULT NULL," +
	" lon float DEFAULT NULL, " +
	" PRIMARY KEY (npanxx))";
	
	private static final String DROP_NPANXX_TABLE_SQL = "drop table npanxx";

private static final String CREATE_PHOTO_TABLE = "CREATE TABLE photo (" +
	" id varchar(30) NOT NULL, " +
	" found_by varchar(30) NOT NULL," +
	" source varchar(20) DEFAULT NULL," +
	" type varchar(10) DEFAULT NULL," +
	" place varchar(100) DEFAULT NULL," +
	" city varchar(100) DEFAULT NULL," +
	" state varchar(20) DEFAULT NULL," +
	" lat float DEFAULT '0'," +
	" lon float DEFAULT '0'," +
	" title varchar," +
	" description varchar," +
	" tags varchar," +
	" license tinyint DEFAULT NULL," +
	" owner_id varchar(30) DEFAULT NULL," +
	" owner varchar(100) DEFAULT NULL," +
	" username varchar(100) DEFAULT NULL," +
	" url varchar(255) DEFAULT NULL," +
	" crop_total int DEFAULT NULL," +
	"  crop_dir varchar(1) DEFAULT NULL," +
	" PRIMARY KEY (id))"
;

	private static final String DROP_PHOTO_TABLE = "drop table photo";
	
private static final String CREATE_NPANXX_PHOTO_MAP_TABLE_SQL = "CREATE TABLE npanxx_photo_map (" +
	" npanxx int NOT NULL," +
	" photo_id varchar(30) DEFAULT NULL," +
	" dist float DEFAULT 0, " +
	" CONSTRAINT npanxx_photo_map_ibfk_1 FOREIGN KEY (npanxx) REFERENCES npanxx (npanxx) ON DELETE CASCADE ON UPDATE NO ACTION," +
	" CONSTRAINT npanxx_photo_map_ibfk_2 FOREIGN KEY (photo_id) REFERENCES photo (id) ON DELETE CASCADE ON UPDATE NO ACTION)";

private static final String DROP_NPANXX_PHOTO_MAP_TABLE_SQL = "drop table npanxx_photo_map";

private static final String CREATE_MERCHANT_TABLE_SQL = "CREATE TABLE merchants_idx (" +
		" business_id int NOT NULL ," +
		" business_name varchar(256) DEFAULT NULL," +
		" address varchar(256) DEFAULT NULL," +
		" city varchar(256) DEFAULT NULL," +
		" state varchar(5) DEFAULT NULL," +
		" zip varchar(16) DEFAULT NULL," +
		" zip5 char(5) DEFAULT NULL," +
		" county varchar(256) DEFAULT NULL," +
		" web_address varchar(256) DEFAULT NULL," +
		" phone varchar(16) NOT NULL ," +
		" fax varchar(32) DEFAULT NULL," +
		" contact_name varchar(256) DEFAULT NULL," +
		" contact_title varchar(256) DEFAULT NULL," +
		" contact_gender varchar(16) DEFAULT NULL," +
		" employee_count decimal(10,0) DEFAULT NULL," +
		" Sales_volume varchar(32) DEFAULT NULL," +
		" Division_desc varchar(512) DEFAULT NULL," +
		" SIC2_CODE_DESC varchar(512) DEFAULT NULL," +
		" SIC4_code decimal(10,0) DEFAULT NULL," +
		" SIC4_code_desc varchar(512) DEFAULT NULL," +
		" GROUP_ID int DEFAULT NULL," +
		" metro_name varchar(128) NOT NULL ," +
		" PRIMARY KEY (business_id,phone,metro_name))"; 

private static final String DROP_MERCHANT_TABLE_SQL = "drop table merchants_idx";

}