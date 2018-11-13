package com.example.components.architecture.nvice.ui.bottomsheetexample


import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.components.architecture.nvice.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_slide_up_bottom_sheet.*
import timber.log.Timber
import javax.inject.Inject


class SlideUpBottomSheetFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SlideUpBottomSheetViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SlideUpBottomSheetViewModel::class.java)

        return inflater.inflate(R.layout.fragment_slide_up_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.i(viewModel.getExampleText())
    }
}
