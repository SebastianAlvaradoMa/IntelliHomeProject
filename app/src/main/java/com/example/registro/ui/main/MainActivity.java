package com.example.registro.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;
import com.example.registro.data.remote.SocketClient;
import com.example.registro.data.remote.SocketManager;
import com.example.registro.data.service.AuthService;
import com.example.registro.ui.auth.login.OlvidoContrasena;
import com.example.registro.ui.auth.login.SecondActivity;
import com.example.registro.ui.auth.registro.SignUpActivity;
import com.example.registro.ui.auth.registro.TerminosCondiciones;
import com.example.registro.ui.menu.MenuPrincipal;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
public class MainActivity extends AppCompatActivity {

    private AuthService authService;

    public TextInputEditText inputUsername;
    public TextInputEditText inputPassword;
    private Button loginButton;
    private ProgressDialog progressDialog;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageButton googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init del socket
        SocketManager.getInstance().connect(new SocketClient.SocketCallback() {
            @Override
            public void onResponse(String response) {
                // Connection successful
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Connected to server", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(String error) {
                // Error connecting
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Connection error: " + error, Toast.LENGTH_LONG).show());
            }
        });

        authService = new AuthService(SocketManager.getInstance().getSocketClient());

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener el TextView y configurar el listener
        TextView textView = findViewById(R.id.textView2);
        textView.setOnClickListener(v -> {
            // Redirigir a la actividad OlvidoContrasena
            Intent intent = new Intent(MainActivity.this, OlvidoContrasena.class);
            startActivity(intent);
        });

        // Inicializar vistas
        initializeViews();
        setupClickListeners();
        setupGoogleSignIn();
    }

    private void initializeViews() {
        inputUsername = findViewById(R.id.usernameEditText);
        inputPassword = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.Button);
        googleBtn = findViewById(R.id.googleBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);
    }

    private void setupClickListeners() {
        // Login button click listener
        loginButton.setOnClickListener(v -> handleLogin());

        // Forgot password click listener
        TextView textView = findViewById(R.id.textView2);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OlvidoContrasena.class);
            startActivity(intent);
        });

        // Register button click listener
        Button buttonRegistrarse = findViewById(R.id.Button1);
        buttonRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TerminosCondiciones.class);
            startActivity(intent);
        });
    }

    private void setupGoogleSignIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleBtn.setOnClickListener(v -> signIn());
    }

    public void handleLogin() {
        String usernameOrEmail = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Validación básica
        if (usernameOrEmail.isEmpty()) {
            inputUsername.setError("El nombre de usuario o e-mail es requerido");
            return;
        }
        if (password.isEmpty()) {
            inputPassword.setError("La contraseña es requerida");
            return;
        }

        // Limpiar errores previos
        inputUsername.setError(null);
        inputPassword.setError(null);

        // Mostrar diálogo de progreso
        progressDialog.show();

        // Intentar login
        authService.login(usernameOrEmail, password, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    // Login exitoso - navegar a la siguiente actividad
                    navigateToMainScreen();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    showError("Error de inicio de sesión", error);
                });
            }
        });
    }

    private void showError(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
        startActivity(intent);
        finish(); // Cerrar la actividad de login
    }

    // Google Sign In methods
    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                navigateToMainScreen();
                task.getResult(ApiException.class);
            } catch(ApiException e) {
                Toast.makeText(this, "Algo salió mal", Toast.LENGTH_SHORT).show();
            }
        }
    }
}