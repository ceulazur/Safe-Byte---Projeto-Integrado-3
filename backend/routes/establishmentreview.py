
from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional
from datetime import datetime
from supabase import Client
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()

class EstablishmentReviews(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: int
    establishment_id: int
    rating: int
    comment: str #text
    review_date: datetime
    
    # Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()


@router.get('/reviews', status_code=HTTPStatus.OK, response_model=List[EstablishmentReviews])
def read_reviews(supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_reviews").select("*").limit(10).execute()  # Limite de 10 registros para teste
        
        if not response.data:
            raise HTTPException(status_code=404, detail="No data found in 'establishment_reviews' table.")
        
        # Retorna os dados como uma lista de objetos EstablishmentReview
        return response.data
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching reviews: {str(e)}")


@router.get('/reviews/{review_id}', status_code=HTTPStatus.OK, response_model=EstablishmentReviews)
def get_review(review_id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_reviews").select("*").eq("id", review_id).execute()
        
        if not response.data:
            raise HTTPException(status_code=404, detail="Review not found.")
        
        # Retorna os dados da avaliação específica
        return response.data[0]
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching review: {str(e)}")


@router.post('/reviews', status_code=HTTPStatus.CREATED, response_model=EstablishmentReviews)
def add_review(review: EstablishmentReviews, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_reviews").insert(review.dict(exclude_unset=True)).execute()
        
        if response.status_code != 201:
            raise HTTPException(status_code=400, detail="Failed to add review")
        
        # Retorna o review adicionado
        return response.data[0]
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error adding review: {str(e)}")


@router.put('/reviews/{review_id}', status_code=HTTPStatus.OK, response_model=EstablishmentReviews)
def update_review(review_id: int, review: EstablishmentReviews, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_reviews").update(review.dict(exclude_unset=True)).eq("id", review_id).execute()
        
        if response.status_code != 200:
            raise HTTPException(status_code=400, detail="Failed to update review")
        
        # Retorna o review atualizado
        return response.data[0]
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error updating review: {str(e)}")


@router.delete('/reviews/{review_id}', status_code=HTTPStatus.NO_CONTENT)
def delete_review(review_id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_reviews").delete().eq("id", review_id).execute()
        
        if response.status_code != 200:
            raise HTTPException(status_code=400, detail="Failed to delete review")
        
        # Retorna uma mensagem de sucesso
        return {"message": "Review deleted successfully"}
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting review: {str(e)}")