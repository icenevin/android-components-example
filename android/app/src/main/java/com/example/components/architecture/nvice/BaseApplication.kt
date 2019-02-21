package com.example.components.architecture.nvice

import android.annotation.SuppressLint
import android.content.Context
import com.example.components.architecture.nvice.di.DaggerAppComponent
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

@SuppressLint("Registered")
class BaseApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(this)
        FirebaseApp.initializeApp(this);
        Timber.i(LocaleHelper.getLanguage(this))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let { LocaleHelper.onAttach(it) })
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}