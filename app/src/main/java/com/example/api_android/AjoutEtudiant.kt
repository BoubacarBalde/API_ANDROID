package com.example.api_android

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import kotlin.math.absoluteValue

class AjoutEtudiant : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajout_etudiant)

        val mySpinner = findViewById<Spinner>(R.id.idSpinner)
        fetchDataFromApi(mySpinner)

        val matricule = findViewById<EditText>(R.id.txtMatricule)
        val nom = findViewById<EditText>(R.id.txtNom)
        val prenom = findViewById<EditText>(R.id.txtPrenom)
        val ajouter = findViewById<Button>(R.id.btnAjouter)


        ajouter.setOnClickListener {

            if(matricule.text.toString().trim() == "" || nom.text.toString().trim() == "" || prenom.text.toString().trim() == ""){
                Toast.makeText(applicationContext,"Veuillez remplire les champs", Toast.LENGTH_SHORT).show()
            }else{

                val ENDPOINT = "http://172.20.10.2:3000/etudiant/create"
                val request: StringRequest = object : StringRequest(
                    Method.POST,ENDPOINT,
                    { response -> Toast.makeText(applicationContext,"Euidiant Ajouter avec succes", Toast.LENGTH_SHORT).show()  },
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
        val request = StringRequest(Request.Method.GET, url,
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