package com.example.buveurs.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.buveurs.MainActivity
import com.example.buveurs.R
import com.example.buveurs.model.Comment
import com.example.buveurs.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_writing.*
import kotlinx.android.synthetic.main.card_post.view.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import java.text.SimpleDateFormat
import java.util.*

class DetailViewFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)
        val detailViewtxtvTitle = view.findViewById<TextView>(R.id.detailViewtxtvTitle)
        val detailRatingBar =  view.findViewById<RatingBar>(R.id.detailRatingBar)
        val detailTxtvAlcoholIntro =  view.findViewById<TextView>(R.id.detailTxtvAlcoholIntro)
        val detailTxtvAlcoholSnack =  view.findViewById<TextView>(R.id.detailTxtvAlcoholSnack)
        val detailViewBackImageButton = view.findViewById<ImageButton>(R.id.detailViewBackImageButton)
        val detailTxtvAlcoholPrice =  view.findViewById<TextView>(R.id.detailTxtvAlcoholPrice)
        val detailEdtComment = view.findViewById<EditText>(R.id.detailEdtComment)
        val detailBtnWrite = view.findViewById<Button>(R.id.detailBtnWrite)
        val alcoholname = arguments?.getString("alcoholname")
        val rating = arguments?.getFloat("rating")
        val intro = arguments?.getString("intro")
        val snack = arguments?.getString("snack")
        val price = arguments?.getString("price")
        val writename = arguments?.getString("writename")
        var mLastClickTime:Long = 0
        detailViewtxtvTitle.setText(alcoholname)
        detailTxtvAlcoholIntro.setText(intro)
        detailTxtvAlcoholSnack.setText(snack)
        detailTxtvAlcoholPrice.setText(price)
        detailRatingBar.rating = rating!!

        Glide.with(view.context).load(arguments?.getString("uri")).into(view.detailImageAlcohol)

        detailViewBackImageButton.setOnClickListener {
            val lastFragmentStack = MainActivity.homeStack.pop()
            fragmentManager!!.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
        }

        detailBtnWrite.setOnClickListener {
            //중복클릭 방지
            if (SystemClock.elapsedRealtime() - mLastClickTime > 1000){
                mLastClickTime = SystemClock.elapsedRealtime();
                val CommentAuth = FirebaseAuth.getInstance()
                var Comment = Comment()
                var userNickname = ""
                //닉네임 가져와서 댓글저장
                val ComentRef =   FirebaseDatabase.getInstance().getReference("Users/UserID/"+CommentAuth.currentUser?.uid)
                ComentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }
                    override fun onDataChange(p0: DataSnapshot) {
                        userNickname =  p0.child("userNickname").getValue().toString()
                        Comment.uid = CommentAuth.currentUser?.uid
                        Comment.userId =  CommentAuth.currentUser?.email
                        Comment.nickname = userNickname
                        Comment.comment = detailEdtComment.text.toString()
                        Comment.timestamp = System.currentTimeMillis()
                        FirebaseFirestore.getInstance().collection(writename!!).document(Comment.timestamp.toString()).set(Comment)
                    }
                })
            }

        }

        return view
    }
}