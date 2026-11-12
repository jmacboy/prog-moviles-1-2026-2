package com.example.practicaapilibros.repositories

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRepository {
    fun getInstance(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apilibreria.jmacboy.dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }
}