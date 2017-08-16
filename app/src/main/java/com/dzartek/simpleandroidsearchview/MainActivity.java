package com.dzartek.simpleandroidsearchview;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private SimpleCursorAdapter mCursorAdapter;
    private SearchView mSearchView;
    private MenuItem mSearchViewItem;
    private static final String[] mAnimalNameList = new String[]{"Lion", "Tiger", "Dog",
            "Cat", "Tortoise", "Rat", "Elephant", "Fox", "Bat", "Cougar",
            "Cow","Donkey","Monkey"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        for (int i = 0; i < animalNameList.length; i++) {
//            AnimalNames animalNames = new AnimalNames(animalNameList[i]);
//            // Binds all strings into an array
//            arraylist.add(animalNames);
//        }

        final String[] from = new String[] {"animal"};
        final int[] to = new int[] {android.R.id.text1};

        // Pass results to ListViewAdapter Class
        mCursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view_menu_item, menu);
        mSearchViewItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchViewItem.getActionView();

        mSearchView.setSuggestionsAdapter(mCursorAdapter);
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Toast.makeText(getApplicationContext(), "onSuggestionSelect!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor c = (Cursor) mCursorAdapter.getItem(position);
                String val = c.getString(c.getColumnIndex("animal"));
                Toast.makeText(getApplicationContext(), "onSuggestionClick! " + val,
                        Toast.LENGTH_SHORT).show();

                mSearchView.setQuery(val, true);
                // Do api query
                
                return true;
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "Submit query! " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                populateAdapter(newText);
                return false;
            }
        });
        return true;
    }

    private void populateAdapter(String newText) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "animal" });
        for (int i=0; i<mAnimalNameList.length; i++) {
            if (mAnimalNameList[i].toLowerCase().startsWith(newText.toLowerCase()))
                c.addRow(new Object[] {i, mAnimalNameList[i]});
        }
        mCursorAdapter.changeCursor(c);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
