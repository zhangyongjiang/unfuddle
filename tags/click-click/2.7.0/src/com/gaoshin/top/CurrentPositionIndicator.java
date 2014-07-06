package com.gaoshin.top;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.gaoshin.shortcut.R;

public class CurrentPositionIndicator extends LinearLayout {
    private static final int[] PositionInGrid = { 2, 5, 8, 7, 6, 3, 0, 1 };

    private ShortcutGroupPosition highlight;
    private int row = 3;
    private int col = 3;
    private int width = 60;
    private int padding = 7;

    public CurrentPositionIndicator(Context context) {
        super(context);
        setOrientation(VERTICAL);

        for (int i = 0; i < row; i++) {
            LinearLayout row = new Row(context);
            row.setOrientation(HORIZONTAL);
            addView(row);
        }
        ImageView item = (ImageView) ((Row) getChildAt(1)).getChildAt(1);
        item.setImageDrawable(null);
    }

    public void setHighlightPostion(ShortcutGroupPosition hl) {
        if (highlight != null)
        {
            int pos = PositionInGrid[highlight.getId()];
            int rowId = pos / col;
            int colId = pos - (rowId * col);
            ImageView item = (ImageView) ((Row) getChildAt(rowId)).getChildAt(colId);
            item.setImageResource(R.drawable.gray_24);
        }

        this.highlight = hl;
        {
            int pos = PositionInGrid[highlight.getId()];
            int rowId = pos / col;
            int colId = pos - (rowId * col);
            ImageView item = (ImageView) ((Row) getChildAt(rowId)).getChildAt(colId);
            item.setImageResource(R.drawable.yellow_24);
        }
    }

    public ShortcutGroupPosition getHighlight() {
        return highlight;
    }

    public class Row extends LinearLayout {

        public Row(Context context) {
            super(context);

            setOrientation(LinearLayout.HORIZONTAL);

            for (int i = 0; i < col; i++) {
                ImageView iv = new ImageView(context);
                iv.setImageResource(R.drawable.gray_24);
                iv.setScaleType(ScaleType.FIT_XY);
                LayoutParams lp = new LayoutParams((width - padding * 2) / 3, (width - padding * 2) / 3);
                iv.setLayoutParams(lp);
                addView(iv);
            }
        }
    }
}
