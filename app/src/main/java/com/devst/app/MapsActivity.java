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

        EditText edtD = findViewById(R.id.edtDireccion);
        Button btnBuscar = findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(v -> {
            String lugar = edtD.getText().toString();
            if (!lugar.isEmpty()) {
                Uri gmIntentUri = Uri.parse("geo:0,0?=" + Uri.encode(lugar));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "No se encontró la Aplicación de Google Mpas", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingresa una Dirección o Lugar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
