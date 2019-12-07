package com.example.buveurs.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.buveurs.MainActivity
import com.example.buveurs.MainActivity.Companion.homeStack
import com.example.buveurs.MainActivity.Companion.lastSelect
import com.example.buveurs.R
import com.example.buveurs.search.SearchViewFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class MyPageViewFragment :Fragment(){
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val nickname = arguments?.getString("nickname")
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_mypage, container, false)
        view.mypageTxtvNickName.setText(nickname)
        view.mypageTxtvEmail.setText(FirebaseAuth.getInstance().currentUser?.email)


        view.mypageBtnMywrite.setOnClickListener {

            val searchViewFragment: Fragment = SearchViewFragment() // Fragment 생성
            val bundle = Bundle(3) // 파라미터는 전달할 데이터 개수
            bundle.putSerializable("searchUser","작성자")
            bundle.putSerializable("searchContent",nickname)
            bundle.putSerializable("stack","mypage")
            searchViewFragment.arguments = bundle
            MainActivity.mypageStack.push(fragmentManager!!.findFragmentById(R.id.mainContent))
            lastSelect = "mypage"
            fragmentManager!!.beginTransaction().replace(R.id.mainContent, searchViewFragment).commit()
        }

        view.mypageBtnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }

        return view
    }

}
