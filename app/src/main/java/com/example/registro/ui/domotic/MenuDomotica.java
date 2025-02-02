package com.example.registro.ui.domotic;

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
import com.example.registro.ui.menu.GestionPropiedad;
import com.example.registro.ui.menu.MenuPrincipal;

public class MenuDomotica extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_domotica);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton button = findViewById(R.id.Menu);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDomotica.this, MenuPrincipal.class);
            startActivity(intent);
        });

        Button button1 = findViewById(R.id.Iluminacion);
        button1.setOnClickListener(v -> {
            Intent intent1 = new Intent(MenuDomotica.this, LucesActivity.class);
            startActivity(intent1);
        });

        Button button2 = findViewById(R.id.Alarmas);
        button2.setOnClickListener(v -> {
            Intent intent2 = new Intent(MenuDomotica.this, Sensores.class);
            startActivity(intent2);
        });
    }
}