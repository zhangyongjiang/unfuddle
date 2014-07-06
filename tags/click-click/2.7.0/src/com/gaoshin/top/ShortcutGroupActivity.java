package com.gaoshin.top;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshin.shortcut.R;

public class ShortcutGroupActivity extends ShortcutActivity {
    private ShortcutGroup currentGroup;
    private GroupListAdapter groupListAdapter;
    private GridView groupListView;
    private boolean fromListActivity;
    private int groupId;
    private ShortcutIconListAdapter currentGroupIconsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shortcut_group);
        setupChangeNameButton();
        setupChangePositionButton();
        setupChangeIconButton();
        setupDeleteButton();
        setupEnableButton();
        setupNewGroupButton();
        setupGroupList();
        setupEditShortcutsButton();
        setupIconsForCurrentGroup();

        groupId = getIntent().getIntExtra("groupId", 0);
        fromListActivity = getIntent().getBooleanExtra("fromListActivity", false);

        applyData();
    }

    private void setupIconsForCurrentGroup() {
        GridView iconsContainer = (GridView) findViewById(R.id.shortcutIcons);
        currentGroupIconsAdapter = new ShortcutIconListAdapter(null);
        iconsContainer.setAdapter(currentGroupIconsAdapter);
        OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Shortcut sc = (Shortcut) currentGroupIconsAdapter.getItem(arg2);
                getApp().startShortcutEditorActivity(ShortcutGroupActivity.this, sc);
            }
        };
        iconsContainer.setOnItemClickListener(listener);
    }

    private void setupEditShortcutsButton() {
        Button btn = (Button) findViewById(R.id.editShortcuts);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromListActivity) {
                    Intent intent = new Intent();
                    intent.putExtra("groupId", currentGroup.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent(ShortcutGroupActivity.this, ShortcutListActivity.class);
                    intent.setData(Uri.parse(groupId + ""));
                    intent.putExtra("fromGroup", true);
                    startActivity(intent);
                }
            }
        });
    }

    private void applyData() {
        currentGroup = orm.getObject(ShortcutGroup.class, "_id=?", new String[] { groupId + "" });

        Button btn = (Button) findViewById(R.id.deleteGroupBtn);
        int count = orm.count(ShortcutGroup.class, null, (String[]) null);
        if (count == 1) {
            btn.setEnabled(false);
            groupListView.setVisibility(View.GONE);
        } else {
            btn.setEnabled(true);
            groupListView.setVisibility(View.VISIBLE);
        }

        TextView text = (TextView) findViewById(R.id.groupName);
        text.setText(currentGroup.getName());

        final Button enableBtn = (Button) findViewById(R.id.enableGroupBtn);
        if (currentGroup.isEnabled()) {
            enableBtn.setText("Disable");
        } else {
            enableBtn.setText("Enable");
        }

        setupGroupIcon();

        List<ShortcutGroup> groupList = orm.getObjectList(ShortcutGroup.class, null, null);
        groupListAdapter.setGroupList(groupList);
        groupListAdapter.notifyDataSetChanged();

        List<Shortcut> shortcuts = orm.getObjectList(Shortcut.class, "groupId=?", new String[] { groupId + "" }, "sequence");
        currentGroupIconsAdapter.setShortcuts(shortcuts);
        currentGroupIconsAdapter.notifyDataSetChanged();
    }

    private void setupGroupList() {
        groupListView = (GridView) findViewById(R.id.groupList);
        groupListAdapter = new GroupListAdapter(null);
        groupListView.setAdapter(groupListAdapter);
        OnItemClickListener listener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GroupIconView giv = (GroupIconView) arg1;
                groupId = giv.getShortcutGroup().getId();
                applyData();
            }
        };
        groupListView.setOnItemClickListener(listener);
    }

    private void setupNewGroupButton() {
        Button btn = (Button) findViewById(R.id.newGroupBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupId = getApp().newShortcutGroup().getId();
                applyData();

                Intent intent = new Intent(ShortcutGroupActivity.this, IconPickerActivity.class);
                intent.putExtra("groupId", currentGroup.getId());
                startActivity(intent);
            }
        });
    }

    private void setupDeleteButton() {
        Button btn = (Button) findViewById(R.id.deleteGroupBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ShortcutGroupActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure to delete this shortcut group?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                orm.delete(Shortcut.class, "groupId=?", new String[] { currentGroup.getId() + "" });
                                orm.delete(currentGroup);
                                List<ShortcutGroup> list = orm.getObjectList(ShortcutGroup.class, null, null);
                                groupId = list.get(0).getId();
                                applyData();
                                setChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void setupEnableButton() {
        final Button btn = (Button) findViewById(R.id.enableGroupBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGroup.isEnabled()) {
                    btn.setText("Enable");
                    currentGroup.setEnabled(false);
                    orm.update(currentGroup);
                }
                else {
                    btn.setText("Disable");
                    currentGroup.setEnabled(true);
                    orm.update(currentGroup);
                }
                setChanged();
            }
        });
    }

    private void setupGroupIcon() {
        byte[] icon = currentGroup.getIcon();
        ImageView view = (ImageView) findViewById(R.id.groupIcon);
        if (icon == null) {
            if (currentGroup.getIconId() == null)
                view.setImageResource(R.drawable.for_trans_64);
            else
                view.setImageResource(currentGroup.getIconId());
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(icon, 0, icon.length);
            view.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onResume() {
        applyData();
        super.onResume();
    }

    private void setupChangeIconButton() {
        Button btn = (Button) findViewById(R.id.changeIconBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShortcutGroupActivity.this, IconPickerActivity.class);
                intent.putExtra("groupId", currentGroup.getId());
                startActivity(intent);
            }
        });
    }

    private void setupChangePositionButton() {
        Button btn = (Button) findViewById(R.id.changePositionBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShortcutGroupActivity.this, ShortcutGroupPositionActivity.class);
                intent.putExtra("groupId", currentGroup.getId());
                startActivity(intent);
            }
        });
    }

    private void setupChangeNameButton() {
        Button btn = (Button) findViewById(R.id.changeNameBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGroupName();
            }
        });
    }

    private void changeGroupName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ShortcutGroupActivity.this);

        alert.setTitle("Change Shortcut Group Name");

        // Set an EditText view to get user input 
        final EditText input = new EditText(ShortcutGroupActivity.this);
        input.setText(currentGroup.getName());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                currentGroup.setName(value);
                orm.update(currentGroup);
                applyData();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("groupId", currentGroup.getId());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
