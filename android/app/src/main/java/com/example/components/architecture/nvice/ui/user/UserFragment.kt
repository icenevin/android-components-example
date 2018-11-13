package com.example.components.architecture.nvice.ui.user


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
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*


class UserFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: UserViewModel
    private lateinit var userPagedListAdapter: UserPagedListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel::class.java)

        userPagedListAdapter = UserPagedListAdapter(context!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initObserver()
        initEvent()
    }

    private fun initView() {
        rvUserList.layoutManager = LinearLayoutManager(context)
        rvUserList.adapter = userPagedListAdapter
    }

    private fun initObserver() {
        viewModel.getUserList().observe(this, Observer { list ->
            list?.let {
                userPagedListAdapter.submitList(it)
                handleListStatus(list)
            }
        })

        viewModel.getUserStatus().observe(this, Observer { statusList ->
            statusList?.let{

            }
        })
    }

    private fun initEvent() {
        initAdapter()
        initFilters()
        initItemTouchHelper()

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
            viewModel.addUserForTest()
        }
    }

    private fun initAdapter() {
        userPagedListAdapter.setOnClickListener { user ->
            navToUserDetails(user)
        }

        userPagedListAdapter.setOnDeleteUserListener { user ->
            viewModel.deleteUser(user)
        }

        userPagedListAdapter.setOnItemSwipeListener { user ->
            viewModel.deleteUser(user)
        }
    }

    private fun initFilters() {
        cFilterPermanent.setOnCheckedChangeListener { _, isChecked ->
            handleFilter(isChecked, UserStatus.PERMANENT)
        }

        cFilterTemporary.setOnCheckedChangeListener { _, isChecked ->
            handleFilter(isChecked, UserStatus.TEMPORARY)
        }

        cFilterUnknown.setOnCheckedChangeListener { _, isChecked ->
            handleFilter(isChecked, UserStatus.UNDEFINED)
        }
    }

    private fun initItemTouchHelper() {
        ItemTouchHelper(UserItemTouchHelperCallback(userPagedListAdapter)).attachToRecyclerView(rvUserList)
    }

    private fun handleFilter(condition: Boolean, status: UserStatus) {
        if (condition)
            viewModel.addStatusFilter(status)
        else
            viewModel.removeStatusFilter(status)
    }

    private fun handleListStatus(list: PagedList<User>) {
        vListStatus.visibility = if (list.size == 0) View.VISIBLE else View.GONE
    }

    private fun navToUserDetails(user: User) {
        // navigate to UserDetails Fragment
    }
}
