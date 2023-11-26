package com.example.reto8.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.reto8.entidades.Empresas;

import java.util.ArrayList;

public class DbEmpresas extends DBHelper {

    Context context;
    public DbEmpresas(@Nullable Context context) {
        super(context);
        this.context= context;
    }
    public long insertarEmpresa(String nombre, String url, String telefono, String email, String productos, String clasificacion){

        long id=0;

        try {
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("url", url);
            values.put("telefono", telefono);
            values.put("email",email);
            values.put("productos", productos);
            values.put("clasificacion", clasificacion);

            id = db.insert(TABLE_EMPRESAS,null, values);
        }catch (Exception ex){
            ex.toString();
        }

        return id;
    }
    public ArrayList<Empresas> mostrasEmpresas(){

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Empresas> listaEmpresas = new ArrayList<>();
        Empresas empresa = null;
        Cursor cursorEmpresas = null;

        cursorEmpresas = db.rawQuery("SELECT * FROM " + TABLE_EMPRESAS, null);

        if(cursorEmpresas.moveToFirst()){
            do{
                empresa = new Empresas();
                empresa.setId(cursorEmpresas.getInt(0));
                empresa.setNombre(cursorEmpresas.getString(1));
                empresa.setUrl(cursorEmpresas.getString(2));
                empresa.setProducto(cursorEmpresas.getString(5));
                listaEmpresas.add(empresa);
            }while(cursorEmpresas.moveToNext());
        }
        cursorEmpresas.close();
        return  listaEmpresas;
    }

    public Empresas verEmpresa(int id){

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Empresas empresa = null;
        Cursor cursorEmpresas = null;

        cursorEmpresas = db.rawQuery("SELECT * FROM " + TABLE_EMPRESAS + " WHERE id = " + id + " LIMIT 1 ", null);

        if(cursorEmpresas.moveToFirst()){
            empresa = new Empresas();
            empresa.setId(cursorEmpresas.getInt(0));
            empresa.setNombre(cursorEmpresas.getString(1));
            empresa.setUrl(cursorEmpresas.getString(2));
            empresa.setTelefono(cursorEmpresas.getString(3));
            empresa.setEmail(cursorEmpresas.getString(4));
            empresa.setProducto(cursorEmpresas.getString(5));
            empresa.setClasificacion(cursorEmpresas.getString(6));

        }
        cursorEmpresas.close();
        return  empresa;
    }

    public boolean editarEmpresa(int id, String nombre, String url, String telefono, String email, String productos, String clasificacion){

        boolean correcto = false;

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL(" UPDATE " + TABLE_EMPRESAS + " SET nombre = '" + nombre + "', url = '" + url +
                    "', telefono = '" + telefono + "', email = '" + email +
                    "', productos = '" + productos + "', clasificacion = '" + clasificacion +
                    "' WHERE id = '" + id + "' ");

            correcto =true;

        }catch (Exception ex){
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

    public boolean eliminarEmpresa(int id){

        boolean correcto = false;

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL(" DELETE FROM " + TABLE_EMPRESAS + " WHERE id = '" + id +  "'");
            correcto =true;

        }catch (Exception ex){
            ex.toString();
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }
}
