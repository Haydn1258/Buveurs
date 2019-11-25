package com.example.buveurs.writing

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.buveurs.R
import com.example.buveurs.model.ContentDTO
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_writing.*
import java.text.SimpleDateFormat
import java.util.*

class WritingActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0;
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null
    var starRating:Float = 0.0.toFloat()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        imageAlcohol.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }

        btn_OK.setOnClickListener {
            if(photoUri==null){
                Toast.makeText(this, "사진을 올려주세요.", Toast.LENGTH_LONG).show()
            }else{
                finish()
                contentUpload()
            }
        }
        imageViewBack.setOnClickListener {
            finish()
        }
        ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            starRating = fl
            Log.d("anya", fl.toString())
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_FROM_ALBUM){
            if(resultCode == Activity.RESULT_OK){
                photoUri = data?.data
                imageAlcohol.setImageURI(photoUri)
            }
        }else{

        }
    }
    //업로드함수
    fun contentUpload(){
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE"+timestamp+"_.png"
        var writeName = "WRITE"+timestamp
        var userNickname :String = ""

        var storageRef = storage?.reference?.child("image")?.child(imageFileName)
        val idRef =   FirebaseDatabase.getInstance().getReference("Users/UserID/"+auth?.currentUser?.uid)
        idRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                userNickname =  p0.child("userNickname").getValue().toString()

            }
        })

        storageRef?.putFile(photoUri!!)?.continueWithTask { task:Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            var contentDTO = ContentDTO()
            contentDTO.imageUri = uri.toString()
            contentDTO.uid = auth?.currentUser?.uid
            contentDTO.userId = auth?.currentUser?.email
            contentDTO.alcoholname = edtAlcoholName.text.toString()
            contentDTO.timestamp = System.currentTimeMillis()
            contentDTO.alcoholcategory = alcoholCategory.selectedItem.toString()
            contentDTO.starRating = starRating
            contentDTO.intro = edtAlcoholIntro.text.toString()
            contentDTO.snack = edtAlcoholSnack.text.toString()
            contentDTO.price = edtAlcoholPrice.text.toString()
            contentDTO.imageFileName = imageFileName
            contentDTO.writeName = writeName
            contentDTO.nickname = userNickname
            firestore?.collection("images")?.document(writeName)?.set(contentDTO)
            setResult(Activity.RESULT_OK)
            Toast.makeText(this, "업로드 완료", Toast.LENGTH_LONG).show()

        }
        /*
        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var contentDTO = ContentDTO()

                contentDTO.imageUri = uri.toString()
                contentDTO.uid = auth?.currentUser?.uid
                contentDTO.userId = auth?.currentUser?.email
                contentDTO.alcoholname = edtAlcoholName.text.toString()
                contentDTO.timestamp = System.currentTimeMillis()

                firestore?.collection("images")?.document()?.set(contentDTO)

                setResult(Activity.RESULT_OK)
                finish()
            }

        }*/
    }
}
