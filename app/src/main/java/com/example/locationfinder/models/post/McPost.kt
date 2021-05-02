package com.example.locationfinder.models.post

data class McPost(
    val id: Int,
    val title: String,
    val description: String,  // 사진,동영상의 설명
    val contents : String, //사진과 같이 올린 내용
    val like: Int,
    val reply: List<Review>,
    val date: String,
    val url : String,
    val dataType:Int,
    val tag : List<String>
)