package com.example.reto8;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.reto8.db.DbEmpresas;

public class NuevoActivity extends AppCompatActivity {

    EditText txtNombre, txtUrl, txtTelefono, txtEmail, txtProductos;
    Button btnRegistrar;
    Spinner spinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

        txtNombre = findViewById(R.id.txtNombre);
        txtUrl = findViewById(R.id.txtUrl);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtEmail = findViewById(R.id.txtEmail);
        txtProductos = findViewById(R.id.txtProductos);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        spinner = findViewById(R.id.spClase);

        String [] opciones = {"Elige una clasificacion","Consultoria", "Desarrollo", "Fabricacion"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(adapter);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!txtNombre.getText().toString().equals("") && !txtProductos.getText().toString().equals("")){
                    DbEmpresas dbEmpresas = new DbEmpresas(NuevoActivity.this);
                    long id = dbEmpresas.insertarEmpresa(txtNombre.getText().toString(),
                            txtUrl.getText().toString(), txtTelefono.getText().toString(),
                            txtEmail.getText().toString(), txtProductos.getText().toString(),
                            spinner.getSelectedItem().toString());

                    if(id>0){
                        Toast.makeText(NuevoActivity.this, "REGISTRO EXITOSO", Toast.LENGTH_LONG).show();
                        limpiar();
                    }else{
                        Toast.makeText(NuevoActivity.this, "ERROR AL GUARDAR EL REGISTRO", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(NuevoActivity.this, "Por favor completa los campos", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void limpiar(){

        txtNombre.setText("");
        txtUrl.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtProductos.setText("");
        spinner.setSelection(0);
    }
}