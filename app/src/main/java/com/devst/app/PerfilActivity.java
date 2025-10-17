package com.devst.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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

    // SharedPreferences
    private static final String PREFS_NAME = "perfil_prefs";
    private static final String KEY_USUARIO = "nombre_usuario";
    private static final String KEY_IMAGEN = "imagen_uri";

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

        //Comprobamos si algunos de los datos es null
        if (tvCorreo == null || tvPassword == null || edtNombre == null || btnGuardarCambios == null || imgPerfil == null) {
            //Si es null regresa un mensaje personalizado
            throw new RuntimeException("Error: revisa los IDs en tu XML. Algún view no existe.");
        }

        // SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Cargar nombre guardado
        String usuarioGuardado = prefs.getString(KEY_USUARIO, "");
        edtNombre.setText(usuarioGuardado);

        // Cargar imagen guardada de forma segura
        String imagenGuardada = prefs.getString(KEY_IMAGEN, null);
        if (imagenGuardada != null && !imagenGuardada.isEmpty()) {
            try {
                imgPerfil.setImageURI(Uri.parse(imagenGuardada));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Inicializar launcher para seleccionar imagen
        seleccionarImagenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imagenUri = result.getData().getData();
                        if (imagenUri != null) {
                            // Guardar la nueva imagen inmediatamente en almacenamiento interno
                            Uri uriInterna = guardarImagenInterna(imagenUri);
                            if (uriInterna != null) {
                                // Actualizar la vista con la nueva imagen
                                imgPerfil.setImageURI(uriInterna);

                                // Guardar en SharedPreferences para que se cargue la próxima vez
                                prefs.edit().putString(KEY_IMAGEN, uriInterna.toString()).apply();
                            }
                        }
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
        tvCorreo.setText("Correo: " + (email != null ? email : ""));
        tvPassword.setText("Contraseña: " + (pass != null ? pass : ""));

        // Guardar cambios
        btnGuardarCambios.setOnClickListener(v -> {
            String nuevoNombre = edtNombre.getText() != null ? edtNombre.getText().toString().trim() : "";

            prefs.edit().putString(KEY_USUARIO, nuevoNombre).apply();

            Intent data = new Intent();
            data.putExtra("nombre_editado", nuevoNombre);
            setResult(RESULT_OK, data);
            finish();
        });
    }

    // Metodo Guardar Imagen para almacenarla aunque se cierre la aplicación
    private Uri guardarImagenInterna(Uri sourceUri) {
        try {
            // InputStream desde la Uri de la imagen seleccionada
            InputStream in = getContentResolver().openInputStream(sourceUri);
            // Si no se puede abrir se retornará null
            if (in == null) return null;

            //Creamos una carpeta llamada perfil
            File dir = new File(getFilesDir(), "perfil");
            //Si no está la carpeta la creamos
            if (!dir.exists()) dir.mkdirs();

            //Definimos el nombre del archivo interno
            String filename = "perfil.jpg"; // siempre se sobreescribe de la imágen anterior
            File outFile = new File(dir, filename);

            //OutputStream para escribir el contnt del archivo interno
            OutputStream out = new FileOutputStream(outFile);

            //Copiamos los bytes del InputStream al OutputStream
            byte[] buffer = new byte[1024];
            int read;
            //Mientras existan bytes qye leer
            while ((read = in.read(buffer)) != -1) {
                //Se escribe en el archivo interno
                out.write(buffer, 0, read);
            }

            //Cerramos los streams para la liberación de recursos
            in.close();
            out.flush();
            out.close();

            //Retornamos el Uti que apuntará al archivo guardado internamente
            return Uri.fromFile(outFile);
        } catch (Exception e) {
            //Si en caso de error se imprime un trazo del error y nos devuelve null
            e.printStackTrace();
            return null;
        }
    }
}