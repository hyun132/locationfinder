package com.example.locationfinder.models

import com.example.locationfinder.db.McLocationItemEntity

/**
 * McSearchResponse
 */

data class McSearchResponseDto(
    val display: Int,
    val items: List<McLocationItemEntity>,
    val lastBuildDate: String,
    val start: Int,
    val total: Int
)