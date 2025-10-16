package com.devst.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.imageview.ShapeableImageView;

public class PerfilActivity extends AppCompatActivity {

    // Declaramos las variables de la vista de perfil
    private TextView tvCorreo, tvPassword;
    private EditText edtNombre;
    private Button btnGuardarCambios;
    private ShapeableImageView imgPerfil;
    private ActivityResultLauncher<Intent> seleccionarImagenLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        // Conectamos las variables del activity_perfil.xml
        imgPerfil = findViewById(R.id.imgPerfil);
        edtNombre = findViewById(R.id.edtNombre);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvPassword = findViewById(R.id.tvPassword);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // Inicializa el launcher
        seleccionarImagenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imagenUri = result.getData().getData();
                        imgPerfil.setImageURI(imagenUri);
                    }
                }
        );

        // Abre la galería al hacer clic
        imgPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            seleccionarImagenLauncher.launch(intent);
        });

        //Recibimos los datos de HomeActivity
        Intent intent = getIntent();
        String email = intent.getStringExtra("email_usuario");
        String pass = intent.getStringExtra("pass_usuario");

        //Mostramos los datos
        tvCorreo.setText("Correo: " + email);
        tvPassword.setText("Contraseña: " + pass);

    }
}