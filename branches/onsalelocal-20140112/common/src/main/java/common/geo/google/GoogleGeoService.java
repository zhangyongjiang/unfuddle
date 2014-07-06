package common.geo.google;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import common.crawler.CrawlerBase;
import common.geo.GeoService;
import common.geo.Geocode;
import common.geo.Location;
import common.geo.PostalAddress;
import common.geo.PostalAddressParser;
import common.util.JacksonUtil;

public class GoogleGeoService extends CrawlerBase implements GeoService {

    public Location fromLatLng(String location) throws Exception {
		String url = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&latlng=" + URLEncoder.encode(location);
		return fromUrl(url);
	}
	
	@Override
    public Location geo(String location) throws Exception {
		String url = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=" + URLEncoder.encode(location);
		return fromUrl(url);
	}
	
    private Location fromUrl(String url) throws Exception {
		Location loc = new Location();
		Geocode geocode = new Geocode();
		loc.setGeocode(geocode);
		String content = getContentFromUrl(url );
		TypeReference<HashMap<String, Object>> typeRef = JacksonUtil.getTypeRef();
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> map = mapper.readValue(content, typeRef);
		List results = (List) map.get("results");
		map = (HashMap<String, Object>) results.get(0);
		
		String[] formattedAddress = map.get("formatted_address").toString().replaceAll(", ", ",").split(",");
		String street = formattedAddress[0];
		String city = formattedAddress[1];
		String state = formattedAddress[2].split(" ")[0];
		String zip = formattedAddress[2].split(" ")[1];
		
		PostalAddress postalAddress = PostalAddressParser.parse(street, city, state);
		postalAddress.setZipcode(zip);
		loc.setAddr(postalAddress);
		
		map = (HashMap<String, Object>) map.get("geometry");
		map = (HashMap<String, Object>) map.get("location");
		geocode.setLatitude(Float.parseFloat(map.get("lat").toString()));
		geocode.setLongitude(Float.parseFloat(map.get("lng").toString()));
	    return loc;
    }

	public static void main(String[] args) throws Exception {
	    System.out.println(new GoogleGeoService().geo("1365 Todd Street, ca 94040"));
    }
}
