package com.example.a3raths25.mapping;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by 3RATHS25 on 01/03/2018.
 */
public class MapChooseListActivity extends ListActivity {
    String[] data;
    public void onCreate(Bundle savesInstanceState) {
        super.onCreate(savesInstanceState);
        data= new String[]{"RegularMapView", "HikeBikeMapView"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
    }
    public void onListItemClick(ListView lv, View view, int index, long id){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
    }
}

