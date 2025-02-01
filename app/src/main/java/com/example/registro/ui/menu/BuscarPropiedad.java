package com.example.registro.ui.menu;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.registro.R;

import java.sql.Array;
import java.util.ArrayList;

public class BuscarPropiedad extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;

    ArrayList<ModelClass> arrayList = new ArrayList<>();
    ArrayList<ModelClass> searchList;

    String [] propiedadList = new String[]{"Casa Los Pinos", "Atenas","Esterillos", "Cabaña Cielo Azul"};
    String [] descripcionList = new String[]{"Descripcion1", "Descripcion2", "Descripcion3", "Descripcion4"};
    int [] imgList = new int[]{R.drawable.casa,R.drawable.casa2,R.drawable.casa2,R.drawable.casa2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_propiedad);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        // Obtener el botón y configurar el listener
        ImageButton button3 = findViewById(R.id.imageButton2);
        button3.setOnClickListener(v -> {
            // Redirigir a la actividad OlvidoContrasena
            Intent intent = new Intent(BuscarPropiedad.this, MenuPrincipal.class);
            startActivity(intent);
        });

        for (int i = 0; i < propiedadList.length; i++) {
            ModelClass modelClass = new ModelClass();
            modelClass.setNombrePropiedad(propiedadList[i]);
            modelClass.setDescripcion(descripcionList[i]);
            modelClass.setImg(imgList[i]);
            arrayList.add(modelClass);

        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BuscarPropiedad.this);
        recyclerView.setLayoutManager(layoutManager);

        Adapter adapter = new Adapter(BuscarPropiedad.this,arrayList);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                searchList = new ArrayList<>();


                if (newText.length()>0){
                    for (int i=0; i <arrayList.size(); i++){
                        if(arrayList.get(i).getNombrePropiedad().toUpperCase().contains(newText.toUpperCase())){
                            ModelClass modelClass = new ModelClass();
                            modelClass.setNombrePropiedad(arrayList.get(i).getNombrePropiedad());
                            modelClass.setDescripcion(arrayList.get(i).getDescripcion());
                            modelClass.setImg(arrayList.get(i).getImg());
                            searchList.add(modelClass);

                        }
                    }
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BuscarPropiedad.this);
                    recyclerView.setLayoutManager(layoutManager);

                    Adapter adapter = new Adapter(BuscarPropiedad.this,searchList);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BuscarPropiedad.this);
                    recyclerView.setLayoutManager(layoutManager);

                    Adapter adapter = new Adapter(BuscarPropiedad.this,searchList);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}