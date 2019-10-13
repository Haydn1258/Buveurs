package com.example.buveurs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.buveurs.SignUpActivity.Companion.PASSWORD_PATTERN
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginAuth= FirebaseAuth.getInstance()
        btn_Login.setOnClickListener {
            if(isValidEmail(loginEdtID.text.toString()) && isValidPasswd(loginEdtPassword.text.toString())) {
                signIn()
            }

        }
        txtv_SignUp.setOnClickListener{
            loginEdtID.setText("")
            loginEdtPassword.setText("")
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
    fun signIn(){
        loginAuth?.signInWithEmailAndPassword(loginEdtID.text.toString(), loginEdtPassword.text.toString())
            ?.addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "로그인성공",Toast.LENGTH_SHORT).show()
                    Log.d("any","sus")
                    this.finish()
                }else{
                    Toast.makeText(applicationContext, "로그인실패",Toast.LENGTH_SHORT).show()
                    loginEdtID.setText("")
                    loginEdtPassword.setText("")
                    Log.d("any","false")
                    (txtv_Fail as View).visibility = View.INVISIBLE
                }

            }
    }
    fun isValidEmail(email:String): Boolean {
        when{
            email.isEmpty() -> {
                // 이메일 공백
                Toast.makeText(applicationContext, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                // 이메일 형식 불일치
                Toast.makeText(applicationContext, "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show()
                loginEdtID.setText("")
                return false
            }
            else-> {return true }
        }
    }

    // 비밀번호 유효성 검사
    fun isValidPasswd(password:String): Boolean {
        when{
            password.isEmpty()-> {
                // 비밀번호 공백
                Toast.makeText(applicationContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return false
            }
            !PASSWORD_PATTERN.matcher(password).matches() -> {
                // 비밀번호 형식 불일치
                Toast.makeText(applicationContext, "비밀번호 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show()
                loginEdtPassword.setText("")
                return false
            }
            else -> { return true }
        }
    }
    companion object{
        var loginAuth:FirebaseAuth?=null
    }
}
