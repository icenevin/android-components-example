package com.example.components.architecture.nvice.ui.user.edit

import android.os.Bundle
import com.example.components.architecture.nvice.BaseActivity
import com.example.components.architecture.nvice.R

class UserEditActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)
        if (savedInstanceState == null) {
            val userId = intent?.getIntExtra("userId", 0)
            supportFragmentManager.beginTransaction().replace(R.id.container, UserEditFragment.newInstance(userId)).commit()
        }
    }
}
