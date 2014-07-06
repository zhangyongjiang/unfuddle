package com.gaoshin.top;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gaoshin.shortcut.R;

public class SettingsActivity extends ShortcutActivity {
    private RadioButton oneClickBtn;
    private RadioButton twoClicksBtn;
    private RadioGroup clickMode;
    private RadioButton independentBtn;
    private RadioButton linkedBtn;
    private RadioButton noIconBtn;
    private RadioButton useIconBtn;
    private RadioGroup notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        setupClickMode();
        setupLinkMode();
        setupInCallMode();
        setupTransparency();
        setupUserIcon();
    }

    private void setupUserIcon() {
        useIconBtn = (RadioButton) findViewById(R.id.useIcon);
        noIconBtn = (RadioButton) findViewById(R.id.noIcon);
        notification = (RadioGroup) findViewById(R.id.notification);

        boolean oneClickMode = getApp().getConfService().get(ConfigEnum.UseIcon, true).getBoolean();
        if (oneClickMode) {
            useIconBtn.setChecked(true);
        } else {
            noIconBtn.setChecked(true);
        }
    }

    private void setupTransparency() {
        int transparency = getApp().getConfService().get(ConfigEnum.Transparency, 125).getIntValue();
        EditText field = (EditText) findViewById(R.id.transparency);
        field.setText(String.valueOf(transparency));
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

        boolean independentBar = getApp().getConfService().get(ConfigEnum.IndependentBar, true).getBoolean();
        if (independentBar) {
            independentBtn.setChecked(true);
        } else {
            linkedBtn.setChecked(true);
        }
    }

    private void setupInCallMode() {
//        incallDisableBtn = (RadioButton) findViewById(R.id.inCallDisable);
//        incallEnableBtn = (RadioButton) findViewById(R.id.inCallEnabled);
//        disabledWhenInCall = (RadioGroup) findViewById(R.id.disabledWhenInCall);
//
//        boolean disabled = getApp().getConfService().get(ConfigEnum.DisabledWhenInCall, false).getBoolean();
//        if (disabled) {
//            incallDisableBtn.setChecked(true);
//        } else {
//            incallEnableBtn.setChecked(true);
//        }
    }

    @Override
    protected void onPause() {
        {
            Configuration useIconConf = getApp().getConfService().get(ConfigEnum.UseIcon, true);
            boolean oneClickMode = useIconBtn.isChecked();
            useIconConf.setValue(oneClickMode);
            getApp().getConfService().save(useIconConf);
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

//        {
//            Configuration conf = getApp().getConfService().get(ConfigEnum.DisabledWhenInCall, false);
//            boolean disabled = incallDisableBtn.isChecked();
//            conf.setValue(disabled);
//            getApp().getConfService().save(conf);
//        }

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
