from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime
from supabase import Client
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()

# Modelo Pydantic para validar os dados do produto
class Product(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    name: str
    description: Optional[str] = None  # Tornando a descrição opcional
    brand: str
    barcode: str
    ingredients: str
    status: bool
    created_at: Optional[datetime] = None  # Mantém opcional para o cliente

# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar um produto
@router.post("/products/")
async def add_product(product: Product, supabase: Client = Depends(get_supabase_client)):
    # Verifica se o produto foi inserido corretamente
    response = supabase.table("products").insert(product.dict(exclude_unset=True)).execute()
    
    if response.status_code == 201:
        return {"message": "Product added successfully", "product": product}
    else:
        raise HTTPException(status_code=400, detail=f"Failed to add product: {response.json()}")

# Rota para pegar um produto pelo ID
@router.get("/products/{product_id}")
async def get_product(product_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("products").select("*").eq("id", product_id).execute()

    if response.status_code == 200 and response.data:
        return {"product": response.data[0]}
    else:
        raise HTTPException(status_code=404, detail="Product not found")

# Rota para atualizar um produto
@router.put("/products/{product_id}")
async def update_product(product_id: int, product: Product, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("products").update(product.dict(exclude_unset=True)).eq("id", product_id).execute()

    if response.status_code == 200:
        return {"message": "Product updated successfully"}
    else:
        raise HTTPException(status_code=400, detail="Failed to update product")

# Rota para deletar um produto
@router.delete("/products/{product_id}")
async def delete_product(product_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("products").delete().eq("id", product_id).execute()

    if response.status_code == 200:
        return {"message": "Product deleted successfully"}
    else:
  
        raise HTTPException(status_code=400, detail="Failed to delete product")