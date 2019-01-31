package com.example.components.architecture.nvice.widget.fields

import android.annotation.SuppressLint
import android.databinding.*
import android.support.annotation.ColorRes
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter


@Suppress("unused")
@SuppressLint("ResourceAsColor")
@BindingAdapter("field_iconTint")

fun setColorTint(view: CustomField, @ColorRes value: Int) {
    view.setIconTint(value)
}

@Suppress("unused")
@BindingAdapter("field_text")
fun setText(view: CustomFieldEditText, value: String?) {
    value?.let {
        if (it != view.getText().toString())
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
    return view.getText().toString()
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

