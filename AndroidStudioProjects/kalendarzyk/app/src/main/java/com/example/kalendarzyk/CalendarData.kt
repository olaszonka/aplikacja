package com.example.kalendarzyk

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*
import com.example.kalendarzyk.firestore.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarData: BasicActivity() {
    val user = FirebaseAuth.getInstance().currentUser?.email!!
    private lateinit var buttonPickDate: Button
    private var selectedDate = Calendar.getInstance()
    private var inputCal: Button? = null
    private var nextView: Button? = null
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendaractivity)

        buttonPickDate = findViewById(R.id.buttonPickDate)
        inputCal = findViewById(R.id.inputCalendar)
        buttonPickDate.setOnClickListener {
            showDatePickerDialog()
        }
        inputCal?.setOnClickListener{
            FirestoreClass().firstDate(this@CalendarData, user, dateFormat.format(selectedDate.time))
        }
        nextView=findViewById(R.id.next)
        nextView?.setOnClickListener {
        CoroutineScope(Dispatchers.Main).launch {
            val result = FirestoreClass().getFirstFS(user)
            if (result != null) {
                goToLevelData()

            } else {
                showErrorSnackBar("Wprowadź datę rozpoczęcia ostatniej miesiączki ", true)
            }
        }
    }


    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->

                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
                val dateString = dateFormat.format(selectedDate.time)
            },
            year, month, day
        )

        datePickerDialog.show()

    }



         fun goToLevelData() {

             val user = FirebaseAuth.getInstance().currentUser;
             val uid = user?.email.toString()

             val intent = Intent(this, DashBoardActivity::class.java)
             intent.putExtra("uid", uid)
             intent.putExtra("user", user)
             startActivity(intent)
         }
}