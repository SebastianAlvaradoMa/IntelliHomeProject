package com.example.registro.ui.domotic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


import com.example.registro.R;
import com.example.registro.data.service.LightService;
import com.example.registro.ui.menu.GestionPropiedad;
import com.example.registro.ui.menu.MenuPrincipal;
import com.google.android.material.snackbar.Snackbar;



public class LucesActivity extends AppCompatActivity {
    public LightService lightService;
    private boolean[] lightStates = new boolean[8]; // Track state of each light

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luces);

        lightService = new LightService();

        // Initialize buttons
        setupButton(R.id.btnBanoPrincipal, "BanoPrincipal", 0);
        setupButton(R.id.btnGaraje, "Garaje", 1);
        setupButton(R.id.btnCuarto2, "Habitacion3", 2);
        setupButton(R.id.btnBanoCuarto, "BanoHabitacion", 3);
        setupButton(R.id.btnCocina, "Cocina", 4);
        setupButton(R.id.btnCuarto1, "Habitacion1", 5);
        setupButton(R.id.btnCuarto3, "Habitacion2", 6);
        setupButton(R.id.btnSala, "Sala", 7);

        ImageButton menuButton = findViewById(R.id.myImageButton);


        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(LucesActivity.this, MenuDomotica.class);
            startActivity(intent);
        });

    }

    private void setupButton(int buttonId, String room, int stateIndex) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            // Toggle state
            lightStates[stateIndex] = !lightStates[stateIndex];

            // Update button appearance immediately for responsiveness
            updateButtonAppearance(button, lightStates[stateIndex]);

            // Send command to server
            lightService.controlLight(room, lightStates[stateIndex], new LightService.LightCallback() {
                @Override
                public void onSuccess(String message) {
                    runOnUiThread(() -> {
                        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        // Revert button state on error
                        lightStates[stateIndex] = !lightStates[stateIndex];
                        updateButtonAppearance(button, lightStates[stateIndex]);
                        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
                    });
                }
            });
        });
    }

    private void updateButtonAppearance(Button button, boolean isOn) {
        button.setBackgroundColor(isOn ? Color.GREEN : Color.WHITE);
        button.setTextColor(isOn ? Color.WHITE : Color.BLACK);
    }
}