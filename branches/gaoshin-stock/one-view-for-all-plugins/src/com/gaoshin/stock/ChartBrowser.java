package com.gaoshin.stock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.model.StockGroup;

public class ChartBrowser extends Activity {
    private StockView stockView;
    private SORMA sorma;
    private StockBroadcastReceiver stockBroadcastReceiver;
    private ConfigurationServiceImpl confService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sorma = SORMA.getInstance(this, StockContentProvider.class); 
        confService = new ConfigurationServiceImpl(sorma);
        setupView();
        Toast.makeText(this, "Use menu key to hide/show left side bar.\n" +
                "Press top/left button to configure the chart.", Toast.LENGTH_LONG).show();
    }
    
    private void setupView() {
        stockView = new StockView(this);
        setContentView(stockView);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        registerStockBroadcastReceiver();
        stockView.applyData();

        if(confService.get(ConfKey.AgreeTOS) == null) {
            new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Terms of Use")
            .setMessage(
                    "PLEASE SCROLL DOWN AND READ THESE TERMS OF USE.\n\n" +
                    "The chart and data might be delayed and not acculate.\n\n" +
                    "This little software is provided as is. Please use it at your own risk.\n\n" +
                    "The service could be terminated at any time.\n\n" +
                    "THANKS FOR USING THIS LITTLE SOFTWARE. ALL THE MONEY WILL BE CONTRIBUTED TO A FUND.\n\n" +
            		"BY CONTINUING TO USE THE SERVICES, YOU ARE INDICATING " +
            		"YOUR AGREEMENT TO BE BOUND BY THE TERMS AND CONDITIONS OF THIS AGREEMENT. " +
            		"IF YOU DO NOT AGREE, YOU SHOULD IMMEDIATELY DISCONTINUE YOUR USE OF THE SERVICES.\n\n"
            		)
            .setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Configuration conf = new Configuration();
                    conf.setKey(ConfKey.AgreeTOS.name());
                    conf.setValue(true);
                    confService.save(conf );
                }
            })
            .setNegativeButton("I Disagree", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
            .show();

        }
    }
    
    public void registerStockBroadcastReceiver() {
        stockBroadcastReceiver = new StockBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        for (BroadcastAction action : BroadcastAction.class.getEnumConstants()) {
            filter.addAction(action.name());
        }
        registerReceiver(stockBroadcastReceiver, filter);
    }
    
    public class StockBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if(BroadcastAction.PluginChanged.name().equals(intent.getAction())) {
                int pluginId = intent.getIntExtra("data", 0);
                stockView.onPluginChange(pluginId);
            }
            else if(BroadcastAction.PluginDeleted.name().equals(intent.getAction())) {
                int pluginId = intent.getIntExtra("data", 0);
                stockView.onPluginDeleted(pluginId);
            }
            else if(BroadcastAction.PluginAdded.name().equals(intent.getAction())) {
                int pluginId = intent.getIntExtra("data", 0);
                stockView.onPluginAdded(pluginId);
            }
        }
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
/*        menu.removeGroup(0);
        List<StockGroup> groups = sorma.select(StockGroup.class, null, null);
        addMenu(menu, MenuEnum.AddSymbol);
        if(groups.size() == 1 ) {
            addMenu(menu, MenuEnum.NewGroup);
        }
        else if(groups.size() > 1 ) {
            SubMenu sub = addSubMenu(menu, MenuEnum.Group);
            addMenu(sub, MenuEnum.NewGroup);
            addMenu(sub, MenuEnum.DelCurrentGroup);
            addMenu(sub, MenuEnum.SwitchGroup);
        }
        addMenu(menu, MenuEnum.Help);
*/        
        return super.onPrepareOptionsMenu(menu);
    }
    
    private void addMenu(Menu menu, MenuEnum menuItem) {
        menu.add(0, menuItem.ordinal(), menu.size(), menuItem.getLabel());
    }
    
    private void addMenu(Menu menu, MenuEnum menuItem, String label) {
        menu.add(0, menuItem.ordinal(), menu.size(), label);
    }
    
    private void addMenu(SubMenu menu, MenuEnum menuItem) {
        menu.add(0, menuItem.ordinal(), menu.size(), menuItem.getLabel());
    }
    
    private SubMenu addSubMenu(Menu menu, MenuEnum menuItem) {
        return menu.addSubMenu(0, menuItem.ordinal(), menu.size(), menuItem.getLabel());
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        addMenu(menu, MenuEnum.Info);
//        return super.onCreateOptionsMenu(menu);
//    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MenuEnum.Info.ordinal()) {
            showInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void showInfo() {
        stockView.loadUrl("file:///android_asset/html/info.html");
    }
    
    private void newDataSource() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void deleteCurrentGroup() {
        int currentGroupId = confService.get(ConfKey.CurrentGroup, -1).getIntValue();
        if(currentGroupId == -1) {
            Toast.makeText(this, "No current group", Toast.LENGTH_LONG).show();
            return;
        }
        
        final StockGroup currentGroup = sorma.get(StockGroup.class, "id=?", new String[]{String.valueOf(currentGroupId)});
        String name = currentGroup.getName();
        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage("Are you sure to delete this stock group " + name + "?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sorma.delete(GroupItem.class, "groupId=" + currentGroup.getId(), null);
                    sorma.delete(StockGroup.class, "id=" + currentGroup.getId(), null);
                    stockView.applyData();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void addNewGroup() {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        alert.setTitle("Stock Group Name");

        // Set an EditText view to get user input 
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String groupName = input.getText().toString();
                if(groupName!=null && groupName.trim().length() == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ChartBrowser.this).create();
                    alertDialog.setMessage("Invalid group name.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    StockGroup stockGroup = sorma.get(StockGroup.class, "name=?", new String[]{groupName});
                    if(stockGroup!=null) {
                        Toast.makeText(getBaseContext(), "Group " + groupName + " already exists. Please pickup another name.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    
                    stockGroup = new StockGroup();
                    stockGroup.setName(groupName.trim());
                    sorma.insert(stockGroup);
                }
            }
        });

        alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private void addNewSymbol() {
        int currentGroupId = confService.get(ConfKey.CurrentGroup, -1).getIntValue();
        if(currentGroupId == -1) {
            Toast.makeText(this, "No current group", Toast.LENGTH_LONG).show();
            return;
        }
        final StockGroup currentGroup = sorma.get(StockGroup.class, "id=?", new String[]{String.valueOf(currentGroupId)});
        
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Add Stock Symbol");

        // Set an EditText view to get user input 
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String symbol = input.getText().toString();
                if(symbol!=null && symbol.trim().length() == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ChartBrowser.this).create();
                    alertDialog.setMessage("Invalid symbol name.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                symbol = symbol.toUpperCase();
                
                GroupItem groupItem = sorma.get(GroupItem.class, "groupId=" + currentGroup.getId() + " and sym=?", new String[]{symbol});
                if(groupItem!=null) {
                    Toast.makeText(getBaseContext(), "Symbol " + symbol + " already exists. Please pickup another name.", Toast.LENGTH_LONG).show();
                    return;
                }
                
                groupItem = new GroupItem();
                groupItem.setGroupId(currentGroup.getId());
                groupItem.setSym(symbol);
                sorma.insert(groupItem);
                
                stockView.addGroupItem(groupItem);
            }
        });

        alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode) {
            if(stockView.canGoBack()) {
                stockView.goBack();
                return true;
            }
        }
        if(KeyEvent.KEYCODE_MENU == keyCode) {
            stockView.toggleLeft();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onPause() {
        if(stockBroadcastReceiver != null) {
            unregisterReceiver(stockBroadcastReceiver);
            stockBroadcastReceiver = null;
        }
        super.onPause();
    }

}
