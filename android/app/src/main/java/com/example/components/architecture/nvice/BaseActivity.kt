package com.example.components.architecture.nvice

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingFragmentInjector
    }

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(context!!))
    }
}