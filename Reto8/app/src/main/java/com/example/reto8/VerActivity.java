package com.example.reto8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.reto8.db.DbEmpresas;
import com.example.reto8.entidades.Empresas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VerActivity extends AppCompatActivity {

    EditText txtNombre, txtUrl, txtTelefono, txtEmail, txtProductos;
    Button btnActualizar;
    Spinner txtclasificacion;
    FloatingActionButton floatingActualizar, floatEliminar;

    Empresas empresa;
    int id = 0;

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
        floatEliminar = findViewById(R.id.floatingEliminar);

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
        DbEmpresas dbEmpresas = new DbEmpresas(VerActivity.this);
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

            btnActualizar.setVisibility(View.INVISIBLE);
            txtNombre.setInputType(InputType.TYPE_NULL);
            txtUrl.setInputType(InputType.TYPE_NULL);
            txtTelefono.setInputType(InputType.TYPE_NULL);
            txtEmail.setInputType(InputType.TYPE_NULL);
            txtProductos.setInputType(InputType.TYPE_NULL);
            txtclasificacion.setEnabled(false);
        }
        floatingActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerActivity.this, EditarActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });

        floatEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerActivity.this);
                builder.setMessage("Esta seguro de eliminar la empresa?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dbEmpresas.eliminarEmpresa(id)){
                                    lista();
                                }
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }

    private  void lista(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}