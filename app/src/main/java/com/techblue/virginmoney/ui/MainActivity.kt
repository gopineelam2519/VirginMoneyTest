package com.techblue.virginmoney.ui

import android.app.ProgressDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.techblue.virginmoney.BuildConfig
import com.techblue.virginmoney.R
import com.techblue.virginmoney.databinding.ActivityMainBinding
import com.techblue.virginmoney.listeners.NavigationListener
import com.techblue.virginmoney.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationListener {

    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var mProgressDialog: ProgressDialog
    lateinit var navController: NavController

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        setUpToolbarWithNavigationComponent()
        prepareProgress()
    }

    private fun setUpToolbarWithNavigationComponent() {
        setupActionBarWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.personDetailFragment -> {
                    supportActionBar?.title = "${sharedViewModel.selectedPersonLiveData.value?.firstName} Profile"
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragmentContainerView).navigateUp()
    }

    //deprecated progress dialog for temporary purpose
    private fun prepareProgress() {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Please wait...")
    }


    override fun navigateToPersonsPage() {
        navController.navigate(R.id.action_homeFragment_to_peoplesFragment)
    }

    override fun navigateToRoomsPage() {
        navController.navigate(R.id.action_homeFragment_to_roomsFragment)
    }

    override fun navigateToPersonDetailsPage() {
        navController.navigate(R.id.action_peoplesFragment_to_personDetailFragment)
    }

    override fun showProgress() {
        if (this::mProgressDialog.isInitialized && !BuildConfig.IS_TESTING.get()) {
            mProgressDialog.show()
        }
    }

    override fun hideProgress() {
        if (this::mProgressDialog.isInitialized && mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }
}