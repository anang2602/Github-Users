package com.anangsw.githubuser.data.repository.datasource

import com.anangsw.githubuser.TestCoroutineRule
import com.anangsw.githubuser.data.cache.GithubUserDatabase
import com.anangsw.githubuser.data.remote.GithubService
import com.anangsw.githubuser.data.remote.dto.UserResponse
import com.anangsw.githubuser.data.repository.remotemediator.GithubUserRemoteMediator
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class GithubDatasourceTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val coroutineDispatcher = TestCoroutineDispatcher()

    private val service = mockk<GithubService>(relaxed = true)

    private val remoteMediator = mockk<GithubUserRemoteMediator>(relaxed = true)

    private val githubDb = mockk<GithubUserDatabase>(relaxed = true)

    private val githubDatasource = GithubDatasource(
        githubDb,
        remoteMediator,
        service,
        coroutineDispatcher
    )

    @Test
    fun `should check if it get user by name and update database`()  = testCoroutineRule.runBlockingTest {
        // given
        val fakeResult = UserResponse(
            "", "", "", "",
            "", "", "", 1,
            "", 1, "", "",
            "", "", "", 1, "", "",
            "","","",1,1,"",
            "",false,"","","",
            "","",""
        )
        // when
        coEvery { service.getUser("") } returns Response.success(fakeResult)

        githubDatasource.letUserFlow("").toList()

        //then
        coVerifyOrder {
            // try get data from db
            githubDb.GithubUserDao().getUser("")

            // fetch data from api
            service.getUser("")

            // update user in db
            githubDb.GithubUserDao().updateUser(1, "", "")

            // return updated
            githubDb.GithubUserDao().getUser("")
        }
    }

}