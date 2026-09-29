from typing import Annotated, List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy import select, update
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.dependencies import require_authenticated_user
from app.database.session import get_db
from app.models.address import Address
from app.models.user import User
from app.schemas.address import AddressCreate, AddressOut, AddressUpdate
from app.schemas.common import MessageResponse

router = APIRouter(prefix="/addresses", tags=["Addresses"])


@router.get("", response_model=List[AddressOut])
async def list_addresses(
    current_user: Annotated[User, Depends(require_authenticated_user)],
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """List all saved addresses for the authenticated user."""
    result = await db.execute(
        select(Address)
        .where(Address.customer_id == current_user.id)
        .order_by(Address.is_default.desc(), Address.id.desc())
    )
    return [AddressOut.model_validate(a) for a in result.scalars().all()]


@router.post("", response_model=AddressOut, status_code=status.HTTP_201_CREATED)
async def create_address(
    data: AddressCreate,
    current_user: Annotated[User, Depends(require_authenticated_user)],
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """Add a new delivery address."""
    # If set as default, unset other addresses
    if data.is_default:
        await db.execute(
            update(Address)
            .where(Address.customer_id == current_user.id)
            .values(is_default=False)
        )

    new_address = Address(
        customer_id=current_user.id,
        full_name=data.full_name,
        phone=data.phone,
        house=data.house or data.address_line1 or "",
        street=data.street or data.address_line2 or "",
        area=data.area or "",
        landmark=data.landmark,
        city=data.city,
        state=data.state,
        pincode=data.pincode,
        address_type=data.address_type,
        is_default=data.is_default,
    )
    db.add(new_address)
    await db.commit()
    await db.refresh(new_address)
    return AddressOut.model_validate(new_address)


@router.put("/{address_id}", response_model=AddressOut)
async def update_address(
    address_id: int,
    data: AddressUpdate,
    current_user: Annotated[User, Depends(require_authenticated_user)],
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """Update an existing address (enforces customer ownership)."""
    result = await db.execute(
        select(Address).where(Address.id == address_id, Address.customer_id == current_user.id)
    )
    address = result.scalar_one_or_none()
    if not address:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Address not found or does not belong to you",
        )

    update_dict = data.model_dump(exclude_unset=True)
    if update_dict.get("is_default") is True:
        await db.execute(
            update(Address)
            .where(Address.customer_id == current_user.id)
            .values(is_default=False)
        )

    for field, val in update_dict.items():
        setattr(address, field, val)

    await db.commit()
    await db.refresh(address)
    return AddressOut.model_validate(address)


@router.delete("/{address_id}", response_model=MessageResponse)
async def delete_address(
    address_id: int,
    current_user: Annotated[User, Depends(require_authenticated_user)],
    db: Annotated[AsyncSession, Depends(get_db)]
):
    """Delete a saved address (enforces customer ownership)."""
    result = await db.execute(
        select(Address).where(Address.id == address_id, Address.customer_id == current_user.id)
    )
    address = result.scalar_one_or_none()
    if not address:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Address not found or does not belong to you",
        )

    await db.delete(address)
    await db.commit()
    return MessageResponse(message="Address deleted successfully")
