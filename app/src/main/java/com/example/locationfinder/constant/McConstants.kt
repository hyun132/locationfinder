package com.example.locationfinder.constant

/**
 * McConstants
 */
object McConstants {
    /* kakao api base url */
    const val BASE_URL = "https://dapi.kakao.com/"
    /* kakao api key */
    const val KAKAO_KEY = "9788065ac345c58b9f8d60a041da8885"

    const val KAKAO_NATIVE_KEY = "538121156c3423354c18f2d7226459b1"

    /* location 저장 db 이름 */
    const val LOCATION_DB = "item_db"

    /* dialog 크기 */
    const val DIALOG_WIDTH = 700

    /* 위치 권한 코드 */
    const val PERMISSION_REQUEST_CODE = 1001
    /* 위치 권한 키 */
    const val PERMISSION_PREFERENCE_KEY = "isFirstPermissionCheck"
    /* 지도 줌 값 */
    const val CAMERA_ZOOM_LEVEL = 18F

    /* splash 지연 값 */
    const val SPLASH_DELAY = 2500L

    /* 카카오 */
    const val KAKAO_PACKAGE_NAME="net.daum.android.map"
    const val MARKET_BASE_URL = "market://details?id="

    /**
     * LocationCategory
     */
    enum class LocationCategory(val categoryCode: String?, val categoryDescription: String) {
        DEFAULT(null, "전체보기"),
        MART("MT1", "대형마트"),
        CONVENIENCE_STORE("CS2", "편의점"),
        RESTAURANT("FD6", "음식점"),
        BANK("BK9", "은행"),
        CAFE("CE7", "카페"),
        HOSPITAL("HP8", "병원"),
        PHARMACY("PM9", "약국"),
        SUBWAY("SW8", "지하철역")
    }
}