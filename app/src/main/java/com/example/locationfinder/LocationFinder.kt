package com.example.locationfinder

import android.app.Application
import android.content.Context
import com.example.locationfinder.constant.McConstants.KAKAO_NATIVE_KEY
import com.example.locationfinder.ui.base.appModules
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * LocationFinder
 */
class LocationFinder :
    Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        instance = this

        KakaoSdk.init(this, KAKAO_NATIVE_KEY)

        startKoin {
            androidContext(appContext)
            modules(appModules)
        }
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    /**
     * LocationFinder(application)
     */
    companion object {
        @Volatile
        private var instance: LocationFinder? = null
        lateinit var appContext: Context
    }
}
