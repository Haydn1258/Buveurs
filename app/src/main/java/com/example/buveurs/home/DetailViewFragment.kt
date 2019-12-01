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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buveurs.MainActivity
import com.example.buveurs.R
import com.example.buveurs.model.Comment
import com.example.buveurs.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_writing.*
import kotlinx.android.synthetic.main.card_comment.view.*
import kotlinx.android.synthetic.main.card_post.view.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.fragment_writelist.view.*
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
        val uid = arguments?.getString("uid")



        var mLastClickTime:Long = 0
        detailViewtxtvTitle.setText(alcoholname)
        detailTxtvAlcoholIntro.setText(intro)
        detailTxtvAlcoholSnack.setText(snack)
        detailTxtvAlcoholPrice.setText(price)
        detailRatingBar.rating = rating!!
        if (uid.equals(FirebaseAuth.getInstance().currentUser?.uid)){
            view.detailViewTxtvDelete.visibility = View.VISIBLE
        }

        view.detailViewTxtvDelete.setOnClickListener {
            FirebaseFirestore.getInstance().collection("images").document(writename!!).delete()
            val lastFragmentStack = MainActivity.homeStack.pop()
            fragmentManager!!.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
        }

        Glide.with(view.context).load(arguments?.getString("uri")).into(view.detailImageAlcohol)

        detailViewBackImageButton.setOnClickListener {
            if(arguments!!.getString("stack")!!.equals("home")){
                val lastFragmentStack = MainActivity.homeStack.pop()
                fragmentManager!!.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
            }else{
                val lastFragmentStack = MainActivity.searchStack.pop()
                fragmentManager!!.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
            }

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
                        detailEdtComment.setText("")
                    }
                })

            }

        }
        view.fragmentDetailRecyclerView.adapter = CardCommentRecyclerViewAdapter()
        view.fragmentDetailRecyclerView.layoutManager = LinearLayoutManager(activity)
        view.fragmentDetailRecyclerView.setNestedScrollingEnabled(false)

        return view
    }
    inner class CardCommentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var comment : ArrayList<Comment> = arrayListOf()
        init{
            //comment.clear()
            FirebaseFirestore.getInstance()?.collection(arguments?.getString("writename")!!)?.orderBy("timestamp",
                Query.Direction.DESCENDING)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(querySnapshot==null){

                }else{
                    for(snapshot in querySnapshot!!.documents){
                        var item = snapshot.toObject(Comment::class.java)
                        if(!comment.contains(item)){
                            comment.add(item!!)
                        }
                    }

                }
                notifyDataSetChanged()

            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.card_comment, parent, false)
            return CustomViewHoler(view)
        }

        inner class CustomViewHoler(view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return comment.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHoler).itemView

            //UserNickname
            viewholder.cardCommentTxtvNickname.text = comment!![position].nickname

            //Comment
            viewholder.cardCommentTxtvComment.text =  comment!![position].comment

            //delete
            if (comment[position].uid.equals(FirebaseAuth.getInstance().currentUser?.uid)){
                viewholder.cardCommentTxtvDelete.visibility = View.VISIBLE

            }
            viewholder.cardCommentTxtvDelete.setOnClickListener {
                FirebaseFirestore.getInstance().collection(arguments?.getString("writename")!!).document(comment[position].timestamp.toString()).delete()
                val fragment: Fragment = DetailViewFragment() // Fragment 생성
                val bundle = Bundle(9) // 파라미터는 전달할 데이터 개수
                bundle.putString("uri", arguments?.getString("uri")) // key , value
                bundle.putSerializable("alcoholname", arguments?.getString("alcoholname"))
                bundle.putSerializable("rating", arguments?.getFloat("rating"))
                bundle.putSerializable("intro", arguments?.getString("intro"))
                bundle.putSerializable("snack", arguments?.getString("snack"))
                bundle.putSerializable("price", arguments?.getString("price"))
                bundle.putString("writename",  arguments?.getString("writename"))
                bundle.putSerializable("uid",  arguments?.getString("uid"))
                bundle.putSerializable("stack","home")
                fragment.arguments = bundle
                fragmentManager!!.beginTransaction().replace(R.id.mainContent, fragment).commit()
            }




        }

    }
}