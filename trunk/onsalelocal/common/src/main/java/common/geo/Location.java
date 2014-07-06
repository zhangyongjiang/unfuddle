package common.geo;

public class Location {
	private Geocode geocode;
	private PostalAddress addr;

	public Geocode getGeocode() {
		return geocode;
	}

	public void setGeocode(Geocode geocode) {
		this.geocode = geocode;
	}

	public PostalAddress getAddr() {
		return addr;
	}

	public void setAddr(PostalAddress addr) {
		this.addr = addr;
	}
	
	@Override
	public String toString() {
	    return addr.toString() + "\n" + geocode.toString();
	}
}
