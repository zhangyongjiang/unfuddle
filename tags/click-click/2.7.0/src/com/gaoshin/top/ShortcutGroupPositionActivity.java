package com.gaoshin.top;

import java.util.List;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.gaoshin.shortcut.R;
import com.gaoshin.shortcut.ads.BarIconOrder;
import com.gaoshin.shortcut.ads.BarOrientation;
import com.gaoshin.shortcut.ads.ShortcutView;

public class ShortcutGroupPositionActivity extends ShortcutActivity {
    private static final String tag = ShortcutGroupPositionActivity.class.getSimpleName();

    private ImageView topBtn;
    private RelativeLayout framelayout;
    private int iconWidth = 48;
    private int iconHeight = 48;
    private int displayWidth;
    private int displayHeight;
    private ShortcutGroup currentGroup;
    private LinearLayout scIconContainer;
    private List<Shortcut> shortcutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int groupId = getIntent().getIntExtra("groupId", 0);
        currentGroup = orm.getObject(ShortcutGroup.class, "_id=?", new String[] { groupId + "" });

        setContentView(R.layout.shortcut_group_position);
        framelayout = (RelativeLayout) findViewById(R.id.shortcutGroupLayout);

        Display display = getWindowManager().getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();

        scIconContainer = (LinearLayout) findViewById(R.id.shortcuts);
        shortcutList = orm.getObjectList(Shortcut.class, "groupId=" + groupId, null, "sequence");
        createListener();

        topBtn = (ImageView) findViewById(R.id.topBtn);
        byte[] icon = currentGroup.getIcon();
        if (icon != null) {
            BitmapDrawable bitmap = new BitmapDrawable(BitmapFactory.decodeByteArray(icon, 0, icon.length));
            topBtn.setImageDrawable(bitmap);
        } else if (currentGroup.getIconId() != null) {
            topBtn.setImageResource(currentGroup.getIconId());
        } else {
            topBtn.setImageResource(R.drawable.for_trans_64);
        }
        RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) topBtn.getLayoutParams();
        params.leftMargin = currentGroup.getX();
        params.topMargin = currentGroup.getY();
        topBtn.setLayoutParams(params);
        topBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startDrag(event);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    endDrag(event);
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    drag(event);
                }
                return true;
            }
        });

        alignButtons();
        Button changeDirectionBtn = (Button) findViewById(R.id.changeDirectionBtn);
        changeDirectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BarOrientation.Vertical.equals(currentGroup.getOrientation())) {
                    if (BarIconOrder.IconFirst.equals(currentGroup.getIconOrder())) {
                        currentGroup.setOrientation(BarOrientation.Horizontal);
                        currentGroup.setIconOrder(BarIconOrder.ShortcutFirst);
                    }
                    else {
                        currentGroup.setOrientation(BarOrientation.Horizontal);
                        currentGroup.setIconOrder(BarIconOrder.IconFirst);
                    }
                }
                else {
                    if (BarIconOrder.IconFirst.equals(currentGroup.getIconOrder())) {
                        currentGroup.setOrientation(BarOrientation.Vertical);
                        currentGroup.setIconOrder(BarIconOrder.IconFirst);
                    }
                    else {
                        currentGroup.setOrientation(BarOrientation.Vertical);
                        currentGroup.setIconOrder(BarIconOrder.ShortcutFirst);
                    }
                }
                orm.update(currentGroup);
                alignButtons();
                setChanged();
            }
        });
    }

    private void alignButtons() {
        Log.i(tag, currentGroup.getOrientation() + " " + currentGroup.getIconOrder());
        if (BarOrientation.Horizontal.equals(currentGroup.getOrientation())) {
            scIconContainer.setOrientation(LinearLayout.HORIZONTAL);
            if (BarIconOrder.IconFirst.equals(currentGroup.getIconOrder())) {
                RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.RIGHT_OF, R.id.topBtn);
                lp.addRule(RelativeLayout.ALIGN_TOP, R.id.topBtn);
                scIconContainer.setLayoutParams(lp);
            } else {
                RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams iconLp = (LayoutParams) topBtn.getLayoutParams();
                lp.leftMargin = iconLp.leftMargin - 80 * shortcutList.size();
                lp.topMargin = iconLp.topMargin;
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                scIconContainer.setLayoutParams(lp);
            }
        } else {
            scIconContainer.setOrientation(LinearLayout.VERTICAL);
            if (BarIconOrder.IconFirst.equals(currentGroup.getIconOrder())) {
                RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, R.id.topBtn);
                lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.topBtn);
                scIconContainer.setLayoutParams(lp);
            } else {
                RelativeLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams iconLp = (LayoutParams) topBtn.getLayoutParams();
                lp.leftMargin = iconLp.leftMargin;
                lp.topMargin = iconLp.topMargin - 80 * shortcutList.size();
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                scIconContainer.setLayoutParams(lp);
            }
        }
    }

    private void startDrag(MotionEvent event) {
    }

    private void endDrag(MotionEvent event) {
        orm.update(currentGroup);
        setChanged();
    }

    private void drag(MotionEvent event) {

        float rawX = event.getRawX();
        float rawY = event.getRawY();
        float x = event.getX();
        float y = event.getY();

        int marginX = (int) rawX;
        if (marginX + iconWidth > displayWidth) {
            marginX = displayWidth - iconWidth;
        }
        int halfw = displayWidth / 2;
        if (halfw >= marginX && (marginX + 80) >= halfw) {
            marginX = halfw - 40;
        } else {
            marginX = (marginX / 80) * 80;
            if (marginX + 80 > displayWidth) {
                marginX = displayWidth - 80;
            }
        }

        int marginY = (int) rawY;
        if (marginY + iconHeight > displayHeight) {
            marginY = displayHeight - iconHeight;
        }
        int halfh = displayHeight / 2;
        if (halfh >= marginY && (marginY + 80) >= halfh) {
            marginY = halfh - 40;
        } else {
            marginY = (marginY / 80) * 80;
            if (marginY + 80 > displayHeight) {
                marginY = displayHeight - 80;
            }
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = marginX;
        lp.topMargin = marginY;
        lp.width = 80;
        lp.height = 80;
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        framelayout.updateViewLayout(topBtn, lp);

        currentGroup.setX(marginX);
        currentGroup.setY(marginY);
        alignButtons();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        intent.putExtra("groupId", currentGroup.getId());
        setResult(RESULT_OK, intent);
        super.onDestroy();
    }

    private class ReorderClickListener implements OnClickListener {
        private int index;

        public ReorderClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            int next = index + 1;
            if (next >= shortcutList.size())
                next = 0;
            Shortcut tmp = shortcutList.get(index);
            shortcutList.set(index, shortcutList.get(next));
            shortcutList.set(next, tmp);
            createListener();
        }
    }

    private void createListener() {
        scIconContainer.removeAllViews();
        for (int i = 0; i < shortcutList.size(); i++) {
            Shortcut sc = shortcutList.get(i);
            ShortcutView icon = new ShortcutView(getBaseContext(), sc);
            ReorderClickListener listener = new ReorderClickListener(i);
            icon.setOnClickListener(listener);
            scIconContainer.addView(icon);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < shortcutList.size(); i++) {
            shortcutList.get(i).setSequence(i);
            orm.update(shortcutList.get(i));
        }
    }
}
