package com.example.registro.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RegistroPropiedad extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private Spinner amenidades1,amenidades2, amenidades3, amenidades4;

    EditText txtlatitud, txtlongitud;
    GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_propiedad);

        txtlatitud=findViewById(R.id.txtlatitud);
        txtlongitud=findViewById(R.id.txtlongitud);


        amenidades1 = findViewById(R.id.amenidades1);
        amenidades2 = findViewById(R.id.amenidades2);
        amenidades3 = findViewById(R.id.amenidades3);
        amenidades4 = findViewById(R.id.amenidades4);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Set up pasatiempos spinner
        ArrayAdapter<CharSequence> pasatiemposAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.amenidades,
                android.R.layout.simple_spinner_item
        );
        pasatiemposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amenidades1.setAdapter(pasatiemposAdapter);

        pasatiemposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amenidades2.setAdapter(pasatiemposAdapter);

        pasatiemposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amenidades3.setAdapter(pasatiemposAdapter);

        pasatiemposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amenidades4.setAdapter(pasatiemposAdapter);
        amenidades1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { //Ignore first item (prompt)
                    String selectedHobby = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        amenidades2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { //Ignore first item (prompt)
                    String selectedHobby = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        amenidades3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { //Ignore first item (prompt)
                    String selectedHobby = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        amenidades4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { //Ignore first item (prompt)
                    String selectedHobby = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });






    // Obtener el botón y configurar el listener
    ImageButton button = findViewById(R.id.back);
        button.setOnClickListener(v -> {
        // Redirigir a la actividad OlvidoContrasena
        Intent intent = new Intent(RegistroPropiedad.this, GestionPropiedad.class);
        startActivity(intent);
    });

        //Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap =googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        LatLng tec = new LatLng(9.8372475,-84.0571092);
        mMap.addMarker(new MarkerOptions().position(tec).title("Tecnologico de Costa Rica"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tec));

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        txtlatitud.setText("" + latLng.latitude);
        txtlongitud.setText(""+ latLng.longitude);

        mMap.clear();
        LatLng tec = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(tec).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tec));


    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txtlatitud.setText(""+latLng.latitude);
        txtlongitud.setText(""+latLng.longitude);
        mMap.clear();
        LatLng tec = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(tec).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tec));

    }
}