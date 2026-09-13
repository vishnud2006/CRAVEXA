package com.cravexa

import androidx.lifecycle.SavedStateHandle
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SellerProfile
import com.cravexa.domain.usecase.AddSellerProductUseCase
import com.cravexa.domain.usecase.GetCategoriesUseCase
import com.cravexa.domain.usecase.GetSellerProductByIdUseCase
import com.cravexa.domain.usecase.GetSellerProfileUseCase
import com.cravexa.domain.usecase.UpdateSellerProductUseCase
import com.cravexa.domain.usecase.UploadProductImageUseCase
import com.cravexa.presentation.seller.products.AddEditProductUiState
import com.cravexa.presentation.seller.products.AddEditProductViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditProductViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSellerProductByIdUseCase = mockk<GetSellerProductByIdUseCase>()
    private val addSellerProductUseCase = mockk<AddSellerProductUseCase>()
    private val updateSellerProductUseCase = mockk<UpdateSellerProductUseCase>()
    private val getSellerProfileUseCase = mockk<GetSellerProfileUseCase>()
    private val getCategoriesUseCase = mockk<GetCategoriesUseCase>()
    private val uploadProductImageUseCase = mockk<UploadProductImageUseCase>()

    private val testProfile = SellerProfile(
        id = "sel_1",
        sellerName = "Lakshmi Devi",
        businessName = "Lakshmi's Home Kitchen",
        city = "Guntur",
        state = "Andhra Pradesh"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getSellerProfileUseCase.observe() } returns flowOf(testProfile)
        coEvery { addSellerProductUseCase(any()) } answers { Resource.Success(firstArg()) }
        coEvery { updateSellerProductUseCase(any()) } answers { Resource.Success(firstArg()) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `validation fails if name or price is invalid`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle()
        val viewModel = AddEditProductViewModel(
            savedStateHandle,
            getSellerProductByIdUseCase,
            addSellerProductUseCase,
            updateSellerProductUseCase,
            getSellerProfileUseCase,
            getCategoriesUseCase,
            uploadProductImageUseCase
        )

        viewModel.onNameChanged("")
        viewModel.onPriceChanged("0")
        viewModel.saveProduct()
        advanceUntilIdle()

        assertNotNull(viewModel.nameError.value)
        assertNotNull(viewModel.priceError.value)
        coVerify(exactly = 0) { addSellerProductUseCase(any()) }
    }

    @Test
    fun `saveProduct adds product when form is valid`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle()
        val viewModel = AddEditProductViewModel(
            savedStateHandle,
            getSellerProductByIdUseCase,
            addSellerProductUseCase,
            updateSellerProductUseCase,
            getSellerProfileUseCase,
            getCategoriesUseCase,
            uploadProductImageUseCase
        )

        viewModel.onNameChanged("Fresh Mysore Pak")
        viewModel.onDescriptionChanged("Handmade with pure desi ghee")
        viewModel.onPriceChanged("400")
        viewModel.onStockChanged("15")

        viewModel.saveProduct()
        advanceUntilIdle()

        coVerify { addSellerProductUseCase(any()) }
        assertTrue(viewModel.uiState.value is AddEditProductUiState.Success)
    }
}

