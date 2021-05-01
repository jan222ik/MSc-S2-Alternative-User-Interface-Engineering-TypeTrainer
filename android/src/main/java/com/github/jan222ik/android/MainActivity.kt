package com.github.jan222ik.android

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.github.jan222ik.common.ui.router.MobileRoutes
import com.github.jan222ik.common.ui.util.router.Router

class MainActivity : AppCompatActivity() {
    var router: Router<MobileRoutes>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContent {
            mobileMain(this) { router = it }
        }
    }

    override fun onBackPressed() {
        println("onBackPressed")
        router?.back()
    }
}
