import asyncio
import os
import pytest
import pytest_asyncio
from httpx import ASGITransport, AsyncClient
from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker, create_async_engine

# Use test SQLite database
TEST_DB_URL = "sqlite+aiosqlite:///./test_cravexa.db"
os.environ["DATABASE_URL"] = TEST_DB_URL

from app.core.config import settings
from app.core.security import create_access_token, get_password_hash
from app.database.base import Base
from app.database.session import get_db
from app.main import app
from app.models.category import Category
from app.models.product import Product, ProductStatus
from app.models.seller import FssaiStatus, SellerProfile, SellerStatus
from app.models.user import User, UserRole, UserStatus
from app.services.auth_service import AuthService

engine_test = create_async_engine(TEST_DB_URL, echo=False)
TestingSessionLocal = async_sessionmaker(
    bind=engine_test, class_=AsyncSession, expire_on_commit=False, autoflush=False
)


@pytest_asyncio.fixture(scope="function", autouse=True)
async def setup_test_db():
    async with engine_test.begin() as conn:
        await conn.run_sync(Base.metadata.drop_all)
        await conn.run_sync(Base.metadata.create_all)
    # Bootstrap default admin
    async with TestingSessionLocal() as session:
        await AuthService.bootstrap_admin(session)
    yield
    async with engine_test.begin() as conn:
        await conn.run_sync(Base.metadata.drop_all)


async def override_get_db():
    async with TestingSessionLocal() as session:
        yield session


app.dependency_overrides[get_db] = override_get_db


@pytest_asyncio.fixture
async def db_session():
    async with TestingSessionLocal() as session:
        yield session


@pytest_asyncio.fixture
async def client():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as ac:
        yield ac


@pytest_asyncio.fixture
async def customer_user(db_session: AsyncSession):
    user = User(
        email="customer_test@cravexa.com",
        phone="9876543210",
        name="Test Customer",
        role=UserRole.CUSTOMER,
        status=UserStatus.ACTIVE,
        hashed_password=get_password_hash("CustomerPass123!"),
    )
    db_session.add(user)
    await db_session.commit()
    await db_session.refresh(user)
    return user


@pytest_asyncio.fixture
def customer_token(customer_user: User):
    return create_access_token(
        data={"sub": str(customer_user.id), "role": customer_user.role.value, "email": customer_user.email}
    )


@pytest_asyncio.fixture
async def seller_user(db_session: AsyncSession):
    user = User(
        email="seller_test@cravexa.com",
        phone="9876543211",
        name="Test Seller",
        role=UserRole.SELLER,
        status=UserStatus.ACTIVE,
        hashed_password=get_password_hash("SellerPass123!"),
    )
    db_session.add(user)
    await db_session.commit()
    await db_session.refresh(user)

    seller_profile = SellerProfile(
        user_id=user.id,
        business_name="Grandma Delights",
        about="Homemade traditional delights",
        phone="9876543211",
        email=user.email,
        address="123 Food Street",
        city="Bengaluru",
        state="Karnataka",
        pincode="560001",
        food_category="Traditional Foods",
        seller_status=SellerStatus.APPROVED,
        fssai_status=FssaiStatus.VERIFIED,
        fssai_number="12345678901234",
    )
    db_session.add(seller_profile)
    await db_session.commit()
    await db_session.refresh(seller_profile)
    user.seller_profile = seller_profile
    return user


@pytest_asyncio.fixture
def seller_token(seller_user: User):
    return create_access_token(
        data={"sub": str(seller_user.id), "role": seller_user.role.value, "email": seller_user.email}
    )


@pytest_asyncio.fixture
async def admin_user(db_session: AsyncSession):
    from sqlalchemy import select
    res = await db_session.execute(select(User).where(User.email == settings.ADMIN_EMAIL))
    user = res.scalar_one_or_none()
    if not user:
        user = await AuthService.bootstrap_admin(db_session)
    return user


@pytest_asyncio.fixture
def admin_token(admin_user: User):
    return create_access_token(
        data={"sub": str(admin_user.id), "role": admin_user.role.value, "email": admin_user.email}
    )


@pytest_asyncio.fixture
async def sample_category(db_session: AsyncSession):
    cat = Category(
        name="Pickles",
        slug="pickles",
        description="Authentic homemade pickles",
        is_active=True,
    )
    db_session.add(cat)
    await db_session.commit()
    await db_session.refresh(cat)
    return cat


@pytest_asyncio.fixture
async def sample_product(db_session: AsyncSession, seller_user: User, sample_category: Category):
    prod = Product(
        seller_id=seller_user.seller_profile.id,
        category_id=sample_category.id,
        name="Traditional Mango Pickle",
        slug="traditional-mango-pickle",
        description="Spicy traditional homemade mango pickle",
        price=250.0,
        original_price=300.0,
        weight="500g",
        stock=50,
        availability=True,
        status=ProductStatus.APPROVED,
    )
    db_session.add(prod)
    await db_session.commit()
    await db_session.refresh(prod)
    return prod
