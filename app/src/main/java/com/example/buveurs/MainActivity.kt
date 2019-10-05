package com.example.buveurs

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.buveurs.home.CategoryViewFragment
import com.example.buveurs.home.WritelistViewFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_category.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.fixedRateTimer


const val stack_1 = 1001
const val stack_2 = 1002
class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeStack.push(first)
        manager.beginTransaction().replace(R.id.mainContent, first).commit()
    }
    fun change(){
        manager.beginTransaction().replace(R.id.mainContent, second).commit()
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
