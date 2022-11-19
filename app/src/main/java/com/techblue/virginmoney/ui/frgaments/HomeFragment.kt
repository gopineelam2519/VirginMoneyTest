package com.techblue.virginmoney.ui.frgaments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techblue.virginmoney.databinding.FragmentHomeBinding
import com.techblue.virginmoney.listeners.NavigationListener

class HomeFragment : Fragment() {

    lateinit var fragmentHomeBinding: FragmentHomeBinding
    lateinit var navigationListener: NavigationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationListener = context as NavigationListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        fragmentHomeBinding.peoplesBtn.setOnClickListener {
            navigationListener.navigateToPersonsPage()
        }
        fragmentHomeBinding.roomsBtn.setOnClickListener {
            navigationListener.navigateToRoomsPage()
        }
    }

}