package com.example.registro.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.registro.ui.main.MainActivity;
import com.example.registro.R;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OlvidoContrasena extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_olvido_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ajuste para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el botón y configurar el listener
        Button button1 = findViewById(R.id.Regresar);
        button1.setOnClickListener(v -> {
            // Redirigir a la actividad OlvidoContrasena
            Intent intent1 = new Intent(OlvidoContrasena.this, MainActivity.class);
            startActivity(intent1);
        });

        Button button = findViewById(R.id.Verificar);
        button.setOnClickListener(v -> {
            // Redirigir a la actividad OlvidoContrasena
            Intent intent2 = new Intent(OlvidoContrasena.this, CambioContrasena.class);
            startActivity(intent2);
        });
    }
}