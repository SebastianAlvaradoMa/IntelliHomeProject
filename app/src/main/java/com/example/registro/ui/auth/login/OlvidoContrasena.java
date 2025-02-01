package com.example.registro.ui.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.registro.R;
import com.example.registro.data.service.ChangePasswordValidationService;
import com.google.android.material.textfield.TextInputEditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class OlvidoContrasena extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText question1EditText;
    private TextInputEditText question2EditText;
    private TextInputEditText question3EditText;

    private ChangePasswordValidationService validationService;

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

        // Initialize the validation service
        validationService = new ChangePasswordValidationService();

        // Find the EditText fields
        emailEditText = findViewById(R.id.emailEditText); // Replace with the actual ID from your layout
        question1EditText = findViewById(R.id.question1EditText); // Replace with the actual ID from your layout
        question2EditText = findViewById(R.id.question2EditText); // Replace with the actual ID from your layout
        question3EditText = findViewById(R.id.question3EditText); // Replace with the actual ID from your layout

        // Find the buttons
        Button regresarButton = findViewById(R.id.Regresar);
        Button verificarButton = findViewById(R.id.Verificar);

        // Set the listener for the Regresar button
        regresarButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(OlvidoContrasena.this, CambioContrasena.class);
            startActivity(intent1);
        });

        // Set the listener for the Verificar button
        // Set the listener for the Verificar button
        verificarButton.setOnClickListener(v -> {
            // Extract the email and security question answers
            String email = emailEditText.getText().toString().trim();
            String pregunta1 = question1EditText.getText().toString().trim();
            String pregunta2 = question2EditText.getText().toString().trim();
            String pregunta3 = question3EditText.getText().toString().trim();

            // Validate the inputs
            if (email.isEmpty() || pregunta1.isEmpty() || pregunta2.isEmpty() || pregunta3.isEmpty()) {
                Toast.makeText(OlvidoContrasena.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the validation service
            validationService.changePasswordValidation(email, pregunta1, pregunta2, pregunta3, new ChangePasswordValidationService.ChangePasswordValidationCallback() {
                @Override
                public void onSuccess(String response) { // response is the raw JSON from the server
                    try {
                        // Parse the raw JSON response
                        JSONObject responseJson = new JSONObject(response);

                        // Check if the status is "success"
                        if (responseJson.getString("status").equals("success")) {
                            JSONObject payload = responseJson.getJSONObject("payload");
                            String userId = payload.getString("userId");

                            // If validation is successful, redirect to the CambioContrasena activity
                            Toast.makeText(OlvidoContrasena.this, "Validation successful", Toast.LENGTH_SHORT).show();

                            // Create an Intent to start the CambioContrasena activity
                            Intent intent2 = new Intent(OlvidoContrasena.this, CambioContrasena.class);

                            // Pass the userId to the next activity
                            intent2.putExtra("USER_ID", userId);

                            // Start the CambioContrasena activity
                            startActivity(intent2);
                        } else {
                            // Handle the case where the status is not success
                            Toast.makeText(OlvidoContrasena.this, "Validation failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(OlvidoContrasena.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {
                    // If validation fails, show an error message
                    Toast.makeText(OlvidoContrasena.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}