package com.example.components.architecture.nvice.ui.user.profile.create


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.*
import com.example.components.architecture.nvice.BaseFragment

import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.FragmentUserCreateBinding
import com.example.components.architecture.nvice.model.user.UserExperienceOperator
import com.example.components.architecture.nvice.model.user.UserSkillOperator
import com.example.components.architecture.nvice.model.user.UserPosition
import com.example.components.architecture.nvice.model.user.UserStatus
import com.example.components.architecture.nvice.ui.camera.CameraActivity
import com.example.components.architecture.nvice.ui.user.profile.UserExperienceField
import com.example.components.architecture.nvice.ui.user.profile.UserSkillField
import com.example.components.architecture.nvice.widget.modal.UserAvatarMenuModal
import com.example.components.architecture.nvice.util.extension.validateWith
import com.example.components.architecture.nvice.widget.fields.CustomFieldSpinnerAdapter
import com.example.components.architecture.nvice.widget.modal.UserCoverMenuModal
import kotlinx.android.synthetic.main.fragment_user_create.*
import javax.inject.Inject


class UserCreateFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: UserCreateViewModel

    private var avatarMenuModal: UserAvatarMenuModal? = null
    private var coverMenuModal: UserCoverMenuModal? = null

    companion object {
        fun newInstance() = UserCreateFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserCreateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val binding: FragmentUserCreateBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_create, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initToolbar()
        initObservers()
        initView()
        initModal()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_user_create, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val mRandomUser = menu.findItem(R.id.action_random)
        mRandomUser?.setOnMenuItemClickListener {
            viewModel.randomUser()
            true
        }

        val mScan = menu.findItem(R.id.action_scan)
        mScan?.setOnMenuItemClickListener {
            activity?.startActivity(Intent(context, CameraActivity::class.java))
            true
        }
    }

    fun showAvatarMenuModal() {
        avatarMenuModal?.show(childFragmentManager)
    }

    fun showCoverMenuModal() {
        coverMenuModal?.show(childFragmentManager)
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = getString(R.string.title_staff_add)
        toolbar.setNavigationOnClickListener { (activity as AppCompatActivity).onBackPressed() }
    }

    private fun initObservers() {

        viewModel.isUserCreated.observe(viewLifecycleOwner, Observer { status ->
            status?.let { isCreated ->
                if (isCreated) {
                    activity?.finish()
                }
            }
        })

        viewModel.formValidator.observe(viewLifecycleOwner, Observer { exception ->
            exception?.let {
                edtFirstName.validateWith(it.errors) { view, error ->
                    view.setError(error.getAlertMessage(context))
                }
                edtLastName.validateWith(it.errors) { view, error ->
                    view.setError(error.getAlertMessage(context))
                }
            }
        })

        viewModel.skillManager.observe(viewLifecycleOwner, Observer { operation ->
            when (operation.first) {
                UserSkillOperator.INCLUDE -> {
                    val field = UserSkillField(context!!)
                    field.bind(viewModel, operation.second)
                    vSkillFields.addView(field)
                }
                UserSkillOperator.EXCLUDE -> {
                    vSkillFields.removeViewAt(operation.second)
                }
            }
        })

        viewModel.experienceManager.observe(viewLifecycleOwner, Observer { operation ->
            when (operation.first) {
                UserExperienceOperator.INCLUDE -> {
                    val field = UserExperienceField(context!!)
                    field.bind(viewModel, operation.second)
                    vExperienceFields.addView(field)
                }
                UserExperienceOperator.EXCLUDE -> {
                    vExperienceFields.removeViewAt(operation.second)
                }
            }
        })
    }

    private fun initView() {
        spPosition.setAdapter(CustomFieldSpinnerAdapter(context!!, UserPosition.values()))
        spStatus.setAdapter(CustomFieldSpinnerAdapter(context!!, UserStatus.values()))
    }

    private fun initModal() {
        initAvatarModal()
        initCoverModal()
    }

    private fun initAvatarModal() {
        avatarMenuModal = UserAvatarMenuModal.newInstance(
                randomEvent = {
                    viewModel.selectAvatar()
                    dismiss()
                },
                chooseCategoryEvent = {
                    dismiss()
                }
        )
    }

    private fun initCoverModal() {
        coverMenuModal = UserCoverMenuModal.newInstance(
                randomEvent = {
                    viewModel.selectCover()
                    dismiss()
                },
                chooseCategoryEvent = {
                    dismiss()
                }
        )
    }
}
