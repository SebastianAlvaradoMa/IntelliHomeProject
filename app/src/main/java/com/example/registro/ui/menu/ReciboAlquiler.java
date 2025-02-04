package com.example.registro.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;

public class ReciboAlquiler extends AppCompatActivity {

    private TextView nombrePropiedad, amenidades, fechaEntrada, fechaSalida, tarjeta, total, cantidadNoches;
    private Button pagarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recibo_alquiler);

        nombrePropiedad = findViewById(R.id.nombrePropiedad);
        amenidades = findViewById(R.id.amenidades);
        fechaEntrada = findViewById(R.id.fechaEntrada);
        fechaSalida = findViewById(R.id.fechaSalida);
        tarjeta = findViewById(R.id.tarjeta);
        total = findViewById(R.id.total);
        cantidadNoches = findViewById(R.id.cantidadNoches);
        pagarButton = findViewById(R.id.pagarButton);

        String nombre = getIntent().getStringExtra("nombrePropiedad");
        String amenities = getIntent().getStringExtra("amenidades");
        String entrada = getIntent().getStringExtra("fechaEntrada");
        String salida = getIntent().getStringExtra("fechaSalida");
        String tarjetaStr = getIntent().getStringExtra("tarjeta");
        int noches = getIntent().getIntExtra("noches", 1);
        double totalAmount = getIntent().getDoubleExtra("total", 0);

        nombrePropiedad.setText("Propiedad: " + nombre);
        amenidades.setText("Amenidades: " + amenities);
        fechaEntrada.setText("Fecha Entrada: " + entrada);
        fechaSalida.setText("Fecha Salida: " + salida);
        tarjeta.setText("Tarjeta: **** **** **** " + tarjetaStr);
        total.setText("Total con IVA: $" + String.format("%.2f", totalAmount));
        cantidadNoches.setText("Cantidad de noches: " + noches);

        pagarButton.setOnClickListener(v -> realizarPago());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void volverAlquilar(View view) {
        Intent intent = new Intent(this, AlquilarPropiedad.class);
        startActivity(intent);
        finish();
    }

    public void realizarPago() {
        Toast.makeText(this, "¡Compra realizada con éxito!", Toast.LENGTH_SHORT).show();
    }
}
