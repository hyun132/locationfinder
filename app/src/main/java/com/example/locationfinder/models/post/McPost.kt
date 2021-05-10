package com.example.locationfinder.models.post

import android.net.Uri

data class McPost(
    val id: Long,
    val description: String,  // 사진,동영상의 설명
    val contents: String, //사진과 같이 올린 내용
    val like: Int,
    val reply: List<Review>,
    val date: String,
    val url: MutableList<Uri>,
    val dataType:Int,
    val tag: List<String>
)