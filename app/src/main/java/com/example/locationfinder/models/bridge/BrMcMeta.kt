package com.example.locationfinder.models.bridge

import com.example.locationfinder.models.McSameNameDto

/**
 * BrMcMeta
 */

interface BrMcMeta {
    val isEnd: Boolean
    val pageableCount: Int
    val sameNameDto: McSameNameDto
    val totalCount: Int
}