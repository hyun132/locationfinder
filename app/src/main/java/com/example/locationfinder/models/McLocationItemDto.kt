package com.example.locationfinder.models

import android.os.Parcelable
import com.example.locationfinder.models.bridge.BrMcLocationItem
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * McLocationItemDto
 */
@Parcelize
data class McLocationItemDto(
    @SerializedName("id")
    val _id: String,

    @SerializedName("address_name")
    val _addressName: String,

    @SerializedName("category_group_code")
    val _categoryGroupCode: String,

    @SerializedName("category_group_name")
    val _categoryGroupName: String,

    @SerializedName("category_name")
    val _categoryName: String,

    @SerializedName("phone")
    val _phone: String,

    @SerializedName("place_name")
    val _placeName: String,

    @SerializedName("place_url")
    val _placeUrl: String,

    @SerializedName("road_address_name")
    val _roadAddressName: String,

    @SerializedName("x")
    val _posX: String,

    @SerializedName("y")
    val _posY: String,

    @SerializedName("distance")
    val _distance: String
) : Parcelable, BrMcLocationItem {
    override val id: String get() = _id
    override val addressName: String get() = _addressName
    override val categoryGroupCode: String get() = _categoryGroupCode
    override val categoryGroupName: String get() = _categoryGroupName
    override val categoryName: String get() = _categoryName
    override val phone: String get() = _phone
    override val placeName: String get() = _placeName
    override val placeUrl: String get() = _placeUrl
    override val roadAddressName: String get() = _roadAddressName
    override val posX: String get() = _posX
    override val posY: String get() = _posY
    override val distance: String get() = if (_distance.isEmpty()) "" else "$_distance m"
    override var favorite: Boolean = false
}