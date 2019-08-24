package com.lailee.eventlist.addeditEvent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lailee.eventlist.R
import com.lailee.eventlist.data.source.*
import com.lailee.eventlist.util.AppExecutors
import com.lailee.eventlist.util.replaceFragmentInActivity
import com.lailee.eventlist.util.setupActionBar

class AddEditEventActivity : AppCompatActivity() {

    private lateinit var addEditEventPresenter: AddEditEventPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addevent_act)
        val eventId = intent.getStringExtra(AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID)

        // Set up the toolbar.
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setTitle(if (eventId == null) R.string.add_event else R.string.edit_event)
        }

        val addEditEventFragment =
                supportFragmentManager.findFragmentById(R.id.contentFrame) as AddEditEventFragment?
                        ?: AddEditEventFragment.newInstance(eventId).also {
                    replaceFragmentInActivity(it, R.id.contentFrame)
                }
        val eventDb = EventDatabase.getInstance(applicationContext)
        val eventsDataSourceImpl = EventsDataSourceImpl.getInstance(AppExecutors(), eventDb.eventDao())
        val eventsRepository = EventsRepository.getInstance(eventsDataSourceImpl)
        val categoryDb = CategoryDatabase.getInstance(applicationContext)
        val categoriesDataSourceImpl = CategoriesDataSourceImpl.getInstance(AppExecutors(), categoryDb.categoryDao())
        val categoriesRepository = CategoriesRepository.getInstance(categoriesDataSourceImpl)
        addEditEventPresenter = AddEditEventPresenter(eventId, eventsRepository, categoriesRepository, addEditEventFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"
        const val REQUEST_ADD_EVENT = 1
    }
}