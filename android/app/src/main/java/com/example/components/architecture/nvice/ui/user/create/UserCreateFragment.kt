package com.example.components.architecture.nvice.ui.user.create


import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*

import com.example.components.architecture.nvice.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_user_create.*
import javax.inject.Inject


class UserCreateFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: UserCreateViewModel

    companion object {
        fun getInstance(): UserCreateFragment {
            return UserCreateFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserCreateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_create, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { (activity as AppCompatActivity).onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_user_create, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val mRandomUser = menu?.findItem(R.id.action_random)
        mRandomUser?.setOnMenuItemClickListener {
            true
        }
    }
}
