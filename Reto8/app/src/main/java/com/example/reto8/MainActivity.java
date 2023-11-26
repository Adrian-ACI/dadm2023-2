package com.example.reto8;

// MainActivity.java
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto8.adaptadores.ListaEmpresasAdapter;
import com.example.reto8.db.DBHelper;
import com.example.reto8.db.DbEmpresas;
import com.example.reto8.entidades.Empresas;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView listaEmpresas;
    ArrayList<Empresas> listaArrayEmpresas;
    SearchView txtBuscar;
    ListaEmpresasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBuscar = findViewById(R.id.txtBuscar);
        listaEmpresas = findViewById(R.id.listaEmpresas);
        listaEmpresas.setLayoutManager(new LinearLayoutManager(this));
        DbEmpresas dbEmpresas = new DbEmpresas(MainActivity.this);

        listaArrayEmpresas = new ArrayList<>();
        adapter = new ListaEmpresasAdapter(dbEmpresas.mostrasEmpresas());
        listaEmpresas.setAdapter(adapter);

        txtBuscar.setOnQueryTextListener(this);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mainMenu) {
            nuevoRegistro();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    public void nuevoRegistro(){
        Intent intent = new Intent(this, NuevoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtrado(newText);
        return false;
    }
}
