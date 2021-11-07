package com.example.toycamping.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.toycamping.BuildConfig
import com.example.toycamping.R
import com.example.toycamping.base.BaseActivity
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.ActivityHomeBinding
import com.example.toycamping.ext.showDialog
import com.example.toycamping.ext.showToast
import com.example.toycamping.ui.mypage.DialogFragment
import com.example.toycamping.ui.mypage.MyPageFragment
import com.example.toycamping.ui.snap.SnapFragment
import com.example.toycamping.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val homeViewModel by viewModel<HomeViewModel>()

    private val tabConfigurationStrategy =
        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.icon = resources.obtainTypedArray(R.array.array_tab_icon).getDrawable(position)
        }

    private var backWait: Long = INIT_TIME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUi()
        initViewModel()
    }

    private fun initViewModel() {
        lifecycle.addObserver(homeViewModel)
        homeViewModel.viewStateLiveData.observe(this) { viewState: ViewState? ->
            (viewState as? HomeViewModel.HomeViewState)?.let { onChangedViewState(viewState) }
        }
    }

    private fun onChangedViewState(viewState: HomeViewModel.HomeViewState) {
        when (viewState) {

            is HomeViewModel.HomeViewState.Error -> {
                showToast(message = viewState.errorMessage)
            }

            is HomeViewModel.HomeViewState.AddBookmarkItem -> {
                showToast(message = "즐겨찾기가 추가되었습니다.")
            }

            is HomeViewModel.HomeViewState.DeleteBookmarkItem -> {
                showToast(message = "즐겨찾기가 제거되었습니다.")
            }

            is HomeViewModel.HomeViewState.StartLoginView -> {
                showDialog(
                    title = "로그인 화면으로 이동하나요?",
                    detail = "즐겨찾기, 스냅 기능을 사용하시려면 로그인이 필요합니다!",
                    titleButton = "이동",
                    type = "loginView"
                ) { _, result ->

                    val getResultType =
                        result.getString(DialogFragment.TYPE)

                    if (getResultType == "loginView") {
                        binding.viewPager.currentItem = 4
                    }
                }
            }

        }
    }

    private fun initUi() {
        val list = listOf(MapFragment(), BookmarkFragment(), SnapFragment(), MyPageFragment())

        val pagerAdapter = FragmentPagerAdapter(list, this)


        with(binding) {
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = 5
            viewPager.isUserInputEnabled = false

            TabLayoutMediator(tabLayout, viewPager, tabConfigurationStrategy).attach()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == MapFragment.REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE) {

            when {
                grantResults.isEmpty() -> {
                    showToast(message = "권한이 없습니다.")
                }

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    showToast(message = "권한이 허용되었습니다.")
                    homeViewModel.permissionGrant()
                }

                else -> {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.APPLICATION_ID,
                        null
                    )
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backWait >= LIMIT_TIME) {
            backWait = System.currentTimeMillis()
            showToast(message = "뒤로가기 버튼을 한번 더 누르면 종료됩니다.")
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val INIT_TIME = 0L
        private const val LIMIT_TIME = 2000

    }
}

class FragmentPagerAdapter(
    private val fragmentList: List<Fragment>,
    fragmentActivity: FragmentActivity
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = fragmentList.size
    override fun createFragment(position: Int) = fragmentList[position]

}