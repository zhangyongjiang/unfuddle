package com.gaoshin.stock;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockGroup;

public class ChartBrowser extends BaseActivity {
    private static final String TAG = ChartBrowser.class.getSimpleName();
    
    private StockView stockView;
    private SORMA sorma;
    private StockBroadcastReceiver stockBroadcastReceiver;
    private ConfigurationServiceImpl confService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sorma = SORMA.getInstance(this); 
        confService = new ConfigurationServiceImpl(sorma);
        setupView();
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

        if(confService.get(ConfKey.AgreeTOS) != null) {
            stockView.applyData();
            return;
        }
        
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
                
                stockView.applyData();
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
            Log.d(TAG, "StockBroadcastReceiver action: " + intent.getAction());
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
            else if(BroadcastAction.NewSymbol.name().equals(intent.getAction())) {
                int groupId = intent.getIntExtra("groupId", -1);
                addNewSymbol(groupId);
            }
            else if(BroadcastAction.NewGroup.name().equals(intent.getAction())) {
                addNewGroup();
            }
            else if(BroadcastAction.SymbolChanged.name().equals(intent.getAction())) {
                int groupItemId = intent.getIntExtra("groupItemId", -1);
                if(groupItemId != -1) {
                    stockView.setCurrentGroupItem(groupItemId);
                }
            }
            else if(BroadcastAction.PluginEnableDisabled.name().equals(intent.getAction())) {
                stockView.applyData(-1);
            }
            else if(BroadcastAction.PluginSwitched.name().equals(intent.getAction())) {
                stockView.pluginSwitched();
            }
            else if(BroadcastAction.SelectGroup.name().equals(intent.getAction())) {
                int groupId = intent.getIntExtra("groupId", -1);
                selectGroup(groupId);
            }
            else if(BroadcastAction.SelectPlugin.name().equals(intent.getAction())) {
                int pluginId = intent.getIntExtra("pluginId", -1);
                selectPlugin(pluginId);
            }
            else if(BroadcastAction.RemoveGroup.name().equals(intent.getAction())) {
                int groupId = intent.getIntExtra("groupId", -1);
                removeGroup(groupId);
            }
            else if(BroadcastAction.EditPlugin.name().equals(intent.getAction())) {
                int pluginId = intent.getIntExtra("pluginId", -1);
                editPlugin(pluginId);
            }
            else if(BroadcastAction.ShowAds.name().equals(intent.getAction())) {
                stockView.showAds();
            }
            else if(BroadcastAction.GroupItemDeleted.name().equals(intent.getAction())) {
                int groupItemId = intent.getIntExtra("groupItemId", -1);
                groupItemDeleted(groupItemId);
            }
            else if(BroadcastAction.Exit.name().equals(intent.getAction())) {
                finish();
            }
            else if(BroadcastAction.ShowMenus.name().equals(intent.getAction())) {
                if(!stockView.isMenuVisible()) {
                    stockView.toggleMenu();
                }
            }
            else if(BroadcastAction.SetMenu.name().equals(intent.getAction())) {
                String menus = intent.getStringExtra("menus");
                MenuGroupList menuGroupList = JsonUtil.toJavaObject(menus, MenuGroupList.class);
                Integer pluginId = intent.getIntExtra("pluginId", -1);
                stockView.setMenus(pluginId, menuGroupList);
            }
            else if(BroadcastAction.MenuClicked.name().equals(intent.getAction())) {
                String menu = intent.getStringExtra("menu");
                GaoshinMenuItem menuItem = JsonUtil.toJavaObject(menu, GaoshinMenuItem.class);
                stockView.onMenu(menuItem);
            }
        }
    }

    private void groupItemDeleted(int groupItemId) {
        stockView.groupItemDeleted(groupItemId);
    }
    
    private void removeGroup(final int groupId) {
        int count = sorma.count(StockGroup.class, null, new String[0]);
        if(count < 2) {
            Toast.makeText(this, "You cannot delete the last stock group", Toast.LENGTH_LONG).show();
            return;
        }
        
        final StockGroup currentGroup = sorma.get(StockGroup.class, "id=?", new String[]{String.valueOf(groupId)});
        if(currentGroup == null) {
            Toast.makeText(this, "Group doesn't exist", Toast.LENGTH_LONG).show();
            return;
        }
        
        String name = currentGroup.getName();
        new AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage("Are you sure to delete this stock group " + name + "?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sorma.delete(GroupItem.class, "groupId=" + currentGroup.getId(), null);
                    sorma.delete(StockGroup.class, "id=" + currentGroup.getId(), null);
                    final Configuration configuration = confService.get(ConfKey.CurrentGroup, 0);
                    if(configuration.getIntValue() == groupId) {
                        confService.delete(ConfKey.CurrentGroup);
                    }
                    stockView.applyData();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }
    
    private void selectGroup(int groupId) {
        Configuration currGroupConf = confService.get(ConfKey.CurrentGroup, -1);
        if(currGroupConf.getIntValue() == -1) {
                currGroupConf = new Configuration();
                currGroupConf.setKey(ConfKey.CurrentGroup.name());
        }
        else if (groupId == currGroupConf.getIntValue()) {
            return;
        }
        currGroupConf.setValue(groupId);
        confService.save(currGroupConf);
        stockView.applyData();
    }
    
    private void selectPlugin(int pluginId) {
        stockView.selectPluginByPluginId(pluginId);
    }
    
    private void editPlugin(int pluginId) {
        stockView.editPlugin(pluginId);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == MenuEnum.AddSymbol.ordinal()) {
//            addNewSymbol(null);
//            return true;
//        }
//        if (item.getItemId() == MenuEnum.Exit.ordinal()) {
//            finish();
//            return true;
//        }
//        if (item.getItemId() == MenuEnum.NewGroup.ordinal()) {
//            addNewGroup();
//            return true;
//        }
//        if (item.getItemId() == MenuEnum.Groups.ordinal()) {
//            addNewGroup();
//            return true;
//        }
//        if (item.getItemId() == MenuEnum.FullScreen.ordinal()) {
//            stockView.fullScreen();
//            return true;
//        }
//        if (item.getItemId() == MenuEnum.Settings.ordinal()) {
//            stockView.selectPlugin(-1);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
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
                    
                    Configuration currGroupConf = confService.get(ConfKey.CurrentGroup);
                    if(currGroupConf == null) {
                            currGroupConf = new Configuration();
                            currGroupConf.setKey(ConfKey.CurrentGroup.name());
                    }
                    currGroupConf.setValue(stockGroup.getId());
                    confService.save(currGroupConf);
                    stockView.applyData();
                }
            }
        });

        alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private void addNewSymbol(Integer groupId) {
        Configuration conf = confService.get(ConfKey.CurrentGroup, -1);
        int currentGroupId = conf.getIntValue();
        if(groupId == null && currentGroupId == -1) {
            Toast.makeText(this, "No current group", Toast.LENGTH_LONG).show();
            return;
        }
        if(groupId == null) {
            groupId = currentGroupId;
        }
        final StockGroup currentGroup = sorma.get(StockGroup.class, "id=?", new String[]{String.valueOf(groupId)});
        
        if(groupId != currentGroupId) {
            selectGroup(groupId);
        }
        
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Add symbol to portfolio " + currentGroup.getName());

        // Set an EditText view to get user input 
        final EditText input = new EditText(this);
        input.setMaxLines(1);
        input.setLines(1);
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
                else {
                    symbol = symbol.toUpperCase().trim();
                    
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
                    stockView.selectPlugin(0);
                }
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
        if(KeyEvent.KEYCODE_MENU == keyCode) {
            stockView.toggleMenu();
            return true;
        }
        if(KeyEvent.KEYCODE_BACK == keyCode) {
            if(stockView.isMenuVisible()) {
                stockView.toggleMenu();
                return true;
            }
            if(stockView.canGoBack()) {
                stockView.goBack();
                return true;
            }
            else {
                new AlertDialog.Builder(ChartBrowser.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
                return true;
            }
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
