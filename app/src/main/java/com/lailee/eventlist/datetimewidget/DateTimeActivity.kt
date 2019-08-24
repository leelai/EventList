package com.lailee.eventlist.datetimewidget

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.lailee.eventlist.R
import kotlinx.android.synthetic.main.activity_main_timepicker.*
import android.app.Activity
import android.content.Intent

class DateTimeActivity : AppCompatActivity() {

    val listFragments = arrayListOf<String>()
    val dateTimes = arrayListOf<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_timepicker)
        listFragments.add("Start")
        listFragments.add("End")
        dateTimes.add(intent.getLongExtra("start", 0))
        dateTimes.add(intent.getLongExtra("end", 0))
        val adapter = SelectTimeFragmentPagerAdapter(listFragments, dateTimes, supportFragmentManager)

        vpPager.adapter = adapter
        val tab = intent.getStringExtra("TAB")
        vpPager.currentItem = if (tab == "end") { 1 } else { 0 }
        vpPager.offscreenPageLimit = adapter.count
        tabs.setViewPager(vpPager)

        btnCancel.setOnClickListener { finish() }
        btnDone.setOnClickListener({ setSaveButton() })
    }

    fun setSaveButton() {

        val fragmentFrom = if ((supportFragmentManager.fragments[0] as FragmentDateTimePicker).typeOfFragment == "Start") {
            (supportFragmentManager.fragments[0] as FragmentDateTimePicker)
        } else {
            (supportFragmentManager.fragments[1] as FragmentDateTimePicker)
        }

        val fragmentTo = if ((supportFragmentManager.fragments[0] as FragmentDateTimePicker).typeOfFragment == "End") {
            (supportFragmentManager.fragments[0] as FragmentDateTimePicker)
        } else {
            (supportFragmentManager.fragments[1] as FragmentDateTimePicker)
        }

        val dateTimeFrom = fragmentFrom.getDateTimeOfFragment()
        val dateTimeTo = fragmentTo.getDateTimeOfFragment()

        if (dateTimeFrom.isAfter(dateTimeTo)) {

            val dialog = AlertDialog.Builder(this@DateTimeActivity)
                    .setTitle(resources.getString(R.string.error))
                    .setMessage(resources.getString(R.string.from_is_after_to))
                    .setNeutralButton(resources.getString(R.string.ok), DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
            dialog.show()
        } else {
            val intent = Intent()
            intent.putExtra("dateTimeFrom", dateTimeFrom)
            intent.putExtra("dateTimeTo", dateTimeTo)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun setViewPagerTab(int : Int) {
        vpPager.setCurrentItem(int)
    }

    class SelectTimeFragmentPagerAdapter internal constructor(private val listFragments: ArrayList<String>,
                                                              private val dateTimes: ArrayList<Long>,
                                                              private val fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager) {

        override fun getCount(): Int {
            return listFragments.size
        }

        // Returns the fragment to display for that page
        override fun getItem(position: Int): Fragment? {
            return newDateTimePickerFragment(listFragments[position], dateTimes[position])
        }

        // Returns the page title for the top indicator
        override fun getPageTitle(position: Int): CharSequence {
            return listFragments[position]
        }


    }
}
