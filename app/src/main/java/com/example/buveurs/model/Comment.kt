package com.example.buveurs.model

import java.sql.Timestamp

data class Comment(var uid : String? = null,
                   var userId : String? = null,
                   var nickname:String? = null,
                   var comment : String? = null,
                   var timestamp : Long? = null)