package com.example.a3raths25.mapping;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
public class MainActivity extends AppCompatActivity implements OnClickListener {
    MapView mv;
    boolean doNotReadPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(51.05, -0.72));
        Button button = (Button) findViewById(R.id.btn1);
        button.setOnClickListener(this);
    }
    public void onClick(View view) {
        EditText lon = (EditText) findViewById(R.id.et2);
        EditText lat = (EditText) findViewById(R.id.et1);
        double latci = Double.parseDouble(lat.getText().toString());
        double lonci = Double.parseDouble(lon.getText().toString());
        mv.getController().setCenter(new GeoPoint(latci, lonci));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.choosemap)
        {
            Intent intent = new Intent(this,MapChooseActivity.class);
            startActivityForResult(intent,0);
            return true;
        }
        if(item.getItemId() == R.id.setlocation)
        {
            Intent intent = new Intent(this,SetLocationActivity.class);
            startActivityForResult(intent,1);
            return true;
        }
        if(item.getItemId() == R.id.preferences)
        {
            Intent intent = new Intent(this,MyPrefsActivity.class);
            startActivityForResult(intent,2);
            return true;
        }

        if(item.getItemId() == R.id.MapChooseList)
        {
            Intent intent = new Intent (this,MapChooseListActivity.class);
            startActivityForResult(intent,3);
            return true;
        }
        return false;
    }
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
        doNotReadPreferences = false;

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                boolean hikebikemap = extras.getBoolean("com.example.hikebikemap");
                if (hikebikemap == true) {
                    mv.setTileSource(TileSourceFactory.HIKEBIKEMAP);
                } else {
                    mv.setTileSource(TileSourceFactory.MAPNIK);
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                double latitude = extras.getDouble("lat");
                double longitude = extras.getDouble("lon");
                mv.getController().setCenter(new GeoPoint(latitude, longitude));
                Log.d("mapping", "latitude=" + latitude + " longitude " + longitude);
                doNotReadPreferences = true;
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String mapType = prefs.getString("mapType", "RMV");
                if (mapType.equals("RMV")) {
                    mv.setTileSource(TileSourceFactory.MAPNIK);
                } else {
                    mv.setTileSource(TileSourceFactory.HIKEBIKEMAP);
                }
            }
        }
    }
    public void onResume()
    {
        super.onResume();
        if (doNotReadPreferences==false) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            double lat = Double.parseDouble(prefs.getString("lat", "50.9"));
            double lon = Double.parseDouble(prefs.getString("lon", "-1.4"));
            mv.getController().setCenter(new GeoPoint(lat, lon));
            int zoomLevel = Integer.parseInt(prefs.getString("zoomLevel", "15"));
            mv.getController().setZoom(zoomLevel);
            String mapType= prefs.getString("mapType","RMV");

            if (mapType.equals("RMV")) {
                mv.setTileSource(TileSourceFactory.MAPNIK);
            } else {
                mv.setTileSource(TileSourceFactory.HIKEBIKEMAP);
            }
        }
    }
    public void onSaveInstanceState (Bundle savedInstanceState) {
    }
    public void onDestroy()
    {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor= prefs.edit();
        String mapType= prefs.getString("mapType","RMV");
        editor.putString("mapType", mapType);
        editor.commit();
        }
    }




