package com.example.toycamping.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.toycamping.BuildConfig
import com.example.toycamping.R
import com.example.toycamping.base.BaseActivity
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.ActivityHomeBinding
import com.example.toycamping.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUi()
        initViewModel()
    }

    private fun initViewModel() {
        homeViewModel.viewStateLiveData.observe(this) { viewState: ViewState? ->
            (viewState as? HomeViewModel.HomeViewState)?.let { onChangedViewState(viewState) }
        }
    }

    private fun onChangedViewState(viewState: HomeViewModel.HomeViewState) {
        when (viewState) {

            is HomeViewModel.HomeViewState.Error -> {
                Toast.makeText(this, viewState.errorMessage, Toast.LENGTH_SHORT).show()
            }

            is HomeViewModel.HomeViewState.AddBookmark -> {
                Toast.makeText(this, "즐겨찾기가 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }

            is HomeViewModel.HomeViewState.DeleteBookmark -> {
                Toast.makeText(this, "즐겨찾기가 제거되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initUi() {
        val list = listOf(MapFragment(), BookmarkFragment(), SnapFragment(), MyPageFragment())

        val pagerAdapter = FragmentPagerAdapter(list, this)
        val titles = listOf("Map", "BookMark", "Snap", "MyPage")

        with(binding) {
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = 5
            viewPager.isUserInputEnabled = false

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()
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
                    Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
                }

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
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
}

class FragmentPagerAdapter(
    private val fragmentList: List<Fragment>,
    fragmentActivity: FragmentActivity
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = fragmentList.size
    override fun createFragment(position: Int) = fragmentList[position]

}