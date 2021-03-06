package com.example.a3raths25.mapping;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
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
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener, LocationListener {
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
    boolean doNotReadPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        Button button = (Button) findViewById(R.id.btn1);
        button.setOnClickListener(this);

        markerGestureListener= new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
        {
            public boolean onItemLongPress(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        BufferedReader reader = null;
        String line;
        try {
            String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/poi.txt";
            File file = new File(filepath);
            reader = new BufferedReader(new FileReader(filepath));
            while ((line = reader.readLine()) != null) {
                String[] components = line.split(",");
                if (components.length == 5) {

                    try {
                        double lon = Double.parseDouble(components[4]);
                        double lat = Double.parseDouble(components[3]);
                        System.out.println("$$$ trying to parse line=" + line + "lat=" +lat+ " lon="+lon);
                        OverlayItem pointOfInterest = new OverlayItem(components[0], components[1], new GeoPoint(lon,lat));
                        items.addItem(pointOfInterest);

                    } catch (NumberFormatException e) {
                        System.out.println("error parsing file" + e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mv.getOverlays().add(items);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.choosemap) {
            Intent intent = new Intent(this, MapChooseActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        if (item.getItemId() == R.id.setlocation) {
            Intent intent = new Intent(this, SetLocationActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }
        if (item.getItemId() == R.id.preferences) {
            Intent intent = new Intent(this, MyPrefsActivity.class);
            startActivityForResult(intent, 2);
            return true;
        }

        if (item.getItemId() == R.id.MapChooseList) {
            Intent intent = new Intent(this, MapChooseListActivity.class);
            startActivityForResult(intent, 3);
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
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

    public void onResume() {
        super.onResume();
        if (doNotReadPreferences == false) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            double lat = Double.parseDouble(prefs.getString("lat", "50.9"));
            double lon = Double.parseDouble(prefs.getString("lon", "-1.4"));
            mv.getController().setCenter(new GeoPoint(lat, lon));
            int zoomLevel = Integer.parseInt(prefs.getString("zoomLevel", "15"));
            mv.getController().setZoom(zoomLevel);
            String mapType = prefs.getString("mapType", "RMV");

            if (mapType.equals("RMV")) {
                mv.setTileSource(TileSourceFactory.MAPNIK);
            } else {
                mv.setTileSource(TileSourceFactory.HIKEBIKEMAP);
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
    }

    public void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        String mapType = prefs.getString("mapType", "RMV");
        editor.putString("mapType", mapType);
        editor.commit();
    }

    public void onLocationChanged(Location newLoc) {
        Toast.makeText
                (this, "Location=" +
                        newLoc.getLatitude() + " " +
                        newLoc.getLongitude(), Toast.LENGTH_LONG).show();
        mv.getController().setCenter(new GeoPoint(newLoc.getLatitude(), newLoc.getLongitude()));
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

        Toast.makeText(this, "Status changed: " + status,
                Toast.LENGTH_LONG).show();
    }
}









