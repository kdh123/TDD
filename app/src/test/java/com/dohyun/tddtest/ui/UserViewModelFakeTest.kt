package com.dohyun.tddtest.ui

import com.dohyun.tddtest.common.UserFailEvent
import com.dohyun.tddtest.data.model.User
import com.dohyun.tddtest.data.repository.FakeUserRepositoryImpl
import com.dohyun.tddtest.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserViewModelFakeTest {

    lateinit var userViewModel: UserViewModel
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = FakeUserRepositoryImpl()
        userViewModel = UserViewModel(userRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `ID가 빈 값일 때`() = runTest {
        userViewModel.getUser(userId = "", password = "nudgeHealthcare")

        val userState = userViewModel.state.first()
        assertEquals(userState, UserState.Fail(failEvent = UserFailEvent.NoUserId))
    }

    @Test
    fun `passwrod가 8자 미만일 경우`() = runTest {
        userViewModel.getUser(userId = "cashwalk", password = "1234")

        val userState = userViewModel.state.first()
        assertEquals(userState, UserState.Fail(failEvent = UserFailEvent.ShortPassword))
    }

    @Test
    fun `유저를 찾았을 때`() = runTest {
        val user = User(userId = "cashwalk", password = "12345678910", name = "홍길동", address = "서울시 강남구")

        userViewModel.saveUser(user = user)
        userViewModel.getUser(userId = "cashwalk", password = "12345678910")

        val userState = userViewModel.state.first()
        assertEquals(userState, UserState.Success(user = user))
    }
}