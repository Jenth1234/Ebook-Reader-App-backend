package com.app.application.comic_app

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.application.comic_app.databinding.ActivityMainBinding
import com.app.application.comic_app.fragment.FavoriteFragment
import com.app.application.comic_app.fragment.HistoryFragment
import com.app.application.comic_app.fragment.HomeFragment
import com.app.application.comic_app.fragment.SettingFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewPager: HomeViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    private fun init() {
        homeViewPager = HomeViewPager()

        binding.apply {
            layoutContainer.apply {
                adapter = homeViewPager
                isUserInputEnabled = false
            }

            bottomSheetBar.setOnItemSelectedListener { p0 ->
                val idItem = p0.itemId
                layoutContainer.currentItem =
                    when (idItem) {
                        R.id.itemHome -> INDEX_TAB_HOME
                        R.id.itemFavorite -> INDEX_TAB_FAVORITE
                        R.id.itemSetting -> INDEX_TAB_SETTING
                        else -> INDEX_TAB_HISTORY
                    }

                true
            }
        }
    }

    inner class HomeViewPager: FragmentStateAdapter(this) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment =
            when (position) {
                0 -> HomeFragment()
                1 -> FavoriteFragment()
                2 -> SettingFragment()
                else -> HistoryFragment()
            }
    }

    companion object {
        const val INDEX_TAB_HOME = 0
        const val INDEX_TAB_FAVORITE = 1
        const val INDEX_TAB_SETTING = 2
        const val INDEX_TAB_HISTORY = 3
    }
}