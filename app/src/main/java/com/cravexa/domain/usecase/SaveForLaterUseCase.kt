package com.cravexa.domain.usecase

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.CartItem
import com.cravexa.domain.repository.CartRepository
import com.cravexa.domain.repository.WishlistRepository
import javax.inject.Inject

class SaveForLaterUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository
) {
    suspend operator fun invoke(item: CartItem): Resource<Unit> {
        return try {
            wishlistRepository.toggleWishlist(item.product)
            cartRepository.removeFromCart(item.product.id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to save item for later.")
        }
    }
}

