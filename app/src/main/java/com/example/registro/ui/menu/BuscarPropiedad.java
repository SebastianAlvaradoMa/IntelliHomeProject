package com.example.registro.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registro.R;
import com.example.registro.data.model.Property;
import com.example.registro.data.service.PropertyService;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class BuscarPropiedad extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<Property> propertyList = new ArrayList<>();
    private List<Property> searchList = new ArrayList<>();
    private Adapter adapter;
    private boolean isLayoutVisible = false;   //Saber si el layout filtros esta visible

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

        //-------------------
        //LAYOUT FILTROS
        //--------------------

        ImageButton buttonShowLayout = findViewById(R.id.filtros);
        FrameLayout secondLayoutContainer = findViewById(R.id.frameFiltros);

        // Inflar el layout una sola vez
        LayoutInflater inflater = LayoutInflater.from(BuscarPropiedad.this);
        View secondLayout = inflater.inflate(R.layout.filtros, null);


        // Configurar el OnClickListener para el botón
        buttonShowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLayoutVisible) {
                    // Si el layout está visible, ocultarlo
                    secondLayoutContainer.setVisibility(View.GONE);
                } else {
                    // Si el layout no está visible, mostrarlo
                    secondLayoutContainer.removeAllViews(); // Limpiar el contenedor
                    secondLayoutContainer.addView(secondLayout); // Añadir el layout inflado
                    secondLayoutContainer.setVisibility(View.VISIBLE);
                }

                // Cambiar el estado de visibilidad
                isLayoutVisible = !isLayoutVisible;
            }
        });



        // Fetch properties from the server
        PropertyService propertyService = new PropertyService();
        propertyService.fetchProperty(new PropertyService.PropertyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    List<Property> properties = parseProperties(response);
                    runOnUiThread(() -> {
                        propertyList.clear();
                        propertyList.addAll(properties);
                        searchList.addAll(properties);
                        adapter.notifyDataSetChanged();
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