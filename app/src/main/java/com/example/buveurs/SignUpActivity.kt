package com.example.buveurs

import android.R.attr.breakStrategy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Pattern
import android.R.attr.password
import android.util.Patterns
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.database.*



class SignUpActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        btn_SignUp.setOnClickListener {
            if(isValidEmail(signupEdtId.text.toString()) && isValidPasswd(signupEdtPassword.text.toString())) {
                signup()
            }
        }
    }
    fun signup(){
        //이메일, 패스워드로 회원가입
        val user = User()
        val newRef = FirebaseDatabase.getInstance().getReference("/Users/"+signupEdtNickname.text.toString())
        var result:Any?=null
        //Firebase에서 해당 닉네임이 있는지 검색
        newRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                result = p0.getValue()
                if(result.toString().equals("null")){
                    auth?.createUserWithEmailAndPassword(signupEdtId.text.toString(), signupEdtPassword.text.toString())
                        ?.addOnCompleteListener {
                                task -> //성공시 Firebase에 회원정보 저장
                                    if (task.isSuccessful){
                                        Toast.makeText(applicationContext, "회원가입완료", Toast.LENGTH_SHORT).show()
                                        user.userID = signupEdtId.text.toString()
                                        user.userNickname = signupEdtNickname.text.toString()
                                        newRef.setValue(user)
                                        this@SignUpActivity.finish()
                                        //실패시
                                    }else{
                                        Toast.makeText(applicationContext, "중복되는 이메일입니다.", Toast.LENGTH_SHORT).show()
                                        signupEdtId.setText("")
                                    }
                        }
                }else{
                    Toast.makeText(applicationContext, "중복되는 닉네임입니다.", Toast.LENGTH_SHORT).show()
                }
                Log.d("any", result.toString())
            }
        })

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
                signupEdtId.setText("")
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
                signupEdtPassword.setText("")
                return false
            }
            else -> { return true }
        }
    }
    companion object{
        val PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")

    }
}
