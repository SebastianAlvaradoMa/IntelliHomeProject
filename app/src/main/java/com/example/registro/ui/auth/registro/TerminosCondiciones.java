package com.example.registro.ui.auth.registro;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registro.R;

public class TerminosCondiciones extends AppCompatActivity {
    TextView terminos;
    CheckBox check1;
    Button Continuar;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terminos_condiciones);

        terminos = findViewById(R.id.terminos);
        Continuar = findViewById(R.id.Continuar);
        check1 = findViewById(R.id.check1);
        scrollView = findViewById(R.id.scrollView);

        // Desactivar inicialmente el CheckBox
        check1.setEnabled(false);

        // Detectar cuando el usuario llega al final del ScrollView
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = scrollView.getScrollY(); // Posición vertical del scroll
            int scrollViewHeight = scrollView.getHeight(); // Altura del ScrollView
            int contentHeight = scrollView.getChildAt(0).getHeight(); // Altura total del contenido

            // Habilitar el CheckBox si se llegó al final del scroll
            if (scrollY + scrollViewHeight >= contentHeight) {
                check1.setEnabled(true);
            }
        });

        // Configurar el botón Continuar
        Continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check1.isChecked()) {
                    Intent intent = new Intent(TerminosCondiciones.this, SignUpActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Manejar insets del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
