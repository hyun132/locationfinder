package com.example.locationfinder.models.bridge

import com.example.locationfinder.db.McLocationItemEntity

/**
 * BrMcSearchResponse
 */

interface BrMcSearchResponse {
    val display: Int
    val items: List<McLocationItemEntity>
    val lastBuildDate: String
    val start: Int
    val total: Int
}