package com.example.kalendarzyk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot


class CustomViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){

    private val documentName: TextView = itemView.findViewById(R.id.textDate)
    private val field1: TextView = itemView.findViewById(R.id.textMood)
    private val field2: TextView = itemView.findViewById(R.id.textSecretion)
    private val field3: TextView = itemView.findViewById(R.id.textTemp)
    private val field4: TextView = itemView.findViewById(R.id.txtBleeding)

    fun bind(snapshot: DocumentSnapshot) {
        val documentId = snapshot.id
        val data = snapshot.data

        documentName.text = documentId
        field1.text = data?.get("mood").toString()
        field2.text = data?.get("secretion").toString()
        field3.text = data?.get("temperature").toString()
        if(data?.get("bleeding")!=false){
            field4.text = "Wystąpiło krwawienie"
        }
        else{
            field4.text = "Dzień jak co dzień"
        }
    }

    companion object {
        fun create(parent: ViewGroup): CustomViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false)
            return CustomViewHolder(view)
        }
    }
}