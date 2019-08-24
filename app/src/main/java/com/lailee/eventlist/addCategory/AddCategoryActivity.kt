package com.lailee.eventlist.addeditEvent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lailee.eventlist.R
import com.lailee.eventlist.data.source.*
import com.lailee.eventlist.util.AppExecutors
import com.lailee.eventlist.util.replaceFragmentInActivity
import com.lailee.eventlist.util.setupActionBar

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var addCategoryPresenter: AddCategoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addcategory_act)

        // Set up the toolbar.
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.add_category)
        }

        val addEditEventFragment =
                supportFragmentManager.findFragmentById(R.id.contentFrame) as AddCategoryFragment?
                        ?: AddCategoryFragment.newInstance().also {
                    replaceFragmentInActivity(it, R.id.contentFrame)
                }
        val categoryDb = CategoryDatabase.getInstance(applicationContext)
        val categoriesDataSourceImpl = CategoriesDataSourceImpl.getInstance(AppExecutors(), categoryDb.categoryDao())
        val categoriesRepository = CategoriesRepository.getInstance(categoriesDataSourceImpl)
        addCategoryPresenter = AddCategoryPresenter(categoriesRepository, addEditEventFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}