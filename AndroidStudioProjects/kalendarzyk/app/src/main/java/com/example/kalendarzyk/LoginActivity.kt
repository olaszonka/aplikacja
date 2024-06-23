package com.example.kalendarzyk


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

/**
 * Aktywność obsługująca logowanie użytkownika za pomocą Firebase Authentication.
 */
class LoginActivity : BasicActivity(), View.OnClickListener {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        // Inicjalizacja pól wejściowych i przycisku logowania
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword2)
        loginButton = findViewById(R.id.loginButton)

        // Ustawienie nasłuchiwania kliknięć przycisku logowania
        loginButton?.setOnClickListener{
            logInRegisteredUser()
        }

    }

    override fun onClick(view: View?) {
        if(view !=null){
            when (view.id){

                R.id.registerTextViewClickable ->{
                    // Przejście do ekranu rejestracji po kliknięciu linku
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }



    /**
     * Metoda walidująca wprowadzone dane logowania.
     * @return True, jeśli dane są poprawne, w przeciwnym razie False.
     */
    private fun validateLoginDetails(): Boolean {

        return when{
            TextUtils.isEmpty(inputEmail?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(inputPassword?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }

            else -> {
                showErrorSnackBar("Zalogowano poprawnie!",false)
                true
            }
        }


    }

    /**
     * Metoda logowania zarejestrowanego użytkownika za pomocą Firebase Authentication.
     */
    private fun logInRegisteredUser(){


        if(validateLoginDetails()){
            val email = inputEmail?.text.toString().trim(){ it<= ' '}
            val password = inputPassword?.text.toString().trim(){ it<= ' '}

            // Logowanie za pomocą FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        showErrorSnackBar("Zalogowano poprawnie!.", false)
                        goLevelData()
//                        finish()

                    } else{
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }
    }

    /**
     * Metoda przechodzenia do głównej aktywności po pomyślnym zalogowaniu i przekazanie uid do głównej aktywności.
     */
    open fun goLevelData() {

        val user = FirebaseAuth.getInstance().currentUser;
        val uid = user?.email.toString()

        val intent = Intent(this, LevelData::class.java)
        intent.putExtra("uID",uid)
        intent.putExtra("user",user)
        startActivity(intent)
    }

}