package com.example.toycamping.viewmodel

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.ext.ioScope
import org.koin.java.KoinJavaComponent

class SplashViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {


    private val goCampingRepository: GoCampingRepository by KoinJavaComponent.inject(
        GoCampingRepository::class.java
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun start() {
        ioScope {
            if (goCampingRepository.checkExistCampingData()) {
                viewStateChanged(SplashViewState.RouteMain)
            } else {
                viewStateChanged(SplashViewState.ShowProgress)

                goCampingRepository.getBasedList(
                    onSuccess = {
                        val toCampingEntity =
                            it.basedResponse.basedListBody.basedListItems.basedListItem.map { it.toCampingEntity() }

                        ioScope {
                            if (goCampingRepository.registerCampingList(toCampingEntity)) {
                                viewStateChanged(SplashViewState.RouteMain)
                                viewStateChanged(SplashViewState.HideProgress)
                            } else {
                                viewStateChanged(SplashViewState.HideProgress)
                            }
                        }
                    },
                    onFailure = {
                        viewStateChanged(SplashViewState.HideProgress)
                    }
                )
            }
        }
    }

    sealed class SplashViewState : ViewState {
        object ShowProgress : SplashViewState()
        object HideProgress : SplashViewState()
        object RouteMain : SplashViewState()
    }


}