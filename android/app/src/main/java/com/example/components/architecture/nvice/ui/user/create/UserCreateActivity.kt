package com.example.components.architecture.nvice.ui.user.create

import android.os.Bundle
import com.example.components.architecture.nvice.BaseActivity
import com.example.components.architecture.nvice.R

class UserCreateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_create)
        if (savedInstanceState == null) {
            initView()
        }
    }

    private fun initView() {
        supportFragmentManager.beginTransaction().replace(R.id.container, UserCreateFragment.newInstance()).commit()
    }
}
