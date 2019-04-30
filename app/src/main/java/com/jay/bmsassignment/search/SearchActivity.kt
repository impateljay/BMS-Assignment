package com.jay.bmsassignment.search

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.jay.bmsassignment.R
import com.jay.bmsassignment.model.Status
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity(), SearchView {

    private lateinit var searchPresenter: SearchPresenter
    private var adapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.hide()

        searchPresenter = SearchPresenter()
        onAttach()
        adapter = SearchAdapter(this@SearchActivity, mutableListOf<Status>() as java.util.ArrayList<Status>)

        recycler_view.layoutManager = LinearLayoutManager(this@SearchActivity)
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.adapter = adapter

        searchViewPadding(20, ((100 * resources.displayMetrics.density + 0.5f).toInt()), 20, 8)
        floating_search_view.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
            }

            override fun onSearchAction(currentQuery: String) {
                searchPresenter.getTweets(currentQuery, resources)
            }
        })

        sort.setOnClickListener {
            val popup = PopupMenu(this@SearchActivity, sort)
            popup.menuInflater.inflate(R.menu.sort_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                item.title?.toString()?.let { it1 ->
                    adapter?.getTweets()?.let { it2 ->
                        searchPresenter.sortTweets(
                            it1,
                            it2,
                            item.itemId
                        )
                    }
                }
                true
            }
            popup.show()
        }
    }

    override fun onAttach() {
        searchPresenter.onAttach(this)
    }

    override fun onDetach() {
        searchPresenter.onDetach()
    }

    override fun showToast(message: String?, length: Int) {
        Toast.makeText(this@SearchActivity, message, length).show()
    }

    override fun changeProgressBarVisibility(visibility: Int) {
        progress_bar.visibility = visibility
    }

    override fun changeRecyclerViewVisibility(visibility: Int) {
        recycler_view.visibility = visibility
    }

    override fun changeNoRecordsVisibility(visibility: Int) {
        textview_instruction.visibility = visibility
    }

    override fun searchViewPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int) {
        floating_search_view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    override fun sortImageViewPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int) {
        sort.visibility = View.VISIBLE
        sort.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    override fun addTweets(newTweets: List<Status>?) {
        adapter?.addTweets(newTweets as ArrayList<Status>)
        adapter?.notifyDataSetChanged()
    }
}