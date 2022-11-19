package com.techblue.virginmoney.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.techblue.virginmoney.api.ApiService
import com.techblue.virginmoney.models.Person
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

@ExperimentalCoroutinesApi
class PagingDataSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val apiService = mockk<ApiService>(relaxed = true)

    lateinit var pagingDataSource: PagingDataSource

    @Before
    fun setUp() {
        pagingDataSource = PagingDataSource(apiService)
    }

    @Test
    fun test_failureScenario() = runBlockingTest {
        val error = RuntimeException("404", Throwable())
        coEvery { apiService.getPersons() } throws error

        val expectedResult = PagingSource.LoadResult.Error<Int, Person>(error)

        assertEquals(
            expectedResult, pagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun test_EmptyResponseScenario() = runBlockingTest {

        coEvery { apiService.getPersons() } returns listOf()

        val expectedResult = PagingSource.LoadResult.Page(
            data = listOf(),
            prevKey = null,
            nextKey = null
        )

        assertEquals(
            expectedResult, pagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun test_1stPageAn2ndPageResponseScenario() = runBlockingTest {

        //here we are passing 25 persons list
        val persons = StubDataProvider.getPersons()

        coEvery { apiService.getPersons() } returns persons

        //1st page
        val expectedResult = PagingSource.LoadResult.Page(
            data = persons.subList(0, 10),
            prevKey = null, //for 1st page prevKey is null
            nextKey = 2
        )

        //assert 1st page response
        assertEquals(
            expectedResult, pagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )

        //2nd page
        val expectedResult2 = PagingSource.LoadResult.Page(
            data = persons.subList(10, 20),
            prevKey = 2,
            nextKey = 3
        )

        assertEquals(
            expectedResult2, pagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 2,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )

        //3rd page
        val expectedResult3 = PagingSource.LoadResult.Page(
            data = persons.subList(20, 25),
            prevKey = 3,
            nextKey = null // for last page next is null
        )

        assertEquals(
            expectedResult3, pagingDataSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 3,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
        )
    }
}