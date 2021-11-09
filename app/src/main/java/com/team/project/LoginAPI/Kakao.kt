
package com.team.project.LoginAPI

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
//import com.nhn.android.naverlogin.OAuthLogin


class Kakao : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "12d9a1fd1f3a5b9027bf344de0a37896")

      //  OAuthLogin.getInstance().init(this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "HelloWorld")

    }
}

