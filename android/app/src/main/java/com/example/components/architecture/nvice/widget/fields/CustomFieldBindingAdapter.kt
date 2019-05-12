package com.example.components.architecture.nvice.widget.fields

import android.annotation.SuppressLint
import androidx.databinding.*
import androidx.annotation.ColorRes
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import com.example.components.architecture.nvice.util.validation.Validator


@Suppress("unused")
@SuppressLint("ResourceAsColor")
@BindingAdapter("field_iconTint")
fun setColorTint(view: CustomField, @ColorRes value: Int) {
    view.setIconTint(value)
}

@Suppress("unused")
@SuppressLint("ResourceAsColor")
@BindingAdapter("field_drawableEndTint")
fun setDrawableEndColorTint(view: CustomField, @ColorRes value: Int) {
    view.setDrawableEndTint(value)
}

@Suppress("unused")
@SuppressLint("ResourceAsColor")
@BindingAdapter("field_inputDrawableEndTint")
fun setInputDrawableEndColorTint(view: CustomFieldEditText, @ColorRes value: Int) {
    view.setInputDrawableEndTint(value)
}

@Suppress("unused")
@BindingAdapter("field_onClick")
fun setOnClick(view: CustomField, listener: View.OnClickListener?) {
    listener?.let {
        view.setOnClick(it)
    }
}

@Suppress("unused")
@BindingAdapter("field_onDrawableEndClick")
fun setOnDrawableEndClick(view: CustomField, listener: View.OnClickListener?) {
    listener?.let {
        view.setOnDrawableEndClick(it)
    }
}

@Suppress("unused")
@BindingAdapter("field_onInputDrawableEndClick")
fun setOnInputDrawableEndClick(view: CustomFieldEditText, listener: View.OnClickListener?) {
    listener?.let {
        view.setOnInputDrawableEndClick(it)
    }
}

@Suppress("unused")
@BindingAdapter("field_showInputDrawableEnd")
fun setShowInputDrawableEnd(view: CustomFieldEditText, requestShow: Boolean?) {
    requestShow?.let {
        view.setShowInputDrawableEnd(it)
    }
}

@Suppress("unused")
@BindingAdapter("field_text")
fun setText(view: CustomFieldEditText, value: String?) {
    value?.let {
        if (it != view.getText()?.toString())
            view.setText(it)
    }
}

@Suppress("unused")
@BindingAdapter("field_textAttrChanged")
fun setTextWatcher(view: CustomFieldEditText, attrChange: InverseBindingListener) {
    val newValue = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            attrChange.onChange()
        }
    }

    view.addTextChangedListener(newValue)
}

@Suppress("unused")
@InverseBindingAdapter(attribute = "field_text")
fun getText(view: CustomFieldEditText): String? {
    return view.getText()?.toString()
}

@Suppress("unused")
@BindingAdapter("field_selectedItem")
fun setSelectedItem(view: CustomFieldSpinner, value: Any?) {
    value?.let {
        view.getSpinner().let { spinner ->
            when (it) {
                is Enum<*> -> {
                    spinner.setSelection(it.ordinal)
                }
                else -> {
                    spinner.setSelection((spinner.adapter as ArrayAdapter<String>).getPosition(it.toString()))
                }
            }
        }
    }
}

@Suppress("unused")
@BindingAdapter("field_error")
fun setError(view: CustomFieldEditText, value: String?) {
    value?.let {
            view.setError(it)
    }
}

@Suppress("unused")
@BindingAdapter("field_error")
fun setError(view: CustomFieldEditText, value: CustomFieldError?) {
    value?.let {
        view.setError(it)
    }
}

@Suppress("unused")
@BindingAdapter("field_validator")
fun setValidator(view: CustomFieldEditText, value: Validator?) {
    value?.let {
        view.validator = it
    }
}