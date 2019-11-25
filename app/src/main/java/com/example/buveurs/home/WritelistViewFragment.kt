package com.example.buveurs.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.buveurs.MainActivity
import com.example.buveurs.R
import com.example.buveurs.home.CategoryViewFragment.Companion.category
import com.example.buveurs.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.card_post.view.*
import kotlinx.android.synthetic.main.fragment_writelist.view.*

class WritelistViewFragment : Fragment(){
    var firestore :FirebaseFirestore? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_writelist, container, false)
        val title = view.findViewById<TextView>(R.id.writeListViewtxtvTitle)
        val backButton = view.findViewById<ImageButton>(R.id.writeListViewimageViewBack)
        val category = arguments?.getString("category")
        backButton.setOnClickListener {
            val lastFragmentStack = MainActivity.homeStack.pop()
            fragmentManager!!.beginTransaction().replace(R.id.mainContent, lastFragmentStack).commit()
        }
        title.setText(category)
        firestore = FirebaseFirestore.getInstance()



        view.writeListFragmentRecyclerview.adapter = CardViewRecyclerViewAdapter()
        view.writeListFragmentRecyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }
    inner class CardViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()

        init{
            firestore?.collection("images")?.document()
            firestore?.collection("images")?.orderBy("timestamp",
                Query.Direction.DESCENDING)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                if(querySnapshot==null){

                }else{
                    for(snapshot in querySnapshot!!.documents){
                        var item = snapshot.toObject(ContentDTO::class.java)
                        if(item!!.alcoholcategory!!.equals(category)){
                            contentDTOs.add(item!!)
                            contentUidList.add(snapshot.id)
                        }

                    }

                }
                notifyDataSetChanged()

            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
            return CustomViewHoler(view)
        }

        inner class CustomViewHoler(view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
           return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHoler).itemView

            //UserNickname
            viewholder.cardPostTxtvUser.text = contentDTOs!![position].nickname

            //Image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUri).into(viewholder.cardPostImageView)

            //Alcohol
            viewholder.cardPostTxtvAlcohol.text =  contentDTOs!![position].alcoholname

            //ratingNum
            viewholder.cardPostTxtvRatingNum.text = contentDTOs!![position].starRating.toString()

            holder.itemView.setOnClickListener {
                MainActivity.homeStack.push(fragmentManager!!.findFragmentById(R.id.mainContent))
                val fragment: Fragment = DetailViewFragment() // Fragment 생성
                val bundle = Bundle(7) // 파라미터는 전달할 데이터 개수
                bundle.putString("uri", contentDTOs!![position].imageUri) // key , value
                bundle.putSerializable("alcoholname",contentDTOs!![position].alcoholname)
                bundle.putSerializable("rating",contentDTOs!![position].starRating)
                bundle.putSerializable("intro",contentDTOs!![position].intro)
                bundle.putSerializable("snack",contentDTOs!![position].snack)
                bundle.putSerializable("price",contentDTOs!![position].price)
                bundle.putSerializable("writename", contentDTOs!![position].writeName)
                fragment.arguments = bundle
                fragmentManager!!.beginTransaction().replace(R.id.mainContent, fragment).commit()
            }

        }

    }
}