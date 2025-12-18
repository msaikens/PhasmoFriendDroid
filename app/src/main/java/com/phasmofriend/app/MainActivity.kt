package com.phasmofriend.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.phasmofriend.app.databinding.ActivityPhasmoFriendBinding
import com.phasmofriend.app.ui.home.EvidenceFragment
import com.phasmofriend.app.ui.notifications.BehaviorFragment
import com.phasmofriend.app.ui.shared.InvestigationViewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhasmoFriendBinding
    private val investigationViewModel: InvestigationViewModel by viewModels()

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

        //
        // 📌 Inflate main UI
        //
        binding = ActivityPhasmoFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        // 🔽 Bottom Navigation Setup
        //
        val navView: BottomNavigationView = binding.bottomNav

        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment
            ) as NavHostFragment

        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(navView, navController)

        //
        // 🧹 Intercept "Clear All" menu item
        //
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_clear_all -> {
                    showClearAllDialog()
                    false // Don't switch tabs when clearing
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                    true
                }
            }
        }
    }

    //
    // 🧼 Clear All Dialog
    //
    private fun showClearAllDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.title_clear_all)
            .setMessage(R.string.clear_all_message)
            .setPositiveButton(R.string.clear_all_yes) { _, _ ->
                clearAllSelections()
            }
            .setNegativeButton(R.string.clear_all_no, null)
            .show()
    }

    //
    // 🧽 Clear evidence + behavior selections in ViewModel + UI fragments
    //
    private fun clearAllSelections() {
        // Reset ViewModel
        investigationViewModel.clearAll()

        // Reset UI elements in currently visible fragments
        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment
            ) as? NavHostFragment ?: return

        navHostFragment.childFragmentManager.fragments.forEach { fragment ->
            when (fragment) {
                is EvidenceFragment -> fragment.clearSelections()
                is BehaviorFragment -> fragment.clearSelections()
            }
        }
    }
}
