package com.example.buveurs

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.buveurs.LoginActivity.Companion.loginAuth
import com.example.buveurs.home.CategoryViewFragment
import com.example.buveurs.mypage.MyPageViewFragment
import com.example.buveurs.search.SearchViewFragment
import com.example.buveurs.writing.WritingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    val manager = supportFragmentManager


    //바텀네비게이션 선택 이벤트
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        loginAuth= FirebaseAuth.getInstance()
        when(p0.itemId){
            R.id.action_home ->{
                // lastSelect가 home 이면 homeStack 클리어 후 CategoryViewFragment보여줌
                // home이 아니면 stackPush에서 lastSelect에 해당하는 스택에 push 후 homeStack에서 pop한 fragment를 보여줌
                // 나머지도 동일
                if(lastSelect.equals("home")){
                    homeStack.clear()
                    manager.beginTransaction().replace(R.id.mainContent, CategoryViewFragment()).commit()
                }else{
                    stackPush(lastSelect)
                    if(homeStack.empty()){
                        manager.beginTransaction().replace(R.id.mainContent, CategoryViewFragment()).commit()
                        lastSelect = "home"
                    }else{
                        val lastFragmentStack = homeStack.pop()
                        manager.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
                        lastSelect = "home"
                    }
                }
                return true
            }
            R.id.action_search ->{
                if(lastSelect.equals("search")){
                    searchStack.clear()
                    manager.beginTransaction().replace(R.id.mainContent, SearchViewFragment()).commit()
                }else{
                    stackPush(lastSelect)
                    if(searchStack.empty()){

                        manager.beginTransaction().replace(R.id.mainContent, SearchViewFragment()).commit()
                        lastSelect = "search"
                    }else{
                        val lastFragmentStack = searchStack.pop()
                        manager.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
                        lastSelect = "search"
                    }
                }
                return true
            }
            R.id.action_writing ->{
                //로그인 안되어있을시 로그인화면으로 이동
                if (loginAuth?.getCurrentUser() == null) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    //overridePendingTransition(0, 0)
                }else{
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        startActivity(Intent(this, WritingActivity::class.java))
                    }
                    /*if(lastSelect.equals("writing")){
                        writingStack.clear()
                        manager.beginTransaction().replace(R.id.mainContent, WritingViewFragment()).commit()
                    }else{
                        stackPush(lastSelect)
                        if(writingStack.empty()){
                            manager.beginTransaction().replace(R.id.mainContent, WritingViewFragment()).commit()
                            lastSelect = "writing"
                        }else{
                            val lastFragmentStack = writingStack.pop()
                            manager.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
                            lastSelect = "writing"
                        }
                    }
                    return true*/
                }

            }
            R.id.action_mypage ->{
                //로그인 안되어있을시 로그인화면으로 이동
                if (loginAuth?.getCurrentUser() == null) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }else{

                    if(lastSelect.equals("mypage")){
                        mypageStack.clear()
                        manager.beginTransaction().replace(R.id.mainContent, MyPageViewFragment()).commit()
                    }else{
                        stackPush(lastSelect)
                        if(mypageStack.empty()){
                            manager.beginTransaction().replace(R.id.mainContent, MyPageViewFragment()).commit()
                            lastSelect = "mypage"
                        }else{
                            val lastFragmentStack = mypageStack.pop()
                            manager.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
                            lastSelect = "mypage"
                        }
                    }
                    return true
                }

            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavView.setOnNavigationItemSelectedListener(this)
        //앱 처음 실행시 Category가 보이게함
        manager.beginTransaction().replace(R.id.mainContent, CategoryViewFragment()).commit()
        lastSelect = "home"

    }

    //마지막 선택별 스택 푸쉬
    fun stackPush(push:String){
        when(push){
            "home" ->{
                homeStack.push(supportFragmentManager.findFragmentById(R.id.mainContent))
            }
            "search" ->{
                searchStack.push(supportFragmentManager.findFragmentById(R.id.mainContent))
            }
            "writing" ->{
                writingStack.push(supportFragmentManager.findFragmentById(R.id.mainContent))
            }
            "mypage" ->{
                mypageStack.push(supportFragmentManager.findFragmentById(R.id.mainContent))
            }
        }
    }

   companion object{
       var homeStack = Stack<Fragment>()
       var searchStack = Stack<Fragment>()
       var writingStack = Stack<Fragment>()
       var mypageStack = Stack<Fragment>()
       var lastSelect = ""
    }
}
