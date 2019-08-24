package com.lailee.eventlist.eventDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lailee.eventlist.R
import com.lailee.eventlist.data.source.*
import com.lailee.eventlist.util.AppExecutors
import com.lailee.eventlist.util.replaceFragmentInActivity
import com.lailee.eventlist.util.setupActionBar

/**
 * Displays task details screen.
 */
class EventDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.eventdetail_act)

        // Set up the toolbar.
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.event_detail)
        }

        // Get the requested task id
        val taskId = intent.getStringExtra(EXTRA_TASK_ID)

        val taskDetailFragment = supportFragmentManager
                .findFragmentById(R.id.contentFrame) as EventDetailFragment? ?:
                EventDetailFragment.newInstance(taskId).also {
                    replaceFragmentInActivity(it, R.id.contentFrame)
                }

        // Create the presenter
        val eventDb = EventDatabase.getInstance(applicationContext)
        val eventsDataSourceImpl = EventsDataSourceImpl.getInstance(AppExecutors(), eventDb.eventDao())
        val categoryDb = CategoryDatabase.getInstance(applicationContext)
        val categoriesDataSourceImpl = CategoriesDataSourceImpl.getInstance(AppExecutors(), categoryDb.categoryDao())
        val eventsRepository = EventsRepository.getInstance(eventsDataSourceImpl)
        EventDetailPresenter(taskId, eventsRepository, taskDetailFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_TASK_ID = "TASK_ID"
    }
}
