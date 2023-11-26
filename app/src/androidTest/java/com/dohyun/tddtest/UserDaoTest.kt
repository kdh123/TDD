package com.dohyun.tddtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.dohyun.tddtest.data.datasource.UserDao
import com.dohyun.tddtest.data.model.User
import com.dohyun.tddtest.data.room.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class UserDaoTest {

    @get:Rule
    val mainCoroutineRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var dao: UserDao

    @Before
    fun setup() {
        appDatabase = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java, "tdd"
        ).build()
        dao = appDatabase.userDao()
    }

    @Test
    fun saveUser() {
        val user = User(userId = "dhkim123", password = "12345678", name = "홍길동", address = "서울시 강남구")
        dao.saveUser(user = user)

        val getUser = dao.getUser("dhkim123", "12345678").getOrAwaitValue()

        assertEquals(getUser.name, "홍길동")
    }

    @Test
    fun deleteUser() = runBlocking {
        val user = User(userId = "dhkim123", password = "1234", name = "홍길동2", address = "서울시 강남구")

        dao.saveUser(user = user)
        dao.delete(user = user)
    }
}