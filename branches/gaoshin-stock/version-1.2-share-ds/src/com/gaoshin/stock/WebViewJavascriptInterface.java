package com.gaoshin.stock;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.sorma.browser.JsonUtil;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.model.StockGroup;
import com.gaoshin.stock.plugin.Plugin;
import com.gaoshin.stock.plugin.PluginList;
import com.gaoshin.stock.plugin.PluginParamList;
import com.gaoshin.stock.plugin.PluginParameter;
import com.gaoshin.stock.plugin.PluginType;

public abstract class WebViewJavascriptInterface {
    protected abstract Context getContext();
    
    public SORMA getSorma() {
        return SORMA.getInstance(getContext(), StockContentProvider.class); 
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
    }
    
    public void rename(String pluginId, String name) {
        Plugin plugin = getSorma().get(Plugin.class, "id=" + pluginId, null);
        if(plugin != null) {
            plugin.setName(name);
            getSorma().update(plugin);
        }
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
    
    public String getGroups() {
        List<StockGroup> list = getSorma().select(StockGroup.class, null, null);
        ListWrapper wrapper = new ListWrapper();
        wrapper.setItems(list);
        return JsonUtil.toJsonString(wrapper);
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
        return String.valueOf(plugin.getId());
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
            
            if(plugin.isEnabled()) {
                selectPlugin(pluginId);
            }
            else {
                new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("Done!")
                .setNeutralButton("OK", null)
                .show();

            }
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
    
}
