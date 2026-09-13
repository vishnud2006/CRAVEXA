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
import com.cravexa.domain.repository.CartRepository
import com.cravexa.domain.repository.OrderRepository
import com.cravexa.domain.usecase.CartCalculator
import com.cravexa.domain.usecase.CreateOrderUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CreateOrderUseCaseTest {

    private val orderRepository = mockk<OrderRepository>()
    private val authRepository = mockk<AuthRepository>()
    private val cartRepository = mockk<CartRepository>()
    private val cartCalculator = CartCalculator()

    private lateinit var createOrderUseCase: CreateOrderUseCase

    private val sampleUser = UserProfile(
        id = "usr_100",
        firebaseUid = "fb_uid_100",
        name = "Ananya Sharma",
        email = "ananya.sharma@example.com",
        role = UserRole.CUSTOMER
    )

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

    @Before
    fun setUp() {
        createOrderUseCase = CreateOrderUseCase(
            orderRepository = orderRepository,
            authRepository = authRepository,
            cartRepository = cartRepository,
            cartCalculator = cartCalculator
        )
    }

    @Test
    fun `unauthenticated user returns error`() = runTest {
        coEvery { authRepository.getCurrentUser() } returns null

        val result = createOrderUseCase(
            items = sampleCartItems,
            deliveryAddress = sampleAddress,
            paymentMethod = PaymentMethod.ONLINE
        )

        assertTrue(result is Resource.Error)
        assertEquals("Please log in to complete your purchase.", result.message)
    }

    @Test
    fun `empty cart returns error`() = runTest {
        coEvery { authRepository.getCurrentUser() } returns sampleUser

        val result = createOrderUseCase(
            items = emptyList(),
            deliveryAddress = sampleAddress,
            paymentMethod = PaymentMethod.ONLINE
        )

        assertTrue(result is Resource.Error)
        assertEquals("Your cart is empty. Please add delicacies before checkout.", result.message)
    }

    @Test
    fun `missing delivery address returns error`() = runTest {
        coEvery { authRepository.getCurrentUser() } returns sampleUser

        val result = createOrderUseCase(
            items = sampleCartItems,
            deliveryAddress = null,
            paymentMethod = PaymentMethod.ONLINE
        )

        assertTrue(result is Resource.Error)
        assertEquals("Please select a complete and valid delivery address.", result.message)
    }

    @Test
    fun `exceeding product stock returns error`() = runTest {
        coEvery { authRepository.getCurrentUser() } returns sampleUser

        val excessiveItems = listOf(
            CartItem(product = sampleProduct.copy(stock = 1), quantity = 5)
        )

        val result = createOrderUseCase(
            items = excessiveItems,
            deliveryAddress = sampleAddress,
            paymentMethod = PaymentMethod.ONLINE
        )

        assertTrue(result is Resource.Error)
        assertTrue(result.message?.contains("Only 1 units") == true)
    }

    @Test
    fun `valid checkout creates order, initializes ORDER_PLACED, and clears cart`() = runTest {
        coEvery { authRepository.getCurrentUser() } returns sampleUser
        coEvery { cartRepository.clearCart() } returns Resource.Success(Unit)
        coEvery { orderRepository.createOrder(any()) } answers {
            val order = firstArg<Order>()
            Resource.Success(order.copy(id = "ord_new_1", orderNumber = "CRV-99999"))
        }

        val result = createOrderUseCase(
            items = sampleCartItems,
            deliveryAddress = sampleAddress,
            paymentMethod = PaymentMethod.ONLINE,
            appliedCoupon = "CRAVE10",
            couponDiscount = 50.0
        )

        assertTrue(result is Resource.Success)
        val createdOrder = result.data!!
        assertEquals("ord_new_1", createdOrder.id)
        assertEquals("CRV-99999", createdOrder.orderNumber)
        assertEquals(OrderStatus.ORDER_PLACED, createdOrder.orderStatus)
        assertEquals(PaymentStatus.PAID, createdOrder.paymentStatus)
        assertEquals(560.0, createdOrder.subtotal, 0.01) // 2 * 280
        assertEquals(515.0, createdOrder.totalAmount, 0.01) // 560 + 0 + 5 - 50

        coVerify { cartRepository.clearCart() }
    }

    @Test
    fun `cash on delivery sets payment status to PENDING`() = runTest {
        coEvery { authRepository.getCurrentUser() } returns sampleUser
        coEvery { cartRepository.clearCart() } returns Resource.Success(Unit)
        coEvery { orderRepository.createOrder(any()) } answers {
            val order = firstArg<Order>()
            Resource.Success(order.copy(id = "ord_new_2", orderNumber = "CRV-88888"))
        }

        val result = createOrderUseCase(
            items = sampleCartItems,
            deliveryAddress = sampleAddress,
            paymentMethod = PaymentMethod.COD
        )

        assertTrue(result is Resource.Success)
        assertEquals(PaymentStatus.PENDING, result.data?.paymentStatus)
    }
}

