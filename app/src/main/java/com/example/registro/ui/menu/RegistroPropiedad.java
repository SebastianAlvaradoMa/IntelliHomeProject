package com.example.registro.ui.menu;

import android.app.AlertDialog;
import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;
import com.example.registro.data.model.Property;
import com.example.registro.data.repository.PropertyRepository;
import com.example.registro.data.service.PropertyService;
import com.example.registro.ui.auth.registro.SignUpActivity;
import com.example.registro.ui.main.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class RegistroPropiedad extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private Spinner spinnerAmenidades1;
    private Spinner spinnerAmenidades2;
    private Spinner spinnerAmenidades3;
    private Spinner spinnerAmenidades4;

    private EditText editNombrePropiedad, editPrecio, editContacto2, editPersonasmax2, editTxtlatitud, editTxtlongitud;

    private Button NoButton;
    private Button SiButton;
    private String mascotasSelection = "No"; // Default value

    private String amenidad1Value = "";
    private String amenidad2Value = "";
    private String amenidad3Value = "";
    private String amenidad4Value = "";

    GoogleMap mMap;

    //Seleccionar foto de galeria
//    private final ActivityResultLauncher<String[]> multiplePermissionLauncher = registerForActivityResult(
//            new ActivityResultContracts.RequestMultiplePermissions(),
//            permissions -> {
//                boolean allGranted = true;
//                for (Boolean granted : permissions.values()) {
//                    allGranted = allGranted && granted;
//                }
//                if (allGranted) {
//                    showImagePickerDialog();
//                }
//            }
//    );
//
//    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == Activity.RESULT_OK) {
//                    fondoCamara.setImageURI(photoUri);
//                }
//            }
//    );
//
//    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
//                    Uri selectedImage = result.getData().getData();
//                    fondoCamara.setImageURI(selectedImage);
//                }
//            }
//    );

    private void registerProperty() {
        // Get values from EditText fields
        String name = editNombrePropiedad.getText().toString().trim();
        String priceStr = editPrecio.getText().toString().trim();
        String contact = editContacto2.getText().toString().trim();
        String maxPeopleStr = editPersonasmax2.getText().toString().trim();
        String latitudeStr = editTxtlatitud.getText().toString().trim();
        String longitudeStr = editTxtlongitud.getText().toString().trim();

        // Validate required fields
        if (name.isEmpty() || priceStr.isEmpty() || contact.isEmpty() ||
                maxPeopleStr.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Please fill in all required fields", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Parse numeric values with try-catch blocks
        double price;
        int maxPeople;
        double latitude;
        double longitude;

        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Price must be greater than 0", Snackbar.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Please enter a valid price", Snackbar.LENGTH_LONG).show();
            return;
        }

        try {
            maxPeople = Integer.parseInt(maxPeopleStr);
            if (maxPeople <= 0) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Maximum people must be greater than 0", Snackbar.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Please enter a valid number for maximum people", Snackbar.LENGTH_LONG).show();
            return;
        }

        try {
            latitude = Double.parseDouble(latitudeStr);
            longitude = Double.parseDouble(longitudeStr);
            if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please enter valid coordinates", Snackbar.LENGTH_LONG).show();
                return;
            }
        } catch (NumberFormatException e) {
            Snackbar.make(findViewById(android.R.id.content),
                    "Please enter valid coordinates", Snackbar.LENGTH_LONG).show();
            return;
        }

        String amenity1 = spinnerAmenidades1.getSelectedItem().toString();
        String amenity2 = spinnerAmenidades2.getSelectedItem().toString();
        String amenity3 = spinnerAmenidades3.getSelectedItem().toString();
        String amenity4 = spinnerAmenidades4.getSelectedItem().toString();

        PropertyService propertyService = new PropertyService();
        propertyService.registerProperty(name, price, contact, maxPeople, latitude, longitude,
                mascotasSelection, amenity1, amenity2, amenity3, amenity4,
                new PropertyService.PropertyCallback() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            runOnUiThread(() -> {
                                try {

                                    new AlertDialog.Builder(RegistroPropiedad.this)
                                            .setTitle("Registro Exitoso")
                                            .setMessage(message)
                                            .setPositiveButton("OK", (dialog, which) -> {
                                                Intent intent = new Intent(RegistroPropiedad.this, GestionPropiedad.class);
                                                startActivity(intent);
                                                finish();
                                            })
                                            .setCancelable(false)
                                            .show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Snackbar.make(findViewById(android.R.id.content),
                                    error, Snackbar.LENGTH_LONG).show();
                        });
                    }
                });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_propiedad);
        //Init user repository


        editNombrePropiedad=findViewById(R.id.nombre);
        editPrecio=findViewById(R.id.precio2);
        editContacto2=findViewById(R.id.contacto2);
        editPersonasmax2=findViewById(R.id.personasmax2);

        editTxtlatitud=findViewById(R.id.txtlatitud);
        editTxtlongitud=findViewById(R.id.txtlongitud);
        //Initialize spinners
        spinnerAmenidades1 = findViewById(R.id.amenidades1);
        spinnerAmenidades2 = findViewById(R.id.amenidades2);
        spinnerAmenidades3 = findViewById(R.id.amenidades3);
        spinnerAmenidades4 = findViewById(R.id.amenidades4);

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
        spinnerAmenidades1.setAdapter(pasatiemposAdapter);

        pasatiemposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmenidades2.setAdapter(pasatiemposAdapter);

        pasatiemposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmenidades3.setAdapter(pasatiemposAdapter);

        pasatiemposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmenidades4.setAdapter(pasatiemposAdapter);
        NoButton = findViewById(R.id.no);
        SiButton = findViewById(R.id.si);

        // Set up button click listeners
        NoButton.setOnClickListener(v -> {
            mascotasSelection = "No";
            NoButton.setSelected(true);
            SiButton.setSelected(false);
        });

        SiButton.setOnClickListener(v -> {
            mascotasSelection = "Yes";
            SiButton.setSelected(true);
            NoButton.setSelected(false);
        });

        // Update spinner listeners to store selected values
        spinnerAmenidades1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                amenidad1Value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                amenidad1Value = "";
            }
        });

        spinnerAmenidades2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                amenidad2Value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                amenidad2Value = "";
            }
        });

        spinnerAmenidades3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                amenidad3Value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                amenidad3Value = "";
            }
        });

        spinnerAmenidades4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                amenidad4Value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                amenidad4Value = "";
            }
        });



    Button registerButton = findViewById(R.id.registerButton); // Assuming you have a button with this ID
    registerButton.setOnClickListener(v -> registerProperty());


    // Obtener el botón y configurar el listener
    ImageButton button = findViewById(R.id.back);
        button.setOnClickListener(v -> {
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
        editTxtlatitud.setText("" + latLng.latitude);
        editTxtlongitud.setText(""+ latLng.longitude);

        mMap.clear();
        LatLng tec = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(tec).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tec));


    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        editTxtlatitud.setText(""+latLng.latitude);
        editTxtlongitud.setText(""+latLng.longitude);
        mMap.clear();
        LatLng tec = new LatLng(latLng.latitude,latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(tec).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tec));

    }
}