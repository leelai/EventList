package com.lailee.eventlist.events


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.lailee.eventlist.R
import com.lailee.eventlist.addeditEvent.AddEditEventActivity
import com.lailee.eventlist.data.source.Event
import com.lailee.eventlist.eventDetail.EventDetailActivity
import com.lailee.eventlist.events.helper.ItemTouchHelperAdapter
import com.lailee.eventlist.events.helper.ItemTouchHelperViewHolder
import com.lailee.eventlist.events.helper.OnStartDragListener
import com.lailee.eventlist.events.helper.SimpleItemTouchHelperCallback
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class EventsFragment : Fragment(), EventsContract.View {

    companion object {
        private const val TAG = "EventsFragment"
        fun newInstance() = EventsFragment()
    }

    override lateinit var presenter: EventsContract.Presenter

    private var itemListener2 : OnStartDragListener = object : OnStartDragListener {
        override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
            mItemTouchHelper.startDrag(viewHolder!!)
        }

        override fun onItemMove(from: Int, to: Int) {
            presenter.swapEvent(from, to)
        }

        override fun onItemSelected(event: Event) {
            presenter.openEventDetails(event)
        }

        override fun onItemDismiss(eventId: String) {
            presenter.deleteEvent(eventId)
        }
    }

    private val listAdapter2 = RecyclerListAdapter(ArrayList(0), itemListener2)
    private lateinit var listView2: RecyclerView
    private lateinit var mItemTouchHelper: ItemTouchHelper

    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.events_frag, container, false)
        with(rootView) {
            listView2 = findViewById<RecyclerView>(R.id.events_list2).apply {
                setHasFixedSize(true)
                adapter = listAdapter2
                layoutManager = LinearLayoutManager(activity)
            }

            val callback = SimpleItemTouchHelperCallback(listAdapter2)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper.attachToRecyclerView(listView2)
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener { presenter.addNewEvent() }
        }
        return rootView
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (active) {
            toast = Toast.makeText(context, "loading", Toast.LENGTH_SHORT)
        } else {
            if (toast != null) {
                toast!!.cancel()
            }
        }
    }

    override fun showEvents(evets: List<Event>) {
        //listAdapter.events = evets
        listAdapter2.mItems = ArrayList(evets)
    }

    override fun showAddEvent() {
        val intent = Intent(context, AddEditEventActivity::class.java)
        startActivityForResult(intent, AddEditEventActivity.REQUEST_ADD_EVENT)
    }

    override fun showEventDetailsUi(eventId: String) {
        val intent = Intent(context, EventDetailActivity::class.java).apply {
            putExtra(EventDetailActivity.EXTRA_TASK_ID, eventId)
        }
        startActivity(intent)
    }

    override fun showNoEvents() {
        listAdapter2.mItems = ArrayList(0)
    }

    override fun showSuccessfullySavedMessage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    private class RecyclerListAdapter(events: ArrayList<Event>, private val mDragStartListener: OnStartDragListener) :
        RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>(), ItemTouchHelperAdapter {

        var mItems: ArrayList<Event> = events
            set(tasks) {
                field = tasks
                Log.d(TAG, "call notifyDataSetChanged")
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            with(holder.textView) {
                text = mItems[position].title
                setOnClickListener( object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            mDragStartListener.onItemSelected(mItems[position])
                        }
                    }
                )
            }
            // Start a drag whenever the handle view it touched
            holder.handleView.setOnTouchListener { v, event ->
                if (MotionEventCompat.getActionMasked(event) === MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder)
                }
                false
            }
        }

        override fun onItemDismiss(position: Int) {
            mDragStartListener.onItemDismiss(mItems[position].id)
            mItems.removeAt(position)
            notifyItemRemoved(position)
        }

        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            mDragStartListener.onItemMove(fromPosition, toPosition)
            Collections.swap(mItems, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun getItemCount(): Int {
            return mItems.size
        }

        /**
         * Simple example of a view holder that implements [ItemTouchHelperViewHolder] and has a
         * "handle" view that initiates a drag event when touched.
         */
        class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {

            val textView: TextView = itemView.findViewById(R.id.event_title)
            val handleView: ImageView = itemView.findViewById(R.id.evnet_handle)

            override fun onItemSelected() {
                Log.d(TAG, "onItemSelected")
                itemView.setBackgroundColor(Color.LTGRAY)
            }

            override fun onItemClear() {
                itemView.setBackgroundColor(0)
            }
        }
    }
}
