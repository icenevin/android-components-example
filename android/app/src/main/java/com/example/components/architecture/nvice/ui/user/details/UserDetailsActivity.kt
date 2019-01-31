package com.example.components.architecture.nvice.ui.user.details

import android.graphics.Color
import android.os.Bundle
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import com.example.components.architecture.nvice.BaseActivity
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.model.User
import org.parceler.Parcels


class UserDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_user_details)
        initView(Parcels.unwrap(intent?.getParcelableExtra("user")))
        iniTransition()
    }

    private fun initView(user: User) {
        supportFragmentManager.beginTransaction().replace(R.id.container, UserDetailsFragment.getInstance(user)).commit()
    }

    private fun iniTransition() {
        val enterTransitionSet = TransitionSet()
        val returnTransitionSet = TransitionSet()

        val fadeIn = Fade()
                .excludeTarget(android.R.id.navigationBarBackground, true)
                .addTarget(android.R.id.statusBarBackground)
                .addTarget(R.id.vBackground)

        val fadeCoverIn = Fade()
                .excludeTarget(android.R.id.navigationBarBackground, true)
                .addTarget(android.R.id.statusBarBackground)
                .addTarget(R.id.ivUserCover)
                .setStartDelay(250)

        val fadeOut = Fade(Fade.MODE_OUT)
                .excludeTarget(android.R.id.navigationBarBackground, true)
                .addTarget(android.R.id.statusBarBackground)
                .addTarget(R.id.vBackground)
                .addTarget(R.id.ivUserCover)

        val slide = Slide(Gravity.TOP)
                .excludeTarget(android.R.id.statusBarBackground, true)
                .excludeTarget(android.R.id.navigationBarBackground, true)
                .addTarget(R.id.appBar)

        val explode = Explode()
                .excludeTarget(android.R.id.statusBarBackground, true)
                .excludeTarget(android.R.id.navigationBarBackground, true)
                .excludeTarget(R.id.ivUserCover, true)
                .excludeTarget(R.id.vBackground, true)
                .excludeTarget(R.id.appBar, true)

        enterTransitionSet.addTransition(fadeIn)
        enterTransitionSet.addTransition(fadeCoverIn)
        enterTransitionSet.addTransition(slide)
        enterTransitionSet.addTransition(explode)

        returnTransitionSet.addTransition(fadeOut)
        returnTransitionSet.addTransition(slide)
        returnTransitionSet.addTransition(explode)

        window.enterTransition = enterTransitionSet
        window.returnTransition = returnTransitionSet
    }
}
