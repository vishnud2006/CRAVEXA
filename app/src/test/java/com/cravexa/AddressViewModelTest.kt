package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.AddressType
import com.cravexa.domain.usecase.AddAddressUseCase
import com.cravexa.domain.usecase.DeleteAddressUseCase
import com.cravexa.domain.usecase.GetAddressesUseCase
import com.cravexa.domain.usecase.SetDefaultAddressUseCase
import com.cravexa.domain.usecase.UpdateAddressUseCase
import com.cravexa.presentation.customer.address.AddressUiState
import com.cravexa.presentation.customer.address.AddressViewModel
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
class AddressViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getAddressesUseCase = mockk<GetAddressesUseCase>()
    private val addAddressUseCase = mockk<AddAddressUseCase>()
    private val updateAddressUseCase = mockk<UpdateAddressUseCase>()
    private val deleteAddressUseCase = mockk<DeleteAddressUseCase>()
    private val setDefaultAddressUseCase = mockk<SetDefaultAddressUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getAddressesUseCase.observe() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveAddress with missing fields sets validation errors`() = runTest(testDispatcher) {
        val viewModel = AddressViewModel(
            getAddressesUseCase,
            addAddressUseCase,
            updateAddressUseCase,
            deleteAddressUseCase,
            setDefaultAddressUseCase
        )
        advanceUntilIdle()

        viewModel.setupForAdd()
        viewModel.saveAddress()

        assertNotNull(viewModel.nameError.value)
        assertNotNull(viewModel.phoneError.value)
        assertNotNull(viewModel.houseError.value)
        assertNotNull(viewModel.streetError.value)
        assertNotNull(viewModel.areaError.value)
        assertNotNull(viewModel.pincodeError.value)
        assertEquals(AddressUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `saveAddress with valid data adds address and sets Success state`() = runTest(testDispatcher) {
        val address = Address(
            id = "addr_1",
            fullName = "Ananya Sharma",
            phone = "9876543210",
            houseBuilding = "Flat 402",
            street = "12th Main Road",
            area = "Indiranagar",
            city = "Bengaluru",
            state = "Karnataka",
            pincode = "560038",
            addressType = AddressType.HOME
        )
        coEvery { addAddressUseCase(any()) } returns Resource.Success(address)

        val viewModel = AddressViewModel(
            getAddressesUseCase,
            addAddressUseCase,
            updateAddressUseCase,
            deleteAddressUseCase,
            setDefaultAddressUseCase
        )
        advanceUntilIdle()

        viewModel.setupForAdd()
        viewModel.onFullNameChanged("Ananya Sharma")
        viewModel.onPhoneChanged("9876543210")
        viewModel.onHouseBuildingChanged("Flat 402")
        viewModel.onStreetChanged("12th Main Road")
        viewModel.onAreaChanged("Indiranagar")
        viewModel.onCityChanged("Bengaluru")
        viewModel.onStateChanged("Karnataka")
        viewModel.onPincodeChanged("560038")

        viewModel.saveAddress()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is AddressUiState.Success)
        coVerify(exactly = 1) { addAddressUseCase(any()) }
    }

    @Test
    fun `deleteAddress triggers deleteAddressUseCase`() = runTest(testDispatcher) {
        coEvery { deleteAddressUseCase("addr_123") } returns Resource.Success(Unit)

        val viewModel = AddressViewModel(
            getAddressesUseCase,
            addAddressUseCase,
            updateAddressUseCase,
            deleteAddressUseCase,
            setDefaultAddressUseCase
        )
        viewModel.deleteAddress("addr_123")
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteAddressUseCase("addr_123") }
    }

    @Test
    fun `setDefault triggers setDefaultAddressUseCase`() = runTest(testDispatcher) {
        coEvery { setDefaultAddressUseCase("addr_123") } returns Resource.Success(Unit)

        val viewModel = AddressViewModel(
            getAddressesUseCase,
            addAddressUseCase,
            updateAddressUseCase,
            deleteAddressUseCase,
            setDefaultAddressUseCase
        )
        viewModel.setDefault("addr_123")
        advanceUntilIdle()

        coVerify(exactly = 1) { setDefaultAddressUseCase("addr_123") }
    }
}

