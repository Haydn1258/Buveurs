package com.example.buveurs

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.buveurs.LoginActivity.Companion.loginAuth
import com.example.buveurs.home.CategoryViewFragment
import com.example.buveurs.mypage.MyPageViewFragment
import com.example.buveurs.search.SearchViewFragment
import com.example.buveurs.writing.WritingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import java.util.*

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    val manager = supportFragmentManager
    val READ_EXTERNAL_STORAGE_PERMISSION = 100


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
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION)
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

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
                val myPageViewFragment: Fragment = MyPageViewFragment() // Fragment 생성
                if (loginAuth?.getCurrentUser() == null) {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    stackPush(lastSelect)
                    var nickname = ""
                    val nicknameRef =   FirebaseDatabase.getInstance().getReference("Users/UserID/"+ FirebaseAuth.getInstance().currentUser?.uid)
                    nicknameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            nickname = p0.child("userNickname").getValue().toString()

                            val bundle = Bundle(1) // 파라미터는 전달할 데이터 개수
                            bundle.putSerializable("nickname",nickname)
                            myPageViewFragment.arguments = bundle
                            if(lastSelect.equals("mypage")){
                                mypageStack.clear()
                                manager.beginTransaction().replace(R.id.mainContent, myPageViewFragment).commit()
                            }else{
                                stackPush(lastSelect)
                                if(mypageStack.empty()){
                                    manager.beginTransaction().replace(R.id.mainContent, myPageViewFragment).commit()
                                    lastSelect = "mypage"
                                }else{
                                    val lastFragmentStack = mypageStack.pop()
                                    manager.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
                                    lastSelect = "mypage"
                                }
                            }

                        }
                    })



                    return true
                }

            }
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivity(Intent(this, WritingActivity::class.java))
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavView.setOnNavigationItemSelectedListener(this)
        bottomNavView.selectedItemId = R.id.action_home
        //앱 처음 실행시 Category가 보이게함
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
