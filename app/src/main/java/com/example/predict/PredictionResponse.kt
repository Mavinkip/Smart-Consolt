package com.example.predict

data class PredictionResponse(val dtPrediction: String, val rfPrediction: String,
                              val nbPrediction: String, val knPrediction: String)
