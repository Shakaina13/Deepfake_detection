package com.example.deepfake_detection

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.deepfake_detection.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
         binding.btnImg.setOnClickListener {
             val intent = Intent(this@MainActivity2, ImageActivity::class.java)
             startActivity(intent)
         }

    }
}