package com.gaoshin.mandarin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.gaoshin.mandarin.data.Book;
import com.gaoshin.mandarin.data.Item;

public class MandarinActivity extends Activity {
    private Book book;
    private WordListAdapter wordListAdapter;
    private ListView wordListView;
    private EditText searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        wordListView = (ListView) findViewById(R.id.wordlist);

        book = ((MandarinApplication) getApplication()).getBook();
        wordListAdapter = new WordListAdapter(book.getBasicItems());
        wordListView.setAdapter(wordListAdapter);
        wordListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                Item item = (Item) wordListAdapter.getItem(position);
                Mp3Player.playMedia(getApplicationContext(), item.getcFemaleAudio());
            }
        });

        ImageButton search = (ImageButton) findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
            }
        });

        searchView = (EditText) findViewById(R.id.searchText);
        searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void search() {
        String pattern = searchView.getText().toString();
        wordListAdapter.setItems(book.searchBasic(pattern));
        wordListView.invalidateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mandarin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about:
            openAboutActivity();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void openAboutActivity() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.setData(Uri.parse("file:///android_asset/html/about.html"));
        startActivity(intent);
    }
}