package com.example.smartgardendemo

import com.example.smartgardendemo.data.model.HistorialEntry
import com.example.smartgardendemo.viewmodel.HistorialViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistorialViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial tiene isLoading true e historial vacio`() {
        val fakeRepo = FakeHistorialRepository()
        val viewModel = HistorialViewModel(fakeRepo)

        assertTrue(viewModel.isLoading.value)
        assertTrue(viewModel.historial.value.isEmpty())
    }

    @Test
    fun `al emitir historial se actualiza la lista y loading cambia a false`() = runTest(testDispatcher) {
        val fakeRepo = FakeHistorialRepository()
        val viewModel = HistorialViewModel(fakeRepo)

        val entries = listOf(
            HistorialEntry(id = "1", sensorHumedad = 0, timestamp = 1000L),
            HistorialEntry(id = "2", sensorHumedad = 1, timestamp = 2000L)
        )
        fakeRepo.historial.value = entries
        advanceUntilIdle()

        assertEquals(2, viewModel.historial.value.size)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `cuando historial esta vacio se mantiene el estado vacio`() = runTest(testDispatcher) {
        val fakeRepo = FakeHistorialRepository()
        val viewModel = HistorialViewModel(fakeRepo)

        fakeRepo.historial.value = emptyList()
        advanceUntilIdle()

        assertTrue(viewModel.historial.value.isEmpty())
        assertEquals(false, viewModel.isLoading.value)
    }
}
