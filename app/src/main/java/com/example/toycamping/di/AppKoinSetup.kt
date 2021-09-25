package com.example.toycamping.di

import com.example.toycamping.api.GoCampingApi
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.data.repo.GoCampingRepositoryImpl
import com.example.toycamping.data.source.remote.GoCampingRemoteDataSource
import com.example.toycamping.data.source.remote.GoCampingRemoteDataSourceImpl
import com.example.toycamping.viewmodel.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppKoinSetup : KoinBaseKoinSetup() {

    companion object {
        private const val GO_CAMPING_BASE_URL =
            "http://api.visitkorea.or.kr/openapi/service/rest/GoCamping/"

    }

    private val viewModelModule = module {
        viewModel { HomeViewModel(androidApplication()) }
    }

    private val repositoryModule = module {
        single<GoCampingRepository> { GoCampingRepositoryImpl() }
    }

    private val sourceModule = module {
        single<GoCampingRemoteDataSource> { GoCampingRemoteDataSourceImpl() }
    }

    private val apiModule = module {
        single<GoCampingApi> {
            Retrofit.Builder()
                .baseUrl(GO_CAMPING_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GoCampingApi::class.java)
        }
    }


    override fun getModules(): List<Module> {
        return listOf(
            viewModelModule,
            repositoryModule,
            sourceModule,
            apiModule
        )
    }
}