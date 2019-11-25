package com.example.buveurs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.buveurs.MainActivity.Companion.homeStack
import com.example.buveurs.R


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
            category = "소주"
            categoryNextFragment()
        }
        cardViewBeer.setOnClickListener {
            category = "맥주"
            categoryNextFragment()
        }
        cardViewMakgeolli.setOnClickListener {
            category = "막걸리"
            categoryNextFragment()
        }
        cardViewWine.setOnClickListener {
            category = "와인"
            categoryNextFragment()
        }
        cardViewLiquor.setOnClickListener {
            category = "양주"
            categoryNextFragment()
        }
        cardViewEct.setOnClickListener {
            category = "기타"
            categoryNextFragment()
        }
        return view
    }
    fun categoryNextFragment(){
        homeStack.push(fragmentManager!!.findFragmentById(R.id.mainContent))
        val fragment: Fragment = WritelistViewFragment() // Fragment 생성
        val bundle = Bundle(1) // 파라미터는 전달할 데이터 개수
        bundle.putString("category", category) // key , value
        fragment.arguments = bundle
        fragmentManager!!.beginTransaction().replace(R.id.mainContent, fragment).commit()

    }
    companion object{
        var category = "소주"
    }
}