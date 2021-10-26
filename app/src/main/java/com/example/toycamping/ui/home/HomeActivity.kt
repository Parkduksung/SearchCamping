package com.example.toycamping.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
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
        val list = listOf(MapFragment(), BookmarkFragment(), SnapFragment())

        val pagerAdapter = FragmentPagerAdapter(list, this)
        val titles = listOf("Map", "BookMark", "Snap")

        with(binding) {
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = 4
            viewPager.isUserInputEnabled = false

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()
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