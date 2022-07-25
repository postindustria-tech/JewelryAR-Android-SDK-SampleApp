package com.postindustria.android.jewelryar.sample.kotlin.singleactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.ar_fragment,
//                    JavaHomeFragment.newInstance()
                    KotlinHomeFragment.newInstance()
                )
                .commit()
        }
    }
}