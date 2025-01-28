package com.example.registro.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;
import com.example.registro.ui.domotic.LucesActivity;
import com.example.registro.ui.main.MainActivity;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Obtener el botón y configurar el listener
        ImageButton button = findViewById(R.id.salir);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, MainActivity.class);
            startActivity(intent);
        });


        ImageButton button2 = findViewById(R.id.imageButton8);
        button2.setOnClickListener(v -> {
            Intent intent2 = new Intent(MenuPrincipal.this, GestionPropiedad.class);
            startActivity(intent2);
        });

        ImageButton luces = findViewById(R.id.imageButton11);
        luces.setOnClickListener(v -> {
            Intent intent2 = new Intent(MenuPrincipal.this,  LucesActivity.class);
            startActivity(intent2);
        });
    }
}