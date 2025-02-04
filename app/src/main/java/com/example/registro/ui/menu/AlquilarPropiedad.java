package com.example.registro.ui.menu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.registro.R;
import com.example.registro.data.model.Property;
import android.widget.ImageButton;


import java.util.Calendar;
import java.util.Locale;





public class AlquilarPropiedad extends AppCompatActivity {

    private EditText entrada, salida;
    private TextView nombrecasa, terminos, textoamenidades;
    private Property selectedProperty;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alquilar_propiedad);


        // Find views
        nombrecasa = findViewById(R.id.nombrecasa);
        terminos = findViewById(R.id.terminos);
        textoamenidades = findViewById(R.id.textoamenidades);

        // Retrieve the selected property
        if (getIntent().hasExtra("SELECTED_PROPERTY")) {
            selectedProperty = getIntent().getParcelableExtra("SELECTED_PROPERTY");

            //Update UI with property details
            if (selectedProperty != null) {
                nombrecasa.setText(selectedProperty.getName());

                // Create a description for the property
                String descripcion = String.format("Precio: $%.2f\nMáx. Personas: %d\nPet friendly: %s\nContacto: %s",
                        selectedProperty.getPrice(),
                        selectedProperty.getMaxPeople(),
                        selectedProperty.getPetsAllowed(),
                        selectedProperty.getContact());
                terminos.setText(descripcion);

                textoamenidades.setText(selectedProperty.getAmenidadesElegidas());
            }
        }

        //Back button functionality
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AlquilarPropiedad.this, BuscarPropiedad.class);
            startActivity(intent);
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        entrada= findViewById(R.id.entrada);
        salida = findViewById(R.id.salida);

        setupDatePicker();
        setupDatePicker1();


    }
    //Set up fecha
    private void setupDatePicker() {
        // Prevent keyboard from showing up
        entrada.setInputType(InputType.TYPE_NULL);

        entrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AlquilarPropiedad.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //Format the date as dd/mm/yyyy
                                String selectedDate = String.format(Locale.getDefault(),
                                        "%02d/%02d/%d",
                                        dayOfMonth,
                                        monthOfYear + 1,
                                        year);
                                entrada.setText(selectedDate);
                            }
                        },
                        year,
                        month,
                        day
                );


                Calendar minDate = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());


                datePickerDialog.show();
            }
        });
    }
    //Set up fecha
    private void setupDatePicker1() {
        // Prevent keyboard from showing up
        salida.setInputType(InputType.TYPE_NULL);

        salida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AlquilarPropiedad.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //Format the date as dd/mm/yyyy
                                String selectedDate = String.format(Locale.getDefault(),
                                        "%02d/%02d/%d",
                                        dayOfMonth,
                                        monthOfYear + 1,
                                        year);
                                salida.setText(selectedDate);
                            }
                        },
                        year,
                        month,
                        day
                );

                Calendar minDate = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }
}
