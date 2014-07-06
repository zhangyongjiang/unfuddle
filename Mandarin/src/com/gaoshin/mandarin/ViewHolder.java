package com.gaoshin.mandarin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gaoshin.mandarin.data.Item;

public class ViewHolder {
    public TextView ewordTextView;
    public TextView cwordTextView;
    public ImageButton playButton;

    private Item item;

    public ViewHolder(Item item) {
        this.item = item;
    }

    public View inflate(LayoutInflater inflater, ViewGroup parent) {
        View convertView = inflater.inflate(R.layout.worditem, parent, false);
        ewordTextView = (TextView) convertView.findViewById(R.id.textViewEN);
        cwordTextView = (TextView) convertView.findViewById(R.id.textViewCN);
        playButton = (ImageButton) convertView.findViewById(R.id.playButton);
        convertView.setTag(this);
        return convertView;
    }

    public void setup(final Context context) {
        ewordTextView.setText(item.getEscript());
        cwordTextView.setText(item.getCscript());
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Mp3Player.playMedia(context, item.getcFemaleAudio());
            }
        });
    }

    public void setItem(Item item2) {
        this.item = item2;
    }
}
