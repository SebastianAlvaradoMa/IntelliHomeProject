package com.example.registro.ui.menu;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.registro.R;

import java.util.Calendar;
import java.util.Locale;





public class AlquilarPropiedad extends AppCompatActivity {

    private EditText entrada, salida;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alquilar_propiedad);
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
