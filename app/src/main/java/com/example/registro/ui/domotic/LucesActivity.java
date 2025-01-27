package com.example.registro.ui.domotic;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class LucesActivity extends AppCompatActivity {

    private Socket socket;
    private PrintWriter out;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_luces);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botones
        Button btnBanoPrincipal = findViewById(R.id.btnBanoPrincipal);
        Button btnGaraje = findViewById(R.id.btnGaraje);
        Button btnCuarto2 = findViewById(R.id.btnCuarto2);
        Button btnBanoCuarto = findViewById(R.id.btnBanoCuarto);
        Button btnCocina = findViewById(R.id.btnCocina);
        Button btnCuarto1 = findViewById(R.id.btnCuarto1);
        Button btnCuarto3 = findViewById(R.id.btnCuarto3);
        Button btnSala = findViewById(R.id.btnSala);

        configureSwitchBehavior(btnBanoPrincipal, "BanoPrincipal");
        configureSwitchBehavior(btnGaraje, "Garaje");
        configureSwitchBehavior(btnCuarto2, "Habitacion2");
        configureSwitchBehavior(btnBanoCuarto, "BanoHabitacion");
        configureSwitchBehavior(btnCocina, "Cocina");
        configureSwitchBehavior(btnCuarto1, "Habitacion1");
        configureSwitchBehavior(btnCuarto3, "Habitacion3");
        configureSwitchBehavior(btnSala, "Sala");

        connectToServer();
    }
    private void configureSwitchBehavior(Button button, String room) {
        button.setOnClickListener(v -> {
            // Alterna entre verde y blanco
            if (button.getCurrentTextColor() == Color.WHITE) {
                button.setBackgroundColor(Color.GREEN); // Cambia a verde
                button.setTextColor(Color.WHITE); // Mantiene el texto blanco
                sendJsonToServer("On" + room); // Enviar JSON cuando la luz está encendida
            } else {
                button.setBackgroundColor(Color.WHITE); // Cambia a blanco
                button.setTextColor(Color.BLACK); // Cambia el texto a negro
                sendJsonToServer("Off" + room); // Enviar JSON cuando la luz está apagada
            }
        });
    }

    // Enviar el JSON al servidor
    private void sendJsonToServer(String command) {
        if (socket != null && socket.isConnected() && out != null) {
            try {
                String json = String.format("{\"action\": \"LUCES\", \"payload\": {\"command\": \"%s\"}}", command);

                out.println(json);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket("192.168.0.1", 1717);
                OutputStream os = socket.getOutputStream();
                out = new PrintWriter(os, true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}