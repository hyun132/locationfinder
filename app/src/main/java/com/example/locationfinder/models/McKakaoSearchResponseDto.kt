package com.example.locationfinder.models

/**
 * McKakaoSearchResponse
 */

data class McKakaoSearchResponseDto(
    val documents: List<McLocationItemDto>,
    val metaDto: McMetaDto
)