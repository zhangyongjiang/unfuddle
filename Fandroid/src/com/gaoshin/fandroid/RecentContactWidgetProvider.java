package com.gaoshin.fandroid;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.ContactsContract;

import com.gaoshin.fandroid.contact.AndroidContact;
import com.gaoshin.fandroid.contact.AndroidContactHelper;
import com.gaoshin.sorma.AndroidContentResolver;

public class RecentContactWidgetProvider extends WidgetProvider {

    @Override
    protected List<RecentItem> getRecents(Context context) {
        List<RecentItem> items = new ArrayList<RecentItem>();
        try {
            int max = 16;
            List<AndroidContact> recents = AndroidContactHelper.recents(new AndroidContentResolver(context.getContentResolver()), max);
            for (AndroidContact contact : recents) {
                RecentItem item = new RecentItem();

                Intent clickIntent = new Intent(context, ContactActivity.class);
                clickIntent.setData(Uri.parse(String.valueOf(contact.getId())));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                item.setIntent(pendingIntent);

                Uri uri = ContentUris.withAppendedId(
                        ContactsContract.Contacts.CONTENT_URI, contact.getId());
                InputStream input = ContactsContract.Contacts
                        .openContactPhotoInputStream(context.getContentResolver(), uri);
                Bitmap photo = BitmapFactory.decodeStream(input);
                if (input != null) {
                    input.close();
                    int w = photo.getWidth(), h = photo.getHeight();

                    Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                    Canvas canvas = new Canvas(output);

                    final int color = 0xff424242;
                    final Paint paint = new Paint();
                    final Rect rect = new Rect(0, 0, w, h);
                    final RectF rectF = new RectF(rect);
                    final float roundPx = 12;

                    paint.setAntiAlias(true);
                    canvas.drawARGB(0, 0, 0, 0);
                    paint.setColor(color);
                    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

                    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                    canvas.drawBitmap(photo, rect, rect, paint);
                    photo.recycle();
                    photo = output;
                }

                item.setText(contact.getDisplayName());
                item.setIcon(photo);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

}
