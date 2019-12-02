package com.example.buveurs.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buveurs.MainActivity
import com.example.buveurs.R
import com.example.buveurs.home.CategoryViewFragment
import com.example.buveurs.home.DetailViewFragment
import com.example.buveurs.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.card_post.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_writelist.view.*

class SearchViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_search, container, false)
        view.searchRecyclerView.adapter = CardViewRecyclerViewAdapter()
        view.searchRecyclerView.layoutManager = LinearLayoutManager(activity)
        view.searchImageButton.setOnClickListener {
            val fragment: Fragment = SearchViewFragment() // Fragment 생성
            val bundle = Bundle(2) // 파라미터는 전달할 데이터 개수
            bundle.putSerializable("searchContent",view.edtSearch.text.toString())
            bundle.putSerializable("searchUser", view.searchUserSpinner.selectedItem.toString())
            fragment.arguments = bundle
            fragmentManager!!.beginTransaction().replace(R.id.mainContent, fragment).commit()

        }

        return view
    }
    inner class CardViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()
        var mode = true

        init{
            val searchContent = arguments?.getString("searchContent")
            val searchUser =  arguments?.getString("searchUser")
            //contentDTOs.clear()
            //contentUidList.clear()
            FirebaseFirestore.getInstance()?.collection("images")?.orderBy("timestamp",
                Query.Direction.DESCENDING)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(querySnapshot==null){
                    Log.d("snapshot","null")
                }else{
                    if(searchContent!=null){
                        for(snapshot in querySnapshot!!.documents){
                            var item = snapshot.toObject(ContentDTO::class.java)
                            if(searchUser.equals("술이름")){
                                if(mode  && item!!.alcoholname.equals(searchContent)){
                                    contentDTOs.add(item)
                                    contentUidList.add(snapshot.id)
                                }
                            }else{
                                if(mode  && item!!.nickname.equals(searchContent)){
                                    contentDTOs.add(item)
                                    contentUidList.add(snapshot.id)
                                }
                            }

                        }
                    }
                }
                mode = false
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
                MainActivity.searchStack.push(fragmentManager!!.findFragmentById(R.id.mainContent))
                val fragment: Fragment = DetailViewFragment() // Fragment 생성
                val bundle = Bundle(8) // 파라미터는 전달할 데이터 개수
                bundle.putString("uri", contentDTOs!![position].imageUri) // key , value
                bundle.putSerializable("alcoholname",contentDTOs!![position].alcoholname)
                bundle.putSerializable("rating",contentDTOs!![position].starRating)
                bundle.putSerializable("intro",contentDTOs!![position].intro)
                bundle.putSerializable("snack",contentDTOs!![position].snack)
                bundle.putSerializable("price",contentDTOs!![position].price)
                bundle.putSerializable("writename", contentDTOs!![position].writeName)
                bundle.putSerializable("stack","search")
                fragment.arguments = bundle
                fragmentManager!!.beginTransaction().replace(R.id.mainContent, fragment).commit()
            }

        }

    }
}