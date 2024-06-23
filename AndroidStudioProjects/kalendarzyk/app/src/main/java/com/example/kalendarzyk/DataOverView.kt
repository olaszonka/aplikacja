package com.example.kalendarzyk

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DataOverView():BasicActivity() {
    val user = FirebaseAuth.getInstance().currentUser?.email!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProjectAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler)

        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.mainWindow)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val query = firestore.collection("users").document(user).collection("data")
        adapter = ProjectAdapter(query)
        recyclerView.adapter = adapter
    }
}
