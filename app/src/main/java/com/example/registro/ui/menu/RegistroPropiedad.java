package com.example.registro.ui.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class RegistroPropiedad extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    private Spinner amenidades1,amenidades2, amenidades3, amenidades4;

    EditText txtlatitud, txtlongitud;
    GoogleMap mMap;
    private Button buttonSi, buttonNo;
    private boolean isSiGreen = false;
    private boolean isNoGreen = false;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_propiedad);

        txtlatitud=findViewById(R.id.txtlatitud);
        txtlongitud=findViewById(R.id.txtlongitud);
        //Color

        buttonSi = findViewById(R.id.si);
        buttonNo=findViewById(R.id.no);

        buttonSi.setOnClickListener(view -> {
            if (isSiGreen) {
                // Cambia a azul oscuro (color original)
                buttonSi.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
                buttonNo.setEnabled(true); // Habilita el otro botón
            } else {
                // Cambia a verde
                buttonSi.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
                buttonNo.setEnabled(false); // Deshabilita el otro botón
            }
            isSiGreen = !isSiGreen; // Alterna el estado del botón "Sí"
        });

        buttonNo.setOnClickListener(view -> {
            if (isNoGreen) {
                // Cambia a azul oscuro (color original)
                buttonNo.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
                buttonSi.setEnabled(true); // Habilita el otro botón
            } else {
                // Cambia a verde
                buttonNo.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
                buttonSi.setEnabled(false); // Deshabilita el otro botón
            }
            isNoGreen = !isNoGreen; // Alterna el estado del botón "No"
        });






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ChipGroup chipGroupAmenidades = findViewById(R.id.chipGroupAmenidades);

// Obtener el array de recursos
        for (String amenidad : getResources().getStringArray(R.array.amenidades)) {
            Chip chip = new Chip(this);
            chip.setText(amenidad);
            chip.setCheckable(true); // Habilita la selección
            chip.setCheckedIconVisible(false); // Oculta el ícono de selección (opcional)

            // Establecer el color de fondo inicial (color azul oscuro)
            chip.setChipBackgroundColorResource(R.color.dark_blue);
            chip.setTextColor(getResources().getColor(R.color.white));

            // Listener para cambiar el color al hacer clic
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Cambiar a color verde cuando esté seleccionado
                    chip.setChipBackgroundColorResource(R.color.green);
                } else {
                    // Volver al color azul oscuro cuando no esté seleccionado
                    chip.setChipBackgroundColorResource(R.color.dark_blue);
                }
            });

            chipGroupAmenidades.addView(chip);
        }









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