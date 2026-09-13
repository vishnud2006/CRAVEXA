package com.cravexa.data.repository

import com.cravexa.R
import com.cravexa.core.common.Resource
import com.cravexa.domain.model.Category
import com.cravexa.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor() : CategoryRepository {

    private val categories = listOf(
        Category(
            id = "cat_pickles",
            name = "Handmade Pickles",
            description = "Sun-ripened mangoes, lemons, red chilis and seasonal berries prepared using traditional sun-drying and cold-pressed mustard & sesame oils.",
            iconRes = R.drawable.ic_onboarding_homemade,
            itemCount = 24
        ),
        Category(
            id = "cat_spices",
            name = "Artisanal Spices & Podis",
            description = "Stone-ground masala powders, roasted sambar podis, aromatic garam masalas and heritage seed mixes ground fresh in small batches.",
            iconRes = R.drawable.ic_onboarding_regional,
            itemCount = 18
        ),
        Category(
            id = "cat_snacks",
            name = "Crunchy Snacks & Namkeens",
            description = "Handmade ribbon pakodas, buttery chaklis, authentic banana chips crisped in pure coconut oil, and spicy mixture crunchies.",
            iconRes = R.drawable.ic_onboarding_creators,
            itemCount = 20
        ),
        Category(
            id = "cat_sweets",
            name = "Traditional Indian Sweets",
            description = "Rich desi ghee Mysore Pak, melt-in-mouth Tirunelveli halwa, motichoor laddoos, and dry fruit barfis crafted with pure organic jaggery.",
            iconRes = R.drawable.ic_onboarding_welcome,
            itemCount = 16
        ),
        Category(
            id = "cat_regional",
            name = "Regional Culinary Specialties",
            description = "Hyper-local heirloom recipes from Konkan, Chettinad, Malabar, Awadh, Kathiawar, and Bengal heritage home kitchens.",
            iconRes = R.drawable.ic_onboarding_regional,
            itemCount = 22
        ),
        Category(
            id = "cat_traditional",
            name = "Heritage Family Recipes",
            description = "Secret grandmothers' recipes perfected across three generations, made with zero synthetic preservatives or artificial food coloring.",
            iconRes = R.drawable.ic_onboarding_homemade,
            itemCount = 15
        ),
        Category(
            id = "cat_baked",
            name = "Fresh Home Bakes",
            description = "Sourdough loaves, organic jaggery tea cakes, millet cookies, and whole wheat baked treats crafted fresh daily.",
            iconRes = R.drawable.ic_onboarding_creators,
            itemCount = 12
        ),
        Category(
            id = "cat_healthy",
            name = "Healthy & Millet Foods",
            description = "Ragi crunch cookies, sprouted grain porridge powders, cold-pressed energy bars, and protein-packed seed blends.",
            iconRes = R.drawable.ic_onboarding_welcome,
            itemCount = 14
        ),
        Category(
            id = "cat_festivals",
            name = "Festival Specials",
            description = "Seasonal festive boxes curated for Diwali, Sankranti, Onam, Rakhi, and Holi with traditional celebratory treats.",
            iconRes = R.drawable.ic_onboarding_homemade,
            itemCount = 10
        ),
        Category(
            id = "cat_hampers",
            name = "Handcrafted Gift Hampers",
            description = "Elegantly packaged assortments of artisanal preserves, exotic spices, and sweet delicacies ideal for gourmet gifting.",
            iconRes = R.drawable.ic_onboarding_creators,
            itemCount = 8
        )
    )

    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(categories))
    }

    override fun getCategoryById(categoryId: String): Flow<Resource<Category>> = flow {
        emit(Resource.Loading())
        val category = categories.find { it.id == categoryId }
        if (category != null) {
            emit(Resource.Success(category))
        } else {
            emit(Resource.Error("Category not found"))
        }
    }
}

