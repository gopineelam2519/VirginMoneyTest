package com.techblue.virginmoney.ui


import android.content.res.Resources
import android.widget.TextView
import androidx.lifecycle.liveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.techblue.virginmoney.BuildConfig
import com.techblue.virginmoney.R
import com.techblue.virginmoney.api.MainRepositoryImpl
import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.models.Room
import com.techblue.virginmoney.testHelper.RecyclerViewUtils.atPosition
import com.techblue.virginmoney.testHelper.StubDataProvider
import com.techblue.virginmoney.testHelper.lazyActivityScenarioRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    val lazyActivityScenarioRule = lazyActivityScenarioRule<MainActivity>(launchActivity = false)

    lateinit var resources: Resources

    @BindValue
    @JvmField
    val mainRepositoryImpl = mockk<MainRepositoryImpl>(relaxed = true)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun before() {
        BuildConfig.IS_TESTING.set(true)
        ViewActions.closeSoftKeyboard()
        resources = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext.resources
    }

    @Test
    fun test_launchActivity() {

        lazyActivityScenarioRule.launch()
        onView(withId(R.id.peoplesBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.peoplesBtn)).check(matches(withText(resources.getString(R.string.peoples))))

        onView(withId(R.id.roomsBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.roomsBtn)).check(matches(withText(resources.getString(R.string.rooms))))
    }

    @Test
    fun test_PeoplesFragmentIsLaunchedOrNot() {
        every { mainRepositoryImpl.getPersons() } returns flow { emit(PagingData.from(StubDataProvider.getPersons())) }
        lazyActivityScenarioRule.launch()

        onView(withId(R.id.peoplesBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.peoplesBtn)).perform(click())

        //check for toolbar title profile
        onView(allOf(instanceOf(TextView::class.java), withParent(withResourceName("action_bar")))).check(matches(withText(resources.getString(R.string.peoples))))

        //assert recyclerview item views
        onView(withId(R.id.peoplesList))
            .check(matches(atPosition(0, hasDescendant(withText("${StubDataProvider.getPersons()[0].firstName} ${StubDataProvider.getPersons()[0].lastName}")))))
        onView(withId(R.id.peoplesList))
            .check(matches(atPosition(0, hasDescendant(withText("${StubDataProvider.getPersons()[0].jobtitle}")))))
    }

    @Test
    fun test_checkEmptyViewIsVisibleOrNot_inCaseOfNoData() {
        every { mainRepositoryImpl.getPersons() } returns flow { emit(PagingData.empty()) }
        lazyActivityScenarioRule.launch()

        onView(withId(R.id.peoplesBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.peoplesBtn)).perform(click())

        //check for toolbar title profile
        onView(allOf(instanceOf(TextView::class.java), withParent(withResourceName("action_bar")))).check(matches(withText(resources.getString(R.string.peoples))))

        //check empty list msg txt
        onView(withId(R.id.emptyListMsg_txt)).check(matches(isDisplayed()))
        onView(withId(R.id.emptyListMsg_txt)).check(matches(withText(resources.getString(R.string.emptyList_msg))))
    }

    @Test
    fun test_checkOnPersonListItemClickNavigatesToDetailsFragment() {
        test_PeoplesFragmentIsLaunchedOrNot()

        val person: Person = StubDataProvider.getPersons()[0]
        //perform click on 1 st item
        onView(withId(R.id.peoplesList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        //check for selected person
        checkForPersonDetails(person)

        //perform backPress
        Espresso.pressBack()

        val personTwo: Person = StubDataProvider.getPersons()[1]
        //perform click on 2nd item
        onView(withId(R.id.peoplesList)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        //check for selected person
        checkForPersonDetails(personTwo)
    }

    private fun checkForPersonDetails(person: Person) {
        //check for details fragment
        onView(allOf(instanceOf(TextView::class.java), withParent(withResourceName("action_bar")))).check(matches(withText("${person.firstName} Profile")))

        //check for person details
        onView(withId(R.id.personDetailName_txt)).check(matches(isDisplayed()))
        onView(withId(R.id.personDetailName_txt)).check(matches(withText("${person.firstName} ${person.lastName}")))

        onView(withId(R.id.personDetailJobTitle_txt)).check(matches(isDisplayed()))
        onView(withId(R.id.personDetailJobTitle_txt)).check(matches(withText(resources.getString(R.string.jobTitleDynamic, person.jobtitle))))

        onView(withId(R.id.personDetailEmail_txt)).check(matches(isDisplayed()))
        onView(withId(R.id.personDetailEmail_txt)).check(matches(withText(resources.getString(R.string.emailIdDynamic, person.email))))
    }

    @Test
    fun test_performClickOnRoomsBtnAndCheckRoomsFragment() {
        every { mainRepositoryImpl.getRooms() } returns liveData {
            emit(
                PagingData.from(StubDataProvider.getRooms())
            )
        }
        lazyActivityScenarioRule.launch()

        onView(withId(R.id.roomsBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.roomsBtn)).perform(click())

        //check for toolbar title rooms
        onView(allOf(instanceOf(TextView::class.java), withParent(withResourceName("action_bar")))).check(matches(withText(resources.getString(R.string.rooms))))
    }

    @Test
    fun test_checkItemsInRoomsList() {
        test_performClickOnRoomsBtnAndCheckRoomsFragment()

        val room: Room = StubDataProvider.getRooms()[0]
        //assert recyclerview item views
        onView(withId(R.id.roomsList))
            .check(matches(atPosition(0, hasDescendant(withText(if (room.isOccupied) "Occupied" else "Available")))))
        onView(withId(R.id.roomsList))
            .check(matches(atPosition(0, hasDescendant(withText("Capacity: ${room.maxOccupancy}")))))
    }
}