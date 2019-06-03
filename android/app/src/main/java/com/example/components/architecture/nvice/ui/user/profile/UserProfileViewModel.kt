package com.example.components.architecture.nvice.ui.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.components.architecture.nvice.BaseViewModel
import com.example.components.architecture.nvice.data.repository.UserGeneratorRepository
import com.example.components.architecture.nvice.data.repository.UserRepository
import com.example.components.architecture.nvice.model.user.*
import com.example.components.architecture.nvice.ui.LoadingStatus
import com.example.components.architecture.nvice.util.benchmark.TimeCapture
import com.example.components.architecture.nvice.util.extension.init
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList


abstract class UserProfileViewModel(
        val userRepository: UserRepository,
        val userGeneratorRepository: UserGeneratorRepository
) : BaseViewModel() {

    private val job: Job = Job()
    val bgScope = CoroutineScope(Dispatchers.IO + job)
    val mScope = CoroutineScope(Dispatchers.Main + job)

    private var userCoverService: Disposable? = null

    private val _skillManager = MutableLiveData<Pair<UserSkillOperator, Int>>()
    val skillManager: LiveData<Pair<UserSkillOperator, Int>>
        get() = _skillManager

    private val _experienceManager = MutableLiveData<Pair<UserExperienceOperator, Int>>()
    val experienceManager: LiveData<Pair<UserExperienceOperator, Int>>
        get() = _experienceManager

    val userDataLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)
    val userAvatarLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)
    val userCoverLoadingStatus = MutableLiveData<LoadingStatus>().init(LoadingStatus.IDLE)

    val avatar = MutableLiveData<String?>().init(null)
    val firstName = MutableLiveData<String?>().init("")
    val lastName = MutableLiveData<String?>().init("")
    val description = MutableLiveData<String?>().init("")
    val dateOfBirth = MutableLiveData<String?>().init("")
    val position = MutableLiveData<UserPosition?>().init(UserPosition.DEV)
    val status = MutableLiveData<UserStatus?>().init(UserStatus.PERMANENT)
    val cover = MutableLiveData<String?>().init(null)
    val skills: CopyOnWriteArrayList<UserSkill?> = CopyOnWriteArrayList()
    val experiences: CopyOnWriteArrayList<UserExperience?> = CopyOnWriteArrayList()

    override fun disposeServices() {
        job.cancel()
        userCoverService?.dispose()
    }

    fun initSkills(list: List<UserSkill?>) {
        if (list.isNotEmpty()) {
            list.forEach {
                includeSkill(it ?: UserSkill(System.currentTimeMillis().toInt()))
            }
        }
        includeEmptySkill()
    }

    fun includeEmptySkill() {
        if (skills.isNotEmpty()) {
            if (!skills.last()?.name.isNullOrEmpty()) {
                includeSkill(UserSkill(System.currentTimeMillis().toInt()))
            }
        } else {
            includeSkill(UserSkill(System.currentTimeMillis().toInt()))
        }
    }

    fun includeSkill(skill: UserSkill) {
        skills.add(skill)
        _skillManager.value = Pair(UserSkillOperator.INCLUDE, skills.lastIndex)
    }

    fun excludeSkill(skill: UserSkill) {
        val position = skills.indexOf(skill)
        if (skills.size > 1) {
            skills.remove(skill)
            _skillManager.value = Pair(UserSkillOperator.EXCLUDE, position)
        }
    }

    fun initExperiences(list: List<UserExperience?>) {
        if (list.isNotEmpty()) {
            list.forEach {
                includeExperience(it ?: UserExperience(System.currentTimeMillis().toInt()))
            }
        }
        includeEmptyExperience()
    }

    fun includeEmptyExperience() {
        if (experiences.isNotEmpty()) {
            if (!experiences.last()?.work.isNullOrEmpty()) {
                includeExperience(UserExperience(System.currentTimeMillis().toInt()))
            }
        } else {
            includeExperience(UserExperience(System.currentTimeMillis().toInt()))
        }
    }

    fun includeExperience(experience: UserExperience) {
        experiences.add(experience)
        _experienceManager.value = Pair(UserExperienceOperator.INCLUDE, experiences.lastIndex)
    }

    fun excludeExperience(experience: UserExperience) {
        val position = experiences.indexOf(experience)
        if (experiences.size > 1) {
            experiences.remove(experience)
            _experienceManager.value = Pair(UserExperienceOperator.EXCLUDE, position)
        }
    }

    fun selectAvatar() {
        if (userAvatarLoadingStatus.value != LoadingStatus.PROCESSING) {
            randomAvatar()
        }
    }

    fun selectCover() {
        if (userCoverLoadingStatus.value != LoadingStatus.PROCESSING) {
            randomCover()
        }
    }

    fun setDateOfBirth(day: Int, month: Int, year: Int) {
        val date = LocalDate.of(year, month + 1, day)
        dateOfBirth.value = date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
    }

    fun clearAvatar() {
        avatar.value = ""
    }

    fun clearCover() {
        cover.value = ""
    }

    // use kotlin coroutines
    fun randomAvatar() {
        userAvatarLoadingStatus.value = LoadingStatus.PROCESSING
        bgScope.launch {
            try {
                val result = userGeneratorRepository.generateUserAvatar().await()
                result[0].photo?.let {
                    avatar.postValue(it)
                }
            } catch (e: Exception) {
                Timber.e("Exception $e")
            }
            userAvatarLoadingStatus.postValue(LoadingStatus.FINISHED)
        }
    }

    // use rxJava
    fun randomCover() {
        userCoverLoadingStatus.postValue(LoadingStatus.PROCESSING)
        userCoverService = userGeneratorRepository.generateUserCover()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response ->
                            cover.postValue(response[0].urls.regular)
                        },
                        { error ->
                            Timber.e(error)
                        },
                        { userCoverLoadingStatus.postValue(LoadingStatus.FINISHED) }
                )
    }

    fun validateSkills(): MutableList<UserSkill?>? {
        return skills.filterNot {
            it?.name.isNullOrEmpty()
        }.toMutableList()
    }

    fun validateExperiences(): MutableList<UserExperience?>? {
        return experiences.filterNot {
            it?.work.isNullOrEmpty()
        }.toMutableList()
    }
}