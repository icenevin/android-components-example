package com.example.components.architecture.nvice.data.repository

import com.example.components.architecture.nvice.api.response.UiFacesResponse
import com.example.components.architecture.nvice.api.response.UiNamesResponse
import com.example.components.architecture.nvice.api.response.UnsplashPhotosResponse
import com.example.components.architecture.nvice.api.service.UiFacesService
import com.example.components.architecture.nvice.api.service.UiNamesService
import com.example.components.architecture.nvice.api.service.UnsplashService
import com.example.components.architecture.nvice.model.user.User
import com.example.components.architecture.nvice.model.user.UserPosition
import com.example.components.architecture.nvice.model.user.UserStatus
import io.reactivex.Flowable
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserGeneratorRepository @Inject constructor(
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

    fun generateUserAvatar(): Deferred<List<UiFacesResponse>> =
            uiFacesService.getAvatarsByProvider("9", "1", "")

    fun generateUserCover(): Flowable<List<UnsplashPhotosResponse>> =
            unsplashService.getPhotosRandom( "1")
}

interface GenerateUserCallback {
    fun onSuccess(user: User)
    fun onFailure(t: Throwable)
}