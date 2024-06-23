package com.example.kalendarzyk.firestore



import com.example.kalendarzyk.CalendarData
import com.example.kalendarzyk.Data
import com.example.kalendarzyk.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()


    fun registerUserFS(activity: RegisterActivity, userInfo: User){

        mFireStore.collection("users")
            .document(userInfo.email)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()

            }
            .addOnFailureListener{

            }}

        fun firstDate(activity:CalendarData, email: String, first:String){
            mFireStore.collection("users")
                .document(email).update("first",first)
                .addOnSuccessListener {  }
                .addOnFailureListener{}
        }

        fun addDataFS(dataInfo: Data, email: String){
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
            mFireStore.collection("users/${email}/data").
            document(dateFormat.format(dataInfo.today)).set(dataInfo, SetOptions.merge()).addOnSuccessListener {
            }
            .addOnFailureListener{

            }
        }



    suspend fun getFirstFS(mail: String): String? {
        return try {
            val documentSnapshot = mFireStore.collection("users").document(mail).get().await()
            if (documentSnapshot != null && documentSnapshot.exists()) {
                documentSnapshot.getString("first")
            } else {
                null
            }
        } catch (e: Exception) {
            println("Błąd podczas pobierania dokumentu: ${e.message}")
            null
        }
    }

    val dataList = mutableListOf("lengh", "today", "secretion", "temperature", "mood")

}



