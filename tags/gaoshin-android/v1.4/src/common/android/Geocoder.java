package common.android;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import common.util.web.WebServiceUtil;

public class Geocoder {
    public static DeviceLocation reverseGeocode(double latitude, double longitude, boolean sensor) {
        DeviceLocation loc = new DeviceLocation();
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);
        loc.setTime(System.currentTimeMillis());

        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=" + sensor;
        try {
            String result = WebServiceUtil.get(new DefaultHttpClient(), url, String.class);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = jsonObject.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                // street address
                JSONArray resultType = obj.getJSONArray("types");
                if (resultType.length() == 1 && resultType.get(0).toString().equals("street_address")) {
                    String address = obj.getString("formatted_address");
                    if (address != null) {
                        if (loc.getAddress() == null || loc.getAddress().length() < address.length()) {
                            loc.setAddress(address);
                        }
                    }
                }

                JSONArray addrComps = obj.getJSONArray("address_components");
                for (int j = 0; j < addrComps.length(); j++) {
                    JSONObject component = addrComps.getJSONObject(j);
                    JSONArray types = component.getJSONArray("types");
                    if (types.length() == 2 && types.getString(0).equals("locality") && types.getString(1).equals("political")) {
                        loc.setCity(component.getString("long_name"));
                    }
                    if (types.length() == 2 && types.getString(0).equals("administrative_area_level_1") && types.getString(1).equals("political")) {
                        loc.setState(component.getString("short_name"));
                    }
                    if (types.length() == 2 && types.getString(0).equals("country") && types.getString(1).equals("political")) {
                        loc.setCountry(component.getString("short_name"));
                    }
                    if (types.length() == 1 && types.getString(0).equals("postal_code")) {
                        loc.setZipcode(component.getString("short_name"));
                    }
                }
            }
        } catch (Exception e) {
        }
        return loc;
    }

    public static DeviceLocation getGeoFromAddress(String address) {
        String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&sensor=false";
        try {
            String result = WebServiceUtil.get(new DefaultHttpClient(), url, String.class);
            JSONObject jsonObject = new JSONObject(result);
            jsonObject = (JSONObject) jsonObject.getJSONArray("results").get(0);
            jsonObject = jsonObject.getJSONObject("geometry");
            jsonObject = jsonObject.getJSONObject("location");
            DeviceLocation location = new DeviceLocation();
            location.setLatitude(jsonObject.getDouble("lat"));
            location.setLongitude(jsonObject.getDouble("lng"));
            location.setAddress(address);
            return location;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
