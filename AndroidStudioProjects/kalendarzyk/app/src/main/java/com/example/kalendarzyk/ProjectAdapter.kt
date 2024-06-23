package com.example.kalendarzyk

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query


class ProjectAdapter(private val query: Query):RecyclerView.Adapter<CustomViewHolder>() {

    // Lista przechowująca dokumenty pobrane z Firestore
    private var documents: List<DocumentSnapshot> = emptyList()

    init {
        // Dodanie nasłuchiwacza do zapytania Firestore
        query.addSnapshotListener { snapshots, e ->
            if (e != null) {
                // Obsługa błędu - jeśli nasłuchiwacz zgłosi błąd, po prostu zwracamy
                println("Błąd podczas pobierania danych: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshots != null) {
                // Aktualizacja listy dokumentów na podstawie migawek z Firestore
                documents = snapshots.documents
                // Powiadomienie adaptera o zmianach, co powoduje odświeżenie RecyclerView
                notifyDataSetChanged()
            }
        }
    }

    // Inne metody adaptera, takie jak onCreateViewHolder, onBindViewHolder, getItemCount
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(documents[position])
    }

    override fun getItemCount(): Int {
        return documents.size
    }
}
