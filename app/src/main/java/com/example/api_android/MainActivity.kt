package com.example.api_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.api_android.models.Etudiant
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
//    lateinit var etudiants:ArrayList<Etudiant>
//    lateinit var recyclerView:RecyclerView

    lateinit var list: ListView
    lateinit var etudiants: ArrayList<Etudiant>
    lateinit var adap: EtudiantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = findViewById(R.id.idListView)

        etudiants = ArrayList()

        getEtudiant()

    }


    fun getEtudiant(){

//      val ENPOINT = "http://10.4.6.73:3000/etudiant/index/"

        val ENPOINT = "http://172.20.10.2:3000/etudiant/index/"
        val request: StringRequest = StringRequest(Request.Method.GET,ENPOINT,
            {response-> parseData(response)},
            { error -> Log.i("GET_RESPONSE",error.toString())}
        )

        Volley.newRequestQueue(applicationContext).add(request)
    }

    fun parseData(response:String){
        val responseData = JSONArray(response)
        for(i in 0..responseData.length()-1){
            val etudiantJson = responseData.getJSONObject(i)
            val id = etudiantJson.getString("_id")
            val matricule = etudiantJson.getString("matricule")
            val nom = etudiantJson.getString("nom")
            val prenom = etudiantJson.getString("prenom")
            val departement = etudiantJson.getJSONObject("departement").getString("nom")
            val etudiant = Etudiant(id,matricule,nom,prenom, departement)
            etudiants.add(etudiant)
        }

        adap = EtudiantAdapter(this, R.layout.item_etudiant,etudiants)
        list.adapter = adap

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuAjouter){
            startActivity(Intent(applicationContext,AjoutEtudiant::class.java))
        }
        return true
    }

}