package com.cravexa.data.repository

import com.cravexa.core.common.Resource
import com.cravexa.domain.model.HeroBanner
import com.cravexa.domain.model.Product
import com.cravexa.domain.model.SearchFilter
import com.cravexa.domain.model.SortOption
import com.cravexa.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor() : ProductRepository {

    private val sampleHeroBanners = listOf(
        HeroBanner(
            id = "banner_1",
            title = "Authentic Homemade Flavors\nDirect from Local Creators",
            subtitle = "Zero preservatives • Traditional family recipes crafted in home kitchens",
            badgeText = "HOMEMADE WITH LOVE",
            categoryId = "cat_pickles",
            tag = "FEATURED"
        ),
        HeroBanner(
            id = "banner_2",
            title = "Aromatic Stone-Ground Masalas\n& Fresh Heritage Podis",
            subtitle = "Sun-dried whole spices roasted to perfection in copper vessels",
            badgeText = "FRESH SMALL BATCHES",
            categoryId = "cat_spices",
            tag = "TRENDING"
        ),
        HeroBanner(
            id = "banner_3",
            title = "Desi Ghee Delicacies &\nHandmade Traditional Sweets",
            subtitle = "Sweetened with organic unrefined jaggery and pure A2 cow ghee",
            badgeText = "FESTIVAL SPECIALS",
            categoryId = "cat_sweets",
            tag = "SWEET_TOOTH"
        )
    )

    private val sampleProducts = listOf(
        Product(
            id = "prod_1",
            name = "Grandma's Andhra Avakaya Mango Pickle",
            description = "Iconic fiery Andhra Avakaya made with raw sour mango cubes, Guntur red chillies, freshly crushed mustard powder, and cold-pressed sesame oil. Aged naturally under sunlight with zero chemical preservatives.",
            price = 280.0,
            originalPrice = 320.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            sellerLocation = "Guntur, Andhra Pradesh",
            categoryId = "cat_pickles",
            categoryName = "Handmade Pickles",
            rating = 4.9,
            reviewCount = 142,
            stock = 25,
            weight = "500g",
            ingredients = listOf("Raw Sour Mango", "Guntur Red Chilli", "Mustard Seed Powder", "Cold-Pressed Sesame Oil", "Turmeric", "Sea Salt", "Garlic"),
            shelfLife = "12 Months",
            storageInstructions = "Store in a cool, dry place. Always use a clean and dry spoon.",
            region = "Andhra Pradesh",
            foodType = "Vegetarian",
            available = true,
            featured = true,
            trending = true,
            tag = "BESTSELLER"
        ),
        Product(
            id = "prod_2",
            name = "Pure Desi Ghee Traditional Mysore Pak",
            description = "Melt-in-the-mouth heritage Mysore Pak crafted with pure golden ghee, roasted besan (gram flour), and fine organic sugar. Soft, porous texture with a rich caramelized aroma.",
            price = 360.0,
            originalPrice = 400.0,
            sellerId = "sel_2",
            sellerName = "Srivari Sweets & Delights",
            sellerLocation = "Mysuru, Karnataka",
            categoryId = "cat_sweets",
            categoryName = "Traditional Indian Sweets",
            rating = 4.8,
            reviewCount = 98,
            stock = 15,
            weight = "400g",
            ingredients = listOf("Pure Cow Ghee", "Fine Gram Flour (Besan)", "Organic Sugar", "Cardamom"),
            shelfLife = "30 Days",
            storageInstructions = "Keep in an airtight container at room temperature.",
            region = "Karnataka",
            foodType = "Vegetarian",
            available = true,
            featured = true,
            trending = true,
            tag = "SIGNATURE"
        ),
        Product(
            id = "prod_3",
            name = "Crispy Kerala Nendran Banana Chips",
            description = "Crispy, wafer-thin raw Nendran banana chips fried in 100% pure cold-pressed coconut oil and gently tossed with turmeric and sea salt. Unbeatable authentic crunch in every bite.",
            price = 220.0,
            originalPrice = 250.0,
            sellerId = "sel_3",
            sellerName = "Malabar Kitchen Delights",
            sellerLocation = "Calicut, Kerala",
            categoryId = "cat_snacks",
            categoryName = "Crunchy Snacks & Namkeens",
            rating = 4.7,
            reviewCount = 115,
            stock = 30,
            weight = "350g",
            ingredients = listOf("Raw Nendran Plantain", "Pure Coconut Oil", "Turmeric Powder", "Rock Salt"),
            shelfLife = "60 Days",
            storageInstructions = "Seal the ziplock pouch after opening to retain crispness.",
            region = "Kerala",
            foodType = "Vegan",
            available = true,
            featured = false,
            trending = true,
            tag = "CRUNCHY"
        ),
        Product(
            id = "prod_4",
            name = "Stone-Ground Roasted Gunpowder Idli Podi",
            description = "Authentic Madurai style spiced lentil powder (Milagai Podi) roasted in small batches with curry leaves, red chilies, urad dal, chana dal, and a dash of hing. Perfect with hot ghee or gingelly oil.",
            price = 190.0,
            originalPrice = 220.0,
            sellerId = "sel_4",
            sellerName = "Meenakshi Ammal Podi Studio",
            sellerLocation = "Madurai, Tamil Nadu",
            categoryId = "cat_spices",
            categoryName = "Artisanal Spices & Podis",
            rating = 4.9,
            reviewCount = 84,
            stock = 40,
            weight = "250g",
            ingredients = listOf("Roasted Urad Dal", "Chana Dal", "Byadgi Chilli", "Curry Leaves", "Sesame Seeds", "Asafoetida (Hing)", "Sea Salt"),
            shelfLife = "9 Months",
            storageInstructions = "Store in an airtight jar.",
            region = "Tamil Nadu",
            foodType = "Vegetarian",
            available = true,
            featured = true,
            trending = false,
            tag = "HERITAGE"
        ),
        Product(
            id = "prod_5",
            name = "Royal Rajasthani Ker Sangri Heritage Pickle",
            description = "The desert delicacy of Rajasthan. Wild Ker berries and Sangri beans simmered with whole spices, amchur, and mustard oil for a deeply tangy, earthy, and aromatic flavor profile.",
            price = 340.0,
            originalPrice = 380.0,
            sellerId = "sel_5",
            sellerName = "Marwar Heritage Kitchen",
            sellerLocation = "Jodhpur, Rajasthan",
            categoryId = "cat_regional",
            categoryName = "Regional Culinary Specialties",
            rating = 4.8,
            reviewCount = 62,
            stock = 12,
            weight = "400g",
            ingredients = listOf("Desert Ker Berries", "Sangri Beans", "Mustard Oil", "Dry Mango Powder", "Fennel", "Kalonji", "Red Chilli"),
            shelfLife = "12 Months",
            storageInstructions = "Keep submerged in spiced mustard oil.",
            region = "Rajasthan",
            foodType = "Vegetarian",
            available = true,
            featured = false,
            trending = true,
            tag = "REGIONAL"
        ),
        Product(
            id = "prod_6",
            name = "Kolkata Nolen Gur Stuffed Sandesh",
            description = "Handmade cottage cheese sweet infused with aromatic date palm jaggery (Nolen Gur) harvested directly from Bengal winter palms. Soft, fragrant, and delicate.",
            price = 320.0,
            originalPrice = 350.0,
            sellerId = "sel_6",
            sellerName = "Shonar Bangla Sweet Studio",
            sellerLocation = "Kolkata, West Bengal",
            categoryId = "cat_sweets",
            categoryName = "Traditional Indian Sweets",
            rating = 4.9,
            reviewCount = 76,
            stock = 8,
            weight = "300g",
            ingredients = listOf("Fresh Chhena (Cottage Cheese)", "Nolen Gur (Date Palm Jaggery)", "Green Cardamom"),
            shelfLife = "10 Days",
            storageInstructions = "Refrigerate upon arrival and consume fresh.",
            region = "West Bengal",
            foodType = "Vegetarian",
            available = true,
            featured = true,
            trending = false,
            tag = "GOURMET"
        ),
        Product(
            id = "prod_7",
            name = "Chettinad Roasted Sambar Masala Powder",
            description = "Aromatic heritage sambar podi with whole coriander seeds, cumin, peppercorns, fenugreek, and hand-plucked curry leaves slow roasted in clay pans and ground coarsely.",
            price = 210.0,
            originalPrice = 240.0,
            sellerId = "sel_4",
            sellerName = "Meenakshi Ammal Podi Studio",
            sellerLocation = "Madurai, Tamil Nadu",
            categoryId = "cat_spices",
            categoryName = "Artisanal Spices & Podis",
            rating = 4.7,
            reviewCount = 53,
            stock = 20,
            weight = "300g",
            ingredients = listOf("Coriander Seeds", "Cumin", "Black Pepper", "Fenugreek", "Curry Leaves", "Turmeric"),
            shelfLife = "12 Months",
            storageInstructions = "Store away from heat and moisture.",
            region = "Tamil Nadu",
            foodType = "Vegetarian",
            available = true,
            featured = false,
            trending = false,
            tag = null
        ),
        Product(
            id = "prod_8",
            name = "Crunchy Ragi & Almond Jaggery Cookies",
            description = "Wholesome diabetic-friendly cookies baked with sprouted finger millet flour, roasted Californian almonds, pure butter, and organic country jaggery. Zero refined maida and zero white sugar.",
            price = 260.0,
            originalPrice = 290.0,
            sellerId = "sel_7",
            sellerName = "Prakriti Wholesome Bakes",
            sellerLocation = "Bengaluru, Karnataka",
            categoryId = "cat_healthy",
            categoryName = "Healthy & Millet Foods",
            rating = 4.6,
            reviewCount = 47,
            stock = 18,
            weight = "250g",
            ingredients = listOf("Sprouted Ragi Flour", "Roasted Almonds", "Country Jaggery", "Pure Butter", "Cardamom"),
            shelfLife = "45 Days",
            storageInstructions = "Keep in an airtight tin.",
            region = "Karnataka",
            foodType = "Vegetarian",
            available = true,
            featured = false,
            trending = true,
            tag = "HEALTHY"
        ),
        Product(
            id = "prod_9",
            name = "Spicy Banarasi Stuffed Red Chilli Pickle",
            description = "Thick, fleshy Banarasi red chillies hand-stuffed with a spicy, pungent masala blend of amchur, saunf, methi, and mustard oil. An iconic classic from Uttar Pradesh.",
            price = 290.0,
            originalPrice = 330.0,
            sellerId = "sel_8",
            sellerName = "Ganga Kinare Kitchens",
            sellerLocation = "Varanasi, Uttar Pradesh",
            categoryId = "cat_pickles",
            categoryName = "Handmade Pickles",
            rating = 4.8,
            reviewCount = 68,
            stock = 14,
            weight = "450g",
            ingredients = listOf("Banarasi Red Chillies", "Mustard Oil", "Fennel Seeds", "Fenugreek", "Dry Mango", "Rock Salt"),
            shelfLife = "12 Months",
            storageInstructions = "Store at room temperature in glass jar.",
            region = "Uttar Pradesh",
            foodType = "Vegetarian",
            available = true,
            featured = false,
            trending = false,
            tag = "SPICY"
        ),
        Product(
            id = "prod_10",
            name = "Handmade Gujarati Methi Khakhra (Pack of 5)",
            description = "Traditional wafer-thin roasted whole wheat flatbread flavored with dried fenugreek leaves, ajwain, turmeric, and pure peanut oil. Crispy, light, and nutritious.",
            price = 180.0,
            originalPrice = 200.0,
            sellerId = "sel_9",
            sellerName = "Surti Rasoi Home Kitchen",
            sellerLocation = "Surat, Gujarat",
            categoryId = "cat_snacks",
            categoryName = "Crunchy Snacks & Namkeens",
            rating = 4.7,
            reviewCount = 59,
            stock = 22,
            weight = "500g",
            ingredients = listOf("Whole Wheat Flour", "Kasuri Methi", "Ajwain", "Turmeric", "Peanut Oil", "Salt"),
            shelfLife = "90 Days",
            storageInstructions = "Store in an airtight container.",
            region = "Gujarat",
            foodType = "Vegetarian",
            available = true,
            featured = false,
            trending = false,
            tag = "TEA TIME"
        ),
        Product(
            id = "prod_11",
            name = "Organic Whole Wheat Sourdough Country Loaf",
            description = "Naturally fermented 36-hour slow-rise rustic sourdough bread baked with stone-ground whole wheat and wild active starter. Crisp golden crust with a soft, airy, and tangy crumb.",
            price = 240.0,
            originalPrice = 270.0,
            sellerId = "sel_7",
            sellerName = "Prakriti Wholesome Bakes",
            sellerLocation = "Bengaluru, Karnataka",
            categoryId = "cat_baked",
            categoryName = "Fresh Home Bakes",
            rating = 4.9,
            reviewCount = 38,
            stock = 6,
            weight = "450g",
            ingredients = listOf("Stone-Ground Whole Wheat", "Wild Sourdough Starter", "Filtered Water", "Sea Salt"),
            shelfLife = "5 Days",
            storageInstructions = "Store sliced in a bread box or freeze for extended freshness.",
            region = "Karnataka",
            foodType = "Vegan",
            available = true,
            featured = false,
            trending = false,
            tag = "ARTISAN BAKE"
        ),
        Product(
            id = "prod_12",
            name = "Grand Festive Gourmet Delicacy Hamper",
            description = "A luxurious collection of our finest artisanal delicacies: Andhra Mango Avakaya (250g), Desi Ghee Mysore Pak (250g), Madurai Gunpowder Podi (150g), and Crispy Kerala Banana Chips (150g) in a handcrafted festive keepsake box.",
            price = 850.0,
            originalPrice = 999.0,
            sellerId = "sel_1",
            sellerName = "Lakshmi's Home Kitchen",
            sellerLocation = "Guntur, Andhra Pradesh",
            categoryId = "cat_hampers",
            categoryName = "Handcrafted Gift Hampers",
            rating = 5.0,
            reviewCount = 31,
            stock = 10,
            weight = "800g",
            ingredients = listOf("Assorted Artisanal Mango Pickle", "Ghee Mysore Pak", "Idli Podi", "Banana Chips"),
            shelfLife = "30 Days",
            storageInstructions = "Keep gift items in dry, cool conditions.",
            region = "India",
            foodType = "Vegetarian",
            available = true,
            featured = true,
            trending = true,
            tag = "LUXURY HAMPER"
        )
    )

    override fun getHeroBanners(): Flow<List<HeroBanner>> = flow {
        emit(sampleHeroBanners)
    }

    override fun getFeaturedProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(sampleProducts.filter { it.featured }))
    }

    override fun getTrendingProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(sampleProducts.filter { it.trending }))
    }

    override fun getRecommendedProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(sampleProducts.sortedByDescending { it.rating }))
    }

    override fun getRegionalSpecialties(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(sampleProducts.filter { it.region != "India" }))
    }

    override fun getProductsByCategory(categoryId: String): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        val filtered = sampleProducts.filter { it.categoryId == categoryId }
        emit(Resource.Success(filtered))
    }

    override fun getProductById(productId: String): Flow<Resource<Product>> = flow {
        emit(Resource.Loading())
        val product = sampleProducts.find { it.id == productId }
        if (product != null) {
            emit(Resource.Success(product))
        } else {
            emit(Resource.Error("Product not found."))
        }
    }

    override fun getProductsBySeller(sellerId: String): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        val filtered = sampleProducts.filter { it.sellerId == sellerId }
        emit(Resource.Success(filtered))
    }

    override fun getRelatedProducts(productId: String, categoryId: String): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        val related = sampleProducts.filter { it.categoryId == categoryId && it.id != productId }
        emit(Resource.Success(related))
    }

    override fun searchProducts(
        query: String,
        filter: SearchFilter?,
        sort: SortOption
    ): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        
        var results = sampleProducts

        val q = query.trim().lowercase()
        if (q.isNotBlank()) {
            results = results.filter {
                it.name.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.sellerName.lowercase().contains(q) ||
                it.categoryName.lowercase().contains(q) ||
                it.region.lowercase().contains(q) ||
                it.ingredients.any { ing -> ing.lowercase().contains(q) }
            }
        }

        if (filter != null) {
            if (filter.categoryId != null) {
                results = results.filter { it.categoryId == filter.categoryId }
            }
            if (filter.minPrice != null) {
                results = results.filter { it.price >= filter.minPrice }
            }
            if (filter.maxPrice != null) {
                results = results.filter { it.price <= filter.maxPrice }
            }
            if (filter.minRating != null) {
                results = results.filter { it.rating >= filter.minRating }
            }
            if (filter.region != null) {
                results = results.filter { it.region.equals(filter.region, ignoreCase = true) }
            }
            if (filter.foodType != null) {
                results = results.filter { it.foodType.equals(filter.foodType, ignoreCase = true) }
            }
            if (filter.inStockOnly) {
                results = results.filter { it.stock > 0 && it.available }
            }
        }

        results = when (sort) {
            SortOption.RELEVANCE -> results
            SortOption.PRICE_LOW_TO_HIGH -> results.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> results.sortedByDescending { it.price }
            SortOption.RATING -> results.sortedByDescending { it.rating }
            SortOption.NEWEST -> results.reversed()
        }

        emit(Resource.Success(results))
    }

    override fun getSearchSuggestions(query: String): Flow<List<String>> = flow {
        val q = query.trim().lowercase()
        if (q.isBlank()) {
            emit(listOf("Andhra Mango Pickle", "Pure Ghee Mysore Pak", "Kerala Banana Chips", "Gunpowder Idli Podi", "Sourdough Bread"))
            return@flow
        }

        val suggestions = mutableSetOf<String>()
        sampleProducts.forEach { product ->
            if (product.name.lowercase().contains(q)) {
                suggestions.add(product.name)
            }
            if (product.categoryName.lowercase().contains(q)) {
                suggestions.add(product.categoryName)
            }
            if (product.sellerName.lowercase().contains(q)) {
                suggestions.add(product.sellerName)
            }
            if (product.region.lowercase().contains(q)) {
                suggestions.add("${product.region} Specialties")
            }
        }
        emit(suggestions.take(6).toList())
    }
}

