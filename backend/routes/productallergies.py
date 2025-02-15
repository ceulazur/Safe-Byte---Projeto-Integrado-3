from fastapi import APIRouter, HTTPException, Depends
from pydantic import BaseModel, Field
from typing import List, Optional
from datetime import datetime
from supabase import Client
from main import create_supabase_client  # Ajuste conforme sua estrutura

# Criando o objeto APIRouter
router = APIRouter()

# Modelo Pydantic para o ProductAllergies
class ProductAllergies(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    product_id: int
    allergy_id: int
    risk_level: str  # VARCHAR
    notes: str  # TEXT

# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar uma alergia a um produto
@router.post("/product_allergies/")
async def add_product_allergy(product_allergy: ProductAllergies, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_allergies").insert(product_allergy.dict(exclude_unset=True)).execute()
    
    if response.status_code == 201:
        return {"message": "Product allergy added successfully", "product_allergy": product_allergy}
    else:
        raise HTTPException(status_code=400, detail=f"Failed to add product allergy: {response.json()}")

# Rota para pegar todas as alergias de um produto
@router.get("/product_allergies/{product_id}", response_model=List[ProductAllergies])
async def get_product_allergies(product_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_allergies").select("*").eq("product_id", product_id).execute()

    if response.status_code == 200 and response.data:
        return response.data
    else:
        raise HTTPException(status_code=404, detail="No allergies found for this product")

# Rota para pegar uma alergia específica de um produto
@router.get("/product_allergies/{product_id}/{allergy_id}", response_model=ProductAllergies)
async def get_product_allergy(product_id: int, allergy_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_allergies").select("*").eq("product_id", product_id).eq("allergy_id", allergy_id).execute()

    if response.status_code == 200 and response.data:
        return response.data[0]
    else:
        raise HTTPException(status_code=404, detail="Product allergy not found")

# Rota para atualizar uma alergia de um produto
@router.put("/product_allergies/{product_id}/{allergy_id}")
async def update_product_allergy(product_id: int, allergy_id: int, product_allergy: ProductAllergies, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_allergies").update(product_allergy.dict(exclude_unset=True)).eq("product_id", product_id).eq("allergy_id", allergy_id).execute()

    if response.status_code == 200:
        return {"message": "Product allergy updated successfully"}
    else:
        raise HTTPException(status_code=400, detail="Failed to update product allergy")

# Rota para deletar uma alergia de um produto
@router.delete("/product_allergies/{product_id}/{allergy_id}")
async def delete_product_allergy(product_id: int, allergy_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_allergies").delete().eq("product_id", product_id).eq("allergy_id", allergy_id).execute()

    if response.status_code == 200:
        return {"message": "Product allergy deleted successfully"}
    else:
        raise HTTPException(status_code=400, detail="Failed to delete product allergy")
