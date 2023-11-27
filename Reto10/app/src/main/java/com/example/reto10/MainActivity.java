package com.example.reto10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.reto10.Interface.JsonFuncionariosApi;
import com.example.reto10.Model.Funcionarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView txtJson;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtJson = findViewById(R.id.txtJson);
        getFuncionarios();
    }
    private void getFuncionarios(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.datos.gov.co/resource/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonFuncionariosApi jsonFuncionariosApi =retrofit.create(JsonFuncionariosApi.class);
        Call<List<Funcionarios>> call = jsonFuncionariosApi.getFuncionarios();
        call.enqueue(new Callback<List<Funcionarios>>() {
            @Override
            public void onResponse(Call<List<Funcionarios>> call, Response<List<Funcionarios>> response) {
                if(!response.isSuccessful()){
                    txtJson.setText("Codigo: " + response.code());
                    return;
                }
                List<Funcionarios> funcionariosList = response.body();

                for (Funcionarios funcionario: funcionariosList) {
                    String content ="";
                    content+= "Nombre: "+ funcionario.getNombre() + "\n";
                    content+= "Cargo: "+ funcionario.getCargo() + "\n";
                    content+= "Dependencia: "+ funcionario.getDependencia() + "\n";
                    content+= "subdependencia: "+ funcionario.getSubdependencia() + "\n";
                    content+= "Tipo de vinculacion: "+ funcionario.getTipo_vinculaci_n() + "\n\n";
                    txtJson.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Funcionarios>> call, Throwable t) {
                txtJson.setText(t.getMessage());
            }
        });
    }

}