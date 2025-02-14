from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional
from datetime import datetime
from supabase import Client
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()


class UserAllergies(BaseModel): 
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: int
    allergy_id: int
    diagnosis_date: datetime
    #severity: VARCHAR
    notes: str #text
    qr_code_hash: str #varchar
    
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar uma alergia a um usuário
@router.post("/user_allergies/")
async def add_user_allergy(user_allergy: UserAllergies, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("user_allergies").insert(user_allergy.dict(exclude_unset=True)).execute()

    if response.status_code == 201:
        return {"message": "User allergy added successfully", "user_allergy": user_allergy}
    else:
        raise HTTPException(status_code=400, detail=f"Failed to add user allergy: {response.json()}")

# Rota para pegar alergias de um usuário
@router.get("/user_allergies/{user_id}", response_model=List[UserAllergies])
async def get_user_allergies(user_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("user_allergies").select("*").eq("user_id", user_id).execute()

    if response.status_code == 200 and response.data:
        return {"user_allergies": response.data}
    else:
        raise HTTPException(status_code=404, detail="No allergies found for this user")

# Rota para atualizar uma alergia do usuário
@router.put("/user_allergies/{user_allergy_id}")
async def update_user_allergy(user_allergy_id: int, user_allergy: UserAllergies, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("user_allergies").update(user_allergy.dict(exclude_unset=True)).eq("id", user_allergy_id).execute()

    if response.status_code == 200:
        return {"message": "User allergy updated successfully"}
    else:
        raise HTTPException(status_code=400, detail="Failed to update user allergy")

# Rota para deletar uma alergia do usuário
@router.delete("/user_allergies/{user_allergy_id}")
async def delete_user_allergy(user_allergy_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("user_allergies").delete().eq("id", user_allergy_id).execute()

    if response.status_code == 200:
        return {"message": "User allergy deleted successfully"}
    else:
        raise HTTPException(status_code=400, detail="Failed to delete user allergy")
