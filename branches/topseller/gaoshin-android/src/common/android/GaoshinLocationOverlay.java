package common.android;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class GaoshinLocationOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public GaoshinLocationOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
	}

	public void addOverlay(OverlayItem overlay, Drawable marker) {
		overlay.setMarker(boundCenterBottom(marker));
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
}
