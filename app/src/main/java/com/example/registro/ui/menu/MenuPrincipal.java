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
import com.example.registro.data.model.UserSession;
import com.example.registro.ui.domotic.LucesActivity;
import com.example.registro.ui.domotic.MenuDomotica;
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

        // Obtener el botón y configurar el listemner
        ImageButton button3 = findViewById(R.id.imageButton9);
        button3.setOnClickListener(v -> {
            // Redirigir a la actividad OlvidoContrasena
            Intent intent1 = new Intent(MenuPrincipal.this, BuscarPropiedad.class);
            startActivity(intent1);
        });


        ImageButton button2 = findViewById(R.id.imageButton8);
        button2.setOnClickListener(v -> {
            Intent intent2 = new Intent(MenuPrincipal.this, GestionPropiedad.class);
            startActivity(intent2);
        });

        ImageButton button4 = findViewById(R.id.imageButton11);
        button4.setOnClickListener(v -> {
            Intent intent3 = new Intent(MenuPrincipal.this, MenuDomotica.class);
            startActivity(intent3);
        });
    }
}