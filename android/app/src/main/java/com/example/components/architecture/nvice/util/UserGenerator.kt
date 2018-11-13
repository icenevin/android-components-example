package com.example.components.architecture.nvice.util

import com.example.components.architecture.nvice.api.response.UiNamesResponse
import com.example.components.architecture.nvice.api.service.UiNamesService
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserGenerator @Inject constructor(private val uiNamesService: UiNamesService) {
    fun generateUser(callback: GenerateUserCallback) {
        uiNamesService.getUserInCountry("england").enqueue(object : Callback<UiNamesResponse> {
            override fun onResponse(call: Call<UiNamesResponse>, response: Response<UiNamesResponse>) {
                val user = User()
                user.firstName = response.body()?.name
                user.lastName = response.body()?.surname
                user.status = UserStatus.random()
                callback.onSuccess(user)
            }

            override fun onFailure(call: Call<UiNamesResponse>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }
}

interface GenerateUserCallback{
    fun onSuccess(user: User)
    fun onFailure(t: Throwable)
}