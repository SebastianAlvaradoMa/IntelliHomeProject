package com.example.registro.ui.menu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.registro.R;
import com.example.registro.data.model.Property;
import com.example.registro.data.model.User;

import android.widget.ImageButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AlquilarPropiedad extends AppCompatActivity {

    private EditText entrada, salida;
    private TextView nombrecasa, terminos, textoamenidades;
    private Property selectedProperty;
    private Button alquilarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alquilar_propiedad);

        nombrecasa = findViewById(R.id.nombrecasa);
        terminos = findViewById(R.id.terminos);
        textoamenidades = findViewById(R.id.textoamenidades);
        entrada = findViewById(R.id.entrada);
        salida = findViewById(R.id.salida);
        alquilarButton = findViewById(R.id.alquilarButton);

        if (getIntent().hasExtra("SELECTED_PROPERTY")) {
            selectedProperty = getIntent().getParcelableExtra("SELECTED_PROPERTY");
            if (selectedProperty != null) {
                nombrecasa.setText(selectedProperty.getName());
                String descripcion = String.format("Precio: $%.2f\nMáx. Personas: %d\nPet friendly: %s\nContacto: %s",
                        selectedProperty.getPrice(),
                        selectedProperty.getMaxPeople(),
                        selectedProperty.getPetsAllowed(),
                        selectedProperty.getContact());
                terminos.setText(descripcion);
                textoamenidades.setText(selectedProperty.getAmenidadesElegidas());
            }
        }

        setupDatePicker(entrada);
        setupDatePicker(salida);

        alquilarButton.setOnClickListener(v -> realizarAlquiler());
    }

    private void setupDatePicker(EditText editText) {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AlquilarPropiedad.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                        editText.setText(selectedDate);
                    },
                    year, month, day);

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    private void realizarAlquiler() {
        String fechaEntrada = entrada.getText().toString();
        String fechaSalida = salida.getText().toString();

        if (fechaEntrada.isEmpty() || fechaSalida.isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona las fechas", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            long diff = sdf.parse(fechaSalida).getTime() - sdf.parse(fechaEntrada).getTime();
            int noches = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if (noches < 1) {
                Toast.makeText(this, "La estancia mínima es de una noche", Toast.LENGTH_SHORT).show();
                return;
            }

            double total = noches * selectedProperty.getPrice() * 1.13;
            String ultimos4DigitosTarjeta = "1234"; // Esta tarjeta es falsa, pone ahi la del usuario Hidalgo

            Intent intent = new Intent(this, ReciboAlquiler.class);
            intent.putExtra("nombrePropiedad", selectedProperty.getName());
            intent.putExtra("amenidades", selectedProperty.getAmenidadesElegidas());
            intent.putExtra("fechaEntrada", fechaEntrada);
            intent.putExtra("fechaSalida", fechaSalida);
            intent.putExtra("noches", noches);
            intent.putExtra("tarjeta", ultimos4DigitosTarjeta);
            intent.putExtra("total", total);
            startActivity(intent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
