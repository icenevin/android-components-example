package com.example.components.architecture.nvice.ui.user.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.components.architecture.nvice.BaseFragment
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.databinding.FragmentUserEditBinding
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import com.example.components.architecture.nvice.util.extension.validateWith
import com.example.components.architecture.nvice.widget.modal.UserAvatarMenuModal
import com.example.components.architecture.nvice.widget.modal.UserCoverMenuModal
import kotlinx.android.synthetic.main.fragment_user_edit.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class UserEditFragment : BaseFragment() {

    companion object {
        fun newInstance(userId: Int?): UserEditFragment {
            val fragment = UserEditFragment()
            val args = Bundle()
            args.putInt("userId", userId ?: 0)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: UserEditViewModel

    private var datePicker: DatePickerDialog? = null
    private var avatarMenuModal: UserAvatarMenuModal? = null
    private var coverMenuModal: UserCoverMenuModal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UserEditViewModel::class.java)
        val userId = arguments?.getInt("userId")
        viewModel.initUser(userId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: FragmentUserEditBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_edit, container, false)
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
        initPickers()
    }

    fun showDatePicker() {
        datePicker?.show()
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
        toolbar.title = "Edit Staff"
        toolbar.setNavigationOnClickListener { (activity as AppCompatActivity).onBackPressed() }
    }

    private fun initObservers() {

        viewModel.isProfileChangeCompleted.observe(viewLifecycleOwner, Observer { status ->
            status?.let { isCompleted ->
                if (isCompleted) {
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
            }
        })

        viewModel.dateOfBirth.observe(viewLifecycleOwner, Observer { value ->
            value?.let { date ->
                if (date.isNotEmpty()) {
                    val dateOfBirth = LocalDate.parse(date, DateTimeFormatter.ofPattern("d MMM yyyy"))
                    datePicker?.updateDate(dateOfBirth.year, dateOfBirth.monthValue - 1, dateOfBirth.dayOfMonth)
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
    }

    private fun initView() {
        spPosition.getSpinner().adapter = ArrayAdapter<UserPosition>(context!!, R.layout.item_dropdown_custom_field_spinner, UserPosition.values())
        spStatus.getSpinner().adapter = ArrayAdapter<UserStatus>(context!!, R.layout.item_dropdown_custom_field_spinner, UserStatus.values())
    }

    private fun initPickers() {
        initDatePicker()
        initAvatarPicker()
        initCoverPicker()
    }

    private fun initDatePicker() {
        val now = Calendar.getInstance()
        datePicker = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    viewModel.setDateOfBirth(d, m, y)
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
        now.add(Calendar.YEAR, -5)
        datePicker?.datePicker?.maxDate = now.timeInMillis
        now.add(Calendar.YEAR, -50)
        datePicker?.datePicker?.minDate = now.timeInMillis
    }

    private fun initAvatarPicker() {
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

    private fun initCoverPicker() {
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
