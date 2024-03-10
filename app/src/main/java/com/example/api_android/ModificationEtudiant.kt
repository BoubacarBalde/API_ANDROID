package com.example.api_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class ModificationEtudiant : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modification_etudiant)

        val mySpinner = findViewById<Spinner>(R.id.idSpinner11)
        fetchDataFromApi(mySpinner)

        val id = findViewById<TextView>(R.id.txtIdddd)
        val matricule = findViewById<EditText>(R.id.txtMatricule11)
        val nom = findViewById<EditText>(R.id.txtNom11)
        val prenom = findViewById<EditText>(R.id.txtPrenom11)
        val btnModifier = findViewById<Button>(R.id.btnModifier)

        var idd = intent.getStringExtra("id")
        var matriculee = intent.getStringExtra("matricule")
        var nomm = intent.getStringExtra("nom")
        var prenomm = intent.getStringExtra("prenom")

        id.setText(idd)
        matricule.setText(matriculee)
        nom.setText(nomm)
        prenom.setText(prenomm)


        btnModifier.setOnClickListener{

            if(matricule.text.toString().trim() == "" || nom.text.toString().trim() == "" || prenom.text.toString().trim() == ""){
                Toast.makeText(applicationContext,"Veuillez remplire les champs", Toast.LENGTH_SHORT).show()
            }else{

                val ENDPOINT = "http://172.20.10.2:3000/etudiant/edit/${id.text}"
                val request: StringRequest = object : StringRequest(
                    Method.PATCH,ENDPOINT,
                    { response -> Toast.makeText(applicationContext,"Euidiant modifier avec succes", Toast.LENGTH_SHORT).show()  },
                    { error -> Log.e("GET_RESPONSE",error.toString())  }){

                    override fun getParams(): MutableMap<String, String>? {
                        val params = HashMap<String,String>()
                        params.put("matricule",matricule.text.toString())
                        params.put("nom",nom.text.toString())
                        params.put("prenom",prenom.text.toString())
                        params.put("departement", mySpinner.selectedItem.toString())
                        return params
                    }
                }
                Volley.newRequestQueue(applicationContext).add(request)
            }
        }



    }

    fun fetchDataFromApi(spinner: Spinner) {

        val url = "http://172.20.10.2:3000/departement/index"
        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                val values = parseResponse(response)
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, values)
                spinner.adapter = adapter
            },
            { error -> Log.e("API Request", "Error: ${error.message}")}
        )
        Volley.newRequestQueue(applicationContext).add(request)
    }

    fun parseResponse(response: String): List<String> {

        val jsonArray = JSONArray(response)
        val names = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("_id")
            names.add(name)
        }
        return names
    }
}