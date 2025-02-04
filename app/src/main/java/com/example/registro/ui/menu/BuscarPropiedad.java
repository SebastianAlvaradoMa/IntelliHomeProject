package com.example.registro.ui.menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registro.R;
import com.example.registro.data.model.Property;
import com.example.registro.data.service.PropertyService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuscarPropiedad extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<Property> propertyList = new ArrayList<>();
    private List<Property> searchList = new ArrayList<>();
    private List<Property> filteredList = new ArrayList<>();

    private Adapter adapter;

    private boolean isLayoutVisible = false;   //Saber si el layout filtros esta visible
    private ArrayList<String> selectedAmenities = new ArrayList<>();
    private int maxPrice = 50000;
    private int maxPeople = 100;
    private boolean petFriendly = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_propiedad);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        // Initialize RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(this, propertyList);
        recyclerView.setAdapter(adapter);



        // Set up item click listener
        adapter.setOnItemClickListener(property -> {
            // Create an intent to open AlquilarPropiedad
            Intent intent = new Intent(BuscarPropiedad.this, AlquilarPropiedad.class);

            // Pass the selected property as a Parcelable extra
            intent.putExtra("SELECTED_PROPERTY", property);

            startActivity(intent);
        });







        //-------------------
        //LAYOUT FILTROS
        //--------------------

        ImageButton buttonShowLayout = findViewById(R.id.filtros);
        FrameLayout secondLayoutContainer = findViewById(R.id.frameFiltros);

        // Inflar el layout una sola vez
        LayoutInflater inflater = LayoutInflater.from(BuscarPropiedad.this);
        View secondLayout = inflater.inflate(R.layout.filtros, null);
        Button resetButton = secondLayout.findViewById(R.id.resetFiltersButton);
        resetButton.setOnClickListener(v -> resetFilters());

        // Configurar el OnClickListener para el botón
        buttonShowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLayoutVisible) {
                    // Si el layout está visible, ocultarlo
                    secondLayoutContainer.setVisibility(View.GONE);
                    applyFilters();

                } else {
                    // Si el layout no está visible, mostrarlo
                    secondLayoutContainer.removeAllViews(); // Limpiar el contenedor
                    secondLayoutContainer.addView(secondLayout); // Añadir el layout inflado
                    secondLayoutContainer.setVisibility(View.VISIBLE);
                }



                // Cambiar el estado de visibilidad
                isLayoutVisible = !isLayoutVisible;
                //------------------
                //    CHIP GROUP
                //------------------

                // Referencia al ChipGroup
                ChipGroup chipGroup = secondLayout.findViewById(R.id.chipGroupActividades);


                // Actividades a elegir
                String[] amenities = {
                        "Cocina equipada (con electrodomésticos modernos)",
                        "Aire acondicionado",
                        "Calefacción",
                        "Wi-Fi gratuito",
                        "Televisión por cable o satélite",
                        "Lavadora y secadora",
                        "Piscina",
                        "Jardín o patio",
                        "Barbacoa o parrilla",
                        "Terraza o balcón",
                        "Gimnasio en casa",
                        "Garaje o espacio de estacionamiento",
                        "Sistema de seguridad",
                        "Habitaciones con baño en suite",
                        "Muebles de exterior",
                        "Microondas",
                        "Lavavajillas",
                        "Cafetera",
                        "Ropa de cama y toallas incluidas",
                        "Acceso a áreas comunes (piscina, gimnasio)",
                        "Camas adicionales o sofá cama",
                        "Servicios de limpieza opcionales",
                        "Acceso a transporte público cercano",
                        "Mascotas permitidas",
                        "Cercanía a tiendas y restaurantes",
                        "Sistema de calefacción por suelo radiante",
                        "Escritorio o área de trabajo",
                        "Chimenea",
                        "Acceso a internet de alta velocidad",
                        "Sistemas de entretenimiento (videojuegos, equipo de música)"
                };
                // Crear chips para cada actividad
                for (String eleccion : amenities) {
                    Chip chip = new Chip(BuscarPropiedad.this);
                    chip.setText(eleccion);
                    chip.setCheckable(true);
                    chip.setChipBackgroundColorResource(R.color.dark_blue);
                    chip.setTextColor(getResources().getColor(R.color.white));

                    chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            chip.setChipBackgroundColorResource(R.color.light_blue);
                            selectedAmenities.add(eleccion);
                            Log.d("SelectedAmenities", selectedAmenities.toString());
                        } else {
                            chip.setChipBackgroundColorResource(R.color.dark_blue);
                            selectedAmenities.remove(eleccion);
                            Log.d("SelectedAmenities", selectedAmenities.toString());
                        }
                        applyFilters();
                    });
                    chipGroup.addView(chip);
                }


                //------------------
                //   NUMBERPICKER
                //-----------------

                // Referencia al NumberPicker y TextView
                NumberPicker numberPicker =secondLayout.findViewById(R.id.numberPicker);
                // Configurar los valores mínimo y máximo
                numberPicker.setMinValue(1);  // Valor mínimo
                numberPicker.setMaxValue(100);  // Valor máximo
                // Establecer el valor inicial
                numberPicker.setValue(1);
                numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
                    maxPeople = newVal;
                    applyFilters();
                });

                // Cambiar el color del texto del NumberPicker
                try {
                    // Obtener los TextViews internos del NumberPicker
                    for (int i = 0; i < numberPicker.getChildCount(); i++) {
                        TextView textView = (TextView) numberPicker.getChildAt(i);
                        if (textView != null) {
                            // Cambiar el color de todos los números
                            textView.setTextColor(Color.WHITE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Switch
                Switch petSwitch = secondLayout.findViewById(R.id.switch1);
                petSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    petFriendly = isChecked;
                    applyFilters();
                });


                //------------------
                //     SEEKBAR
                //------------------

                SeekBar seekBar = secondLayout.findViewById(R.id.seekBar);
                TextView seekBarValue = secondLayout.findViewById(R.id.seekBarProgress);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    seekBar.setMin(10000);
                }
                seekBar.setMax(50000);

                // Establecer el valor inicial
                seekBar.setProgress(0);  // Este es el valor inicial de progreso

                // Configurar el OnSeekBarChangeListener
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        maxPrice = progress;
                        seekBarValue.setText(String.valueOf(progress));
                        applyFilters();
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                });
            }

        });


        // Fetch properties from the server
        PropertyService propertyService = new PropertyService();
        propertyService.fetchProperty(new PropertyService.PropertyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    propertyList = parseProperties(response);
                    runOnUiThread(() -> {
                        filteredList.addAll(propertyList);
                        adapter.updateList(filteredList);
                    });
                } catch (JSONException e) {
                    onError("Error parsing properties: " + e.getMessage());
                    Log.println(Log.ERROR, "Error","Error parsing properties: " + e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(BuscarPropiedad.this, error, Toast.LENGTH_LONG).show()
                );
            }
        });

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProperties(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProperties(newText);
                return false;
            }
        });

        // Handle button click to return to the main menu
        ImageButton button3 = findViewById(R.id.imageButton2);
        button3.setOnClickListener(v -> {
            Intent intent = new Intent(BuscarPropiedad.this, MenuPrincipal.class);
            startActivity(intent);
        });
    }

    private List<Property> parseProperties(String response) throws JSONException {
        List<Property> properties = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(response);

        if (jsonResponse.getString("status").equals("success")) {
            JSONArray propertyArray = jsonResponse.getJSONArray("payload");

            for (int i = 0; i < propertyArray.length(); i++) {
                JSONObject propertyObj = propertyArray.getJSONObject(i);
                JSONObject propertyData = propertyObj.getJSONObject("propertyData");

                try {
                    Property property = new Property();
                    property.setUserId(propertyData.getString("userId"));
                    property.setName(propertyData.getString("nombrePropiedad"));
                    property.setPetsAllowed(propertyData.getString("mascotas"));

                    // Handle numeric values that come as strings from the server
                    String priceStr = propertyData.getString("precio");
                    property.setPrice(Double.parseDouble(priceStr));

                    property.setContact(propertyData.getString("contacto"));

                    String maxPeopleStr = propertyData.getString("maxPersonas");
                    property.setMaxPeople(Integer.parseInt(maxPeopleStr));

                    String latStr = propertyData.getString("latitud");
                    String lonStr = propertyData.getString("longitud");
                    property.setAmenidadesElegidas(propertyData.getString("amenidades"));
                    property.setLatitude(Double.parseDouble(latStr));
                    property.setLongitude(Double.parseDouble(lonStr));

                    properties.add(property);
                } catch (NumberFormatException e) {
                    Log.e("ParseError", "Error parsing numeric values: " + e.getMessage());
                    // Continue with next property if one fails
                    continue;
                }
            }
        }

        return properties;
    }

    private void applyFilters() {
        Log.d("FilterDebug", "Selected Amenities: " + selectedAmenities);
        filteredList.clear(); // Clear the filtered list before applying new filters

        for (Property property : propertyList) {
            Log.d("FilterDebug", "Property: " + property.getName() +
                    ", Amenities: " + property.getAmenidadesElegidas());

            // Price filter
            if (property.getPrice() > maxPrice) continue;

            // Max people filter
            if (property.getMaxPeople() > maxPeople) continue;

            // Pet-friendly filter
            if (petFriendly && !"Yes".equalsIgnoreCase(property.getPetsAllowed())) continue;

            // Amenities filter
            if (!selectedAmenities.isEmpty()) {
                String amenitiesString = property.getAmenidadesElegidas();
                if (amenitiesString == null || amenitiesString.isEmpty()) continue;





                // Convert the amenities string into a properly formatted list
                List<String> propertyAmenities;
                if (amenitiesString.startsWith("[") && amenitiesString.endsWith("]")) {
                    // Handle JSON-style lists stored as strings (remove brackets)
                    amenitiesString = amenitiesString.substring(1, amenitiesString.length() - 1);
                }

                // Normalize and split by comma
                propertyAmenities = Arrays.stream(amenitiesString.split(","))
                        .map(String::trim)  // Remove spaces around entries
                        .map(s -> s.replaceAll("\"", ""))  // Remove any remaining quotes
                        .collect(Collectors.toList());

                Log.d("FilterDebug", "Formatted Property Amenities: " + propertyAmenities);

                // Check if ALL selected amenities are present in the property's amenities
                boolean matchesAll = selectedAmenities.stream()
                        .allMatch(selectedAmenity ->
                                propertyAmenities.stream()
                                        .anyMatch(propertyAmenity ->
                                                propertyAmenity.equalsIgnoreCase(selectedAmenity)
                                        )
                        );

                if (!matchesAll) continue;
            }

            // If the property passes all filters, add it to the filtered list
            filteredList.add(property);
        }

        Log.d("FilterDebug", "Filtered List Size: " + filteredList.size());
        // Update the RecyclerView adapter with the filtered list
        adapter.updateList(filteredList);
    }


    private void resetFilters() {
        // Reset filter values to default
        maxPrice = 50000;
        maxPeople = 100;
        petFriendly = false;
        selectedAmenities.clear();

        // Reset UI components
        FrameLayout secondLayoutContainer = findViewById(R.id.frameFiltros);
        View secondLayout = LayoutInflater.from(BuscarPropiedad.this).inflate(R.layout.filtros, null);

        SeekBar seekBar = secondLayout.findViewById(R.id.seekBar);
        seekBar.setProgress(50000);
        TextView seekBarValue = secondLayout.findViewById(R.id.seekBarProgress);
        seekBarValue.setText("Valor colones: 50000");

        NumberPicker numberPicker = secondLayout.findViewById(R.id.numberPicker);
        numberPicker.setValue(100);

        Switch petSwitch = secondLayout.findViewById(R.id.switch1);
        petSwitch.setChecked(false);

        ChipGroup chipGroup = secondLayout.findViewById(R.id.chipGroupActividades);
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setChecked(false);
        }

        // Reset the filtered list to show all properties
        filteredList.clear();
        filteredList.addAll(propertyList);
        adapter.updateList(filteredList);
    }


    private void searchProperties(String query) {
        searchList.clear();
        if (query.isEmpty()) {
            searchList.addAll(propertyList);
        } else {
            for (Property property : propertyList) {
                if (property.getName().toLowerCase().contains(query.toLowerCase())) {
                    searchList.add(property);
                }
            }
        }
        adapter.updateList(searchList);
    }
}