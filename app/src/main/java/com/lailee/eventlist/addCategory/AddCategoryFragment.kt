package com.lailee.eventlist.addeditEvent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.lailee.eventlist.R
import com.lailee.eventlist.util.showSnackBar

class AddCategoryFragment : Fragment(), AddCategoryContract.View {

    override lateinit var presenter: AddCategoryContract.Presenter
    private lateinit var categoryTv: TextView

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_event_done)?.apply {
            setOnClickListener {
                presenter.saveCategory(categoryTv.text.toString())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.addcategory_frag, container, false)
        with(root) {
            findViewById<TextView>(R.id.add_event_category).apply {
                categoryTv = this

                setOnEditorActionListener { v, actionId, event ->
                    return@setOnEditorActionListener when (actionId) {
                        EditorInfo.IME_ACTION_DONE -> {
                            presenter.saveCategory(categoryTv.text.toString())
                            true
                        }
                        else -> false
                    }
                }
            }
        }
        setHasOptionsMenu(true)
        return root
    }

    override fun successfullySaved(category: String) {
        activity?.apply {
            val intent = Intent()
            intent.putExtra("category", category)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun showEmptyEventError() {
        categoryTv.showSnackBar(getString(R.string.empty_event_message), Snackbar.LENGTH_LONG)
    }

    override fun showCategories(categories: List<String>) {
    }

    companion object {

        private const val TAG = "AddCategoryFragment"
        fun newInstance() = AddCategoryFragment()
    }
}
