package com.lailee.eventlist.addeditEvent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.lailee.eventlist.R
import com.lailee.eventlist.datetimewidget.DateTimeActivity

import com.lailee.eventlist.util.showSnackBar
import org.joda.time.DateTime
import android.os.Handler
import org.joda.time.format.DateTimeFormat
import pyxis.uzuki.live.actionsheet.ActionSheet
import pyxis.uzuki.live.actionsheet.config.ActionSheetConfig
import pyxis.uzuki.live.actionsheet.listener.OnActionButtonClickListener
import pyxis.uzuki.live.actionsheet.listener.OnDismissListener


class AddEditEventFragment : Fragment(), AddEditEventContract.View, View.OnClickListener {

    override lateinit var presenter: AddEditEventContract.Presenter

    private lateinit var title: TextView
    private lateinit var categoryTv: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var description: TextView

    private var startTimeMilliSec: Long = 0
    private var endTimeMilliSec: Long = 0
    var fmt = DateTimeFormat.forPattern("MMM dd,yyyy HH:mm")

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_event_done)?.apply {
            setOnClickListener {
                presenter.saveEvent(title.text.toString(), categoryTv.text.toString(), startTimeMilliSec, endTimeMilliSec, description.text.toString())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.addevent_frag, container, false)
        with(root) {
            title = findViewById(R.id.add_evnet_title)
            findViewById<TextView>(R.id.add_event_category).apply {
                categoryTv = this
                categoryTv.text = DEFAULT_CATEGORY
                categoryTv.setOnClickListener(this@AddEditEventFragment)
            }
            description = findViewById(R.id.add_event_description)
            findViewById<TextView>(R.id.add_event_start_time).apply{
                startTime = this
                startTime.setOnClickListener(this@AddEditEventFragment)
            }
            findViewById<TextView>(R.id.add_event_end_time).apply{
                endTime = this
                endTime.setOnClickListener(this@AddEditEventFragment)
            }
        }
        setStartTime(System.currentTimeMillis())
        setEndTime(System.currentTimeMillis())
        setHasOptionsMenu(true)
        return root
    }

    override fun onClick(v: View?) {
        when(v) {
            categoryTv ->
                presenter.selectCategory()
            startTime -> {
                showDateTimePicker("start")
            }
            endTime -> {
                showDateTimePicker("end")
            }
        }
    }

    private fun showDateTimePicker(title: String) {
        var intent = Intent(context, DateTimeActivity::class.java)
        intent.putExtra("TAB", title)
        intent.putExtra("start", startTimeMilliSec)
        intent.putExtra("end", endTimeMilliSec)
        startActivityForResult(intent, PICK_DATE_TIME_RANGE)
    }

    override fun showEmptyEventError() {
        title.showSnackBar(getString(R.string.empty_event_message), Snackbar.LENGTH_LONG)
    }

    override fun setStartTime(time: Long) {
        startTimeMilliSec = time
        startTime.text = DateTime(startTimeMilliSec).toLocalDateTime().toString(fmt)
    }

    override fun setEndTime(time: Long) {
        endTimeMilliSec = time
        endTime.text = DateTime(endTimeMilliSec).toLocalDateTime().toString(fmt)
    }

    override fun setCategory(category: String) {
        categoryTv.text = category
    }

    override fun showEventsList() {
        activity?.apply {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun setTitle(title: String) {
        this.title.text = title
    }

    override fun setDescription(description: String) {
        this.description.text = description
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_DATE_TIME_RANGE) {
            if (resultCode == Activity.RESULT_OK) {

                Handler().post(Runnable {
                    val dateTimeFrom = data!!.getSerializableExtra("dateTimeFrom") as DateTime
                    val dateTimeTo = data.getSerializableExtra("dateTimeTo") as DateTime
                    startTimeMilliSec = dateTimeFrom.millis
                    endTimeMilliSec = dateTimeTo.millis

                    Log.d(TAG, "dateTimeFrom:" + dateTimeFrom.toLocalDateTime().toString(fmt))
                    Log.d(TAG, "dateTimeTo:" + dateTimeTo.toLocalDateTime().toString(fmt))

                    var newStartTime = dateTimeFrom.toLocalDateTime().toString(fmt)//sdf.format(Date(startTimeMilliSec))
                    var newEndTime = dateTimeTo.toLocalDateTime().toString(fmt)//sdf.format(Date(endTimeMilliSec))
                    startTime.text = newStartTime
                    endTime.text = newEndTime
                })
            }
        } else if (requestCode == CREATE_CATEGORY) {
            if (resultCode == Activity.RESULT_OK) {
                Handler().post(Runnable {
                    val category = data!!.getStringExtra("category")
                    categoryTv.text = category
                })
            }
        }
    }

    override fun showCategories(categories: List<String>) {
        val config = ActionSheetConfig.Builder()
            .setCancelableOnTouchOutside(true)
            .setCancelButton("Cancel")
            .setOnDismissListener(OnDismissListener { actionSheet, isCancel ->

            })
            .setOnActionButtonClickListener(OnActionButtonClickListener { actionSheet, actionButton, index ->
                if (actionButton.title == "add...") {
                    val intent = Intent(context, AddCategoryActivity::class.java)
                    startActivityForResult(intent, CREATE_CATEGORY)
                } else {
                    categoryTv.text = actionButton.title
                }
            })
        categories.forEach {
            config.addItem(it)
        }

        if (categories.size < 8)
            config.addItem("add...")

        ActionSheet.show(this.context!!, fragmentManager!!, config.build())
    }

    companion object {

        private const val TAG = "AddEditEventFragment"
        const val ARGUMENT_EDIT_EVENT_ID = "EDIT_EVENT_ID"
        const val PICK_DATE_TIME_RANGE = 1
        const val CREATE_CATEGORY = 2
        private const val DEFAULT_CATEGORY = "Personal"
        fun newInstance(eventId: String?) =
                AddEditEventFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARGUMENT_EDIT_EVENT_ID, eventId)
                    }
                }
    }
}
