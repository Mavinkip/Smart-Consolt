package com.example.predict

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val l1 = listOf(
        "back_pain", "constipation", "abdominal_pain", "diarrhoea", "mild_fever", "yellow_urine",
        "yellowing_of_eyes", "acute_liver_failure", "fluid_overload", "swelling_of_stomach",
        "swelled_lymph_nodes", "malaise", "blurred_and_distorted_vision", "phlegm", "throat_irritation",
        "redness_of_eyes", "sinus_pressure", "runny_nose", "congestion", "chest_pain", "weakness_in_limbs",
        "fast_heart_rate", "pain_during_bowel_movements", "pain_in_anal_region", "bloody_stool",
        "irritation_in_anus", "neck_pain", "dizziness", "cramps", "bruising", "obesity", "swollen_legs",
        "swollen_blood_vessels", "puffy_face_and_eyes", "enlarged_thyroid", "brittle_nails",
        "swollen_extremeties", "excessive_hunger", "extra_marital_contacts", "drying_and_tingling_lips",
        "slurred_speech", "knee_pain", "hip_joint_pain", "muscle_weakness", "stiff_neck", "swelling_joints",
        "movement_stiffness", "spinning_movements", "loss_of_balance", "unsteadiness",
        "weakness_of_one_body_side", "loss_of_smell", "bladder_discomfort", "foul_smell_of_urine",
        "continuous_feel_of_urine", "passage_of_gases", "internal_itching", "toxic_look_(typhos)",
        "depression", "irritability", "muscle_pain", "altered_sensorium", "red_spots_over_body", "belly_pain",
        "abnormal_menstruation", "dischromic_patches", "watering_from_eyes", "increased_appetite", "polyuria", "family_history", "mucoid_sputum",
        "rusty_sputum", "lack_of_concentration", "visual_disturbances", "receiving_blood_transfusion",
        "receiving_unsterile_injections", "coma", "stomach_bleeding", "distention_of_abdomen",
        "history_of_alcohol_consumption", "fluid_overload", "blood_in_sputum", "prominent_veins_on_calf",
        "palpitations", "painful_walking", "pus_filled_pimples", "blackheads", "scurring", "skin_peeling",
        "silver_like_dusting", "small_dents_in_nails", "inflammatory_nails", "blister", "red_sore_around_nose",
        "yellow_crust_ooze"
    )

    private val disease = listOf(
        "Fungal infection", "Allergy", "GERD", "Chronic cholestasis", "Drug Reaction",
        "Peptic ulcer disease", "AIDS", "Diabetes", "Gastroenteritis", "Bronchial Asthma",
        "Hypertension", "Migraine", "Cervical spondylosis",
        "Paralysis (brain hemorrhage)", "Jaundice", "Malaria", "Chicken pox", "Dengue", "Typhoid",
        "Hepatitis A", "Hepatitis B", "Hepatitis C", "Hepatitis D", "Hepatitis E", "Alcoholic hepatitis",
        "Tuberculosis", "Common Cold", "Pneumonia", "Dimorphic hemmorhoids(piles)",
        "Heart attack", "Varicose veins", "Hypothyroidism", "Hyperthyroidism", "Hypoglycemia", "Osteoarthritis",
        "Arthritis", "(vertigo) Paroymsal Positional Vertigo", "Acne", "Urinary tract infection", "Psoriasis",
        "Impetigo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val symptomsLayout = findViewById<LinearLayout>(R.id.symptoms_layout)
        val predictButton = findViewById<Button>(R.id.predict_button)

        l1.forEach { symptom ->
            val checkBox = CheckBox(this).apply {
                text = symptom
                id = View.generateViewId()
            }
            symptomsLayout.addView(checkBox)
        }

        predictButton.setOnClickListener {
            val selectedSymptoms = l1.filterIndexed { index, _ ->
                (symptomsLayout.getChildAt(index) as CheckBox).isChecked
            }
            predictDisease(selectedSymptoms)
        }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.1.134.135:5000") // Use your IPv4 address here
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    private fun predictDisease(selectedSymptoms: List<String>) {
        val request = SymptomsRequest(selectedSymptoms)

        apiService.predictDisease(request).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val predictions = response.body()
                    // Display the predictions in your app
                    // For example, you can show them in a dialog or toast
                    val message = "DT Prediction: ${predictions?.dtPrediction}, RF Prediction: ${predictions?.rfPrediction}, NB Prediction: ${predictions?.nbPrediction}, KN Prediction: ${predictions?.knPrediction}"
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to get predictions.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
