package com.example.a3raths25.mapping;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    MapView mv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this,PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);
        mv = (MapView)findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(51.05,-0.72));
        Button button =(Button)findViewById(R.id.btn1);
        button.setOnClickListener(this);
    }
        public void onClick(View view)
        {
            EditText lon = (EditText)findViewById(R.id.et2);
            EditText lat = (EditText)findViewById(R.id.et1);
            double latci=Double.parseDouble(lat.getText().toString());
            double lonci= Double.parseDouble(lon.getText().toString());
            mv.getController().setCenter(new GeoPoint(latci,lonci));
        }
    }

