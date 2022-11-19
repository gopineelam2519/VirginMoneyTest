package com.techblue.virginmoney.ui.frgaments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.techblue.virginmoney.adapters.RoomsPagingAdapter
import com.techblue.virginmoney.databinding.FragmentRoomsBinding
import com.techblue.virginmoney.listeners.ItemCountListener
import com.techblue.virginmoney.listeners.NavigationListener
import com.techblue.virginmoney.utils.observeLoader
import com.techblue.virginmoney.utils.prepareGridList
import com.techblue.virginmoney.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

class RoomsFragment : Fragment() {

    private lateinit var fragmentRoomsBinding: FragmentRoomsBinding
    lateinit var navigationListener: NavigationListener

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val roomsPagingAdapter = RoomsPagingAdapter()

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
        fragmentRoomsBinding = FragmentRoomsBinding.inflate(layoutInflater)
        return fragmentRoomsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        roomsPagingAdapter.observeLoader(navigationListener, context, object : ItemCountListener {
            override fun onItemCountChangeListener(itemCount: Int) {
                if (itemCount == 0) {
                    fragmentRoomsBinding.roomsList.visibility = View.GONE
                    fragmentRoomsBinding.emptyListMsg2Txt.visibility = View.VISIBLE
                } else {
                    fragmentRoomsBinding.roomsList.visibility = View.VISIBLE
                    fragmentRoomsBinding.emptyListMsg2Txt.visibility = View.GONE
                }
            }

        })
        initObservers()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        fragmentRoomsBinding.roomsList.prepareGridList(roomsPagingAdapter)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            sharedViewModel.getRooms().observe(viewLifecycleOwner) { persons ->
                persons?.let {
                    roomsPagingAdapter.submitData(lifecycle, it)
                }
            }
        }
    }
}