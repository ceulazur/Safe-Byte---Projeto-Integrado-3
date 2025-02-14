from datetime import datetime
from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()

class DoctorReview(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    user_id: int
    doctor_id: int
    doctor_crm: str #VARCHAR
    rating: int
    comment: str #text
    review_date: datetime
    
# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()
    
# Rota para adicionar uma avaliação de um médico
@router.post('/doctor-reviews', status_code=HTTPStatus.CREATED)
async def add_doctor_review(doctor_review: DoctorReview, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_reviews").insert(doctor_review.dict(exclude_unset=True)).execute()

        if response.status_code != 201:
            raise HTTPException(status_code=400, detail=f"Failed to add doctor review: {response.json()}")

        return {"message": "Doctor review added successfully", "doctor_review": doctor_review}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error adding doctor review: {str(e)}")

# Rota para pegar uma avaliação de um médico pelo ID
@router.get('/doctor-reviews/{review_id}', status_code=HTTPStatus.OK, response_model=DoctorReview)
async def get_doctor_review(review_id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_reviews").select("*").eq("id", review_id).execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="Doctor review not found")

        return response.data[0]

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching doctor review: {str(e)}")

# Rota para listar todas as avaliações de médicos
@router.get('/doctor-reviews', status_code=HTTPStatus.OK, response_model=List[DoctorReview])
async def list_doctor_reviews(supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_reviews").select("*").execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="No doctor reviews found")

        return response.data

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching doctor reviews: {str(e)}")

# Rota para atualizar uma avaliação de um médico
@router.put('/doctor-reviews/{review_id}', status_code=HTTPStatus.OK)
async def update_doctor_review(review_id: int, doctor_review: DoctorReview, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_reviews").update(doctor_review.dict(exclude_unset=True)).eq("id", review_id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to update doctor review: {response.json()}")

        return {"message": "Doctor review updated successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error updating doctor review: {str(e)}")

# Rota para deletar uma avaliação de um médico
@router.delete('/doctor-reviews/{review_id}', status_code=HTTPStatus.OK)
async def delete_doctor_review(review_id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_reviews").delete().eq("id", review_id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to delete doctor review: {response.json()}")

        return {"message": "Doctor review deleted successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting doctor review: {str(e)}")
