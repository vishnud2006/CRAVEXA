import asyncio
from sqlalchemy import select
from app.database.session import AsyncSessionLocal
from app.models.category import Category

INITIAL_CATEGORIES = [
    {
        "name": "Pickles",
        "slug": "pickles",
        "description": "Authentic regional and homemade Indian pickles made with traditional spices and cold-pressed oils.",
        "image_url": "https://images.unsplash.com/photo-1589135233689-d56d7d2a58b2?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Spices",
        "slug": "spices",
        "description": "Pure, stone-ground homemade masalas, aromatic whole spices, and secret culinary blends.",
        "image_url": "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Snacks",
        "slug": "snacks",
        "description": "Crispy, savory homemade namkeens, murukkus, mathris, and evening tea accompaniments.",
        "image_url": "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Sweets",
        "slug": "sweets",
        "description": "Handcrafted mithai, laddoos, halwas, and festive confections prepared with pure desi ghee.",
        "image_url": "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Regional Foods",
        "slug": "regional-foods",
        "description": "Heritage recipes and culinary staples representing India's rich regional culinary traditions.",
        "image_url": "https://images.unsplash.com/photo-1613292443284-8d10ef9383fe?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Traditional Foods",
        "slug": "traditional-foods",
        "description": "Ancestral pantry staples, sun-dried vadams, papads, and slow-crafted essentials.",
        "image_url": "https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Baked Goods",
        "slug": "baked-goods",
        "description": "Fresh oven-baked artisan breads, cookies, tea cakes, and savory baked bites.",
        "image_url": "https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Healthy Homemade Foods",
        "slug": "healthy-homemade-foods",
        "description": "Millet-based treats, protein snacks, low-sugar sweets, and wholesome organic pantry items.",
        "image_url": "https://images.unsplash.com/photo-1490645935967-10de6ba17061?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Festival Specials",
        "slug": "festival-specials",
        "description": "Seasonal delicacies and handcrafted offerings for Diwali, Pongal, Eid, and celebrations.",
        "image_url": "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=800&q=80",
    },
    {
        "name": "Gift Hampers",
        "slug": "gift-hampers",
        "description": "Carefully curated gourmet assortments and customizable homemade festive gift boxes.",
        "image_url": "https://images.unsplash.com/photo-1549465220-1a8b9238cd48?auto=format&fit=crop&w=800&q=80",
    },
]


async def seed_categories():
    async with AsyncSessionLocal() as session:
        for cat_data in INITIAL_CATEGORIES:
            res = await session.execute(select(Category).where(Category.slug == cat_data["slug"]))
            existing = res.scalar_one_or_none()
            if not existing:
                cat = Category(
                    name=cat_data["name"],
                    slug=cat_data["slug"],
                    description=cat_data["description"],
                    image_url=cat_data["image_url"],
                    is_active=True,
                )
                session.add(cat)
                print(f"Created category: {cat_data['name']}")
            else:
                print(f"Category already exists: {cat_data['name']}")
        await session.commit()
    print("Categories seeding complete.")


if __name__ == "__main__":
    asyncio.run(seed_categories())
