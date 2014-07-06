package com.gaoshin.stock;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.GroupItemsView.SymbolChangedListener;
import com.gaoshin.stock.SettingsView.SettingListener;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.model.StockGroup;
import com.gaoshin.stock.plugin.PageFinishedListener;
import com.gaoshin.stock.plugin.PageStartedListener;
import com.gaoshin.stock.plugin.PluginListWebView;

public class StockView extends RelativeLayout implements PageStartedListener, PageFinishedListener {
    private ImageView btnStart;
    private PluginListWebView pluginViewContainer;
    private TimeView timeView;
    private GroupItemsView groupItemsView;
    private Handler handler;
    private SettingsView settingsView;
    private YahooQuoteView quoteView;
    private LoadingView loadingView;

    private SORMA sorma = null;
    private Integer sym = null;
    private ConfigurationServiceImpl confService;
    private Activity activity;

    public StockView(Activity context) {
        super(context);
        this.activity = context;
        sorma = SORMA.getInstance(context, StockContentProvider.class);
        confService = new ConfigurationServiceImpl(sorma);
        handler = new Handler();
        setBackgroundColor(Color.WHITE);
        setupView();
    }

    private void setupView() {
        addStartButton();
        addTimeView();
        addSymbolListView();
        addSettingsView();
        addPluginView();
        setupQuoteView();
        setupLoadingView();
    }

    private void setupLoadingView() {
        loadingView = new LoadingView(activity);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loadingView, param);
    }

    private void setupQuoteView() {
        quoteView = new YahooQuoteView(activity);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(quoteView, param);
    }

    private void addSettingsView() {
        settingsView = new SettingsView(activity);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.RIGHT_OF, btnStart.getId());
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        settingsView.setVisibility(View.GONE);
        addView(settingsView, param);
        
        settingsView.setExitListener(new SettingListener() {
            @Override
            public void exit() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showChart();
                        pluginViewContainer.reload();
                    }
                });
            }

            @Override
            public void addSymbol() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        addNewSymbol();
                    }
                });
            }

            @Override
            public void addGroup() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        addNewGroup();
                    }
                });
            }

            @Override
            public void removeGroup() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        deleteCurrentGroup();
                    }
                });
            }

            @Override
            public void selectGroup(final Integer groupId) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Configuration currGroupConf = confService.get(ConfKey.CurrentGroup);
                        if(currGroupConf == null) {
                                currGroupConf = new Configuration();
                                currGroupConf.setKey(ConfKey.CurrentGroup.name());
                        }
                        currGroupConf.setValue(groupId);
                        confService.save(currGroupConf);
                        applyData();
                    }
                });
            }

            @Override
            public void renameGroup() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        renameCurrentGroup();
                    }
                });
            }
        });
    }

    private void addStartButton() {
        btnStart = new ImageButton(getContext());
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChartVisible()) {
                    showSettings();
                }
                else {
                    showChart();
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
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(timeView, param);
    }
    
    private void addSymbolListView() {
        groupItemsView = new GroupItemsView(getContext());
        RelativeLayout.LayoutParams param = new LayoutParams(60, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.addRule(RelativeLayout.ABOVE, timeView.getId());
        param.addRule(RelativeLayout.BELOW, btnStart.getId());
        addView(groupItemsView, param);
        groupItemsView.setSymbolChangedListener(new SymbolChangedListener() {
            @Override
            public void onSymbolChanged(Integer groupItemId) {
                if(!isChartVisible()) {
                    showChart();
                }
                sym = groupItemId;
                GroupItem groupItem = sorma.get(GroupItem.class, "id=" + sym, null);
                pluginViewContainer.setSymbol(groupItem.getSym());
                quoteView.setSymbol(groupItem.getSym());
                Configuration currGroupConf = confService.get(ConfKey.CurrentGroup);
                if(currGroupConf != null) {
                    StockGroup currGroup = sorma.get(StockGroup.class, "id=?", new String[]{currGroupConf.getValue()});
                    currGroup.setDefaultItem(groupItemId);
                    sorma.update(currGroup);
                }
            }
        });
    }
    
    private void addPluginView() {
        pluginViewContainer = new PluginListWebView(activity);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.RIGHT_OF, btnStart.getId());
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(pluginViewContainer, param);
        pluginViewContainer.setPageStartedListener(this);
        pluginViewContainer.setPageFinishedListener(this);
    }
    
    public void applyData() {
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
                pluginViewContainer.setSymbol(item.getSym());
                groupItemsView.setCurrentSelected(item.getId());
            }
            else {
                addNewSymbol();
            }
        }
    }
    
    public void addGroupItem(GroupItem groupItem) {
        sym = groupItem.getId();
        pluginViewContainer.setSymbol(groupItem.getSym());
        groupItemsView.addGroupItem(groupItem);
    }

    public void onPluginDeleted(int pluginId) {
    }

    public void onPluginAdded(int pluginId) {
    }

    public void onPluginChange(int pluginId) {
    }

    public boolean canGoBack() {
        return !isChartVisible();
    }

    public void goBack() {
        showChart();
    }
    
    public boolean isChartVisible() {
        return pluginViewContainer.getVisibility() == View.VISIBLE;
    }
    
    public void showChart() {
        settingsView.setVisibility(View.GONE);
        pluginViewContainer.setVisibility(View.VISIBLE);
        quoteView.setVisibility(View.VISIBLE);
    }
    
    public void showSettings() {
        settingsView.reload();
        settingsView.setVisibility(View.VISIBLE);
        pluginViewContainer.setVisibility(View.GONE);
        quoteView.setVisibility(View.GONE);
    }

    private void addNewSymbol() {
        int currentGroupId = confService.get(ConfKey.CurrentGroup, -1).getIntValue();
        if(currentGroupId == -1) {
            Toast.makeText(getContext(), "No current group", Toast.LENGTH_LONG).show();
            return;
        }
        final StockGroup currentGroup = sorma.get(StockGroup.class, "id=?", new String[]{String.valueOf(currentGroupId)});
        
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();
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
        });

        alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private void addNewGroup() {
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        alert.setTitle("Stock Group Name");

        // Set an EditText view to get user input 
        final EditText input = new EditText(getContext());
        alert.setView(input);

        alert.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String groupName = input.getText().toString();
                if(groupName!=null && groupName.trim().length() == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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
                        Toast.makeText(getContext(), "Group " + groupName + " already exists. Please pickup another name.", Toast.LENGTH_LONG).show();
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
                    
                    applyData();
                }
            }
        });

        alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private void deleteCurrentGroup() {
        int count = sorma.count(StockGroup.class, null, new String[0]);
        if(count < 2) {
            Toast.makeText(getContext(), "You cannot delete the last stock group", Toast.LENGTH_LONG).show();
            return;
        }
        
        final Configuration configuration = confService.get(ConfKey.CurrentGroup, -1);
        int currentGroupId = configuration.getIntValue();
        if(currentGroupId == -1) {
            Toast.makeText(getContext(), "No current group", Toast.LENGTH_LONG).show();
            return;
        }
        
        final StockGroup currentGroup = sorma.get(StockGroup.class, "id=?", new String[]{String.valueOf(currentGroupId)});
        String name = currentGroup.getName();
        new AlertDialog.Builder(getContext())
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage("Are you sure to delete this stock group " + name + "?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sorma.delete(GroupItem.class, "groupId=" + currentGroup.getId(), null);
                    sorma.delete(StockGroup.class, "id=" + currentGroup.getId(), null);
                    sorma.delete(configuration);
                    applyData();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void renameCurrentGroup() {
        final Integer groupId = confService.get(ConfKey.CurrentGroup).getIntValue();
        final StockGroup stockGroup = sorma.get(StockGroup.class, "id=" + groupId, null);
        AlertDialog alert = new AlertDialog.Builder(getContext()).create();

        alert.setTitle("Stock Group Name");

        // Set an EditText view to get user input 
        final EditText input = new EditText(getContext());
        input.setText(stockGroup.getName());
        alert.setView(input);

        alert.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String groupName = input.getText().toString();
                if(groupName!=null && groupName.trim().length() == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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
                    stockGroup.setName(groupName.trim());
                    sorma.update(stockGroup);
                    applyData();
                }
            }
        });

        alert.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    public void loadUrl(String url) {
        settingsView.loadUrl(url);
        settingsView.clearHistory();
        showSettings();
    }

    @Override
    public void onPageStarted() {
        loadingView.start();
    }

    @Override
    public void onPageFinished() {
        loadingView.stop();
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
        }
        else {
            showLeft();
        }
    }
}
