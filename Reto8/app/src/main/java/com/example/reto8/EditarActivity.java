package com.example.reto8;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reto8.db.DbEmpresas;
import com.example.reto8.entidades.Empresas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public class EditarActivity extends AppCompatActivity {

    EditText txtNombre, txtUrl, txtTelefono, txtEmail, txtProductos;
    Button btnActualizar;
    Spinner txtclasificacion;
    FloatingActionButton floatingActualizar, floatEliminar;


    Empresas empresa;
    int id = 0;
    boolean correcto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        txtNombre = findViewById(R.id.txtNombre);
        txtUrl = findViewById(R.id.txtUrl);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtEmail = findViewById(R.id.txtEmail);
        txtProductos= findViewById(R.id.txtProductos);

        txtclasificacion = findViewById(R.id.txtClasificacion);

        String [] opciones = {"Consultoria", "Desarrollo", "Fabricacion"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,opciones);
        txtclasificacion.setAdapter(adapter);

        btnActualizar = findViewById(R.id.btnActualizar);
        floatingActualizar = findViewById(R.id.floatingActualizar);
        floatingActualizar.setVisibility(View.INVISIBLE);
        floatEliminar = findViewById(R.id.floatingEliminar);
        floatEliminar.setVisibility(View.INVISIBLE);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                id = Integer.parseInt(null);
            }else{
                id = extras.getInt("ID");
            }
        }else{
            id = (int) savedInstanceState.getSerializable("ID");
        }
        DbEmpresas dbEmpresas = new DbEmpresas(EditarActivity.this);
        empresa = dbEmpresas.verEmpresa(id);

        if(empresa != null){
            txtNombre.setText(empresa.getNombre());
            txtUrl.setText(empresa.getUrl());
            txtTelefono.setText(empresa.getTelefono());
            txtEmail.setText(empresa.getEmail());
            txtProductos.setText(empresa.getProducto());

            // Encontrar la posición de la clasificación en las opciones
            int posicion = -1;
            for (int i = 0; i < opciones.length; i++) {
                if (empresa.getClasificacion().equals(opciones[i])) {
                    posicion = i;
                    break;
                }
            }
            // Establecer la posición seleccionada en el Spinner
            if (posicion != -1) {
                txtclasificacion.setSelection(posicion);
            }
        }

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtNombre.getText().toString().equals("") && !txtProductos.getText().toString().equals("")){
                    correcto = dbEmpresas.editarEmpresa(id,
                            txtNombre.getText().toString(),
                            txtUrl.getText().toString(),
                            txtTelefono.getText().toString(),
                            txtEmail.getText().toString(),
                            txtProductos.getText().toString(),
                            txtclasificacion.getSelectedItem().toString());

                    if(correcto){
                        Toast.makeText(EditarActivity.this, "REGISTRO MODIFICADO", Toast.LENGTH_LONG).show();
                        verRegistro();
                    } else {
                        Toast.makeText(EditarActivity.this, "ERROR AL ACTUALIZAR", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditarActivity.this, "CAMPOS OBLIGATORIOS VACIOS", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void verRegistro(){
        Intent intent = new Intent(this, VerActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
}
