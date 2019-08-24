package com.lailee.eventlist.datetimewidget

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lailee.eventlist.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_date_time.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*


/**
 * Created by kristywelsh on 2/20/18.
 */
class FragmentDateTimePicker : Fragment() {
    var typeOfFragment = ""
    var selectedDate : Long = 0
    var selectedCalendarDate : CalendarDay? = null
    private val dataAmPm = arrayListOf<String>()
    private val dataHours = ArrayList<String>()
    private val dataMinutes = ArrayList<String>()
    var hours = 0
    var minutes = 0
    var amPm = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        arguments?.let { a ->
            typeOfFragment = a.getString(FRAGMENT_ARG_TYPE_OF_FRAG)
            selectedDate = a.getLong(FRAGMENT_ARG_TIME_OF_FRAG)
        }
        return inflater.inflate(R.layout.fragment_date_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (DateFormat.is24HourFormat(context)) {
            for (i in 0..23) {
                if (i<10) {
                    dataHours.add("0" + i.toString())
                } else {
                    dataHours.add(i.toString())
                }
                am_pm.visibility = View.GONE
            }

        } else {
            for (i in 1..12) {
                if (i < 9) {
                    dataHours.add("0" + i.toString())
                } else {
                    dataHours.add(i.toString())
                }
            }
        }
        hour.data = dataHours

        for (i in 0..59) {
            if (i<10) {
                dataMinutes.add("0" + i.toString())
            } else {
                dataMinutes.add(i.toString())
            }
        }
        minute.data = dataMinutes
        dataAmPm.add("AM")
        dataAmPm.add("PM")
        am_pm.data = dataAmPm

        calendarView.setOnDateChangedListener(OnDateSelectedListener({ widget, calendarDay, selected ->
                selectedCalendarDate = widget.selectedDate
//                val hours = hour.currentItemPosition
//                val minutes = minute.currentItemPosition
//                val amPM = dataAmPm.get(am_pm.currentItemPosition)
//                Toast.makeText(widget.context, "Year=${calendarDay.year} Month=${calendarDay.month + 1} Day=${calendarDay.day}", Toast.LENGTH_LONG).show()
        }))

        hour.setOnItemSelectedListener { picker, data, position ->
            var hoursString = ""
            if (DateFormat.is24HourFormat(context)) {
                if (position < 9) {
                    hoursString = dataHours.get(position).replace("0","")
                }
                else hoursString = dataHours.get(position)
            } else {
                hoursString = dataHours.get(position)
                if (hoursString != "10") {
                    hoursString = hoursString.replace("0", "")
                }
                else hoursString = dataHours.get(position)
            }
            if (!hoursString.isEmpty()) {
                hours = hoursString.toInt()
            } else {
                hours = 0
            }
        }
        minute.setOnItemSelectedListener { picker, data, position ->
            var minutesString = ""
            if (position < 9) {
                minutesString = dataMinutes.get(position).replace("0","")
            }
            else minutesString = dataMinutes.get(position).replace("0","")

            if (!minutesString.isEmpty()) {
                minutes = minutesString.toInt()
            } else {
                minutes = 0
            }
        }
        am_pm.setOnItemSelectedListener { picker, data, position ->
            amPm = dataAmPm.get(position)
        }
        if (selectedDate == 0L) {
            setDateToCurrentDateTime()
        } else {
            setDateTime()
        }
    }

    private fun setDateToCurrentDateTime() {
        val dateTime = DateTime()
        selectedCalendarDate = CalendarDay.today()
        calendarView.selectedDate = CalendarDay.today()
        minute.selectedItemPosition = dateTime.minuteOfHour
        minutes = dateTime.minuteOfHour
        hours = dateTime.hourOfDay

        if (!DateFormat.is24HourFormat(context) ) {
            if (dateTime.hourOfDay == 0) {
                hour.setSelectedItemPosition(11)
                am_pm.setSelectedItemPosition(0)

            } else if (dateTime.hourOfDay > 0 && dateTime.hourOfDay < 13) {
                am_pm.setSelectedItemPosition(0)
                hour.selectedItemPosition = dateTime.hourOfDay - 1
            } else {
                am_pm.selectedItemPosition = 1
                hour.selectedItemPosition = dateTime.hourOfDay - 13
            }

        } else {
            hour.selectedItemPosition = dateTime.hourOfDay
        }
    }

    private fun setDateTime() {
        val dateTime = DateTime(selectedDate)
        selectedCalendarDate = CalendarDay.from(dateTime.toDate())
        calendarView.selectedDate = CalendarDay.from(dateTime.toDate())
        minute.selectedItemPosition = dateTime.minuteOfHour
        minutes = dateTime.minuteOfHour
        hours = dateTime.hourOfDay

        if (!DateFormat.is24HourFormat(context) ) {
            if (dateTime.hourOfDay == 0) {
                hour.setSelectedItemPosition(11)
                am_pm.setSelectedItemPosition(0)

            } else if (dateTime.hourOfDay > 0 && dateTime.hourOfDay < 13) {
                am_pm.setSelectedItemPosition(0)
                hour.selectedItemPosition = dateTime.hourOfDay - 1
            } else {
                am_pm.selectedItemPosition = 1
                hour.selectedItemPosition = dateTime.hourOfDay - 13
            }

        } else {
            hour.selectedItemPosition = dateTime.hourOfDay
        }
    }

    fun getDateTimeOfFragment() : DateTime {
        val calendarDate = selectedCalendarDate as CalendarDay
        var date = DateTime(calendarDate.year,calendarDate.month + 1,calendarDate.day,0,0)
        if (!DateFormat.is24HourFormat(context) && amPm == "AM") {
            if (hours == 12) {
                hours = 0
            }
        } else if (!DateFormat.is24HourFormat(context) && amPm == "PM") {
            if (hours != 12 ) {
                hours += 12
            }
        }
        return date.plusHours(hours).plusMinutes(minutes)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { a ->
            typeOfFragment = a.getString(FRAGMENT_ARG_TYPE_OF_FRAG) ?: throw IllegalArgumentException("Type Of Fragment Required")
        }
    }

    companion object {
        val FRAGMENT_ARG_TYPE_OF_FRAG = "typeOfFrag"
        val FRAGMENT_ARG_TIME_OF_FRAG = "timeOfFrag"
        val DATE_FORMATTER_24_HR = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
        var DATE_FORMATTER_12_HR = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm a")

    }


}

fun newDateTimePickerFragment(type: String, time: Long): FragmentDateTimePicker {
    val myFragment = FragmentDateTimePicker()
    val args = Bundle()
    args.putString(FragmentDateTimePicker.FRAGMENT_ARG_TYPE_OF_FRAG, type)
    args.putLong(FragmentDateTimePicker.FRAGMENT_ARG_TIME_OF_FRAG, time)
    myFragment.arguments = args

    return myFragment
}

