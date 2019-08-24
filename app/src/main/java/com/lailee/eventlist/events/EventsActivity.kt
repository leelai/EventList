package com.lailee.eventlist.events

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import com.lailee.eventlist.R
import com.lailee.eventlist.addeditEvent.AddEditEventPresenter
import com.lailee.eventlist.data.source.*
import com.lailee.eventlist.util.AppExecutors
import com.lailee.eventlist.util.replaceFragmentInActivity
import com.lailee.eventlist.util.setupActionBar

class EventsActivity : AppCompatActivity() {

    private lateinit var eventsPresenter: EventsPresenter
    private lateinit var eventsRepository: EventsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.events_act)
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_event_btn)
        }

        val eventsFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as EventsFragment? ?: EventsFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        val eventDb = EventDatabase.getInstance(applicationContext)
        val eventsDataSourceImpl = EventsDataSourceImpl.getInstance(AppExecutors(), eventDb.eventDao())
        eventsRepository = EventsRepository.getInstance(eventsDataSourceImpl)
        eventsPresenter = EventsPresenter(eventsRepository, eventsFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                for(i in 0 until 1000) {
                    eventsRepository.saveEvent(Event(i.toString(),"Personal", System.currentTimeMillis(), System.currentTimeMillis()))
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
