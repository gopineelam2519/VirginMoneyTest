package com.techblue.virginmoney.ui.frgaments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.techblue.virginmoney.adapters.PersonsPagingAdapter
import com.techblue.virginmoney.databinding.FragmentPeoplesBinding
import com.techblue.virginmoney.listeners.ItemCountListener
import com.techblue.virginmoney.listeners.NavigationListener
import com.techblue.virginmoney.listeners.PersonItemClickListener
import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.utils.observeLoader
import com.techblue.virginmoney.utils.prepareLinearList
import com.techblue.virginmoney.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PeoplesFragment : Fragment(), PersonItemClickListener {

    lateinit var fragmentPeoplesBinding: FragmentPeoplesBinding
    lateinit var navigationListener: NavigationListener

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val peoplesPagingAdapter = PersonsPagingAdapter(this)

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
        fragmentPeoplesBinding = FragmentPeoplesBinding.inflate(layoutInflater)
        return fragmentPeoplesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        //here observeLoader is a higher level extension function. call on any adapter which extends PagingDataAdapter
        peoplesPagingAdapter.observeLoader(navigationListener, context, object : ItemCountListener {
            override fun onItemCountChangeListener(itemCount: Int) {
                if (itemCount == 0) {
                    fragmentPeoplesBinding.peoplesList.visibility = GONE
                    fragmentPeoplesBinding.emptyListMsgTxt.visibility = VISIBLE
                } else {
                    fragmentPeoplesBinding.peoplesList.visibility = VISIBLE
                    fragmentPeoplesBinding.emptyListMsgTxt.visibility = GONE
                }
            }
        })
        initRecyclerView()
        initObservers()
    }

    private fun initRecyclerView() {
        //here prepareLinearList is a higher level extension function.
        fragmentPeoplesBinding.peoplesList.prepareLinearList(peoplesPagingAdapter)
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            sharedViewModel.getPersons().collectLatest {
                peoplesPagingAdapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onPersonItemClickListener(person: Person) {
        sharedViewModel.setSelectedPerson(person)
        navigationListener.navigateToPersonDetailsPage()
    }
}