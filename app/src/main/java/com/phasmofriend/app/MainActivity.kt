package com.phasmofriend.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.phasmofriend.app.ui.navigation.PhasmoApp
import com.phasmofriend.app.ui.theme.PhasmoFriendTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //
        // 🚀 Install system SplashScreen BEFORE super.onCreate()
        //
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        //
        // 🎬 Animate exit → launcher icon flies upward
        //
        splashScreen.setOnExitAnimationListener { splashView ->
            val iconView = splashView.iconView
            val screenHeight = splashView.view.height.toFloat()

            iconView.animate()
                .translationY(-screenHeight) // fly up off top
                .setDuration(550L)
                .withEndAction {
                    splashView.remove()
                }
                .start()
        }

        setContent {
            PhasmoFriendTheme {
                PhasmoApp()
            }
        }
    }
}
