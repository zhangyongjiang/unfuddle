package com.gaoshin.stock.plugin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.BroadcastAction;
import com.gaoshin.stock.model.ConfKey;
import com.gaoshin.stock.model.Configuration;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.StockContentProvider;

public class PluginView extends RelativeLayout {
    protected Plugin plugin;
    private TextView label;
    private SORMA sorma;
    private ConfigurationServiceImpl confService;

    public PluginView(Context context, Plugin plugin) {
        super(context);
        sorma = SORMA.getInstance(context, StockContentProvider.class);
        confService = new ConfigurationServiceImpl(sorma);
        this.plugin = plugin;
        setBackgroundColor(Color.LTGRAY);
        
        label = new Button(context);
        label.setTextColor(Color.BLACK);
        label.setBackgroundColor(Color.LTGRAY);
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration conf = confService.get(ConfKey.CurrentPlugin, PluginView.this.plugin.getId());
                conf.setValue(PluginView.this.plugin.getId());
                confService.save(conf);
                Intent intent = new Intent(BroadcastAction.PluginChanged.name());
                intent.putExtra("data", PluginView.this.plugin.getId());
                getContext().sendBroadcast(intent);
            }
        });
        label.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                if(PluginView.this.plugin.getUrl().equals(BuiltInPlugins.BuiltinPluginListHtml)) {
                    return true;
                }
                new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure to remove " + PluginView.this.plugin.getName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sorma.delete(PluginView.this.plugin);
                        Intent intent = new Intent(BroadcastAction.PluginDeleted.name());
                        intent.putExtra("data", PluginView.this.plugin.getId());
                        getContext().sendBroadcast(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
                return true;
            }
        });
        
        LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(label, param);
        applyData();
    }

    public void applyData() {
        label.setText(plugin.getName());
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin sc) {
        this.plugin = sc;
        applyData();
    }
    
    public void setSelected(boolean selected) {
        label.setBackgroundColor(selected ? Color.WHITE : Color.LTGRAY);
    }
}
