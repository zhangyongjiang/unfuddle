package com.gaoshin.stock;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.model.StockGroup;
import com.gaoshin.stock.plugin.Plugin;
import com.gaoshin.stock.plugin.PluginListView;
import com.gaoshin.stock.plugin.PluginViewContainer;

public class StockView extends RelativeLayout{
    private ImageView btnStart;
    private PluginViewContainer pluginViewContainer;
    private TimeView timeView;
    private GroupItemsView groupItemsView;
    private Handler handler;

    private SORMA sorma = null;
    private Integer sym = null;
    private ConfigurationServiceImpl confService;
    private ChartBrowser context;
    private HorizontalScrollView scrollView;
    private PluginListView pluginListView;

    public StockView(ChartBrowser context) {
        super(context);
        this.context = context;
        sorma = SORMA.getInstance(context, StockContentProvider.class);
        confService = new ConfigurationServiceImpl(sorma);
        handler = new Handler();
        setBackgroundColor(Color.LTGRAY);
        setupView();
    }

    private void setupView() {
        addStartButton();
        addTimeView();
        addSymbolListView();
//        addPluginListView();
        addPluginView();
    }

    private void addPluginListView() {
        scrollView = new HorizontalScrollView(getContext());
        scrollView.setId(Math.abs(scrollView.hashCode()));
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.RIGHT_OF, btnStart.getId());
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(scrollView, param);
         
        pluginListView = new PluginListView(getContext());
        scrollView.addView(pluginListView);
    }
    
    private void addStartButton() {
        btnStart = new ImageButton(getContext());
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current() == size() - 1) {
                    selectPlugin(0);
                }
                else {
                    selectPlugin(-1);
                }
            }
        });
        
        btnStart.setId(Math.abs(btnStart.hashCode()));
        btnStart.setImageResource(R.drawable.candle);
        RelativeLayout.LayoutParams param = new LayoutParams(60, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(btnStart, param);
    }

    private void addTimeView() {
        timeView = new TimeView(getContext());
        RelativeLayout.LayoutParams param = new LayoutParams(60, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.addRule(RelativeLayout.BELOW, btnStart.getId());
        addView(timeView, param);
    }
    
    private void addSymbolListView() {
        groupItemsView = new GroupItemsView(getContext());
        RelativeLayout.LayoutParams param = new LayoutParams(60, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param.addRule(RelativeLayout.BELOW, timeView.getId());
        addView(groupItemsView, param);
    }
    
    private void addPluginView() {
        pluginViewContainer = new PluginViewContainer(context);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.RIGHT_OF, btnStart.getId());
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        param.addRule(RelativeLayout.BELOW, scrollView.getId());
        
        addView(pluginViewContainer, param);
    }
    
    public void applyData() {
        applyData(0);
    }
    
    public void applyData(int current) {
//        pluginListView.applyData();
        pluginViewContainer.setSymbol(null);
        pluginViewContainer.applyData(current);
        
        Configuration currGroupConf = confService.get(ConfKey.CurrentGroup);
        StockGroup currGroup = null;
        if(currGroupConf == null) {
            List<StockGroup> list = sorma.select(StockGroup.class, null, null);
            if(list.size() > 0) {
                currGroup = list.get(0);
                currGroupConf = new Configuration();
                currGroupConf.setKey(ConfKey.CurrentGroup.name());
                currGroupConf.setValue(currGroup.getId());
                confService.save(currGroupConf);
            }
        }
        else {
            currGroup = sorma.get(StockGroup.class, "id=?", new String[]{currGroupConf.getValue()});
        }
        
        if(currGroup != null) {
            groupItemsView.applyData(currGroup);
            Integer groupDefaultItem = currGroup.getDefaultItem();
            if(groupDefaultItem != null) {
                GroupItem item = sorma.get(GroupItem.class, "id=?", new String[]{String.valueOf(groupDefaultItem)});
                if(item == null) {
                    groupDefaultItem = null;
                }
            }
            if(groupDefaultItem == null) {
                List<GroupItem> items = sorma.select(GroupItem.class, "groupId=?", new String[]{String.valueOf(currGroup.getId())});
                if(items.size() > 0) {
                    groupDefaultItem = items.get(0).getId();
                    currGroup.setDefaultItem(groupDefaultItem);
                    sorma.update(currGroup);
                }
            }
            if(groupDefaultItem != null) {
                GroupItem item = sorma.get(GroupItem.class, "id=?", new String[]{String.valueOf(groupDefaultItem)});
                pluginViewContainer.setSymbol(item.getId());
                groupItemsView.setCurrentSelected(item.getId(), false);
            }
            else {
                addNewSymbol();
            }
        }
        
        pluginSwitched();
    }
    
    public void addGroupItem(GroupItem groupItem) {
        sym = groupItem.getId();
        pluginViewContainer.setSymbol(groupItem.getId());
        groupItemsView.addGroupItem(groupItem);
    }

    public void onPluginDeleted(int pluginId) {
    }

    public void onPluginAdded(int pluginId) {
    }

    public void onPluginChange(int pluginId) {
    }

    public boolean canGoBack() {
        return pluginViewContainer.canGoBack();
    }

    public void goBack() {
        pluginViewContainer.goBack();
    }
    
    public boolean isChartVisible() {
        return pluginViewContainer.getVisibility() == View.VISIBLE;
    }
    
    private void addNewSymbol() {
        int currentGroupId = confService.get(ConfKey.CurrentGroup, -1).getIntValue();
        if(currentGroupId == -1) {
            Toast.makeText(getContext(), "No current group", Toast.LENGTH_LONG).show();
            return;
        }
        final StockGroup currentGroup = sorma.get(StockGroup.class, "id=?", new String[]{String.valueOf(currentGroupId)});
        
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle("Add a symbol to group " + currentGroup.getName());

        // Set an EditText view to get user input 
        final EditText input = new EditText(getContext());
        alert.setView(input);

        alert.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String symbol = input.getText().toString();
                if(symbol!=null && symbol.trim().length() == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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
                    symbol = symbol.toUpperCase();
                    
                    GroupItem groupItem = sorma.get(GroupItem.class, "groupId=" + currentGroup.getId() + " and sym=?", new String[]{symbol});
                    if(groupItem!=null) {
                        Toast.makeText(getContext(), "Symbol " + symbol + " already exists. Please pickup another name.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    
                    groupItem = new GroupItem();
                    groupItem.setGroupId(currentGroup.getId());
                    groupItem.setSym(symbol);
                    sorma.insert(groupItem);
                    addGroupItem(groupItem);
                }
            }
        });

        alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    public void hideLeft() {
        btnStart.setVisibility(View.GONE);
        timeView.setVisibility(View.GONE);
        groupItemsView.setVisibility(View.GONE);
    }
    
    public void showLeft() {
        btnStart.setVisibility(View.VISIBLE);
        timeView.setVisibility(View.VISIBLE);
        groupItemsView.setVisibility(View.VISIBLE);
    }
    
    public void toggleLeft() {
        if(btnStart.getVisibility() == View.VISIBLE) {
            hideLeft();
            ViewMode viewMode = ViewMode.FullScreen;
            context.getApp().setViewMode(viewMode);
        }
        else {
            showLeft();
            ViewMode viewMode = ViewMode.Normal;
            context.getApp().setViewMode(viewMode);
        }
        pluginViewContainer.adjustWidth();
    }

    public void selectPlugin(int index) {
        pluginViewContainer.selectPlugin(index);
    }

    public void moveToFirstPlugin() {
        selectPlugin(0);
    }

    public void moveToLastPlugin() {
        selectPlugin(-1);
    }
    
    public int size() {
        return pluginViewContainer.size();
    }
    
    public int current() {
        return pluginViewContainer.current();
    }

    public void setCurrentGroupItem(int groupItemId) {
        sym = groupItemId;
        Configuration currGroupConf = confService.get(ConfKey.CurrentGroup);
        if(currGroupConf != null) {
            StockGroup currGroup = sorma.get(StockGroup.class, "id=?", new String[]{currGroupConf.getValue()});
            currGroup.setDefaultItem(groupItemId);
            sorma.update(currGroup);
        }
        pluginViewContainer.setSymbol(groupItemId);
        if(pluginViewContainer.current() == (pluginViewContainer.size() - 1) && pluginViewContainer.size() > 1) {
            pluginViewContainer.selectPlugin(0);
        }
    }

    public void pluginSwitched() {
        pluginViewContainer.pluginSwitched();
        Plugin plugin = pluginViewContainer.getCurrentPlugin();
        if(plugin.getUrl().indexOf("__SYM__") == -1) {
            groupItemsView.disableCurrent();
        }
        else {
            groupItemsView.enableCurrent();
        }
    }

    public void selectPluginByPluginId(int pluginId) {
        pluginViewContainer.selectPluginByPluginId(pluginId);
    }

    public void editPlugin(int pluginId) {
        pluginViewContainer.editPlugin(pluginId);
    }
}
