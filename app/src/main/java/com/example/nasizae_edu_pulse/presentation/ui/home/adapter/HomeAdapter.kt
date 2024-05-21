package com.example.edupulse.presentation.ui.home.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.edupulse.presentation.ui.home.library.LibraryFragment
import com.example.edupulse.presentation.ui.home.tasks.TasksFragment

class HomeAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private var fragmentArgs: Bundle? = null

    fun setArguments(args: Bundle) {
        fragmentArgs = args
    }

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> TasksFragment()
            1 -> LibraryFragment()
            else -> TasksFragment()
        }
        fragment.arguments=fragmentArgs
        return fragment
    }
}