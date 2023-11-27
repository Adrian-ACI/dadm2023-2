package com.example.reto10.Interface;

import com.example.reto10.Model.Funcionarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonFuncionariosApi {
    @GET("7j7g-rasr.json")
    Call<List<Funcionarios>> getFuncionarios();
}
