package com.example.kalendarzyk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.kalendarzyk.firestore.FirestoreClass
import com.example.kalendarzyk.firestore.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Aktywność rejestracji użytkownika.
 */
class RegisterActivity : BasicActivity() {

    private var registerButton: Button? = null
    private var inputEmail: EditText? = null
    private var inputName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepPass: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicjalizacja pól wejściowych i przycisku rejestracji
        registerButton = findViewById(R.id.registerButton)
        inputEmail = findViewById(R.id.inputLEmaill)
        inputName = findViewById(R.id.inputName)
        inputPassword = findViewById(R.id.inputPassword2)
        inputRepPass = findViewById(R.id.inputPassword2repeat)

        // Ustawienie nasłuchiwania kliknięć przycisku rejestracji
        registerButton?.setOnClickListener{
            registerUser()
        }
    }

    /**
     * Metoda walidująca wprowadzone dane rejestracji.
     * @return True, jeśli dane są poprawne, w przeciwnym razie False.
     */
    private fun validateRegisterDetails(): Boolean {

        return when{
            TextUtils.isEmpty(inputEmail?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(inputName?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_name),true)
                false
            }
            (!(inputPassword?.text.toString().contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()))) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_char), true)
                false
            } // Znak specjalny
            (!(inputPassword?.text.toString().contains("[A-Z]".toRegex()))) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_great), true)
                false
            } // Duża litera

            (!(inputPassword?.text.toString().contains("[a-z]".toRegex()))) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_small), true)
                false
            } // Mała litera

            (inputPassword?.length()!! <8) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_short), true)
                false
            } // Minimalna długość

            TextUtils.isEmpty(inputPassword?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            TextUtils.isEmpty(inputRepPass?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_reppassword),true)
                false
            }
            inputPassword?.text.toString().trim {it <= ' '} != inputRepPass?.text.toString().trim{it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_mismatch),true)
                false
            }
            else -> true
        }
    }

    /**
     * Metoda przechodzenia do aktywności logowania.
     */
    fun goToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // finish(), po to aby użytkownik nie mógł już wrócić do aktualnej aktywności bez restartowania aplikacji
    }

    /**
     * Metoda rejestracji użytkownika za pomocą Firebase Authentication.
     */
    private fun registerUser(){
        if (validateRegisterDetails()){
            val password: String = inputPassword?.text.toString().trim() {it <= ' '}
            val name: String = inputName?.text.toString().trim() {it <= ' '}
            val email: String = inputEmail?.text.toString().trim() {it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener <AuthResult>{ task ->
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!
//                        showErrorSnackBar("Zarejestrowałeś się pomyślnie! Twoje ID to:  ${firebaseUser.uid}",false)

                        val user = User(email,
                            firebaseUser.uid,
                            name,
                            true,
                            null
                        )
                        FirestoreClass().registerUserFS(this@RegisterActivity, user)
                        goToCalendar()


                    } else{
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
            )
        }
    }

    /**
     * Metoda wywoływana po udanej rejestracji użytkownika wyświetlająca wiadomość Toast.
     */
    fun  userRegistrationSuccess(){
        Toast.makeText(this@RegisterActivity, resources.getString(R.string.register_success), Toast.LENGTH_LONG).show()
    }

open fun goToCalendar() {

    val user = FirebaseAuth.getInstance().currentUser;
    val uid = user?.email.toString()

    val intent = Intent(this, CalendarData::class.java)
    intent.putExtra("uid", uid)
    intent.putExtra("user", user)
    startActivity(intent)
}
}