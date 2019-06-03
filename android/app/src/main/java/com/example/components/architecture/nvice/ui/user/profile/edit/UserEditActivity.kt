package com.example.components.architecture.nvice.ui.user.profile.edit

import android.os.Bundle
import android.os.Parcel
import com.example.components.architecture.nvice.BaseActivity
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.model.user.User
import org.parceler.Parcels

class UserEditActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)
        if (savedInstanceState == null) {
            val user = Parcels.unwrap<User>(intent?.getParcelableExtra("user"))
            supportFragmentManager.beginTransaction().replace(R.id.container, UserEditFragment.newInstance(user)).commit()
        }
    }
}
