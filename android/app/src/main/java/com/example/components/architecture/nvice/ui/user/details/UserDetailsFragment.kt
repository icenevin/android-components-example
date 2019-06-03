package com.example.components.architecture.nvice.ui.user.details


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.components.architecture.nvice.BaseFragment

import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.FragmentUserDetailsBinding
import com.example.components.architecture.nvice.ui.user.profile.UserExperienceListAdapter
import com.example.components.architecture.nvice.ui.user.profile.UserSkillListAdapter
import com.example.components.architecture.nvice.ui.user.profile.edit.UserEditActivity
import com.example.components.architecture.nvice.util.DimensUtils

import kotlinx.android.synthetic.main.fragment_user_details.*
import org.parceler.Parcels
import timber.log.Timber
import javax.inject.Inject


class UserDetailsFragment : BaseFragment() {

    private val EDIT_USER_REQUEST: Int = 100

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: UserDetailsViewModel

    private lateinit var skillsAdapter: UserSkillListAdapter
    private lateinit var experiencesAdapter: UserExperienceListAdapter

    private var isAppBarBgShowing: Boolean = false

    companion object {
        fun newInstance(userId: Int?): UserDetailsFragment {
            val fragment = UserDetailsFragment()
            val args = Bundle()
            args.putInt("userId", userId ?: 0)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserDetailsViewModel::class.java)
        val userId = arguments?.getInt("userId", 0)
        viewModel.initUser(userId)

        skillsAdapter = UserSkillListAdapter()
        experiencesAdapter = UserExperienceListAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentUserDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_details, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initObserver()
        initView()
        initEvent()
    }

    private fun initView() {
        rvSkills.layoutManager = LinearLayoutManager(context)
        rvSkills.adapter = skillsAdapter

        rvExperiences.layoutManager = LinearLayoutManager(context)
        rvExperiences.adapter = experiencesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_user_details, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val mEditUser = menu.findItem(R.id.action_edit)
        mEditUser?.setOnMenuItemClickListener {
            val intent = Intent(activity, UserEditActivity::class.java)
            intent.putExtra("user", Parcels.wrap(viewModel.getUser()))
            startActivityForResult(intent, EDIT_USER_REQUEST)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("requestCode: $requestCode")
        if (requestCode == EDIT_USER_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    viewModel.refreshDetails()
                }
            }
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { (activity as AppCompatActivity).onBackPressed() }
    }

    private fun initObserver() {
        viewModel.avatar.observe(viewLifecycleOwner, Observer {
            initBackground(it)
        })

        viewModel.skills.observe(viewLifecycleOwner, Observer {
            skillsAdapter.submitList(it)
        })

        viewModel.experiences.observe(viewLifecycleOwner, Observer {
            experiencesAdapter.submitList(it)
        })
    }

    private fun initBackground(url: String?) {

        Glide.with(context!!).asBitmap().load(url ?: "").into(object : SimpleTarget<Bitmap>() {
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
        appBar.elevation = DimensUtils.dpToPx(if (alpha >= 255) 8f else 0f)
    }
}
