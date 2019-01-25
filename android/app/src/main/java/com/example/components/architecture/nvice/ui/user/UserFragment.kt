package com.example.components.architecture.nvice.ui.user


import android.app.ActivityOptions
import android.arch.lifecycle.*
import android.os.Bundle

import com.example.components.architecture.nvice.R
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserStatus
import com.example.components.architecture.nvice.scheduler.DefaultScheduler
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_user_list.*
import javax.inject.Inject
import android.arch.paging.PagedList
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.support.v7.widget.SearchView
import android.view.ViewGroup
import com.example.components.architecture.nvice.ui.user.create.UserCreateActivity
import com.example.components.architecture.nvice.ui.user.details.UserDetailsActivity
import kotlinx.android.synthetic.main.item_user.*
import kotlinx.android.synthetic.main.item_user.view.*
import org.parceler.Parcels


class UserFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: UserViewModel
    private lateinit var userPagedListAdapter: UserPagedListAdapter
    private lateinit var userStatusListAdapter: UserStatusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel::class.java)

        userPagedListAdapter = UserPagedListAdapter(context!!)
        userStatusListAdapter = UserStatusListAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initView()
        initObserver()
        initEvent()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initView() {
        rvUserList.layoutManager = LinearLayoutManager(context)
        rvUserList.adapter = userPagedListAdapter

        rvUserStatusFilters.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvUserStatusFilters.adapter = userStatusListAdapter
    }

    private fun initObserver() {
        viewModel.getUserList().observe(this, Observer { list ->
            list?.let {
                userPagedListAdapter.submitList(it)
                handleListStatus(list)
            }
        })

        viewModel.getUserStatus().observe(this, Observer { statusList ->
            statusList?.let {
                userStatusListAdapter.submitList(it)
            }
        })
    }

    private fun initEvent() {
        initUsersAdapterEvent()
        initUserStatusFiltersAdapterEvent()

        rvUserList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && btnAddUser.isShown) {
                    btnAddUser.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    DefaultScheduler.postDelayedToMainThread(500) {
                        btnAddUser.show()
                    }
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        btnFilter.setOnClickListener {
            viewModel.toggleSort()
        }

        btnAddUser.setOnClickListener {
//            startActivity(Intent(context, UserCreateActivity::class.java))
            viewModel.addUserForTest()
        }
    }

    private fun initUsersAdapterEvent() {
        initItemTouchHelper()

        userPagedListAdapter.setOnClickListener { v, user ->
            navToUserDetails(v.ivUserAvatar, user)
        }

        userPagedListAdapter.setOnDeleteUserListener { user ->
            viewModel.deleteUser(user)
        }

        userPagedListAdapter.setOnItemSwipeListener { user ->
            viewModel.deleteUser(user)
        }
    }

    private fun initUserStatusFiltersAdapterEvent() {
        userStatusListAdapter.onCheckedChangeListener = object : UserStatusListAdapter.OnCheckedChangeListener {
            override fun onCheckedChangeListener(isChecked: Boolean, userStatus: UserStatus) {
                handleFilter(isChecked, userStatus)
            }
        }
    }

    private fun initItemTouchHelper() {
        ItemTouchHelper(UserItemTouchHelperCallback(userPagedListAdapter)).attachToRecyclerView(rvUserList)
    }

    private fun handleFilter(isChecked: Boolean, status: UserStatus) {
        if (isChecked)
            viewModel.addStatusFilter(status)
        else
            viewModel.removeStatusFilter(status)
    }

    private fun handleListStatus(list: PagedList<User>) {
        vListStatus.visibility = if (list.size == 0) View.VISIBLE else View.GONE
    }

    private fun navToUserDetails(view: View, user: User) {
        val intent = Intent(context, UserDetailsActivity::class.java)
        intent.putExtra("user", Parcels.wrap(user))
        val options = ActivityOptions
                .makeSceneTransitionAnimation(activity, view, "transitionUserAvatar")
        activity?.startActivity(intent, options.toBundle())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_user_list, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val mSearchMenuItem = menu?.findItem(R.id.action_search)
        val searchView = mSearchMenuItem?.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.toolbar_search_user_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.searchUser(query?.let { it } ?: "")
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }
}
