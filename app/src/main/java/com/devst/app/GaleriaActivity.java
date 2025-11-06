package com.devst.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class GaleriaActivity extends AppCompatActivity {

    //Identificamos la selección
    private static final int PICK_IMAGE = 100;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_galeria);

        // Referencias a UI
        imageView = findViewById(R.id.imagenSeleccionada);
        Button btnSeleccionar = findViewById(R.id.btnSeleccionarImagen);

        // Intent implícito -> seleccionar imagen directa de la galería
        btnSeleccionar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*"); // Solo imágenes
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE);
        });
    }

    // Recibe resultado de la selección
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica que sea la selección de imagen y que haya resultado OK
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData(); // Obtiene la URI de la imagen seleccionada
            if (imageUri != null) {
                imageView.setImageURI(imageUri); // Muestra la imagen en el ImageView
            } else {
                Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
