package com.example.components.architecture.nvice.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.components.architecture.nvice.api.response.UiFacesResponse
import com.example.components.architecture.nvice.api.response.UiNamesResponse
import com.example.components.architecture.nvice.api.service.UiFacesService
import com.example.components.architecture.nvice.api.service.UiNamesService
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class UserGenerator @Inject constructor(private val uiNamesService: UiNamesService, private val uiFacesService: UiFacesService) {

    fun generateUser(callback: GenerateUserCallback) {
        uiNamesService.getUserInCountry("", "england").enqueue(object : Callback<UiNamesResponse> {
            override fun onResponse(call: Call<UiNamesResponse>, response: Response<UiNamesResponse>) {
                val user = User()
                user.firstName = response.body()?.name
                user.lastName = response.body()?.surname
                user.avatar = response.body()?.photo
                user.status = UserStatus.random()
                user.position = UserPosition.random()
                callback.onSuccess(user)
            }

            override fun onFailure(call: Call<UiNamesResponse>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    fun generateUserAvatar(callback: GenerateAvatarCallback) {
        uiFacesService.getAvatarsByProvider("9", "1", "").enqueue(object : Callback<List<UiFacesResponse>> {
            override fun onResponse(call: Call<List<UiFacesResponse>>, response: Response<List<UiFacesResponse>>) {
                Timber.i(response.body().toString())
                callback.onSuccess(response.body()?.get(0)?.photo)
            }

            override fun onFailure(call: Call<List<UiFacesResponse>>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

}

interface GenerateUserCallback {
    fun onSuccess(user: User)
    fun onFailure(t: Throwable)
}

interface GenerateAvatarCallback {
    fun onSuccess(url: String?)
    fun onFailure(t: Throwable)
}