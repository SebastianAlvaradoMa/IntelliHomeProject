package com.example.registro.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;
import com.example.registro.data.service.ChangePasswordValidationService;
import com.example.registro.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

public class CambioContrasena extends AppCompatActivity {

    private String userId; // Variable to store the userId
    private ChangePasswordValidationService validationService; // Service for password change

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambio_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the validation service
        validationService = new ChangePasswordValidationService();

        // Retrieve the userId from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_ID")) {
            userId = intent.getStringExtra("USER_ID");
            Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_SHORT).show(); // Optional: Display userId for debugging
        }

        // Obtener el botón y configurar el listener para regresar
        Button regresarButton = findViewById(R.id.RegresarCambio);
        regresarButton.setOnClickListener(v -> {
            // Redirigir a la actividad OlvidoContrasena
            Intent intent1 = new Intent(CambioContrasena.this, OlvidoContrasena.class);
            startActivity(intent1);
        });

        // Obtener el botón y configurar el listener para cambiar la contraseña
        Button cambiarContrasenaButton = findViewById(R.id.CambiarContrasena);
        cambiarContrasenaButton.setOnClickListener(v -> {
            // Obtener la nueva contraseña y la confirmación de la contraseña
            TextInputEditText newPasswordEditText = findViewById(R.id.newPasswordEditText);
            TextInputEditText newConfirmPasswordEditText = findViewById(R.id.newConfirmPasswordEditText);

            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = newConfirmPasswordEditText.getText().toString().trim();

            // Validar que los campos no estén vacíos
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar que las contraseñas coincidan
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the changePassword method from the service
            validationService.changePassword(userId, newPassword, new ChangePasswordValidationService.ChangePasswordValidationCallback() {
                @Override
                public void onSuccess(String message) {
                    // Password change successful
                    Toast.makeText(CambioContrasena.this, message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CambioContrasena.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close the current activity
                }

                @Override
                public void onError(String error) {
                    // Password change failed
                    Toast.makeText(CambioContrasena.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}