package com.gaoshin.onsalelocal.walmart.beans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.gaoshin.onsalelocal.api.Store;
import common.db.util.SqlUtil;
import common.util.JacksonUtil;

public class WalmartStore {
	private Address address;
	private String iD;
	private Float latitude;
	private Float longitude;
	private String phone;
	private String storeNumber;
	private String storeType;
	private String description;
	private Boolean localAdAvailable;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getLocalAdAvailable() {
		return localAdAvailable;
	}

	public void setLocalAdAvailable(Boolean localAdAvailable) {
		this.localAdAvailable = localAdAvailable;
	}
	
	public static List<WalmartStore> parseJsonFile(String file) throws Exception {
        TypeReference<List<WalmartStore>> typeRef = new TypeReference<List<WalmartStore>>() {
        };
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<WalmartStore> stores = mapper.readValue(new FileReader(file), typeRef);
        return stores;
	}
	
	public static void main(String[] args) throws Exception {
//		Map<String, Store> storeMap = new HashMap<String, Store>();
//		String path = "/Users/kzhang/tmp/walmart/";
//		for(String s : new File(path).list()) {
//			try {
//	            Integer.parseInt(s);
//            } catch (Exception e) {
//            	continue;
//            }
//			System.out.println("process file " + path + s);
//		    List<WalmartStore> stores = null;
//            try {
//	            stores = parseJsonFile(path + s);
//            } catch (Exception e) {
//            	System.err.println(e.getMessage());
//            	continue;
//            }
//		    for(WalmartStore ws : stores) {
//		    	String id = ws.getiD();
//		    	if(storeMap.containsKey(id))
//		    		continue;
//		    	Store store = new Store();
//		    	store.setName("Walmart");
//		    	store.setAddress(ws.getAddress().getStreet1());
//		    	store.setCity(ws.getAddress().getCity());
//		    	store.setState(ws.getAddress().getState());
//		    	store.setCountry(ws.getAddress().getCountry());
//		    	store.setZipcode(ws.getAddress().getZip());
//		    	store.setPhone(ws.getPhone());
//		    	store.setLatitude(new BigDecimal(ws.getLatitude()));
//		    	store.setLongitude(new BigDecimal(ws.getLongitude()));
//		    	store.setChainStoreId(ws.getStoreNumber());
//		    	store.setEmail(ws.getLocalAdAvailable().toString());
//		    	store.setId(UUID.randomUUID().toString());
//		    	
//		    	storeMap.put(id, store);
//		    }
//		}
//
//		FileWriter fw = new FileWriter("./src/main/resources/stores");
//		for(Store store : storeMap.values()) {
//			fw.write(JacksonUtil.obj2Json(store));
//			fw.write("\n");
//		}
//		fw.close();
		
		FileWriter fw = new FileWriter("./src/main/resources/stores.sql");
		FileReader fr = new FileReader("./src/main/resources/stores.txt");
		BufferedReader br = new BufferedReader(fr);
		while(true) {
			String line = br.readLine();
			if(line == null)
				break;
			Store store = JacksonUtil.json2Object(line, Store.class);
			store.setParentId("walmart");
			String sql = SqlUtil.getInsertIgnoreStatement(store);
			fw.write(sql);
			fw.write(";\n");
		}
		fr.close();
		fw.close();
    }
}
