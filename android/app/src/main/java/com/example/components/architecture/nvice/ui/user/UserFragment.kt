package com.example.components.architecture.nvice.ui.user


import android.app.ActivityOptions
import androidx.lifecycle.*
import android.os.Bundle

import com.example.components.architecture.nvice.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.components.architecture.nvice.model.user.User
import com.example.components.architecture.nvice.model.user.UserStatus
import com.example.components.architecture.nvice.util.scheduler.DefaultScheduler
import kotlinx.android.synthetic.main.fragment_user_list.*
import javax.inject.Inject
import androidx.paging.PagedList
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.*
import androidx.appcompat.widget.SearchView
import android.view.ViewGroup
import com.example.components.architecture.nvice.BaseFragment
import com.example.components.architecture.nvice.ui.user.profile.create.UserCreateActivity
import com.example.components.architecture.nvice.ui.user.details.UserDetailsActivity
import com.example.components.architecture.nvice.widget.alert.CustomAlertDialog
import kotlinx.android.synthetic.main.item_user.view.*


class UserFragment : BaseFragment() {

    companion object {
        fun newInstance() = UserFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: UserViewModel
    private lateinit var userPagedListAdapter: UserPagedListAdapter
    private lateinit var userStatusListAdapter: UserStatusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserViewModel::class.java)

        userPagedListAdapter = UserPagedListAdapter()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_user_list, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val mSearchMenuItem = menu.findItem(R.id.action_search)
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

        viewModel.getUserList().observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                userPagedListAdapter.submitList(it)
                handleListStatus(list)
            }
        })

        viewModel.getUserStatus().observe(viewLifecycleOwner, Observer { statusList ->
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
                    DefaultScheduler.postDelayedToMainThread(300) {
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
            navToCreateUser()
        }
    }

    private fun initUsersAdapterEvent() {
        initItemTouchHelper()

        userPagedListAdapter.setOnClickListener { v, user ->
            navToUserDetails(v.ivUserAvatar, user)
        }

        userPagedListAdapter.setOnDeleteListener { user ->
            CustomAlertDialog.with(context)
                    ?.setTitleText(getString(R.string.alert_title_delete_staff))
                    ?.setSupportingText(getString(R.string.alert_support_text_delete_staff, user.getFullName()))
                    ?.setPositiveButton(getString(R.string.action_delete)) {
                        viewModel.deleteUser(user)
                    }
                    ?.setNegativeButton {
                        user.id?.let {
                            userPagedListAdapter.notifyItemChanged(it - 1)
                        }
                    }
                    ?.show()
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
        if (isChecked) {
            viewModel.addStatusFilter(status)
        } else {
            viewModel.removeStatusFilter(status)
        }
    }

    private fun handleListStatus(list: PagedList<User>) {
        vListStatus.visibility = if (list.size == 0) View.VISIBLE else View.GONE
    }

    private fun navToUserDetails(view: View, user: User) {
        val intent = Intent(context, UserDetailsActivity::class.java)
        intent.putExtra("userId", user.id)
        val options = ActivityOptions
                .makeSceneTransitionAnimation(activity, view, "transitionUserAvatar")
        activity?.startActivity(intent, options.toBundle())
    }

    private fun navToCreateUser() {
        startActivity(Intent(context, UserCreateActivity::class.java))
    }
}