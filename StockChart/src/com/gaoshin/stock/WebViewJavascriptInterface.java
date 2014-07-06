package com.gaoshin.stock;

import java.net.URLEncoder;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockGroup;
import com.gaoshin.stock.plugin.Plugin;
import com.gaoshin.stock.plugin.PluginList;
import com.gaoshin.stock.plugin.PluginParamList;
import com.gaoshin.stock.plugin.PluginParameter;
import com.gaoshin.stock.plugin.PluginType;
import com.gaoshin.stock.plugin.PluginWebView;

public abstract class WebViewJavascriptInterface {
    protected abstract PluginWebView getWebView();
    
    public void zoomIn() {
        getWebView().getHandler().post(new Runnable() {
            @Override
            public void run() {
                getWebView().zoomIn();
            }
        });
    }
    
    public void zoomOut() {
        getWebView().getHandler().post(new Runnable() {
            @Override
            public void run() {
                getWebView().zoomOut();
            }
        });
    }
    
    public void shake() {
        float oldScale = getScale();
        zoomIn();
        float newScale = getScale();
        zoomOut();
        if(Math.abs(oldScale-newScale)<0.001) {
            zoomIn();
        }
    }
    
    public void redraw() {
        getWebView().getHandler().post(new Runnable() {
            @Override
            public void run() {
                getWebView().invalidate();
            }
        });
    }
    
    public float getScale() {
        return getWebView().getScale();
    }
    
    protected Context getContext() {
        return getWebView().getContext();
    }
    
    public int getDisplayWidth() {
        return getWebView().getMeasuredWidth();
    }
    
    public int getDisplayHeight() {
        return getWebView().getMeasuredHeight();
    }
    
    public int getOffsetTop() {
        return getWebView().getScrollY();
    }
    
    public int getOffsetLeft() {
        return getWebView().getScrollX();
    }
    
    public SORMA getSorma() {
        return SORMA.getInstance(getContext()); 
    }
    
    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
    
    public void log(String msg) {
        System.out.println("webview log: " + msg);
    }
    
    public void email(String to, String subject, String msg) {
        String action = "android.intent.action.SENDTO";
        Uri uri = Uri.parse("mailto:" + to);
        Intent intent = new Intent(action, uri);
        intent.putExtra("android.intent.extra.TEXT", msg);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        getContext().startActivity(intent);
    }
    
    public String getCurrentGroup() {
        Configuration conf = getSorma().get(Configuration.class, "_key=?", new String[]{ConfKey.CurrentGroup.name()});
        if(conf == null) {
            return null;
        }
        StockGroup current = getSorma().get(StockGroup.class, "id=" + conf.getValue(), null);
        return JsonUtil.toJsonString(current);
    }
    
    public void exit() {
        Intent intent = new Intent(BroadcastAction.Exit.name());
        getContext().sendBroadcast(intent);
    }
    
    public void rename(String pluginId, String name) {
        Plugin plugin = getSorma().get(Plugin.class, "id=" + pluginId, null);
        if(plugin != null) {
            plugin.setName(name);
            getSorma().update(plugin);
        }
    }
    
    public void fullScreen() {
        Intent intent = new Intent(BroadcastAction.FullScreen.name());
        getContext().sendBroadcast(intent);
    }
    
    public void setMenus(String menus) {
        Intent intent = new Intent(BroadcastAction.SetMenu.name());
        intent.putExtra("menus", menus);
        intent.putExtra("pluginId", getWebView().getPlugin().getId());
        getContext().sendBroadcast(intent);
    }
    
    public void showMenus() {
        Intent intent = new Intent(BroadcastAction.ShowMenus.name());
        getContext().sendBroadcast(intent);
    }
    
    public String getGroupItem(String id) {
        return JsonUtil.toJsonString(getSorma().get(GroupItem.class, "id=?", new String[]{id}));
    }
    
    public void normalScreen() {
        Intent intent = new Intent(BroadcastAction.NormalScreen.name());
        getContext().sendBroadcast(intent);
    }
    
    public void addSymbol(String groupId) {
        Intent intent = new Intent(BroadcastAction.NewSymbol.name());
        intent.putExtra("groupId", Integer.parseInt(groupId));
        getContext().sendBroadcast(intent);
    }
    
    public void addGroup() {
        Intent intent = new Intent(BroadcastAction.NewGroup.name());
        getContext().sendBroadcast(intent);
    }
    
    public void renameGroup(String groupId, String newName) {
        StockGroup group = getSorma().get(StockGroup.class, "id=" + groupId, null);
        group.setName(newName);
        getSorma().update(group);
    }
    
    public void removeGroup(String groupId) {
        Intent intent = new Intent(BroadcastAction.RemoveGroup.name());
        intent.putExtra("groupId", Integer.parseInt(groupId));
        getContext().sendBroadcast(intent);
    }
    
    public void selectGroup(String groupId) {
        Intent intent = new Intent(BroadcastAction.SelectGroup.name());
        intent.putExtra("groupId", Integer.parseInt(groupId));
        getContext().sendBroadcast(intent);
    }
    
    public void selectPlugin(String pluginId) {
        Intent intent = new Intent(BroadcastAction.SelectPlugin.name());
        intent.putExtra("pluginId", Integer.parseInt(pluginId));
        getContext().sendBroadcast(intent);
    }
    
    public void selectPluginByName(String pluginName) {
        Plugin plugin = getSorma().get(Plugin.class, "name=?", new String[]{pluginName});
        selectPlugin(String.valueOf(plugin.getId()));
    }
    
    public void setCurrentGroupItem(String groupItemId) {
        Intent intent = new Intent(BroadcastAction.SymbolChanged.name());
        intent.putExtra("groupItemId", Integer.parseInt(groupItemId));
        getContext().sendBroadcast(intent);
    }
    
    public String getGroups() {
        List<StockGroup> list = getSorma().select(StockGroup.class, null, null);
        ListWrapper wrapper = new ListWrapper();
        wrapper.setItems(list);
        return JsonUtil.toJsonString(wrapper);
    }
    
    public String getSymbolsInGroup(String groupId) {
        List<GroupItem> list = getSorma().select(GroupItem.class, "groupId=?", new String[]{groupId}, "sequence desc");
        ListWrapper wrapper = new ListWrapper();
        wrapper.setItems(list);
        return JsonUtil.toJsonString(wrapper);
    }
    
    public void deleteSymbol(final String groupItemId) {
        getSorma().delete(GroupItem.class, "id=?", new String[]{groupItemId});
        Intent intent = new Intent(BroadcastAction.GroupItemDeleted.name());
        intent.putExtra("groupItemId", Integer.parseInt(groupItemId));
        getContext().sendBroadcast(intent);
    }
    
    public void getQuote(final String groupItemId, final String callback) {
        try {
            getWebView().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                GroupItem gi = getSorma().get(GroupItem.class, "id=?", new String[]{groupItemId});
                                String quote = YahooQuote.getQuote(gi.getSym());
                                getWebView().loadUrl("javascript:" + callback + "('" + groupItemId + "', '" + gi.getSym() + "', '" + URLEncoder.encode(quote) + "')");
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                        
                    }.execute(null);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String saveAs(String pluginId, String name, String setting) {
        Plugin plugin = getSorma().get(Plugin.class, "id=" + pluginId, null);
        if(plugin.getName().equals(name)) {
            return null;
        }
        plugin.setId(null);
        plugin.setSequence(System.currentTimeMillis());
        plugin.setName(name);
        getSorma().insert(plugin);
        saveChartSetting(String.valueOf(plugin.getId()), setting);
        Intent intent = new Intent(BroadcastAction.PluginEnableDisabled.name());
        getContext().sendBroadcast(intent);
        
        final String newPluginId = String.valueOf(plugin.getId());
        getWebView().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                selectPlugin(newPluginId);
            }
        }, 300);
        return newPluginId;
    }
    
    public boolean saveChartSetting(String pluginId, String setting) {
        try {
            Plugin plugin = getSorma().get(Plugin.class, "id=" + pluginId, null);
            MapWrapper map = JsonUtil.toJavaObject(setting, MapWrapper.class);
            PluginParamList paramList;
            if(plugin.getParamsJson() != null) {
                paramList = JsonUtil.toJavaObject(plugin.getParamsJson(), PluginParamList.class);
            }
            else {
                paramList = new PluginParamList();
            }
            for(PluginParameter pp : paramList.getItems()) {
                pp.setDefValue((String) map.getMap().get(pp.getName()));
            }
            plugin.setParamsJson(JsonUtil.toJsonString(paramList));
            getSorma().update(plugin);

            if(pluginId.equals(String.valueOf(getWebView().getPlugin().getId()))){
                getWebView().setPlugin(plugin);
                getWebView().setSymbol(getWebView().getSymbol());
            }
//            if(plugin.isEnabled()) {
//                selectPlugin(pluginId);
//            }
//            else {
//                new AlertDialog.Builder(getContext())
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setMessage("Done!")
//                .setNeutralButton("OK", null)
//                .show();
//
//            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public String listPlugins() {
        List<Plugin> list = getSorma().select(Plugin.class, "type!="+PluginType.System.ordinal(), null, "enabled desc, country, lower(name)");
        ListWrapper wrapper = new ListWrapper();
        wrapper.setItems(list);
        return JsonUtil.toJsonString(wrapper);
    }
    
    public String getCurrentPlugin() {
        return getPlugin(String.valueOf(getWebView().getPlugin().getId()));
    }
    
    public String getPlugin(String pluginId) {
        Plugin p = getSorma().get(Plugin.class, "id=" + pluginId, null);
        return JsonUtil.toJsonString(p);
    }
    
    public String exportPlugin(String pluginId) {
        Plugin p = getSorma().get(Plugin.class, "id=" + pluginId, null);
        p.setParamList(JsonUtil.toJavaObject(p.getParamsJson(), PluginParamList.class));
        p.setParamsJson(null);
        return JsonUtil.toJsonString(p);
    }
    
    public void emailPlugin(String pluginId) {
        Plugin p = getSorma().get(Plugin.class, "id=" + pluginId, null);
        p.setParamList(JsonUtil.toJavaObject(p.getParamsJson(), PluginParamList.class));
        p.setParamsJson(null);
        String json = JsonUtil.toJsonString(p);
        email("share@gaoshin.com", "Gaoshin Stock Data Source " + p.getName(), json);
    }
    
    public String importPlugin(String json) {
        Plugin plugin = JsonUtil.toJavaObject(json, Plugin.class);
        plugin.setParamsJson(JsonUtil.toJsonString(plugin.getParamList()));
        plugin.setId(null);
        plugin.setEnabled(true);
        plugin.setType(PluginType.Normal.ordinal());
        plugin.setSequence(System.currentTimeMillis());
        getSorma().insert(plugin);
        Intent intent = new Intent(BroadcastAction.PluginEnableDisabled.name());
        getContext().sendBroadcast(intent);
        return String.valueOf(plugin.getId());
    }
    
    public void enableDisable(String pluginId) {
        Plugin p = getSorma().get(Plugin.class, "id=" + pluginId, null);
        p.setEnabled(!Boolean.TRUE.equals(p.isEnabled()));
        getSorma().update(p);
        Intent intent = new Intent(BroadcastAction.PluginEnableDisabled.name());
        getContext().sendBroadcast(intent);
    }
    
    public void deletePlugin(String pluginId) {
        Plugin p = getSorma().get(Plugin.class, "id=" + pluginId, null);
        getSorma().delete(p);
        Intent intent = new Intent(BroadcastAction.PluginEnableDisabled.name());
        getContext().sendBroadcast(intent);
    }
    
    public String getPlugins() {
        List<Plugin> list = getSorma().select(Plugin.class, null, null);
        PluginList pl = new PluginList();
        pl.setList(list);
        return JsonUtil.toJsonString(pl);
    }
    
    public boolean addPlugin(String json) {
        try {
            Plugin plugin = JsonUtil.toJavaObject(json, Plugin.class);
            getSorma().insert(plugin);
            Intent intent = new Intent(BroadcastAction.PluginAdded.name());
            intent.putExtra("data", plugin.getId());
            getContext().sendBroadcast(intent);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public void setUseWideViewPort(String use) {
        try {
            getWebView().getSettings().setUseWideViewPort(Boolean.parseBoolean(use));
        }
        catch (Exception e) {
        }
    }
    
    public void moveUp() {
        Configuration conf = getSorma().get(Configuration.class, "_key=?", new String[]{ConfKey.CurrentGroup.name()});
        if(conf == null) {
            return;
        }
        StockGroup current = getSorma().get(StockGroup.class, "id=" + conf.getValue(), null);
        List<GroupItem> items = getSorma().select(GroupItem.class, "groupId=?", new String[] {current.getId().toString()}, "sequence desc");
        for(int i=0; i<items.size(); i++) {
            if(items.get(i).getId() == current.getDefaultItem()) {
                if(i > 0) {
                    long seq = items.get(i-1).getSequence();
                    items.get(i-1).setSequence(items.get(i).getSequence());
                    items.get(i).setSequence(seq);
                    getSorma().update(items.get(i));
                    getSorma().update(items.get(i-1));
                }
                break;
            }
        }
    }
    
    public void moveDown() {
        Configuration conf = getSorma().get(Configuration.class, "_key=?", new String[]{ConfKey.CurrentGroup.name()});
        if(conf == null) {
            return;
        }
        StockGroup current = getSorma().get(StockGroup.class, "id=" + conf.getValue(), null);
        List<GroupItem> items = getSorma().select(GroupItem.class, "groupId=?", new String[] {current.getId().toString()}, "sequence desc");
        for(int i=0; i<items.size(); i++) {
            if(items.get(i).getId() == current.getDefaultItem()) {
                if(i < (items.size()-1)) {
                    long seq = items.get(i+1).getSequence();
                    items.get(i+1).setSequence(items.get(i).getSequence());
                    items.get(i).setSequence(seq);
                    getSorma().update(items.get(i));
                    getSorma().update(items.get(i+1));
                }
                break;
            }
        }
    }
    
    public void saveConf(String key, String value) {
        ConfigurationServiceImpl confService = new ConfigurationServiceImpl(getSorma());
        Configuration conf = confService.get(key, value);
        conf.setValue(value);
        confService.save(conf);
    }
    
    public void deleteConf(String key) {
        getSorma().delete(Configuration.class, "_key=?", new String[]{key});
    }
    
    public String getConf(String key) {
        ConfigurationServiceImpl confService = new ConfigurationServiceImpl(getSorma());
        Configuration conf = confService.get(key, null);
        return conf.getValue();
    }
}
