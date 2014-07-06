package com.gaoshin.top;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gaoshin.shortcut.R;

public class SettingsActivity extends ShortcutActivity {
    private RadioGroup typeSelector;
    private RadioButton visibleTopBtn;
    private RadioButton invisibleTopBtn;
    private RadioButton oneClickBtn;
    private RadioButton twoClicksBtn;
    private RadioGroup clickMode;
    private RadioButton independentBtn;
    private RadioButton linkedBtn;
    private RadioGroup linkMode;
    private RadioButton portraitBtn;
    private RadioButton landscapeBtn;
    private RadioButton bothBtn;
    private RadioGroup orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        setupTopBtnType();
        setupClickMode();
        setupLinkMode();
        setupTransparency();
        setupOrientation();
    }

    private void setupTransparency() {
        int transparency = getApp().getConfService().get(ConfigEnum.Transparency, 125).getIntValue();
        EditText field = (EditText) findViewById(R.id.transparency);
        field.setText(String.valueOf(transparency));
    }

    private void setupTopBtnType() {
        visibleTopBtn = (RadioButton) findViewById(R.id.visibleTopBtn);
        invisibleTopBtn = (RadioButton) findViewById(R.id.invisibleTopBtn);
        typeSelector = (RadioGroup) findViewById(R.id.shortcutType);

        boolean showTopBtn = getApp().getConfService().get(ConfigEnum.ShowTopButton, true).getBoolean();
        if (showTopBtn) {
            visibleTopBtn.setChecked(true);
        } else {
            invisibleTopBtn.setChecked(true);
        }
    }

    private void setupClickMode() {
        oneClickBtn = (RadioButton) findViewById(R.id.oneClick);
        twoClicksBtn = (RadioButton) findViewById(R.id.twoClicks);
        clickMode = (RadioGroup) findViewById(R.id.clickMode);

        boolean oneClickMode = getApp().getConfService().get(ConfigEnum.OneClickMode, true).getBoolean();
        if (oneClickMode) {
            oneClickBtn.setChecked(true);
        } else {
            twoClicksBtn.setChecked(true);
        }
    }

    private void setupLinkMode() {
        independentBtn = (RadioButton) findViewById(R.id.independent);
        linkedBtn = (RadioButton) findViewById(R.id.linked);
        linkMode = (RadioGroup) findViewById(R.id.linkMode);

        boolean independentBar = getApp().getConfService().get(ConfigEnum.IndependentBar, true).getBoolean();
        if (independentBar) {
            independentBtn.setChecked(true);
        } else {
            linkedBtn.setChecked(true);
        }
    }

    private void setupOrientation() {
        portraitBtn = (RadioButton) findViewById(R.id.portrait);
        landscapeBtn = (RadioButton) findViewById(R.id.landscape);
        bothBtn = (RadioButton) findViewById(R.id.both);
        orientation = (RadioGroup) findViewById(R.id.orientation);

        Configuration conf = getApp().getConfService().get(ConfigEnum.Orientation, "Portrait");
        if ("Both".equals(conf.getValue())) {
            bothBtn.setChecked(true);
        } else if ("Portrait".equals(conf.getValue())) {
            portraitBtn.setChecked(true);
        } else if ("Landscape".equals(conf.getValue())) {
            portraitBtn.setChecked(true);
        }
    }

    @Override
    protected void onPause() {
        {
            Configuration conf = getApp().getConfService().get(ConfigEnum.ShowTopButton, true);
            boolean checked = visibleTopBtn.isChecked();
            conf.setValue(checked);
            getApp().getConfService().save(conf);
        }

        {
            Configuration oneClickConf = getApp().getConfService().get(ConfigEnum.OneClickMode, true);
            boolean oneClickMode = oneClickBtn.isChecked();
            oneClickConf.setValue(oneClickMode);
            getApp().getConfService().save(oneClickConf);
        }

        {
            Configuration indenpendentConf = getApp().getConfService().get(ConfigEnum.IndependentBar, true);
            boolean indenpendent = independentBtn.isChecked();
            indenpendentConf.setValue(indenpendent);
            getApp().getConfService().save(indenpendentConf);
        }

        {
            Configuration conf = getApp().getConfService().get(ConfigEnum.Orientation, "Portrait");
            if (bothBtn.isChecked())
                conf.setValue("Both");
            else if (landscapeBtn.isChecked())
                conf.setValue("Landscape");
            else
                conf.setValue("Portrait");
            getApp().getConfService().save(conf);
        }

        {
            try {
                EditText field = (EditText) findViewById(R.id.transparency);
                int transparency = Integer.parseInt(field.getEditableText().toString());
                Configuration configuration = getApp().getConfService().get(ConfigEnum.Transparency, 125);
                configuration.setValue(transparency);
                getApp().getConfService().save(configuration);
            } catch (Exception e) {
                Toast.makeText(this, "Cannot set transparency. Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        setChanged();
        super.onPause();
    }
}
