package com.ch4vi.distilled.featureTVShows.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.ch4vi.distilled.R
import com.ch4vi.distilled.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var bindingView: ActivityMainBinding? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = bindingView?.root ?: with(ActivityMainBinding.inflate(layoutInflater)) {
            bindingView = this
            root
        }
        setContentView(view)

        bindingView?.apply {
            setSupportActionBar(toolbar)
            with(findNavHostFragment(R.id.nav_host_fragment_content_main)) {
                appBarConfiguration = AppBarConfiguration(navController.graph)
                setupActionBarWithNavController(navController, appBarConfiguration)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

fun FragmentActivity.findNavHostFragment(navId: Int): NavHostFragment {
    return supportFragmentManager.findFragmentById(navId) as NavHostFragment
}
