package com.example.toycamping.ui.mypage

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.databinding.ResetPassFragmentBinding

class ResetPassFragment : BaseFragment<ResetPassFragmentBinding>(R.layout.reset_pass_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_back, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        setToolbarVisibility(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.back -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_route, LoginFragment()).commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}