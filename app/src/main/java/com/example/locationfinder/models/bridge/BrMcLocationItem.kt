package com.example.locationfinder.models.bridge

/**
 * BrMcLocationItem
 */

interface BrMcLocationItem {
    val id: String
    val addressName: String
    val categoryGroupCode: String
    val categoryGroupName: String
    val categoryName: String
    val phone: String
    val placeName: String
    val placeUrl: String
    val roadAddressName: String
    val posX: String
    val posY: String
    val distance: String
    var favorite: Boolean
}