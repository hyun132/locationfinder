package com.example.locationfinder.models.post

data class Review (
    val id: Int,
    val title: String,
    val description: String,
    val like: Int,
    val reply: List<Review>,
    val date: String
)