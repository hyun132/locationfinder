package com.example.locationfinder.models.bridge

import com.example.locationfinder.models.McLocationItemDto
import com.example.locationfinder.models.McMetaDto

/**
 * BrMcKakaoSearchResponse
 */
interface BrMcKakaoSearchResponse {
    val documents: List<McLocationItemDto>
    val metaDto: McMetaDto
}