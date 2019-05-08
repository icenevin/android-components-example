package com.example.components.architecture.nvice.ui

import android.os.Bundle
import com.example.components.architecture.nvice.BaseActivity
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.data.preference.AppSettingsPreference
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
        if (savedInstanceState == null) {
            initView()
        }
    }

    private fun initView() {
        supportFragmentManager.beginTransaction().replace(container.id, UserFragment.newInstance()).commit()
        Timber.i(appSettingsPreference.has().toString())
    }
}
