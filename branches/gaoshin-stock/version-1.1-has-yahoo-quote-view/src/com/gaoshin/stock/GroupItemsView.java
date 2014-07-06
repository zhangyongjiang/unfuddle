package com.gaoshin.stock;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gaoshin.sorma.SORMA;
import com.gaoshin.stock.model.ConfigurationServiceImpl;
import com.gaoshin.stock.model.GroupItem;
import com.gaoshin.stock.model.StockContentProvider;
import com.gaoshin.stock.model.StockGroup;

public class GroupItemsView extends ScrollView {
    public static interface SymbolChangedListener {
        void onSymbolChanged(Integer groupItemId);
    }

    private SORMA sorma = null;
    private List<SymbolView> symViewList = new ArrayList<SymbolView>();
    private int current = 0;
    private LinearLayout container;
    private ConfigurationServiceImpl confService;
    private StockGroup stockGroup;

    public GroupItemsView(Context context) {
        super(context);
        this.setPadding(0, 0, 0, 0);
        sorma = SORMA.getInstance(context, StockContentProvider.class);
        confService = new ConfigurationServiceImpl(sorma);
        setId(Math.abs(this.hashCode()));
        setBackgroundColor(Color.LTGRAY);
        setFadingEdgeLength(0);
        container = new LinearLayout(context);
        container.setPadding(0, 0, 0, 0);
        container.setOrientation(LinearLayout.VERTICAL);
        addView(container);
    }

    public void applyData(StockGroup stockGroup) {
        symViewList.clear();
        current = 0;
        container.removeAllViews();
        
        this.stockGroup = stockGroup;
        Integer groupId = stockGroup.getId();
        Integer currentItemId = stockGroup.getDefaultItem();
        List<GroupItem> items = sorma.select(GroupItem.class, "groupId=?", new String[]{String.valueOf(groupId)});
        for(GroupItem item : items) {
            SymbolView sv = new SymbolView(getContext());
            symViewList.add(sv);
            sv.setGroupItem(item);
            sv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SymbolView sv = (SymbolView) v;
                    setCurrentSelected(sv.getGroupItem().getId(), true);
                }
            });
            sv.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SymbolView sv = (SymbolView) v;
                    deleteItem(sv.getGroupItem());
                    return true;
                }
            });
            
            if(item.getId() == currentItemId) {
                sv.setSelected();
            }
            
            container.addView(sv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        }
    }

    public void setCurrentSelected(Integer groupItemId, boolean notify) {
        symViewList.get(current).setUnselected();
        for(int i=0; i<symViewList.size(); i++) {
            SymbolView sv = symViewList.get(i);
            if(sv.getGroupItem().getId().equals(groupItemId)) {
                sv.setSelected();
                current = i;
                stockGroup.setDefaultItem(sv.getId());
            }
        }
        if(notify) {
            Intent intent = new Intent(BroadcastAction.SymbolChanged.name());
            intent.putExtra("groupItemId", groupItemId);
            getContext().sendBroadcast(intent);
        }
    }

    public int getCurrentSelected() {
        return current;
    }

    public void addGroupItem(GroupItem groupItem) {
        SymbolView sv = new SymbolView(getContext());
        sv.setGroupItem(groupItem);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SymbolView sv = (SymbolView) v;
                setCurrentSelected(sv.getGroupItem().getId(), true);
            }
        });
        sv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SymbolView sv = (SymbolView) v;
                deleteItem(sv.getGroupItem());
                return true;
            }
        });
        container.addView(sv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        symViewList.add(sv);
        setCurrentSelected(groupItem.getId(), true);
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
    
    private void deleteItem(final GroupItem groupItem) {
        new AlertDialog.Builder(getContext())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setMessage("Are you sure to delete this symbol " + groupItem.getSym() + "?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sorma.delete(groupItem);
                int i;
                for(i=0; i<symViewList.size(); i++) {
                    if(symViewList.get(i).getGroupItem().getId() == groupItem.getId()) {
                        container.removeView(symViewList.get(i));
                        symViewList.remove(i);
                        break;
                    }
                }
                if(i<=current) {
                    current--;
                    if(current>=0) {
                        setCurrentSelected(symViewList.get(current).getGroupItem().getId(), true);
                    }
                }
            }
        })
        .setNegativeButton("No", null)
        .show();

    }

    public void disableCurrent() {
        if(symViewList.size() > 0)
            symViewList.get(current).setUnselected();
    }

    public void enableCurrent() {
        if(symViewList.size() > 0)
            symViewList.get(current).setSelected();
    }
}
