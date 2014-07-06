package com.gaoshin.shortcut.ads;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gaoshin.shortcut.R;
import com.gaoshin.top.BroadcastAction;
import com.gaoshin.top.Shortcut;
import com.gaoshin.top.ShortcutGroup;
import com.gaoshin.top.ShortcutGroupActivity;
import com.gaoshin.top.TopViewMode;
import common.util.web.JsonUtil;

public class ShortcutBar extends LinearLayout {
    private static final String TAG = ShortcutBar.class.getSimpleName();

    private TopViewMode previousMode;

    private List<ShortcutView> shortcutViews = new ArrayList<ShortcutView>();
    protected TopButton topBtn;
    protected FrameLayout scrollView;
    protected LinearLayout shortcutsContainer;
    private TopViewMode mode;
    private Handler handler;
    private boolean oneClickMode;
    private boolean showTopBtn = false;
    private ShortcutGroup shortcutGroup;
    private Drawable bitmap;

    private int rootViewHashCode;

    private Drawable getUpDrawable() {
        if (!showTopBtn)
            return getContext().getResources().getDrawable(R.drawable.trans_80x56);
        else if (bitmap != null)
            return bitmap;
        else {
            return null;
            //return getContext().getResources().getDrawable(R.drawable.favorite_80x56);
        }
    }

    private Drawable getDownDrawable() {
        if (bitmap != null)
            return bitmap;
        else
            return getContext().getResources().getDrawable(R.drawable.favorite_80x56);
    }

    public ShortcutBar(Context context, ShortcutGroup shortcutGroup, int transparency) {
        super(context);
        this.shortcutGroup = shortcutGroup;
        handler = new Handler();
        setOrientation(shortcutGroup.getOrientation().getOrientation());

        byte[] icon = shortcutGroup.getIcon();
        if (icon != null) {
            bitmap = new BitmapDrawable(BitmapFactory.decodeByteArray(icon, 0, icon.length));
        } else if (shortcutGroup.getIconId() != null) {
            bitmap = getContext().getResources().getDrawable(shortcutGroup.getIconId());
        }

        topBtn = new TopButton(context, 48, transparency);
        topBtn.setImageDrawable(getUpDrawable());

        TouchListener touchListener = new TouchListener();
        topBtn.setOnLongClickListener(touchListener);
        topBtn.setOnTouchListener(touchListener);

        if(getOrientation()==LinearLayout.HORIZONTAL) {
            scrollView = new HorizontalScrollView(context);
        } else {
            scrollView = new ScrollView(context);
        }
        shortcutsContainer = new LinearLayout(context);
        shortcutsContainer.setOrientation(shortcutGroup.getOrientation().getOrientation());
        scrollView.addView(shortcutsContainer);

        if (shortcutGroup.getIconOrder().equals(BarIconOrder.IconFirst)) {
            addView(topBtn);
            addView(scrollView);
        } else {
            addView(scrollView);
            addView(topBtn);
        }

        setMode(TopViewMode.COMPACT);
    }
    
    private class TouchListener implements View.OnTouchListener, View.OnLongClickListener {
        private int count;
        private boolean longClicked = false;

        @Override
        public boolean onLongClick(View v) {
            Log.i(TAG, "LongClick");

            boolean showAll = false;
            if (showAll) {
                showAll();
            } else {
                Intent intent = new Intent(getContext(), ShortcutGroupActivity.class);
                intent.putExtra("groupId", shortcutGroup.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
            return false;
        }

        private void showAll() {
            if (TopViewMode.ShowAll.equals(mode)) {
                setMode(TopViewMode.COMPACT);
            } else {
                setMode(TopViewMode.ShowAll);
                Toast.makeText(getContext(), "Shortcut icons now are always on top. To revert, make a long touching on the corner.", Toast.LENGTH_LONG).show();
            }
            longClicked = true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (longClicked) {
                    Log.i(TAG, "LongClicked");
                    longClicked = false;
                    count = 0;
                } else {
                    Log.i(TAG, "UP " + count);
                    if (oneClickMode) {
                        userClick();
                    } else {
                        if (count == 1) {
                            userClick();
                        } else {
                            Log.i(TAG, "UP " + count);
                            if (count == 0) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        count = 0;
                                    }
                                }, 800);
                            }
                        }
                        count++;
                    }
                }
            }

            return false;
        }

        private void userClick() {
            if (shortcutViews.size() == 1) {
                getShortcutViews().get(0).getShortcut().exec(getContext());
            } else {
                Intent intent = new Intent(BroadcastAction.StarClicked.name());
                intent.putExtra("data", getGroupId().toString());
                getContext().sendBroadcast(intent);
                topBtnAction();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow-----------------");
        Intent intent = new Intent(BroadcastAction.ShortcutBarDetached.name());
        intent.putExtra("groupId", shortcutGroup.getId());
        getContext().sendBroadcast(intent);
        super.onDetachedFromWindow();
    }

    private void topBtnAction() {
        if (TopViewMode.ShowAll.equals(mode)) {
            return;
        }

        if (TopViewMode.VIEW.equals(mode)) {
            setMode(TopViewMode.COMPACT);
        } else {
            setMode(TopViewMode.VIEW);
        }
    }

    public void restore() {
        if (TopViewMode.ShowAll.equals(mode)) {
            return;
        }
        if (TopViewMode.VIEW.equals(mode)) {
            setMode(TopViewMode.COMPACT);
        }
    }

    public List<ShortcutView> getShortcutViews() {
        return shortcutViews;
    }

    public void removeAllViews() {
        shortcutViews.clear();
        shortcutsContainer.removeAllViews();
    }

    public void hide() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                setVisibility(View.GONE);
            }
        });
    }

    public void show() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                setVisibility(View.VISIBLE);
            }
        });
    }

    public void setMode(final TopViewMode mode) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mode.equals(ShortcutBar.this.mode)) {
                    return;
                }
                Log.i(TAG, "setMode " + mode);

                ShortcutBar.this.mode = mode;
                if (TopViewMode.COMPACT.equals(mode)) {
                    topBtn.setImageDrawable(getUpDrawable());
                    shortcutsContainer.setVisibility(View.GONE);
                } else {
                    topBtn.setImageDrawable(getDownDrawable());
                    shortcutsContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void addShortcut(final Shortcut sc) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ShortcutView view = new ShortcutView(getContext(), sc);
                shortcutViews.add(view);
                shortcutsContainer.addView(view);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            ShortcutView view = (ShortcutView) v;
                            view.getShortcut().exec(getContext());
                            shortcutClicked(view.getShortcut());

                            Intent intent = new Intent(BroadcastAction.ShortcutClicked.name());
                            intent.putExtra("data", JsonUtil.toJsonString(view.getShortcut()));
                            getContext().sendBroadcast(intent);
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Cannot launch application. Is it uninstalled?", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    public void shortcutClicked(Shortcut sc) {
        if (TopViewMode.VIEW.equals(mode)) {
            setMode(TopViewMode.COMPACT);
        }
    }

    public int size() {
        return shortcutViews.size();
    }

    public TopViewMode getMode() {
        return mode;
    }

    public Integer getGroupId() {
        return shortcutGroup.getId();
    }

    public void starClicked(Integer groupId2) {
        if (shortcutGroup.getId().equals(groupId2)) {
            return;
        }
        topBtnAction();
    }

    public void enterPreviewMode() {
        previousMode = mode;
        setMode(TopViewMode.ShowAll);
    }

    public void exitPreviewMode() {
        if (previousMode == null) {
            return;
        }
        setMode(previousMode);
    }

    public void setOneClickMode(boolean oneClickMode) {
        this.oneClickMode = oneClickMode;
    }

    public boolean isOneClickMode() {
        return oneClickMode;
    }

    public void setShowTopBtn(boolean showTopBtn) {
        this.showTopBtn = showTopBtn;
        topBtn.setImageDrawable(getUpDrawable());
    }

    public boolean isShowTopBtn() {
        return showTopBtn;
    }

    public void setRootViewHashCode(int rootViewHashCode) {
        this.rootViewHashCode = rootViewHashCode;
    }

    public int getRootViewHashCode() {
        return rootViewHashCode;
    }

    public void setIcon(byte[] data) {
        shortcutGroup.setIcon(data);
        if (data == null || data.length == 0) {
            bitmap = null;
        } else {
            bitmap = new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
        }
        topBtn.setImageDrawable(getUpDrawable());
    }
}
