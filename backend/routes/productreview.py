from pydantic import BaseModel, Field
from typing import Optional
from fastapi import APIRouter, HTTPException, Depends
from supabase import Client
from main import create_supabase_client

class ProductReview(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: int
    product_id: int
    rating: int
    comment: str
    review_date: str

# Criando o objeto APIRouter
router = APIRouter()

# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar uma avaliação de produto
@router.post("/reviews/")
async def add_product_review(review: ProductReview, supabase: Client = Depends(get_supabase_client)):
    # Verifica se a avaliação foi inserida corretamente
    response = supabase.table("product_reviews").insert(review.dict(exclude_unset=True)).execute()
    
    if response.status_code == 201:
        return {"message": "Review added successfully", "review": review}
    else:
        raise HTTPException(status_code=400, detail=f"Failed to add review: {response.json()}")

# Rota para pegar todas as avaliações de um produto pelo ID
@router.get("/reviews/{product_id}")
async def get_product_reviews(product_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_reviews").select("*").eq("product_id", product_id).execute()

    if response.status_code == 200 and response.data:
        return {"reviews": response.data}
    else:
        raise HTTPException(status_code=404, detail="Reviews not found")

# Rota para pegar uma avaliação pelo ID
@router.get("/reviews/{review_id}")
async def get_product_review(review_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_reviews").select("*").eq("id", review_id).execute()

    if response.status_code == 200 and response.data:
        return {"review": response.data[0]}
    else:
        raise HTTPException(status_code=404, detail="Review not found")

# Rota para atualizar uma avaliação
@router.put("/reviews/{review_id}")
async def update_product_review(review_id: int, review: ProductReview, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_reviews").update(review.dict(exclude_unset=True)).eq("id", review_id).execute()

    if response.status_code == 200:
        return {"message": "Review updated successfully"}
    else:
        raise HTTPException(status_code=400, detail="Failed to update review")

# Rota para deletar uma avaliação
@router.delete("/reviews/{review_id}")
async def delete_product_review(review_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("product_reviews").delete().eq("id", review_id).execute()

    if response.status_code == 200:
        return {"message": "Review deleted successfully"}
    else:
        raise HTTPException(status_code=400, detail="Failed to delete review")
