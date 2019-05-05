package com.example.components.architecture.nvice.ui.user.details


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import androidx.appcompat.widget.Toolbar
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.components.architecture.nvice.BaseFragment

import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.FragmentUserDetailsBinding
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.util.DimensUtil

import kotlinx.android.synthetic.main.fragment_user_details.*
import org.parceler.Parcels
import javax.inject.Inject


class UserDetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: UserDetailsViewModel

    private var isAppBarBgShowing: Boolean = false

    companion object {
        fun getInstance(user: User?): UserDetailsFragment {
            val fragment = UserDetailsFragment()
            val args = Bundle()
            args.putParcelable("user", Parcels.wrap(user))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserDetailsViewModel::class.java)
        viewModel.initUser(Parcels.unwrap(arguments?.getParcelable("user")))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentUserDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_details, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initView()
        initObserver()
        initEvent()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_user_details, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val mEditUser = menu.findItem(R.id.action_edit)
        mEditUser?.setOnMenuItemClickListener {
            true
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.disposeServices()
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { (activity as AppCompatActivity).onBackPressed() }
    }

    private fun initView() {
        initBackground()
    }


    private fun initObserver() {

    }

    private fun initBackground() {
        Glide.with(context!!).asBitmap().load(viewModel.getAvatar()).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                Palette.from(resource).generate { palette ->
                    palette?.let {
                        val bgGradientDrawable = GradientDrawable()
                        bgGradientDrawable.orientation = GradientDrawable.Orientation.TL_BR
                        bgGradientDrawable.colors = intArrayOf(
                                it.getDarkMutedColor(ContextCompat.getColor(context!!, R.color.black_500)),
                                it.getLightMutedColor(ContextCompat.getColor(context!!, R.color.black_500)),
                                it.getLightMutedColor(ContextCompat.getColor(context!!, R.color.black_500))
                        )
                        vBackground.background = bgGradientDrawable
                    }
                }
            }
        })
    }

    private fun initEvent() {
        svContent.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val offset = resources.getDimension(R.dimen.dp_96)
            val distance = scrollY - offset
            showAppBarWithAlphaAnimator(distance)
        })
    }

    private fun showAppBarByAlphaScrolling(distance: Float) {
        val alpha = when {
            distance >= 100 -> 255
            distance >= 0 -> (distance * 2.55).toInt()
            else -> 0
        }
        setAppBarBackgroundAlpha(alpha)
    }

    private fun showAppBarWithAlphaAnimator(distance: Float) {
        when {
            distance >= 100 -> if (!isAppBarBgShowing) {
                isAppBarBgShowing = true
                animateAppBarBackgroundColor(0, 255, 200)
            }
            else -> if (isAppBarBgShowing) {
                isAppBarBgShowing = false
                animateAppBarBackgroundColor(255, 0, 200)
            }
        }
    }

    private fun animateAppBarBackgroundColor(from: Int, to: Int, duration: Long) {
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), from, to)
        colorAnimator.duration = duration
        colorAnimator.addUpdateListener {
            val alpha = it.animatedValue as Int
            setAppBarBackgroundAlpha(alpha)
        }
        colorAnimator.start()
    }

    private fun setAppBarBackgroundAlpha(alpha: Int) {
        val statusBarColor = ContextCompat.getColor(context!!, R.color.primary_dark)
        val appBarColor = ContextCompat.getColor(context!!, R.color.primary)
        appBar.setBackgroundColor(ColorUtils.setAlphaComponent(appBarColor, alpha))
        (activity as AppCompatActivity).window.statusBarColor = ColorUtils.setAlphaComponent(statusBarColor, alpha)
        appBar.elevation = DimensUtil.dpToPx(if (alpha >= 255) 8f else 0f)
    }
}
