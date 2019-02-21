package com.example.components.architecture.nvice.ui.camera

import android.os.Bundle
import com.example.components.architecture.nvice.BaseActivity
import com.example.components.architecture.nvice.R

class CameraActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        if (savedInstanceState == null) {
            initView()
        }
    }

    private fun initView() {
        supportFragmentManager.beginTransaction().replace(R.id.container, CameraFragment.getInstance()).commit()
    }
}
