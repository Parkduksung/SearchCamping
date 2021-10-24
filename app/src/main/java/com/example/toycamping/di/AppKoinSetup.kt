package com.example.toycamping.di

import androidx.room.Room
import com.example.toycamping.api.GoCampingApi
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.data.repo.GoCampingRepositoryImpl
import com.example.toycamping.data.source.loca.GoCampingLocalDataSource
import com.example.toycamping.data.source.loca.GoCampingLocalDataSourceImpl
import com.example.toycamping.data.source.remote.GoCampingRemoteDataSource
import com.example.toycamping.data.source.remote.GoCampingRemoteDataSourceImpl
import com.example.toycamping.room.database.CampingDatabase
import com.example.toycamping.viewmodel.HomeViewModel
import com.example.toycamping.viewmodel.MapViewModel
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
        viewModel { MapViewModel(androidApplication()) }
    }

    private val repositoryModule = module {
        single<GoCampingRepository> { GoCampingRepositoryImpl() }
    }

    private val sourceModule = module {
        single<GoCampingRemoteDataSource> { GoCampingRemoteDataSourceImpl() }
        single<GoCampingLocalDataSource> { GoCampingLocalDataSourceImpl() }
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

    private val databaseModule = module {
        single {
            Room.databaseBuilder(
                get(),
                CampingDatabase::class.java,
                "camping_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }


    override fun getModules(): List<Module> {
        return listOf(
            viewModelModule,
            repositoryModule,
            sourceModule,
            apiModule,
            databaseModule
        )
    }
}