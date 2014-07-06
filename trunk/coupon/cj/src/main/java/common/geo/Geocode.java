package common.geo;


public class Geocode {
    public static float oneMileLat = 0.0145f;
    public static float[] oneMileLngTable = {
        0.0144f,        0.0144f,        0.0144f,        0.0144f,        0.0145f,        0.0145f,        0.0145f,        0.0145f,
        0.0146f,        0.0146f,        0.0146f,        0.0147f,        0.0147f,        0.0148f,        0.0149f,        0.0149f,
        0.0150f,        0.0151f,        0.0152f,        0.0153f,        0.0154f,        0.0155f,        0.0156f,        0.0157f,
        0.0158f,        0.0159f,        0.0161f,        0.0162f,        0.0163f,        0.0165f,        0.0167f,        0.0168f,
        0.0170f,        0.0172f,        0.0174f,        0.0176f,        0.0178f,        0.0181f,        0.0183f,        0.0186f,
        0.0188f,        0.0191f,        0.0194f,        0.0197f,        0.0201f,        0.0204f,        0.0208f,        0.0212f,
        0.0216f,        0.0220f,        0.0225f,        0.0229f,        0.0235f,        0.0240f,        0.0246f,        0.0252f,
        0.0258f,        0.0265f,        0.0273f,        0.0281f,        0.0289f,        0.0298f,        0.0308f,        0.0318f,
        0.0330f,        0.0342f,        0.0355f,        0.0370f,        0.0386f,        0.0403f,        0.0423f,        0.0444f,
        0.0468f,        0.0495f,        0.0525f,        0.0559f,        0.0598f,        0.0643f,        0.0696f,        0.0758f,
        0.0833f,        0.0925f,        0.1040f,        0.1187f,        0.1384f,        0.1660f,        0.2074f,        0.2765f,
        0.4147f,        0.8293f,
    };

    private float latitude;
    private float longitude;

    public Geocode() {
    }

    public Geocode(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return latitude + "^" + longitude;
    }

    public static Geocode fromString(String s) {
        float latitude = Float.parseFloat(s.split("\\^")[0]);
        float longitude = Float.parseFloat(s.split("\\^")[1]);
        Geocode geocode = new Geocode(latitude, longitude);
        return geocode;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public double distance(Geocode geocode) {
        return distance(latitude, longitude, geocode.getLatitude(), geocode.getLongitude(), 'M');
    }

    public GeoRange getRange(float miles) {
        return getRange(latitude, longitude, miles);
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        return distance(lat1, lon1, lat2, lon2, 'M');
    }

    public static float distance(float lat1, float lon1, float lat2, float lon2) {
        return (float) distance(lat1, lon1, lat2, lon2, 'M');
    }

    public static GeoRange getRange(float lat, float lng, float miles) {
        int index = Math.abs((int)lng)/2-1;
        if(index < 0) index = 0;
        float oneMileLng = Geocode.oneMileLngTable[index];
        float latDiff = Geocode.oneMileLat * miles;
        float lngDiff = oneMileLng * miles;
        float minLat = lat - latDiff;
        float maxLat = lat + latDiff;
        float minLng = lng - lngDiff;
        float maxLng = lng + lngDiff;
        GeoRange range = new GeoRange();
        range.setMinLat(minLat);
        range.setMaxLat(maxLat);
        range.setMinLng(minLng);
        range.setMaxLng(maxLng);
        return range;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static void main(String[] args) {
        double dis = distance(37.3188f, -122.029f,  37.3895f, -122.082);
        System.out.println(dis);
    }
}
