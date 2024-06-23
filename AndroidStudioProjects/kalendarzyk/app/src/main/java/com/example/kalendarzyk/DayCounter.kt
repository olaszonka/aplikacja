package com.example.kalendarzyk

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

class DayCounter(user: String) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
    private val user = user
    private val mFirestore = FirebaseFirestore.getInstance()

    // Funkcja getFieldFS przekształcona na funkcję suspend
    private suspend fun getFieldFS(user: String?): String? {
        return withContext(Dispatchers.IO) {
            val document = mFirestore.collection("users").document(user ?: "")
                .get()
                .await() // Oczekiwanie na zakończenie operacji

            if (document.exists()) {
                document.getString("first")
            } else {
                null
            }
        }
    }

    private val length = 28

    private fun extractDateFromString(dateString: String): Calendar? {
        val pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})")
        val matcher = pattern.matcher(dateString)

        return if (matcher.matches()) {
            val year = matcher.group(1).toInt()
            val month = matcher.group(2).toInt() - 1
            val day = matcher.group(3).toInt()

            Calendar.getInstance().apply {
                set(year, month, day)
            }
        } else {
            null
        }
    }

    // Funkcja calculateNextPeriod korzystająca z getFieldFS
    suspend fun calculateNextPeriod(): String? {
        var data: String? = "2024-07-24"
        data = getFieldFS(user)
        val today = extractDateFromString(data ?: "")
        val nextPeriod = today?.clone() as Calendar
        nextPeriod?.add(Calendar.DAY_OF_YEAR, length)
        return dateFormat.format(nextPeriod.time)
    }

    // Funkcja calculateFertileDays korzystająca z getFieldFS jako suspend function
    suspend fun calculateFertileDays(): String {
        val data: String? = getFieldFS(user)

        val today = extractDateFromString(data ?: "2024-07-24")
        val fertileDay = today ?: Calendar.getInstance()
        fertileDay.add(Calendar.DAY_OF_YEAR, 1)

        return dateFormat.format(fertileDay.time)
    }
}
