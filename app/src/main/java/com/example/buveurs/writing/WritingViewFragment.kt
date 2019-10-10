package com.example.buveurs.writing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import com.example.buveurs.R

class WritingViewFragment :Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_writing, container, false)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)

        var rating:Float
        ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            rating = fl
        }
        return view
    }

}