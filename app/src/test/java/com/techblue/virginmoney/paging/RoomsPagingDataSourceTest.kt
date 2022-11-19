package com.techblue.virginmoney.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.techblue.virginmoney.api.ApiService
import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.models.Room
import com.techblue.virginmoney.testHelper.CoroutineTestRule
import com.techblue.virginmoney.testHelper.StubDataProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class RoomsPagingDataSourceTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val apiService = mockk<ApiService>(relaxed = true)

    lateinit var roomsPagingDataSource: RoomsPagingDataSource

    @Before
    fun setUp() {
        roomsPagingDataSource = RoomsPagingDataSource(apiService)
    }

    @Test
    fun test_failureScenario() = runBlockingTest {
        val error = RuntimeException("404", Throwable())
        coEvery { apiService.getRooms() } throws error

        val expectedResult = PagingSource.LoadResult.Error<Int, Room>(error)

        assertEquals(
            expectedResult, roomsPagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun test_EmptyResponseScenario() = runBlockingTest {

        coEvery { apiService.getRooms() } returns listOf()

        val expectedResult = PagingSource.LoadResult.Page(
            data = listOf(),
            prevKey = null,
            nextKey = null
        )

        assertEquals(
            expectedResult, roomsPagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun test_1stPageAn2ndPageResponseScenario() = runBlockingTest {

        //here we are passing 25 rooms list
        val rooms = StubDataProvider.getRooms()

        coEvery { apiService.getRooms() } returns rooms

        //1st page
        val expectedResult = PagingSource.LoadResult.Page(
            data = rooms.subList(0, 10),
            prevKey = null, //for 1st page prevKey is null
            nextKey = 10
        )

        //assert 1st page response
        assertEquals(
            expectedResult, roomsPagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )

        //2nd page
        val expectedResult2 = PagingSource.LoadResult.Page(
            data = rooms.subList(10, 20),
            prevKey = 10,
            nextKey = 20
        )

        assertEquals(
            expectedResult2, roomsPagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 10,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )

        //3rd page
        val expectedResult3 = PagingSource.LoadResult.Page(
            data = rooms.subList(20, 25),
            prevKey = 20,
            nextKey = null // for last page next is null
        )

        assertEquals(
            expectedResult3, roomsPagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 20,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }
}