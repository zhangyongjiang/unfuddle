package com.gaoshin.top;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;

public class ShortcutGroupPositionActivity extends ShortcutActivity {
    private static final String tag = ShortcutGroupPositionActivity.class.getSimpleName();
    private ShortcutGroupPositionView posView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int groupId = getIntent().getIntExtra("groupId", 0);
        Display display = getWindowManager().getDefaultDisplay();
        posView = new ShortcutGroupPositionView(this, groupId, display);
        setContentView(posView);
    }
    
    @Override
    protected void onPause() {
        if(posView.isChanged()) {
            setChanged();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        int groupId = getIntent().getIntExtra("groupId", 0);
        intent.putExtra("groupId", groupId);
        setResult(RESULT_OK, intent);
        super.onDestroy();
    }

}
