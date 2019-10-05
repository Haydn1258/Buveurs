package com.example.buveurs

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.buveurs.home.CategoryViewFragment
import com.example.buveurs.home.WritelistViewFragment
import com.example.buveurs.search.SearchViewFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_category.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.fixedRateTimer


const val stack_1 = 1001
const val stack_2 = 1002
class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    val manager = supportFragmentManager
    var lastSelect = ""
    //바텀네비게이션 선택 이벤트
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.action_home ->{
                //
                if(lastSelect.equals("home")){
                    homeStack.clear()
                    manager.beginTransaction().replace(R.id.mainContent, CategoryViewFragment()).commit()
                }else{
                    stackPush(lastSelect)
                    if(homeStack.empty()){
                        val categoryViewFragment = CategoryViewFragment()
                        manager.beginTransaction().replace(R.id.mainContent, categoryViewFragment).commit()
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
                    homeStack.clear()
                    manager.beginTransaction().replace(R.id.mainContent, SearchViewFragment()).commit()
                }else{
                    stackPush(lastSelect)
                    if(searchStack.empty()){
                        val searchViewFragment = SearchViewFragment()
                        manager.beginTransaction().replace(R.id.mainContent, searchViewFragment).commit()
                        lastSelect = "search"
                    }else{
                        val lastFragmentStack = searchStack.pop()
                        manager.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
                        lastSelect = "search"
                    }
                }
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavView.setOnNavigationItemSelectedListener(this)
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
       val first = CategoryViewFragment()
       var second = WritelistViewFragment()
       var homeStack = Stack<Fragment>()
       var searchStack = Stack<Fragment>()
       var writingStack = Stack<Fragment>()
       var mypageStack = Stack<Fragment>()



    }
}
