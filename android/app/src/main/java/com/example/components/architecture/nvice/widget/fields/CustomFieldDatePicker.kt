package com.example.components.architecture.nvice.widget.fields

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.util.DateUtils
import com.example.components.architecture.nvice.util.getMillis
import kotlinx.android.synthetic.main.view_custom_field_edittext.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.*

class CustomFieldDatePicker @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int = 0
) : CustomFieldEditText(context, attrs, defStyle) {

    private var datePicker: DatePickerDialog? = null
    private var maxDate: Long? = null
    private var minDate: Long? = null

    init {
        initView(attrs)
        edtInputField.isLongClickable = false
        edtInputField.isFocusable = false
        setClearable(true)
    }

    fun setMaxDate(text: String) {
        maxDate = DateUtils.parseToMillis(text)
    }

    fun setMinDate(text: String) {
        minDate = DateUtils.parseToMillis(text)
    }

    private fun initView(attrs: AttributeSet?) {

        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.CustomFieldDatePicker, 0, 0)
            val maxDateString = attributes.getString(R.styleable.CustomFieldDatePicker_field_maxDate)
            val minDateString = attributes.getString(R.styleable.CustomFieldDatePicker_field_minDate)
            val additionalMaxYear = attributes.getInteger(R.styleable.CustomFieldDatePicker_field_additionalMaxYear, 0)
            val additionalMinYear = attributes.getInteger(R.styleable.CustomFieldDatePicker_field_additionalMinYear, 0)

            if (!isInEditMode) {
                if (additionalMaxYear == 0) {
                    if (!maxDateString.isNullOrEmpty()) maxDate = DateUtils.parseToMillis(maxDateString)
                } else {
                    maxDate = DateUtils.nowInMillis(additionalMaxYear.toLong())
                }

                if (additionalMinYear == 0) {
                    if (!minDateString.isNullOrEmpty()) minDate = DateUtils.parseToMillis(minDateString)
                } else {
                    minDate = DateUtils.nowInMillis(additionalMinYear.toLong())
                }

                initDatePicker()
                edtInputField.setOnClickListener {
                    datePicker?.show()
                }
            }
            attributes.recycle()
        }
    }

    private fun initDatePicker() {
        val now = DateUtils.now()
        datePicker = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener { _, y, m, d ->
                    setDate(d, m, y)
                }, now.year, now.monthValue - 1, now.dayOfMonth)
        maxDate?.let {
            datePicker?.datePicker?.maxDate = it
        }
        minDate?.let {
            datePicker?.datePicker?.minDate = it
        }
    }

    private fun setDate(day: Int, month: Int, year: Int) {
        val date = LocalDate.of(year, month + 1, day)
        edtInputField.setText(DateUtils.format(date))
    }

    interface OnDateSetListener {
        fun onDateSet(year: Int, month: Int, dayOfMonth: Int)
    }
}