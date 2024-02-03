package com.example.linstagram.model

import com.google.firebase.Timestamp
import java.time.LocalTime

data class Journal(
     val tit:String = "",
     val descrp:String = "",
     val imageUrl:String = "",
     val userId:String = "",
     val timeAdded: String,
     val username:String=""
    )
