package com.gaoshin.top;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.gaoshin.shortcut.R;
import com.gaoshin.shortcut.ads.ShortcutView;

public class ShortcutListActivity extends ShortcutActivity {
    private static final String TAG = ShortcutListActivity.class.getSimpleName();

    private static final String KeyShowClickTimeInfo = "ShowClickTimeInfo";
    public static final int ShortcutGroupPicker = 1;

    private Integer currentShortcutGroup;
    protected List<App> appList;
    private LinearLayout infoBtn;
    private boolean fromGroup = false;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = ProgressDialog.show(this, null, "Scanning applications ...");
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ShortcutListActivity.this.finish();
                }
                return false;
            }
        });
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                long t0 = System.currentTimeMillis();
                Log.i(TAG, "get app begin");
                appList = getApplicationList(false);
                long t1 = System.currentTimeMillis();
                Log.i(TAG, "get app end. costs " + (t1 - t0));
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (progressDialog != null) {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDialog = null;
                }
                setContentView(R.layout.fav_edit_gridview);
                setupView();
                displayShortcuts();
                super.onPostExecute(result);
            }
        }.execute(new Void[0]);
    }

    private void setupView() {
        getApp().hideShortcutBars();

        if (confService.get("EXPIRED", 0).getIntValue() == 1) {
            String defMsg = "This version is no longer supported. Please upgrade to latest version";
            String expireMsg = confService.get("ExpireMsg", defMsg).getValue();
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage(expireMsg);
            alertDialog.setButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int which) {
                            dialog.cancel();
                            ShortcutListActivity.this.finish();
                        }
                    });
            alertDialog.show();
            return;
        }

        Log.i(TAG, "setup view begin");

        Uri data = getIntent().getData();
        if (data != null) {
            currentShortcutGroup = Integer.parseInt(data.toString());
            fromGroup = getIntent().getBooleanExtra("fromGroup", false);
        } else {
            List<ShortcutGroup> groups = orm.getObjectList(ShortcutGroup.class, null, null);
            for (ShortcutGroup group : groups) {
                if (group.getName().equals(TopApplication.DefaultGroupName)) {
                    currentShortcutGroup = group.getId();
                    break;
                }
            }
            if (currentShortcutGroup == null) {
                currentShortcutGroup = groups.get(0).getId();
            }
        }

        Log.i(TAG, "setup adapter");
        GridView allapp = (GridView) findViewById(R.id.allapp);
        ListAdapter adapter = new ApplicationListAdapter(appList, getPackageManager());
        allapp.setAdapter(adapter);
        allapp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                AppView appView = (AppView)arg1;

                String pkg = appView.getApp().getPkgName();
                String activity = appView.getApp().getActivityName();

                Shortcut sc = new Shortcut();
                sc.setPkg(pkg);
                sc.setActivity(activity);
                sc.setType(ShortcutType.Launch);
                sc.setGroupId(currentShortcutGroup);
                sc.setSequence((int) System.currentTimeMillis() / 1000);

                boolean directAdd = true;
                if (directAdd) {
                    long id = orm.insert(sc);
                    sc.setId((int) id);
                    createShortcutView(sc);

                    View clickRemove = ShortcutListActivity.this.findViewById(R.id.clickremove);
                    View rightArrow = ShortcutListActivity.this.findViewById(R.id.right);
                    clickRemove.setVisibility(View.VISIBLE);
                    rightArrow.setVisibility(View.VISIBLE);
                    setChanged();
                } else {
                    getApp().startShortcutEditorActivity(ShortcutListActivity.this, sc);
                }
            }
        });

        boolean longClickEdit = true;
        if (longClickEdit) {
            allapp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    AppView appView = (AppView) arg1;
                    String pkg = appView.getApp().getPkgName();
                    String activity = appView.getApp().getActivityName();
                    Shortcut sc = new Shortcut();
                    sc.setPkg(pkg);
                    sc.setActivity(activity);
                    sc.setType(ShortcutType.Launch);
                    sc.setGroupId(currentShortcutGroup);
                    sc.setSequence((int) System.currentTimeMillis() / 1000);
                    getApp().startShortcutEditorActivity(ShortcutListActivity.this, sc);
                    return true;
                }
            });
        }

        infoBtn = (LinearLayout) findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGroupPage();
            }
        });

        Log.i(TAG, "end");
    }

    private void gotoGroupPage() {
        if (!fromGroup) {
            Intent intent = new Intent(ShortcutListActivity.this, ShortcutGroupActivity.class);
            intent.putExtra("groupId", currentShortcutGroup);
            intent.putExtra("fromListActivity", true);
            startActivityForResult(intent, ShortcutGroupPicker);
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if(requestCode == ShortcutGroupPicker) {
            int newGroupId = data.getIntExtra("groupId", -1);
            currentShortcutGroup = newGroupId;
        }
    }

    private void displayShortcuts() {
        Log.d(TAG, "displayShortcuts begin");
        final LinearLayout shortcutsView = (LinearLayout) findViewById(R.id.shortcuts);
        shortcutsView.removeAllViews();
        infoBtn.removeAllViews();

        ShortcutGroup shortcutGroup = orm.getObject(ShortcutGroup.class, "_id=?", new String[] { currentShortcutGroup + "" });
        LinearLayout.LayoutParams lp = new LayoutParams(48, 48);
        lp.gravity = Gravity.CENTER;

        ImageView iv = new ImageView(this);
        byte[] icon = shortcutGroup.getIcon();
        if (icon == null) {
            if (shortcutGroup.getIconId() == null)
                iv.setImageResource(R.drawable.for_trans_64);
            else
                iv.setImageResource(shortcutGroup.getIconId());
        } else {
            BitmapDrawable bitmap = new BitmapDrawable(BitmapFactory.decodeByteArray(icon, 0, icon.length));
            iv.setImageDrawable(bitmap);
        }
        infoBtn.addView(iv, lp);

        List<Shortcut> shortcuts = orm.getObjectList(Shortcut.class, "groupId=?", new String[] { currentShortcutGroup + "" }, "sequence");
        for (Shortcut sc : shortcuts) {
            createShortcutView(sc);
        }

        View clickRemove = ShortcutListActivity.this.findViewById(R.id.clickremove);
        View rightArrow = ShortcutListActivity.this.findViewById(R.id.right);
        if (shortcutsView.getChildCount() == 0) {
            clickRemove.setVisibility(View.GONE);
            rightArrow.setVisibility(View.GONE);
        } else {
            clickRemove.setVisibility(View.VISIBLE);
            rightArrow.setVisibility(View.VISIBLE);
        }

        Log.d(TAG, "displayShortcuts end");
    }

    private void createShortcutView(Shortcut sc) {
        final LinearLayout shortcutsView = (LinearLayout) findViewById(R.id.shortcuts);
        ShortcutView scv = new ShortcutView(this, sc);
        scv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortcutView scv = (ShortcutView) v;
                getApp().startShortcutEditorActivity(ShortcutListActivity.this, scv.getShortcut());
            }
        });
        scv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShortcutView scv = (ShortcutView) v;
                orm.delete(scv.getShortcut());

                Job job = orm.getObject(Job.class, "shortcut=" + scv.getShortcut().getId(), null);
                if (job != null && job.getId() != null) {
                    orm.delete(job);
                    orm.delete(JobExecution.class, "job = " + job.getId(), null);
                }

                shortcutsView.removeView(scv);
                if (shortcutsView.getChildCount() == 0) {
                    View clickRemove = ShortcutListActivity.this.findViewById(R.id.clickremove);
                    clickRemove.setVisibility(View.GONE);
                    View rightArrow = ShortcutListActivity.this.findViewById(R.id.right);
                    rightArrow.setVisibility(View.GONE);
                }
                setChanged();
                return true;
            }
        });
        shortcutsView.addView(scv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (progressDialog == null) {
            displayShortcuts();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !fromGroup) {
            preview(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.info:
            openInfoActivity();
            return true;
        case R.id.groups:
            gotoGroupPage();
            return true;
        case R.id.settings:
            openSettingsActivity();
            return true;
        case R.id.changeIcon:
            openChangeIconActivity();
            return true;
        case R.id.changePosition:
            openChangePositionActivity();
            return true;
        case R.id.newGroup:
            openNewGroupActivity();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void openNewGroupActivity() {
        currentShortcutGroup = getApp().newShortcutGroup().getId();
        displayShortcuts();

        Intent intent = new Intent(this, IconPickerActivity.class);
        intent.putExtra("groupId", currentShortcutGroup);
        startActivity(intent);
    }

    private void openChangeIconActivity() {
        Intent intent = new Intent(this, IconPickerActivity.class);
        intent.putExtra("groupId", currentShortcutGroup);
        startActivity(intent);
    }

    private void openChangePositionActivity() {
        Intent intent = new Intent(this, ShortcutGroupPositionActivity.class);
        intent.putExtra("groupId", currentShortcutGroup);
        startActivity(intent);
    }

    protected void openInfoActivity() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("file:///android_asset/html/about.html"));
        startActivity(intent);
    }

    protected void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void preview() {
        preview(false);
    }

    protected void preview(final boolean exit) {
        setVisible(false);
        getApp().updateShortcuts();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getApp().displayShortcutBars();
                getApp().enterPreviewMode();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getApp().exitPreviewMode();
                        getApp().hideShortcutBars();
                        if (exit) {
                            Toast.makeText(getBaseContext(), "You can enable/disable shortcuts by shaking your device couple times", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            setVisible(true);
                        }
                    }
                }, 2000);
            }
        }, 200);
    }
}