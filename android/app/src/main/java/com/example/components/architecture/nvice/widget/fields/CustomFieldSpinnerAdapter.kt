package com.example.components.architecture.nvice.widget.fields

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.components.architecture.nvice.R


class CustomFieldSpinnerAdapter<T> constructor(
        context: Context,
        objects: Array<T>
) : ArrayAdapter<T>(context, R.layout.item_dropdown_custom_field_spinner, objects) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        with(super.getDropDownView(position, null, parent)) {
            setPadding(
                    context.resources.getDimensionPixelSize(R.dimen.dp_8),
                    paddingTop,
                    context.resources.getDimensionPixelSize(R.dimen.dp_8),
                    paddingBottom
            )
            return this
        }
    }
}