package com.example.konturtest.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.konturtest.R
import com.example.konturtest.screen.contacts.ContactListFragment
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
                    as? ContactListFragment ?: ContactListFragment.newInstance().also {
                replaceFragmentInActivity(it, R.id.container, false)
            }
        }

    }


/*    private fun isLastEventWasMoreThan15SecondsAgo(eventsResponse: EventsResponse): Boolean {
        val lastEvent = eventsResponse.eventLog?.lastOrNull() ?: return false
        val lastTimeStamp = lastEvent.timeStamp
        val currentTimeStamp = System.currentTimeMillis() / 1000
        return (currentTimeStamp - lastTimeStamp > 15)

    }*/

}
