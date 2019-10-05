package com.example.buveurs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.buveurs.MainActivity
import com.example.buveurs.MainActivity.Companion.second
import com.example.buveurs.R
import kotlinx.android.synthetic.main.activity_main.*

class CategoryViewFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_category, container, false)
        val button = view.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            fragmentManager!!.beginTransaction().replace(R.id.mainContent, second).commit()
        }
        return view
    }
}