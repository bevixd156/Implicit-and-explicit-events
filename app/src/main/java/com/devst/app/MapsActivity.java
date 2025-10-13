package com.devst.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MapsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maps);

        //Referenciamos los elementos de activity_maps por su ID
        EditText edtDireccion = findViewById(R.id.edtDireccion);
        Button btnBuscar = findViewById(R.id.btnBuscar);

        // Definimos la acción del botón Buscar
        btnBuscar.setOnClickListener(v -> {
            // Definimos un String para el lugar que el usuario ingresará
            String lugar = edtDireccion.getText().toString();
            // Verificamos que el lugar no esté vacío
            if (!lugar.isEmpty()) {
                // Buscar dirección o lugar en maps
                Uri gmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(lugar));
                // Intent para abrir maps en la ubicación indicada
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
                // Intent para buscar si existe una aplicación que maneje este Intent
                // Abrir con cualquier app disponible de mapas
                Intent chooser = Intent.createChooser(mapIntent, "Abrir con");
                // Intentamos abrir las aplicaciones de mapas
                try {
                    // Inicia la app de mapas seleccionados
                    startActivity(chooser);
                } catch (android.content.ActivityNotFoundException ex) {
                    //Si no hay ninguna app de mapas
                    Toast.makeText(this, "No se encontró ninguna aplicación de mapas 😓😓😓", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Si no se Ingresó una dirección o lugar
                Toast.makeText(this, "Ingresa una Dirección o Lugar 🙄🙄🙄", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
