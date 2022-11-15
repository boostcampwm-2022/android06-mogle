package com.wakeup.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wakeup.presentation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.menu.getItem(1).isEnabled = false
    }
}