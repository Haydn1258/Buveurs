package com.example.buveurs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


    }
    fun signup(){
        auth?.createUserWithEmailAndPassword(signupEdtId.text.toString(), signupEdtPassword.text.toString())
            ?.addOnCompleteListener {
            task ->
                if (task.isSuccessful){

                }

        }

    }
    fun moveMainPage(user: FirebaseUser?){
        if(user != null){
            finish()
        }
    }
}
