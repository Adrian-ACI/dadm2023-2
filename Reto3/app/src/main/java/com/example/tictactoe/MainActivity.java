package com.example.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textoVictoria, turnocambio, victoriasJugadorTextView, victoriasIATextView, empatesTextView ;
    MediaPlayer sonidobtn, victoria, derrota, empate;
    ImageButton salir, reiniciar, dificultad;
    Integer[] botones;
    int[] tablero = new int[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0,
    };
    int estado = 0;
    int fichasPuestas = 0;
    int turno = 1;

    // variables para guardar estado
    private static final String PREFS_NAME = "TicTacToePrefs";
    private static final String VICTORIAS_JUGADOR_KEY = "victoriasJugador";
    private static final String VICTORIAS_IA_KEY = "victoriasIA";
    private static final String EMPATES_KEY = "empates";

    private int victoriasJugador = 1;
    private int victoriasIA = 1;
    private int empates = 1;

    int[] posGanadora = new int[]{-1, -1, -1};
    private static final int FACIL = 0;
    private static final int INTERMEDIO = 1;
    private static final int DIFICIL = 2;
    private int dificultadActual = DIFICIL; // Inicialmente, establece la dificultad en Fácil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoVictoria = findViewById(R.id.textwin);
        turnocambio = findViewById(R.id.cambioturno);
        textoVictoria.setVisibility(View.INVISIBLE);
        salir = findViewById(R.id.salirbtn);
        sonidobtn = MediaPlayer.create(this, R.raw.sonido);
        victoria = MediaPlayer.create(this, R.raw.victoria);
        empate = MediaPlayer.create(this, R.raw.empate);
        derrota = MediaPlayer.create(this, R.raw.derrota);
        reiniciar = findViewById(R.id.nuevojuego);
        dificultad = findViewById(R.id.dificultadbtn);
        victoriasJugadorTextView = findViewById(R.id.victoriasJugador);
        victoriasIATextView = findViewById(R.id.victoriasIA);
        empatesTextView = findViewById(R.id.empate);


        botones = new Integer[]{
                R.id.b1, R.id.b2, R.id.b3,
                R.id.b4, R.id.b5, R.id.b6,
                R.id.b7, R.id.b8, R.id.b9,
        };

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        dificultadActual = preferences.getInt("dificultadActual", FACIL);

        dificultad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDifficultyDialog();
            }
        });
        // Inicializa las estadísticas desde las preferencias compartidas
        cargarEstadisticas();

        // Muestra las estadísticas iniciales
        victoriasJugadorTextView.setText("Victorias del Jugador: " + victoriasJugador);
        victoriasIATextView.setText("Victorias de la IA: " + victoriasIA);
        empatesTextView.setText("Empates: " + empates);
    }

    public void showDifficultyDialog() {
        final String[] difficultyLevels = {"Fácil", "Intermedio", "Difícil"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona la dificultad del juego");
        builder.setItems(difficultyLevels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedDifficulty = difficultyLevels[which];
                if (selectedDifficulty.equals("Fácil")) {
                    dificultadActual = FACIL;
                } else if (selectedDifficulty.equals("Intermedio")) {
                    dificultadActual = INTERMEDIO;
                } else if (selectedDifficulty.equals("Difícil")) {
                    dificultadActual = DIFICIL;
                }
                // Guarda la dificultad seleccionada
                SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("dificultadActual", dificultadActual);
                editor.apply();
                reiniciarJuego();
            }
        });

        builder.show();
    }

    public void ponerFicha(View v) {
        if (estado == 0) {
            turno = 1;
            int numBoton = Arrays.asList(botones).indexOf(v.getId());
            if (tablero[numBoton] == 0) {
                sonidobtn.start();
                v.setBackgroundResource(R.drawable.x);
                tablero[numBoton] = 1;
                fichasPuestas += 1;
                estado = comprobarEstado();
                terminarPartida();
                if (estado == 0) {
                    turno = -1;
                    turnocambio.setText("Tu turno");
                    ia();
                    fichasPuestas += 1;
                    estado = comprobarEstado();
                    terminarPartida();
                }
            }
            if (estado == 1) {
                // El jugador ganó
                victoriasJugador++;
                guardarEstadisticas();
                actualizarEstadisticasEnPantalla();
            } else if (estado == -1) {
                // La IA ganó
                victoriasIA++;
                guardarEstadisticas();
                actualizarEstadisticasEnPantalla();
            } else if (estado == 2) {
                // Empate
                empates++;
                guardarEstadisticas();
                actualizarEstadisticasEnPantalla();
            }
        }

    }
    private void actualizarEstadisticasEnPantalla() {
        TextView victoriasJugadorTextView = findViewById(R.id.victoriasJugador);
        TextView victoriasIATextView = findViewById(R.id.victoriasIA);
        TextView empatesTextView = findViewById(R.id.empate);

        victoriasJugadorTextView.setText("Victorias del Jugador: " + victoriasJugador);
        victoriasIATextView.setText("Victorias de la IA: " + victoriasIA);
        empatesTextView.setText("Empates: " + empates);
        terminarPartida();
    }
    private void cargarEstadisticas() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        victoriasJugador = preferences.getInt(VICTORIAS_JUGADOR_KEY, 0);
        victoriasIA = preferences.getInt(VICTORIAS_IA_KEY, 0);
        empates = preferences.getInt(EMPATES_KEY, 0);
    }
    private void guardarEstadisticas() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(VICTORIAS_JUGADOR_KEY, victoriasJugador);
        editor.putInt(VICTORIAS_IA_KEY, victoriasIA);
        editor.putInt(EMPATES_KEY, empates);
        editor.apply();
    }

    public void terminarPartida() {
        int fichaVictoria = R.drawable.x2;
        if (estado == 1 || estado == -1) {
            if (estado == 1) {
                textoVictoria.setVisibility(View.VISIBLE);
                textoVictoria.setText("Has ganado! :)");
                textoVictoria.setTextColor(Color.GREEN);
                victoria.start();
            } else {
                textoVictoria.setVisibility(View.VISIBLE);
                textoVictoria.setText("Has perdido! :(");
                textoVictoria.setTextColor(Color.RED);
                fichaVictoria = R.drawable.o2;
                derrota.start();
            }
            for (int i = 0; i < posGanadora.length; i++) {
                Button b = findViewById(botones[posGanadora[i]]);
                b.setBackgroundResource(fichaVictoria);
            }
        } else if (estado == 2) {
            textoVictoria.setVisibility(View.VISIBLE);
            textoVictoria.setText("Has Empatado! :v");
            empate.start();
        }
    }

    public void ia() {
        if (dificultadActual == FACIL) {
            movimientoAleatorio();
        } else if (dificultadActual == INTERMEDIO) {
            movimientoIntermedio();
        } else if (dificultadActual == DIFICIL) {
            movimientoInteligente();
        }
    }

    private void realizarMovimiento(int pos) {
        Button b = findViewById(botones[pos]);
        b.setBackgroundResource(R.drawable.o);
        tablero[pos] = -1;
    }
    private void movimientoAleatorio() {
        int move = getRandomMove();
        if (move != -1) {
            realizarMovimiento(move);
        }
    }

    private void movimientoIntermedio() {
        int move = getWinningMove();
        if (move == -1) {
            move = getRandomMove();
        }
        if (move != -1) {
            realizarMovimiento(move);
        }
    }

    private void movimientoInteligente() {
        int move = getWinningMove();
        if (move == -1) {
            move = getBlockingMove();
        }
        if (move == -1) {
            move = getRandomMove();
        }
        if (move != -1) {
            realizarMovimiento(move);
        }
    }

    private int getRandomMove() {
        Random ran = new Random();
        int pos = ran.nextInt(tablero.length);
        while (tablero[pos] != 0) {
            pos = ran.nextInt(tablero.length);
        }
        return pos;
    }

    private int getWinningMove() {
        for (int i = 0; i < tablero.length; i++) {
            if (tablero[i] == 0) {
                tablero[i] = -1; // Prueba un movimiento de la IA
                if (comprobarEstado() == 1) {
                    tablero[i] = 0; // Deshacer el movimiento
                    return i; // Devuelve el movimiento ganador
                }
                tablero[i] = 0; // Deshacer el movimiento
            }
        }
        return -1; // No hay un movimiento ganador disponible
    }

    private int getBlockingMove() {
        for (int i = 0; i < tablero.length; i++) {
            if (tablero[i] == 0) {
                tablero[i] = 1; // Prueba un movimiento del oponente
                if (comprobarEstado() == -1) {
                    tablero[i] = 0; // Deshacer el movimiento
                    return i; // Devuelve el movimiento de bloqueo
                }
                tablero[i] = 0; // Deshacer el movimiento
            }
        }
        return -1; // No hay un movimiento de bloqueo necesario
    }

    public int comprobarEstado() {
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

    public void salir(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Quieres salir del juego?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void reiniciar(View view) {
        reiniciarJuego();
    }

    private void reiniciarJuego() {
        // Guarda la dificultad seleccionada
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("dificultadActual", dificultadActual);
        editor.apply();
        Intent intento = new Intent(this, MainActivity.class);
        finish();
        startActivity(intento);
    }
}
