package common.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import common.android.contentprovider.Configuration;
import common.util.web.JsonUtil;

public class GenericActivity extends Activity {
    private JSONObject config = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String confStr = getIntent().getStringExtra(this.getClass().getName() + ".input");
        try {
            config = JsonUtil.toJavaObject(confStr, JSONObject.class);
            if (config == null)
                config = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setWindow();
    }

    private void setWindow() {
        if (getConf(this.getClass().getName() + "." + ConfKeyList.FullScreen.name()).getBoolean(false)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (getConf(this.getClass().getName() + "." + ConfKeyList.NoTitle.name()).getBoolean(false)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    public Configuration getConf(String key) {
        String value = null;
        if (config.has(key)) {
            try {
                value = config.getString(key);
            } catch (JSONException e) {
            }
        }

        if (value == null) {
            GenericApplication app = (GenericApplication) getApplication();
            return app.getConf(key);
        } else {
            return new Configuration(key, value);
        }
    }

    public Configuration getConf(Enum key) {
        return getConf(key.name());
    }
}
