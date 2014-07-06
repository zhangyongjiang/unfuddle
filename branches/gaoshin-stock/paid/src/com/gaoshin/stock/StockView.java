package com.gaoshin.stock;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockGroup;
import com.gaoshin.stock.plugin.PluginViewContainer;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class StockView extends RelativeLayout{
    private PluginViewContainer pluginViewContainer;
    private Handler handler;

    private SORMA sorma = null;
    private Integer sym = null;
    private ConfigurationServiceImpl confService;
    private ChartBrowser context;
    private AdView adView;
    private TextView btnBuy;
    private MenuView menuView;
    
    private boolean loaded = false;

    public StockView(ChartBrowser context) {
        super(context);
        this.context = context;
        sorma = SORMA.getInstance(context);
        confService = new ConfigurationServiceImpl(sorma);
        handler = new Handler();
        setBackgroundColor(Color.WHITE);
        setupView();
    }

    private void setupView() {
        addAdView();
        addPluginView();
        addBuyView();
        addMenuView();
    }

    private void addMenuView() {
        menuView = new MenuView(context);
        menuView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(menuView, param);
    }

    private void addBuyView() {
        btnBuy = new Button(context);
        btnBuy.setId(Math.abs(btnBuy.hashCode()));
        btnBuy.setText("No Ads Version...");
        btnBuy.setTextColor(Color.BLACK);
        btnBuy.setPadding(10, 4, 10, 4);
        btnBuy.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.LEFT_OF, adView.getId());
        param.addRule(RelativeLayout.BELOW, pluginViewContainer.getId());
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(btnBuy, param);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, 
                Uri.parse("market://details?id=com.gaoshin.stock3"));
                context.startActivity(intent);
           }
        });
    }

    private void addAdView() {
        adView = new AdView(context, AdSize.BANNER, "a14ef69f6692dd6");
        adView.setId(Math.abs(adView.hashCode()));
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(adView, param);
    }
    
    private void addPluginView() {
        pluginViewContainer = new PluginViewContainer(context);
        RelativeLayout.LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.addRule(RelativeLayout.ABOVE, adView.getId());
        param.addRule(ALIGN_PARENT_TOP);
        addView(pluginViewContainer, param);
    }
    
    public void applyData() {
        if(!loaded) {
            applyData(pluginViewContainer.current());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pluginViewContainer.scrollTo(0, 0);
                }
            }, 400);
            loaded = true;
        }
    }
    
    public void applyData(int current) {
        DisplayMetrics metrics = context.getApp().getDisplayMetrics();
        if(metrics.widthPixels > metrics.heightPixels) {
            btnBuy.setVisibility(View.VISIBLE);
        }
        else {
            btnBuy.setVisibility(View.GONE);
        }
        
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
    
    public void showAds() {
        if(!context.getApp().getMeta("serveAds", false).getBoolean()) {
            adView.setVisibility(View.GONE);
        }
        else {
            adView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest();
            adRequest.setTesting(context.getApp().getMeta("isTestVersion", true).getBoolean());
            adRequest.addKeyword("stock market");
            adRequest.addKeyword("stock trading");
            adRequest.addKeyword("option trading");
            adRequest.addKeyword("investment");
            adRequest.addKeyword("investment tool");
            adRequest.addKeyword("portfolio management");
            adRequest.addKeyword("stock quote");
            adRequest.addKeyword("stock chart");
            adView.loadAd(adRequest);
        }
    }

    public void pluginSwitched() {
        menuView.removeAllViews();
        pluginViewContainer.pluginSwitched();
    }

    public void selectPluginByPluginId(int pluginId) {
        pluginViewContainer.selectPluginByPluginId(pluginId);
    }

    public void editPlugin(int pluginId) {
        pluginViewContainer.editPlugin(pluginId);
    }

    public void groupItemDeleted(int groupItemId) {
    }

    public void onMenu(final GaoshinMenuItem item) {
        menuView.setVisibility(View.GONE);
        pluginViewContainer.onMenu(item);
    }
    
    public void setMenus(Integer pluginId, MenuGroupList list) {
        if(pluginId.equals(pluginViewContainer.getCurrentPlugin().getId())) {
            menuView.setMenus(list);
        }
    }
    
    public void toggleMenu() {
        if(menuView.getVisibility() == View.GONE) {
            menuView.setVisibility(View.VISIBLE);
        }
        else {
            menuView.setVisibility(View.GONE);
        }
    }

    public boolean isMenuVisible() {
        return menuView.getVisibility() == View.VISIBLE;
    }
}
