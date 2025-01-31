package com.example.registro.ui.menu;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.AutoCompleteTextView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import androidx.core.content.FileProvider;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;
import com.example.registro.data.service.PropertyService;
import com.example.registro.data.model.Property;
import com.example.registro.data.repository.PropertyRepository;
import com.example.registro.ui.auth.registro.SignUpActivity;
import com.example.registro.ui.main.MainActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.File;
import java.io.IOException;



public class RegistroPropiedad extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
   
    GoogleMap mMap;
    private Button SiButton, NoButton;
    private boolean isSiGreen = false;
    private boolean isNoGreen = false;
    private ChipGroup chipGroupDays;
    private Button saveAvailabilityButton;
    private ArrayList<String> selectedDays;

    //Variables de la Camara
    private ImageButton camara;
    private File photoFile;
    private Uri photoUri;

    //Variables del Mapa
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private AutoCompleteTextView searchText;

    private Spinner spinnerAmenidades1;
    private Spinner spinnerAmenidades2;
    private Spinner spinnerAmenidades3;
    private Spinner spinnerAmenidades4;

    private EditText editNombrePropiedad, editPrecio, editContacto2, editPersonasmax2, editTxtlatitud, editTxtlongitud;


    private String mascotasSelection = "No"; // Default value

    private String amenidad1Value = "";
    private String amenidad2Value = "";
    private String amenidad3Value = "";
    private String amenidad4Value = "";


    //Busca la ubicacion en el mapa
    private void buscarUbicacion(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setLocationBias(RectangularBounds.newInstance(new LatLngBounds(
                        new LatLng(-90, -180), new LatLng(90, 180))))
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                String placeId = prediction.getPlaceId();

                placesClient.fetchPlace(com.google.android.libraries.places.api.net.FetchPlaceRequest.builder(
                                placeId, java.util.Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG))
                        .build()
                ).addOnSuccessListener(fetchPlaceResponse -> {
                    LatLng latLng = fetchPlaceResponse.getPlace().getLatLng();
                    if (latLng != null) {
                        // Actualizar los campos de latitud y longitud
                        editTxtlatitud.setText(String.valueOf(latLng.latitude));
                        editTxtlongitud.setText(String.valueOf(latLng.longitude));

                        // Mover el mapa a la nueva ubicación
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Ubicación seleccionada")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); // Marcador rojo
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12)); // Zoom a la ciudad
                    }
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), "No se encontró la ubicación", Snackbar.LENGTH_LONG).show();
                });

                break;
            }
        });
    }

    //Seleccionar foto de galeria
    private final ActivityResultLauncher<String[]> multiplePermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            permissions -> {
                boolean allGranted = true;
                for (Boolean granted : permissions.values()) {
                    allGranted = allGranted && granted;
                }
                if (allGranted) {
                    showImagePickerDialog();
                }
            }
    );
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    camara.setImageURI(photoUri);
                }
            }
    );
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    camara.setImageURI(selectedImage);
                }
            }
    );

    //Registrar propiedad
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




    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_propiedad);
        //Init user repository

        Button btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(v -> {
            String query = searchText.getText().toString().trim();
            if (!query.isEmpty()) {
                buscarUbicacion(query);
            } else {
                Snackbar.make(v, "Ingresa una ubicación", Snackbar.LENGTH_SHORT).show();
            }
        });

        //Inicializar CAMARA
        camara = findViewById(R.id.camara);
        camara.setOnClickListener(v -> checkPermissionsAndShowPicker());


        //Color


        SiButton.setOnClickListener(view -> {
            if (isSiGreen) {
                // Cambia a azul oscuro (color original)
                SiButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
                NoButton.setEnabled(true); // Habilita el otro botón
            } else {
                // Cambia a verde
                SiButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
                NoButton.setEnabled(false); // Deshabilita el otro botón
            }
            isSiGreen = !isSiGreen; // Alterna el estado del botón "Sí"
        });

        NoButton.setOnClickListener(view -> {
            if (isNoGreen) {
                // Cambia a azul oscuro (color original)
                NoButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_blue));
                SiButton.setEnabled(true); // Habilita el otro botón
            } else {
                // Cambia a verde
                NoButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
                SiButton.setEnabled(false); // Deshabilita el otro botón
            }
            isNoGreen = !isNoGreen; // Alterna el estado del botón "No"
        });

        //Disponibilidad
        chipGroupDays = findViewById(R.id.chipGroupDisponibilidad);
        saveAvailabilityButton = findViewById(R.id.BotonDisponibilidad);
        selectedDays = new ArrayList<>();

        // Días de la semana
        String[] daysOfWeek = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        // Crear chips para cada día de la semana
        for (String day : daysOfWeek) {
            Chip chip = new Chip(this);
            chip.setText(day);
            chip.setCheckable(true);  // Hacer que los chips sean seleccionables
            chip.setChipBackgroundColorResource(R.color.dark_blue);  // Color inicial

            chip.setTextColor(getResources().getColor(R.color.white));  // Color de texto

            // Listener para cambiar el color al hacer clic
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Cambiar a color verde cuando esté seleccionado
                    chip.setChipBackgroundColorResource(R.color.green);
                    selectedDays.add(day); // Agregar el día a la lista de días seleccionados
                } else {
                    // Volver al color azul oscuro cuando no esté seleccionado
                    chip.setChipBackgroundColorResource(R.color.dark_blue);
                    selectedDays.remove(day); // Eliminar el día de la lista de días seleccionados
                }
            });

            chipGroupDays.addView(chip);
        }

        // Guardar la disponibilidad cuando el usuario presiona el botón
        saveAvailabilityButton.setOnClickListener(v -> {
            if (!selectedDays.isEmpty()) {
                // Mostrar los días seleccionados
                StringBuilder availability = new StringBuilder("Días seleccionados: ");
                for (String day : selectedDays) {
                    availability.append(day).append(", ");
                }
                Toast.makeText(RegistroPropiedad.this,
                        availability.toString(), Toast.LENGTH_LONG).show();

                // Aquí puedes guardar la disponibilidad en la base de datos o hacer lo que necesites.
                // Por ejemplo: saveAvailabilityToDatabase(selectedDays);

            } else {
                Toast.makeText(RegistroPropiedad.this,
                        "No se ha seleccionado ningún día.",
                        Toast.LENGTH_SHORT).show();
            }
        });



        // Inicializar Places API MAPA y Llave
        Places.initialize(getApplicationContext(), "");
        placesClient = Places.createClient(this);


        searchText = findViewById(R.id.searchText);
        searchText.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1));
        searchText.setOnItemClickListener((parent, view, position, id) -> buscarUbicacion(searchText.getText().toString()));



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);


        editNombrePropiedad=findViewById(R.id.name);
        editPrecio=findViewById(R.id.price);
        editContacto2=findViewById(R.id.contact);
        editPersonasmax2=findViewById(R.id.maxPeople);

        editTxtlatitud=findViewById(R.id.latitude);
        editTxtlongitud=findViewById(R.id.longitude);



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



        Button registerButton = findViewById(R.id.registerButton); // Assuming you have a button with this ID
        registerButton.setOnClickListener(v -> registerProperty());


        // Obtener el botón y configurar el listener
        ImageButton button = findViewById(R.id.back);
            button.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroPropiedad.this, GestionPropiedad.class);
            startActivity(intent);
        });

    }
    //CAMARA!!!!!!!!!!!!!!!!!!!!!!!!!!
    //Permisos de la camara
    private void checkPermissionsAndShowPicker() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        multiplePermissionLauncher.launch(permissions);
    }

    //Elegir opciones para seleccionar la foto de la propiedad
    private void showImagePickerDialog() {
        String[] options = {"Tomar foto", "Elegir de galería", "Cancelar"};

        new AlertDialog.Builder(this)
                .setTitle("Seleccionar foto de perfil")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openCamera();
                            break;
                        case 1:
                            openGallery();
                            break;
                        case 2:
                            dialog.dismiss();
                            break;
                    }
                })
                .show();
    }

    //Tomar foto desde la camara
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this,
                            "com.example.registro.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    cameraLauncher.launch(takePictureIntent);
                }
            } catch (IOException ex) {
                // Handle error
            }
        }
    }

    //Abrir galeria
    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhoto);
    }

    //Crear archivo de imagen
    private File createImageFile() throws IOException {
        String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss",
                java.util.Locale.getDefault()).format(new java.util.Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }


    //MAPA!!!!!!!!!!!!!!!!!!
    //Caracteristicas del mapa, ubicacion del tec, mover el mapa, etc...
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34.397, 150.644)));
        mMap =googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        LatLng tec = new LatLng(9.8372475,-84.0571092);
        mMap.addMarker(new MarkerOptions().position(tec).title("Tecnologico de Costa Rica"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tec));
        mMap.setMinZoomPreference(8);
    }

    //Agrega las coordenadas segun el marcador rojo
    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        editTxtlatitud.setText(String.valueOf(latLng.latitude));
        editTxtlongitud.setText(String.valueOf(latLng.longitude));

        mMap.clear(); // Limpiar marcadores anteriores
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Ubicación seleccionada")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    //Agrega las coordenadas de la ubicacion
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