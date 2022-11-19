package com.techblue.virginmoney.ui.frgaments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.techblue.virginmoney.R
import com.techblue.virginmoney.databinding.FragmentPersonDetailBinding
import com.techblue.virginmoney.viewmodel.SharedViewModel

class PersonDetailFragment : Fragment() {

    private lateinit var fragmentPersonDetailBinding: FragmentPersonDetailBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        fragmentPersonDetailBinding = FragmentPersonDetailBinding.inflate(layoutInflater)
        return fragmentPersonDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initObservers()
    }

    private fun initObservers() {
        sharedViewModel.selectedPersonLiveData.observe(viewLifecycleOwner) { it ->
            it.let { person ->
                if (!person.avatar.isNullOrEmpty()) {
                    fragmentPersonDetailBinding.personDetailAvatarImg.contentDescription = "${person.firstName} ${person.lastName} Profile picture"
                    Glide.with(fragmentPersonDetailBinding.personDetailAvatarImg.context).load(person.avatar).placeholder(R.drawable.ic_baseline_broken_image_24).into(fragmentPersonDetailBinding.personDetailAvatarImg)
                }
                fragmentPersonDetailBinding.personDetailNameTxt.text = "${person.firstName} ${person.lastName}"
                fragmentPersonDetailBinding.personDetailNameTxt.contentDescription = "${person.firstName} ${person.lastName}"

                fragmentPersonDetailBinding.personDetailJobTitleTxt.text = resources.getString(R.string.jobTitleDynamic, person.jobtitle)
                fragmentPersonDetailBinding.personDetailJobTitleTxt.contentDescription = resources.getString(R.string.jobTitleDynamic, person.jobtitle)

                fragmentPersonDetailBinding.personDetailEmailTxt.text = resources.getString(R.string.emailIdDynamic, person.email)
                fragmentPersonDetailBinding.personDetailEmailTxt.contentDescription = resources.getString(R.string.emailIdDynamic, person.email)
            }
        }
    }
}