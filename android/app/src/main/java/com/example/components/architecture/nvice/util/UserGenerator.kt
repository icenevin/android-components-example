package com.example.components.architecture.nvice.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.components.architecture.nvice.api.response.UiFacesResponse
import com.example.components.architecture.nvice.api.response.UiNamesResponse
import com.example.components.architecture.nvice.api.response.UnsplashPhotosResponse
import com.example.components.architecture.nvice.api.service.UiFacesService
import com.example.components.architecture.nvice.api.service.UiNamesService
import com.example.components.architecture.nvice.api.service.UnsplashService
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.User
import com.example.components.architecture.nvice.model.UserPosition
import com.example.components.architecture.nvice.model.UserStatus
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class UserGenerator @Inject constructor(
        private val uiNamesService: UiNamesService,
        private val uiFacesService: UiFacesService,
        private val unsplashService: UnsplashService
) {

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

    fun generateUserCover(): Flowable<List<UnsplashPhotosResponse>> = unsplashService.getPhotosRandom(
            "f06db831e385882fcb08879965e768be1451e1f6e05da3bd4f074c8afa023e75",
            "1"
    )
}

interface GenerateUserCallback {
    fun onSuccess(user: User)
    fun onFailure(t: Throwable)
}

interface GenerateAvatarCallback {
    fun onSuccess(url: String?)
    fun onFailure(t: Throwable)
}