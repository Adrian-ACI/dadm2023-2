package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textoVictoria;
    Button salir;
    Integer[] botones;
    int[] tablero = new int[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0,
    };
    int estado = 0;
    int fichasPuestas = 0;
    int turno = 1;
    int [] posGanadora = new int[]{-1, -1, -1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoVictoria = (TextView) findViewById(R.id.textwin);
        textoVictoria.setVisibility(View.INVISIBLE);
        salir = findViewById(R.id.salirbtn);

        botones = new Integer[]{
                R.id.b1, R.id.b2, R.id.b3,
                R.id.b4, R.id.b5, R.id.b6,
                R.id.b7, R.id.b8, R.id.b9,
        };

    }

    public void ponerFicha(View v){
        if(estado==0){
            turno = 1;
            int numBoton = Arrays.asList(botones).indexOf(v.getId());
            if(tablero[numBoton] == 0){
                v.setBackgroundResource(R.drawable.x1);
                tablero[numBoton] = 1;
                fichasPuestas += 1;
                estado = comprobarEstado();
                terminarPartida();
                if(estado == 0){
                    turno = -1;
                    ia();
                    fichasPuestas += 1;
                    estado = comprobarEstado();
                    terminarPartida();
                }
            }
        }

    }
    public void terminarPartida(){
        int fichaVictoria = R.drawable.x2;
        if(estado == 1 || estado == -1){
            if(estado==1){
                textoVictoria.setVisibility(View.VISIBLE);
                textoVictoria.setText("Has ganado! :)");
                textoVictoria.setTextColor(Color.GREEN);
            }
            else{
                textoVictoria.setVisibility(View.VISIBLE);
                textoVictoria.setText("Has perdido! :(");
                textoVictoria.setTextColor(Color.RED);
                fichaVictoria = R.drawable.o2;
            }
            for(int i = 0; i<posGanadora.length; i++){
                Button b = findViewById(botones[posGanadora[i]]);
                b.setBackgroundResource(fichaVictoria);
            }
        }
        else if(estado==2){
            textoVictoria.setVisibility(View.VISIBLE);
            textoVictoria.setText("Has Empatado! :v");
        }
    }

    public void ia(){
        Random ran = new Random();
        int pos = ran.nextInt(tablero.length);
        while(tablero[pos] != 0){
            pos = ran.nextInt(tablero.length);
        }
        Button b = (Button) findViewById(botones[pos]);
        b.setBackgroundResource(R.drawable.o1);
        tablero[pos] = -1;
    }

    public int comprobarEstado(){
       int nuevoEstado = 0;
        if(Math.abs(tablero[0]+tablero[1]+tablero[2]) == 3){
            posGanadora = new int[]{0,1,2};
            nuevoEstado = 1*turno;
        }
        else if (Math.abs(tablero[3]+tablero[4]+tablero[5])==3) {
            posGanadora = new int[]{3,4,5};
            nuevoEstado = 1*turno;
        }
        else if (Math.abs(tablero[6]+tablero[7]+tablero[8])==3) {
            posGanadora = new int[]{6,7,8};
            nuevoEstado = 1*turno;
        }
        else if (Math.abs(tablero[0]+tablero[3]+tablero[6])==3) {
            posGanadora = new int[]{0,3,6};
            nuevoEstado = 1*turno;
        }
        else if (Math.abs(tablero[1]+tablero[4]+tablero[7])==3) {
            posGanadora = new int[]{1,4,7};
            nuevoEstado = 1*turno;
        }
        else if (Math.abs(tablero[2]+tablero[5]+tablero[8])==3) {
            posGanadora = new int[]{2,5,8};
            nuevoEstado = 1*turno;
        }
        else if (Math.abs(tablero[0]+tablero[4]+tablero[8])==3) {
            posGanadora = new int[]{0,4,8};
            nuevoEstado = 1*turno;
        }
        else if (Math.abs(tablero[2]+tablero[4]+tablero[6])==3) {
            posGanadora = new int[]{2,4,6};
            nuevoEstado = 1*turno;
        }
        else if (fichasPuestas==9) {
            nuevoEstado=2;
        }
       return nuevoEstado;
    }

    // cerrar app
    @SuppressLint("")
    public void salir(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Quieres salir del juego?").setPositiveButton("Si", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                Intent intent =new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        builder.show();
        //finishAffinity();
    }
}