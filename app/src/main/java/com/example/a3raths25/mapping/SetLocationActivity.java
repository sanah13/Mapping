package com.example.a3raths25.mapping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 3RATHS25 on 15/02/2018.
 */

public class SetLocationActivity extends AppCompatActivity implements View.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setlocation);

        Button go_button = (Button) findViewById(R.id.btnGo);
        go_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // retrieve latitude and longitude
        EditText lat=(EditText)findViewById(R.id.lat);
        double lat_value = Double.parseDouble(lat.getText().toString());

        EditText lon=(EditText)findViewById(R.id.lon);
        double lon_value = Double.parseDouble(lon.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putDouble("lat",lat_value);
        bundle.putDouble("lon",lon_value);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();

    }
}