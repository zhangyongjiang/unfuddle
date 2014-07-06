package com.gaoshin.top;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.gaoshin.shortcut.R;

public class IconPickerActivity extends ShortcutActivity {
    private static final int CropPictureIntentResult = 300;
    private int groupId;
    private Uri userImageUri;
    private ProgressDialog progressDialog;
    protected List<App> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.icon_picker);
        progressDialog = ProgressDialog.show(this, null, "Collecting icons ...");
        progressDialog.show();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                appList = getApplicationList(false);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                setupView();
                super.onPostExecute(result);
            }
        }.execute(new Void[0]);
    }

    private void setupView() {
        groupId = getIntent().getIntExtra("groupId", 0);
        final List<Drawable> iconList = new ArrayList<Drawable>();
        iconList.add(getResources().getDrawable(R.drawable.trans_80x80));
        PackageManager pm = getPackageManager();
        for (App app : appList) {
            if (app.getIcon() == null) {
                ComponentName cn = new ComponentName(app.getPkgName(), app.getActivityName());
                try {
                    ActivityInfo info = pm.getActivityInfo(cn, 0);
                    app.setIcon(info.loadIcon(pm));
                } catch (NameNotFoundException e) {
                }
            }
            iconList.add(app.getIcon());
        }

        GridView allapp = (GridView) findViewById(R.id.icons);
        ListAdapter adapter = new ApplicationIconListAdapter(iconList);
        allapp.setAdapter(adapter);
        allapp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                ShortcutGroup shortcutGroup = orm.getObject(ShortcutGroup.class, "_id=?", new String[] { groupId + "" });

                if (position == 0) {
                    shortcutGroup.setIcon(null);
                    shortcutGroup.setIconId(null);
                } else {
                    Drawable icon = iconList.get(position);
                    Rect rect = icon.getBounds();
                    Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    icon.draw(canvas);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(CompressFormat.PNG, 100, baos);
                    shortcutGroup.setIcon(baos.toByteArray());
                    shortcutGroup.setIconId(null);
                }
                orm.update(shortcutGroup);
                setChanged();
                finish();
            }
        });

        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        progressDialog.dismiss();
    }

    private void openGallery() {
        String actionGetContent = Intent.ACTION_GET_CONTENT;
        Intent intent = new Intent(actionGetContent);

        ContentValues values = new ContentValues();
        values.put(Media.TITLE, "User" + "_Image");
        values.put(Media.BUCKET_ID, "User");
        values.put(Media.BUCKET_DISPLAY_NAME, "image");
        userImageUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
                values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, userImageUri);

        intent.putExtra("scale", true);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 4);
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        startActivityForResult(intent, CropPictureIntentResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropPictureIntentResult) {
            try {
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "resultCode != RESULT_OK",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Bitmap photo = null;
                String filePath = null;

                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    filePath = getPath(selectedImageUri);
                    if (filePath != null) {
                        photo = BitmapFactory.decodeFile(filePath);
                    }
                }

                if (photo == null) {
                    photo = data.getParcelableExtra("data");
                }
                if (photo == null) {
                    return;
                }
                ShortcutGroup shortcutGroup = orm.getObject(ShortcutGroup.class, "_id=?", new String[] { groupId + "" });
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(CompressFormat.PNG, 100, baos);
                shortcutGroup.setIcon(baos.toByteArray());
                shortcutGroup.setIconId(null);
                orm.update(shortcutGroup);
                setChanged();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error. Please try later.", Toast.LENGTH_LONG).show();
            } finally {
                deleteTempImgFile();
            }
        }
    }

    private void deleteTempImgFile() {
        if (userImageUri != null) {
            try {
                getContentResolver().delete(userImageUri, null, null);
                userImageUri = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        String selectedImagePath;
        // 1:MEDIA GALLERY --- query from MediaStore.Images.Media.DATA
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
        } else {
            selectedImagePath = null;
        }

        if (selectedImagePath == null) {
            // 2:OI FILE Manager --- call method: uri.getPath()
            selectedImagePath = uri.getPath();
        }
        return selectedImagePath;
    }
}
