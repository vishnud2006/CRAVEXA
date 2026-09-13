package com.cravexa

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Address
import com.cravexa.domain.model.AddressType
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.model.Order
import com.cravexa.domain.model.OrderStatus
import com.cravexa.domain.model.PaymentMethod
import com.cravexa.domain.model.PaymentStatus
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.UserProfile
import com.cravexa.domain.model.UserRole
import com.cravexa.domain.repository.AuthRepository
import com.cravexa.domain.usecase.CartCalculator
import com.cravexa.domain.usecase.CreateOrderUseCase
import com.cravexa.domain.usecase.GetAddressesUseCase
import com.cravexa.domain.usecase.GetCartUseCase
import com.cravexa.presentation.checkout.CheckoutEvent
import com.cravexa.presentation.checkout.CheckoutViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getCartUseCase = mockk<GetCartUseCase>()
    private val getAddressesUseCase = mockk<GetAddressesUseCase>()
    private val createOrderUseCase = mockk<CreateOrderUseCase>()
    private val authRepository = mockk<AuthRepository>()
    private val cartCalculator = CartCalculator()

    private val sampleAddress = Address(
        id = "addr_1",
        fullName = "Ananya Sharma",
        phone = "9876543210",
        houseBuilding = "Flat 402, Shanti Nilayam",
        street = "12th Main Road",
        area = "Indiranagar",
        city = "Bengaluru",
        state = "Karnataka",
        pincode = "560038",
        addressType = AddressType.HOME,
        isDefault = true
    )

    private val sampleProduct = Product(
        id = "prod_1",
        name = "Grandma's Andhra Avakaya Mango Pickle",
        description = "Spicy homemade pickle",
        price = 280.0,
        sellerId = "sel_1",
        sellerName = "Lakshmi's Andhra Kitchen",
        categoryId = "cat_pickles",
        categoryName = "Pickles",
        stock = 10,
        available = true
    )

    private val sampleCartItems = listOf(
        CartItem(product = sampleProduct, quantity = 2)
    )

    private val sampleUser = UserProfile(
        id = "usr_100",
        firebaseUid = "fb_100",
        name = "Ananya Sharma",
        role = UserRole.CUSTOMER
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getCartUseCase.getCartItems() } returns flowOf(sampleCartItems)
        every { getAddressesUseCase.observe() } returns flowOf(listOf(sampleAddress))
        coEvery { authRepository.getCurrentUser() } returns sampleUser
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState auto selects default address and calculates pricing`() = runTest(testDispatcher) {
        val viewModel = CheckoutViewModel(
            getCartUseCase = getCartUseCase,
            getAddressesUseCase = getAddressesUseCase,
            cartCalculator = cartCalculator,
            createOrderUseCase = createOrderUseCase,
            authRepository = authRepository
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertNotNull(state.selectedAddress)
        assertEquals("addr_1", state.selectedAddress?.id)
        assertEquals(560.0, state.pricing.subtotal, 0.01)
        assertEquals(0.0, state.pricing.deliveryFee, 0.01) // 560 >= 499
        assertEquals(565.0, state.pricing.total, 0.01)
    }

    @Test
    fun `selecting payment method updates state`() = runTest(testDispatcher) {
        val viewModel = CheckoutViewModel(
            getCartUseCase = getCartUseCase,
            getAddressesUseCase = getAddressesUseCase,
            cartCalculator = cartCalculator,
            createOrderUseCase = createOrderUseCase,
            authRepository = authRepository
        )

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()
        viewModel.selectPaymentMethod(PaymentMethod.COD)
        advanceUntilIdle()

        assertEquals(PaymentMethod.COD, viewModel.uiState.value.selectedPaymentMethod)
    }

    @Test
    fun `placeOrder emits OrderPlacedSuccess on successful creation`() = runTest(testDispatcher) {
        val createdOrder = Order(
            id = "ord_success_1",
            orderNumber = "CRV-77777",
            totalAmount = 565.0,
            orderStatus = OrderStatus.ORDER_PLACED,
            paymentStatus = PaymentStatus.PAID
        )

        coEvery {
            createOrderUseCase(
                items = any(),
                deliveryAddress = any(),
                paymentMethod = any(),
                appliedCoupon = any(),
                couponDiscount = any()
            )
        } returns Resource.Success(createdOrder)

        val viewModel = CheckoutViewModel(
            getCartUseCase = getCartUseCase,
            getAddressesUseCase = getAddressesUseCase,
            cartCalculator = cartCalculator,
            createOrderUseCase = createOrderUseCase,
            authRepository = authRepository
        )

        val emittedEvents = mutableListOf<CheckoutEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.events.collect { emittedEvents.add(it) }
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        advanceUntilIdle()
        viewModel.placeOrder()
        advanceUntilIdle()

        assertEquals(1, emittedEvents.size)
        val event = emittedEvents.first() as CheckoutEvent.OrderPlacedSuccess
        assertEquals("ord_success_1", event.orderId)
        assertEquals("CRV-77777", event.orderNumber)
    }
}

