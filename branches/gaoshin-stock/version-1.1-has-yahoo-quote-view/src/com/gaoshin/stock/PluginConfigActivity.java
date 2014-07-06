package com.gaoshin.stock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.plugin.Plugin;

public class PluginConfigActivity extends Activity {
    private SORMA sorma;
    private ConfigurationServiceImpl confService;
    private Plugin plugin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        sorma = SORMA.getInstance(this, StockContentProvider.class);
        confService = new ConfigurationServiceImpl(sorma);
        
        int id = getIntent().getIntExtra("pluginId", -1);
        if(id == -1) {
            plugin = new Plugin();
        }
        else {
            plugin = sorma.get(Plugin.class, "id=" + id, null);
        }
        
        setContentView(R.layout.plug_config);
        setupView();
    }

    private void setupView() {
        EditText editName = (EditText) findViewById(R.id.name);
        editName.setText(plugin.getName());
        
        TextView textDesc = (TextView) findViewById(R.id.description);
        textDesc.setText(plugin.getDescription());
        
        EditText editUrl = (EditText) findViewById(R.id.url);
        editUrl.setText(plugin.getUrl());
        
        EditText editPostAction = (EditText) findViewById(R.id.modification);
        editPostAction.setText(plugin.getPostAction());

        setupSaveButton();
        setupDeleteButton();
        setupCancelButton();
    }

    private void setupCancelButton() {
        Button btn = (Button) findViewById(R.id.cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupDeleteButton() {
        Button btn = (Button) findViewById(R.id.delete);
        if(plugin.getId() == null) {
            btn.setVisibility(View.GONE);
            return;
        }
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PluginConfigActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure to remove " + PluginConfigActivity.this.plugin.getName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sorma.delete(PluginConfigActivity.this.plugin);
                        Intent intent = new Intent(BroadcastAction.PluginDeleted.name());
                        intent.putExtra("data", PluginConfigActivity.this.plugin.getId());
                        sendBroadcast(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
            }
        });
    }

    private void setupSaveButton() {
        Button btn = (Button) findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editName = (EditText) findViewById(R.id.name);
                plugin.setName(editName.getText().toString());
                
                EditText editUrl = (EditText) findViewById(R.id.url);
                plugin.setUrl(editUrl.getText().toString());
                
                EditText editPostAction = (EditText) findViewById(R.id.modification);
                plugin.setPostAction(editPostAction.getText().toString());
                
                if(plugin.getId() == null) {
                    sorma.insert(plugin);
                }
                else {
                    sorma.update(plugin);
                }
                finish();
            }
        });
    }

}


