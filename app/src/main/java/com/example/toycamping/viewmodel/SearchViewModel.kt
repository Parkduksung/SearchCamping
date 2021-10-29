package com.example.toycamping.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.ext.ioScope
import com.example.toycamping.room.CampingEntity
import com.example.toycamping.utils.Result
import org.koin.java.KoinJavaComponent.inject

class SearchViewModel(app: Application) : BaseViewModel(app) {

    private val goCampingRepository by inject<GoCampingRepository>(GoCampingRepository::class.java)

    val searchLiveData = MutableLiveData<String>()

    fun getAllData() {
        ioScope {
            viewStateChanged(SearchViewState.ShowProgress)
            when (val result = goCampingRepository.getAllCampingData()) {

                is Result.Success -> {
                    viewStateChanged(SearchViewState.GetAllData(result.data))
                    viewStateChanged(SearchViewState.HideProgress)
                }

                is Result.Error -> {
                    viewStateChanged(SearchViewState.Error("데이터를 가져올 수 없습니다."))
                    viewStateChanged(SearchViewState.HideProgress)
                }
            }
        }
    }

    fun search() {
        searchLiveData.value?.let { keyword ->

            ioScope {

                when (val result = goCampingRepository.getAllCampingData()) {

                    is Result.Success -> {

                        val toFilterResult = result.data.filter { it.name.contains(keyword) }

                        if (toFilterResult.isNotEmpty()) {
                            viewStateChanged(SearchViewState.GetSearchData(toFilterResult))
                        } else {
                            viewStateChanged(SearchViewState.EmptySearchResult)
                        }
                    }

                    is Result.Error -> {
                        viewStateChanged(SearchViewState.Error(result.exception.message.toString()))
                    }

                }
            }

        }
    }

    fun resetSearchView() {
        viewStateChanged(SearchViewState.ResetSearchView)
    }

    sealed class SearchViewState : ViewState {
        data class Error(val message: String) : SearchViewState()
        data class GetAllData(val list: List<CampingEntity>) : SearchViewState()
        data class GetSearchData(val list: List<CampingEntity>) : SearchViewState()
        object EmptySearchResult : SearchViewState()
        object ResetSearchView : SearchViewState()
        object ShowProgress : SearchViewState()
        object HideProgress : SearchViewState()
    }
}