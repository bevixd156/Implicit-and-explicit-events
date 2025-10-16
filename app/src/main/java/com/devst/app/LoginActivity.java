package com.devst.app;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Patterns;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    //Creamos variables
    private EditText edtEmail, edtPass;
    private ImageView ivConfig;
    private Button btnLogin;
    private View overlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPass  = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        ivConfig = findViewById(R.id.ivConfig);

        btnLogin.setOnClickListener(v -> intentoInicioSesion());
        findViewById(R.id.tvRecuperarpass).setOnClickListener(v ->
                Toast.makeText(this, "Función pendiente: recuperar contraseña", Toast.LENGTH_SHORT).show());
        findViewById(R.id.tvCrear).setOnClickListener(v ->
                Toast.makeText(this, "Función pendiente: crear cuenta", Toast.LENGTH_SHORT).show());

        // Evento Intent explicito -> Redirige desde la vista LoginActivity.class a la vista de ConfigActivity.class
        ivConfig.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ConfigActivity.class);
            startActivity(intent);
        });
    }



    private void intentoInicioSesion() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";
        String pass  = edtPass.getText()  != null ? edtPass.getText().toString() : "";

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Ingresa tu correo");
            edtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Correo inválido");
            edtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            edtPass.setError("Ingresa tu contraseña");
            edtPass.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            edtPass.setError("Mínimo 6 caracteres");
            edtPass.requestFocus();
            return;
        }

        boolean ok = email.equals("estudiante@st.cl") && pass.equals("123456");

        if (ok) {
            // 💠 Creamos la pantalla azul que cubrirá todo
            View overlay = getLayoutInflater().inflate(R.layout.activity_transicion, null);
            addContentView(overlay, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            // Animación fade-in
            overlay.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .withEndAction(() -> {
                        // Cambiamos de pantalla
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("email_usuario", email);
                        intent.putExtra("pass_usuario", pass);
                        startActivity(intent);

                        // Transición fade-out
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        finish();
                    })
                    .start();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}


