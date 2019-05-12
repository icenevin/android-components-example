package com.example.components.architecture.nvice.data.error

import android.content.Context

interface Error {
    val code: Int
    val alertMsgRes: Int?
    val defMsg: String

    fun getAlertMessage(context: Context?): String {
        this.alertMsgRes?.let {
            return context?.getString(it) ?: this.defMsg
        } ?: return this.defMsg
    }
}