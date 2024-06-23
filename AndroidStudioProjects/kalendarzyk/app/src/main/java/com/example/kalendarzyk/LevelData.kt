package com.example.kalendarzyk

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import com.example.kalendarzyk.firestore.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
class LevelData:BasicActivity() {

    private var inputTemperature: EditText? = null
    private var inputMood: EditText? = null
    private var inputSecretion: EditText? = null
    private lateinit var buttonPickDate: Button
    private var selectedDate = Calendar.getInstance()
    private var buttonToFS: Button? = null
    private var buttonNext: Button? = null
    private var boolCheck: Boolean = false
    private var buttonBleeding: RadioButton? = null
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY)
    val user = FirebaseAuth.getInstance().currentUser?.email!!
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        buttonPickDate = findViewById(R.id.buttonPickDate)
        buttonToFS = findViewById(R.id.buttonToFS)
        buttonBleeding = findViewById(R.id.radioButton)
        buttonNext = findViewById(R.id.next)
        inputMood = findViewById(R.id.inputMood)
        inputMood?.filters = arrayOf(InputFilter.LengthFilter(1))
        inputSecretion = findViewById(R.id.inputSecretion)
        inputSecretion?.filters = arrayOf(InputFilter.LengthFilter(1))
        inputTemperature = findViewById(R.id.inputTemperature)
        inputTemperature?.filters = arrayOf(InputFilter.LengthFilter(5))
            inputTemperature?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val text = s.toString()
                    if (!text.isNotEmpty()) {
                        if (!isValidFormat(text)) {
                            inputTemperature?.error = "Wprowadź poprawny format temperatury XX.XX"
                        }
                    }
                }})
        inputSecretion?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()
            if (text.isNotEmpty()) {
                    inputSecretion?.error = "Wprowadź liczbę określającą gęstość, \n" +
                            "gdzie 0 to najrzadszy, a 9 najgęstszy"
            }}
            override fun afterTextChanged(s: Editable?) {

            }})

        inputMood?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text.isNotEmpty()) {

                        inputMood?.error = "Wprowadź liczbę określającą samopoczucie, \n" +
                                "gdzie 0 to okropny, a 9 cudowny"

                }}
            override fun afterTextChanged(s: Editable?) {

            }})

        buttonBleeding?.setOnCheckedChangeListener { _, isChecked ->
            boolCheck = true
        }



        buttonPickDate.setOnClickListener {
            showDatePickerDialog()
        }
        buttonToFS?.setOnClickListener {
            successfullData()
            reloadActivity()
        }
        buttonNext?.setOnClickListener {
            goToDashBoard()
        }

    }
    private fun successfullData() {
        val temperature = inputTemperature?.text.toString().toDouble()
        val mood = inputMood?.text.toString().toDouble()
        val secretion = inputSecretion?.text.toString().toDouble()

        val data = Data(
        selectedDate.time,
            mood,
            secretion,
            temperature,
            boolCheck
        )
        FirestoreClass().addDataFS(data, user)
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

            },
            year, month, day
        )

        datePickerDialog.show()


    }
    private fun isValidFormat(text: String): Boolean {
        val regex = Regex("^\\d{0,2}(\\.\\d{0,2})?$")
        return regex.matches(text)
}
    open fun goToDashBoard() {

        val user = FirebaseAuth.getInstance().currentUser;
        val uid = user?.email.toString()

        val intent = Intent(this, DashBoardActivity::class.java)
        intent.putExtra("uid", uid)
        intent.putExtra("user", user)
        startActivity(intent)


    }
    private fun reloadActivity() {
        val intent = Intent(this, LevelData::class.java)
        finish()
        startActivity(intent) }

}
