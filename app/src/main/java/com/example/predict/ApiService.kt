package com.example.predict


import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/predict_disease")
    fun predictDisease(@Body request: SymptomsRequest): retrofit2.Call<PredictionResponse>
}
