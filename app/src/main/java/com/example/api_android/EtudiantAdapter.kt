package com.example.api_android

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.api_android.models.Etudiant
import java.io.ByteArrayOutputStream

class EtudiantAdapter(var mContext: Context,
                      var ressource: Int,
                      var values: ArrayList<Etudiant>
): ArrayAdapter<Etudiant>(mContext,ressource,values){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val positi = values[position]
        val itemView = LayoutInflater.from(mContext).inflate(ressource, parent, false)
        var id = itemView.findViewById<TextView>(R.id.txtId)
        var matricule = itemView.findViewById<TextView>(R.id.txtMatricule1)
        var nomPrenom = itemView.findViewById<TextView>(R.id.txtNomPrenom)
        var departement = itemView.findViewById<TextView>(R.id.txtDepartement)
        var menuEtudian = itemView.findViewById<ImageView>(R.id.idMenuEtudiant)

        id.text = positi.id
        matricule.text = positi.matricule
        nomPrenom.text = "${positi.nom} ${positi.prenom}"
        departement.text = positi.departement



        menuEtudian.setOnClickListener{
            val popupmenu = PopupMenu(mContext,menuEtudian)
            popupmenu.menuInflater.inflate(R.menu.option_etudiant, popupmenu.menu)

            popupmenu.setOnMenuItemClickListener { item ->

                when(item.itemId){
                    R.id.idModifier -> {
                        Intent(mContext,ModificationEtudiant::class.java).also {
                            it.putExtra("id", id.text.toString())
                            it.putExtra("matricule", matricule.text.toString())
                            it.putExtra("nom", positi.nom)
                            it.putExtra("prenom", positi.prenom)
                            mContext.startActivity(it)
                        }
                    }
                    R.id.idSupprimer ->{

                        val ENPOINT = "http://172.20.10.2:3000/etudiant/delete/${id.text}"
                        val request: StringRequest = StringRequest(
                            Request.Method.DELETE,ENPOINT,
                            {response-> Toast.makeText(mContext,"Euidiant Supprimer avec succes", Toast.LENGTH_SHORT).show()},
                            { error -> Log.i("GET_RESPONSE",error.toString())}
                        )
                        Volley.newRequestQueue(mContext).add(request)

                        values.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
                true
            }
            popupmenu.show()
        }

        return itemView
    }

}