package com.example.kalendarzyk

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : BasicActivity() {

    // Deklaracja obiektow
    private var logViewButton: Button? = null
    private var textView: TextView? = null
    private var regViewButton: Button? = null
    private var testViewButton: Button? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ustawienie widoku interfejsu użytkownika na activity_main.xml
        setContentView(R.layout.activity_main)
        // Ustawienie przewijania w TextView
        textView?.movementMethod = ScrollingMovementMethod()

        // Inicjalizacja przycisku przejścia do kolejnej aktywności
        regViewButton = findViewById(R.id.registerButton)

        logViewButton = findViewById(R.id.loginButton)

//        testViewButton = findViewById(R.id.testButton)

        // Ustawienie nasłuchiwacza zdarzeń dla przycisku przejścia do kolejnej aktywności
        regViewButton?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                openRegister()
            }
        })

        // Ustawienie nasłuchiwacza zdarzeń dla przycisku
        logViewButton?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                openLogin()
            }
        })
//        testViewButton?.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                openTest()
//            }
//        })
    }

    // Metoda otwierająca drugą aktywność
    private fun openRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
//        intent.putExtra("name",userInput?.text.toString())
        startActivity(intent)
    }

    private fun openLogin(){
        val intent = Intent(this, LoginActivity::class.java)
//        intent.putExtra("name",userInput?.text.toString())
        startActivity(intent)
    }
//    private fun openTest(){
//        val intent = Intent(this, DataOverView::class.java)
//        startActivity(intent)
//    }
}