package com.example.konturtest.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.konturtest.R
import com.example.konturtest.screen.contacts.ContactsFragment
import replaceFragmentInActivity

/**
 * Created by Vladimir Kraev
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.findFragmentById(R.id.container)
                    as? ContactsFragment ?: ContactsFragment.newInstance().also {
                replaceFragmentInActivity(it, R.id.container, false)
            }
        }

    }
}
