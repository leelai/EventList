package com.lailee.eventlist.eventDetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.lailee.eventlist.R
import com.lailee.eventlist.addeditEvent.AddEditEventActivity
import com.lailee.eventlist.addeditEvent.AddEditEventFragment
import kotlinx.android.synthetic.main.eventdetail_frag.*
import kotlin.text.category

/**
 * Main UI for the task detail screen.
 */
class EventDetailFragment : Fragment(), EventDetailContract.View {

    private lateinit var detailTitle: TextView
    private lateinit var detailDescription: TextView
    private lateinit var category: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView

    override lateinit var presenter: EventDetailContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.eventdetail_frag, container, false)
        setHasOptionsMenu(true)
        with(root) {
            detailTitle = findViewById(R.id.event_detail_title)
            detailDescription = findViewById(R.id.event_detail_description)
            category = findViewById(R.id.category)
            startTime = findViewById(R.id.start_time)
            endTime = findViewById(R.id.end_time)
        }

        // Set up floating action button
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_event)?.setOnClickListener {
            presenter.editEvent()
        }

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val deletePressed = item.itemId == R.id.menu_delete
        if (deletePressed) presenter.deleteEvent()
        return deletePressed
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.eventdetail_fragment_menu, menu)
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (active) {
            detailTitle.text = ""
            detailDescription.text = getString(R.string.loading)
        }
    }

    override fun hideDescription() {
        detailDescription.visibility = View.GONE
    }

    override fun hideTitle() {
        detailTitle.visibility = View.GONE
    }

    override fun showDescription(description: String) {
        with(detailDescription) {
            visibility = View.VISIBLE
            text = description
        }
    }

    override fun showEditEvent(eventId: String) {
        Log.d(TAG, "showEditEvent eventId:$eventId")
        val intent = Intent(context, AddEditEventActivity::class.java)
        intent.putExtra(AddEditEventFragment.ARGUMENT_EDIT_EVENT_ID, eventId)
        startActivityForResult(intent, REQUEST_EDIT_EVENT)
    }

    override fun showEventDeleted() {
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_EDIT_EVENT) {
            if (resultCode == Activity.RESULT_OK) {
                activity?.finish()
            }
        }
    }

    override fun showTitle(title: String) {
        with(detailTitle) {
            visibility = View.VISIBLE
            text = title
        }
    }

    override fun showCategory(category: String) {
        this.category.text = category
    }

    override fun showStartTime(startTime: String) {
        this.startTime.text = startTime
    }

    override fun showEndTime(endTime: String) {
        this.endTime.text = endTime
    }

    override fun showMissingEvent() {
        detailTitle.text = ""
        detailDescription.text = getString(R.string.no_data)
    }

    companion object {

        private const val TAG = "EventDetailFragment"
        private const val ARGUMENT_EVENT_ID = "TASK_ID"
        private const val REQUEST_EDIT_EVENT = 1

        fun newInstance(taskId: String?) =
                EventDetailFragment().apply {
                    arguments = Bundle().apply { putString(ARGUMENT_EVENT_ID, taskId) }
                }
    }

}
