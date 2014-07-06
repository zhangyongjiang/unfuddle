package com.gaoshin.top;

import java.text.ParseException;

import org.quartz.CronExpression;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoshin.shortcut.R;
import com.gaoshin.top.plugin.LabeledTextEdit;
import common.util.web.JsonUtil;

public class ShortcutEditActivity extends ShortcutActivity {
    private static final String tag = ShortcutEditActivity.class.getSimpleName();
    
    protected Shortcut shortcut;
    protected Job job;
    protected ActivityInfo app;
    private RadioButton launchAppBtn;
    private RadioButton dispInfoBtn;
    private RadioGroup typeSelector;
    private LinearLayout editorContainer;

    private LabeledTextEdit shortcutLabel;

    private LabeledTextEdit timerLabel;

    private RadioButton killAppBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_shortcut_list);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        shortcut = JsonUtil.toJavaObject(data, Shortcut.class);
        job = orm.getObject(Job.class, "shortcut=" + shortcut.getId(), null);
        String pkg = shortcut.getPkg();
        String activity = shortcut.getActivity();

        PackageManager packageManager = null;
        try {
            app = getApp(pkg, activity);
            ImageView appIcon = (ImageView) findViewById(R.id.appIcon);
            packageManager = getPackageManager();
            appIcon.setImageDrawable(app.loadIcon(packageManager));
        } catch (Exception e1) {
            Toast.makeText(this, "Cannot start edit activity. Is it uninstalled? If so, long touch the shortcut icon will have it removed.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            PackageInfo info = packageManager.getPackageInfo(pkg, 0);

            TextView appLabel = (TextView) findViewById(R.id.activityLabel);
            appLabel.setText(app.loadLabel(packageManager));

            TextView appVersion = (TextView) findViewById(R.id.appVersion);
            appVersion.setText("Verson " + info.versionName);

            TextView activityName = (TextView) findViewById(R.id.activityName);
            if (activity.startsWith(pkg)) {
                activityName.setText(pkg + " " + activity.substring(pkg.length()));
            } else {
                activityName.setText(pkg + ", " + activity);
            }
        } catch (NameNotFoundException e) {
            Log.i(tag, "", e);
        }

        launchAppBtn = (RadioButton) findViewById(R.id.launchApp);
        killAppBtn = (RadioButton) findViewById(R.id.killApp);
        dispInfoBtn = (RadioButton) findViewById(R.id.displayInfo);
        typeSelector = (RadioGroup) findViewById(R.id.shortcutType);
        typeSelector.setOnCheckedChangeListener(new TypeChangeListener());
        if (shortcut.getType().equals(ShortcutType.Info)) {
            dispInfoBtn.setChecked(true);
        } else if (shortcut.getType().equals(ShortcutType.Kill)) {
            killAppBtn.setChecked(true);
        } else {
            launchAppBtn.setChecked(true);
        }

        editorContainer = (LinearLayout) findViewById(R.id.options);

        shortcutLabel = new LabeledTextEdit(this, "Label (optional, will be put on top of the shortcut icon):", 1);
        shortcutLabel.setValue(shortcut.getLabel());
        editorContainer.addView(shortcutLabel);

        timerLabel = new LabeledTextEdit(this, "Auto Execution Timer Expression (optional. Use it when you need to start an application automatically at a given gime. Click the menu button for timer expression foramt):", 1);
        editorContainer.addView(timerLabel);
        if (job != null)
            timerLabel.setValue(job.getCron());

        View editor = getEditor();
        if (editor != null) {
            editorContainer.addView(editor);
        }

        View delBtn = findViewById(R.id.delete);
        if (shortcut.getId() == null) {
            delBtn.setVisibility(View.GONE);
        } else {
            delBtn.setOnClickListener(new DeleteListener());
        }

        findViewById(R.id.save).setOnClickListener(new SaveListener());
        findViewById(R.id.cancel).setOnClickListener(new CancelListener());
    }

    private class DeleteListener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            new AlertDialog.Builder(ShortcutEditActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Are you sure to delete this shortcut?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (shortcut.getId() != null) {
                                orm.delete(shortcut);
                                if (job != null && job.getId() != null) {
                                    orm.delete(job);
                                    orm.delete(JobExecution.class, "job = " + job.getId(), null);
                                }
                                setChanged();
                            }
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    protected void save() {
        if (launchAppBtn.isChecked()) {
            String cron = timerLabel.getValue();
            if (cron != null && cron.trim().length() > 0) {
                cron = cron.trim();
                String expression = "0 " + cron;
                try {
                    CronExpression ce = new CronExpression(expression);
                } catch (ParseException e) {
                    new AlertDialog.Builder(ShortcutEditActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Invalid timer experssion: \n" + cron + "\nPress the menu button for more information about valid format.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }
            }
        }

        if (launchAppBtn.isChecked()) {
            shortcut.setType(ShortcutType.Launch);
        }

        if (dispInfoBtn.isChecked()) {
            shortcut.setType(ShortcutType.Info);
        }

        killAppBtn = (RadioButton) findViewById(R.id.killApp);
        if (killAppBtn.isChecked()) {
            shortcut.setType(ShortcutType.Kill);
        }

        String label = shortcutLabel.getValue();
        shortcut.setLabel(label);

        if (shortcut.getId() != null) {
            orm.update(shortcut);
        } else {
            orm.insert(shortcut);
        }

        if (launchAppBtn.isChecked()) {
            String cron = timerLabel.getValue();
            if (cron == null || cron.trim().length() == 0) {
                if (job != null && job.getId() != null) {
                    orm.delete(job);
                    orm.delete(JobExecution.class, "job = " + job.getId(), null);
                }
            } else {
                if (job == null) {
                    job = new Job();
                }
                job.setShortcut(shortcut.getId());
                job.setCron(cron);

                if (job.getId() == null) {
                    orm.insert(job);
                } else {
                    orm.update(job);
                    orm.delete(JobExecution.class, "job = " + job.getId(), null);
                }
            }
        }

        setChanged();
        finish();
    }

    private class SaveListener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            save();
        }
    }

    private class CancelListener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            finish();
        }
    }

    private class TypeChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup arg0, int arg1) {
            RadioButton launchAppBtn = (RadioButton) findViewById(R.id.launchApp);
            if (launchAppBtn.isChecked()) {
                findViewById(R.id.options).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.options).setVisibility(View.GONE);
            }
        }
    }

    protected View getEditor() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cron, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.timerHelp:
            openTimerHelpActivity();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void openTimerHelpActivity() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("file:///android_asset/html/cron_format.html"));
        startActivity(intent);
    }
}
