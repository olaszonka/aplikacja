package com.example.kalendarzyk

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.example.kalendarzyk.firestore.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class DashBoardActivity:BasicActivity(), TimePickerDialog.OnTimeSetListener {

    private var buttonShow: Button? = null
    private var buttonWhen: Button? = null
    private var buttonInput: Button? = null
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var selectedTimeTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)

        buttonShow = findViewById(R.id.buttonShow)
//        buttonWhen = findViewById(R.id.buttonWhen)
        buttonInput = findViewById(R.id.buttonInput)

        buttonShow?.setOnClickListener {
            goToDataOverView()
        }
//        buttonWhen?.setOnClickListener { Toast.makeText(this, "Najbliższe krwawienie:", Toast.LENGTH_SHORT).show() }
        buttonInput?.setOnClickListener {
            goToLevelData()
        }

        selectedTimeTextView = findViewById(R.id.selectedTimeTextView)
        val scheduleButton: Button = findViewById(R.id.scheduleButton)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyNotificationReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        scheduleButton.setOnClickListener {
            showTimePickerDialog()}

    }


    fun goToLevelData() {

        val user = FirebaseAuth.getInstance().currentUser;
        val uid = user?.email.toString()

        val intent = Intent(this, LevelData::class.java)
        intent.putExtra("uid", uid)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    fun goToDataOverView() {

        val user = FirebaseAuth.getInstance().currentUser;
        val uid = user?.email.toString()

        val intent = Intent(this, DataOverView::class.java)
        intent.putExtra("uid", uid)
        intent.putExtra("user", user)
        startActivity(intent)
    }
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        val timeText = String.format("%02d:%02d", hourOfDay, minute)
        selectedTimeTextView.text = "Wyślemy powiadomienie o $timeText"
    }

}