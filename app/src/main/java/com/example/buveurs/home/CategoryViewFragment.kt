package com.example.buveurs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.buveurs.MainActivity
import com.example.buveurs.MainActivity.Companion.homeStack
import com.example.buveurs.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryViewFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_category, container, false)
        val cardviewSoju = view.findViewById<CardView>(R.id.cardViewSoju)
        val cardViewBeer =  view.findViewById<CardView>(R.id.cardViewBeer)
        val cardViewMakgeolli =  view.findViewById<CardView>(R.id.cardViewMakgeolli)
        val cardViewWine = view.findViewById<CardView>(R.id.cardViewWine)
        val cardViewLiquor =  view.findViewById<CardView>(R.id.cardViewLiquor)
        val cardViewEct =  view.findViewById<CardView>(R.id.cardViewEct)
        //카드뷰 클릭 후 homeStack에 푸쉬 후 WritelistViewFragment로 화면 전환
        cardviewSoju.setOnClickListener {
            categoryNextFragment()
        }
        cardViewBeer.setOnClickListener {
            categoryNextFragment()
        }
        cardViewMakgeolli.setOnClickListener {
            categoryNextFragment()
        }
        cardViewWine.setOnClickListener {
            categoryNextFragment()
        }
        cardViewLiquor.setOnClickListener {
            categoryNextFragment()
        }
        cardViewEct.setOnClickListener {
            categoryNextFragment()
        }
        return view
    }
    fun categoryNextFragment(){
        homeStack.push(fragmentManager!!.findFragmentById(R.id.mainContent))
        fragmentManager!!.beginTransaction().replace(R.id.mainContent, WritelistViewFragment()).commit()
    }
}