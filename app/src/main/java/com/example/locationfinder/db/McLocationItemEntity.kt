package com.example.locationfinder.db


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.locationfinder.constant.McConstants.LOCATION_DB
import com.example.locationfinder.models.bridge.BrMcLocationItem
import kotlinx.android.parcel.Parcelize

/**
 * McItemEntity
 */
@Parcelize
@Entity(tableName = LOCATION_DB)
data class McLocationItemEntity(
    @PrimaryKey
    @ColumnInfo(name = "entity_id")
    val entityId: String,

    @ColumnInfo(name = "entity_address_name")
    val entityAddressName: String,

    @ColumnInfo(name = "entity_category_group_code")
    val entityCategoryGroupCode: String,

    @ColumnInfo(name = "entity_category_group_name")
    val entityCategoryGroupName: String,

    @ColumnInfo(name = "entity_category_name")
    val entityCategoryName: String,

    @ColumnInfo(name = "entity_phone")
    val entityPhone: String,

    @ColumnInfo(name = "entity_place_name")
    val entityPlaceName: String,

    @ColumnInfo(name = "entity_place_url")
    val entityPlaceUrl: String,

    @ColumnInfo(name = "entity_road_address_name")
    val entityRoadAddressName: String,

    @ColumnInfo(name = "entity_x")
    val entityX: String,

    @ColumnInfo(name = "entity_y")
    val entityY: String,

    @ColumnInfo(name = "entity_favorite")
    var entityFavorite: Boolean
) : Parcelable, BrMcLocationItem {
    override val id: String get() = entityId
    override val addressName: String get() = entityAddressName
    override val categoryGroupCode: String get() = entityCategoryGroupCode
    override val categoryGroupName: String get() = entityCategoryGroupName
    override val categoryName: String get() = entityCategoryName
    override val phone: String get() = entityPhone
    override val placeName: String get() = entityPlaceName
    override val placeUrl: String get() = entityPlaceUrl
    override val roadAddressName: String get() = entityRoadAddressName
    override val posX: String get() = entityX
    override val posY: String get() = entityY
    override val distance: String get() = ""
    override var favorite: Boolean = entityFavorite

    companion object {
        fun getBrAsEntity(locationItem: BrMcLocationItem): McLocationItemEntity {
            return McLocationItemEntity(
                entityId = locationItem.id,
                entityAddressName = locationItem.addressName,
                entityRoadAddressName = locationItem.categoryGroupCode,
                entityCategoryGroupName = locationItem.categoryGroupName,
                entityCategoryName = locationItem.categoryName,
                entityPhone = locationItem.phone,
                entityPlaceName = locationItem.placeName,
                entityPlaceUrl = locationItem.placeUrl,
                entityX = locationItem.posX,
                entityY = locationItem.posY,
                entityCategoryGroupCode = locationItem.categoryGroupCode,
                entityFavorite = locationItem.favorite,
            )
        }
    }
}

