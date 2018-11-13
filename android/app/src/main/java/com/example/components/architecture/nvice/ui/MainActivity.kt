package com.example.components.architecture.nvice.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.example.components.architecture.nvice.BaseActivity
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.ui.bottomsheetexample.SlideUpBottomSheetFragment
import com.example.components.architecture.nvice.data.preference.AppSettingsPreference
import com.example.components.architecture.nvice.ui.bottomsheetexample.SlideUpBottomSheetViewModel
import com.example.components.architecture.nvice.ui.user.UserFragment
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : BaseActivity() {

    @Inject
    lateinit var appSettingsPreference: AppSettingsPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentContainer.id, UserFragment())
        fragmentTransaction.commit()
        Timber.i(appSettingsPreference.hasSettings().toString())
    }
}
