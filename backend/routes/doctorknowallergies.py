from datetime import datetime
from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()

class DoctorKnowAllergies(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    doctor_id: int
    crm: str #VARCHAR
    allergy_id: int
    expertise_level: str #VARCHAR
    
# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar o conhecimento de um médico sobre alergias
@router.post('/doctor-know-allergies', status_code=HTTPStatus.CREATED)
async def add_doctor_know_allergy(doctor_know_allergy: DoctorKnowAllergies, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_know_allergies").insert(doctor_know_allergy.dict(exclude_unset=True)).execute()

        if response.status_code != 201:
            raise HTTPException(status_code=400, detail=f"Failed to add doctor knowledge about allergy: {response.json()}")

        return {"message": "Doctor knowledge about allergy added successfully", "doctor_know_allergy": doctor_know_allergy}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error adding doctor knowledge about allergy: {str(e)}")

# Rota para pegar o conhecimento de um médico sobre alergias pelo ID
@router.get('/doctor-know-allergies/{id}', status_code=HTTPStatus.OK, response_model=DoctorKnowAllergies)
async def get_doctor_know_allergy(id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_know_allergies").select("*").eq("id", id).execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="Doctor knowledge about allergy not found")

        return response.data[0]

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching doctor knowledge about allergy: {str(e)}")

# Rota para listar todos os conhecimentos dos médicos sobre alergias
@router.get('/doctor-know-allergies', status_code=HTTPStatus.OK, response_model=List[DoctorKnowAllergies])
async def list_doctor_know_allergies(supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_know_allergies").select("*").execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="No doctor knowledge about allergies found")

        return response.data

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching doctor knowledge about allergies: {str(e)}")

# Rota para atualizar o conhecimento de um médico sobre alergias
@router.put('/doctor-know-allergies/{id}', status_code=HTTPStatus.OK)
async def update_doctor_know_allergy(id: int, doctor_know_allergy: DoctorKnowAllergies, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_know_allergies").update(doctor_know_allergy.dict(exclude_unset=True)).eq("id", id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to update doctor knowledge about allergy: {response.json()}")

        return {"message": "Doctor knowledge about allergy updated successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error updating doctor knowledge about allergy: {str(e)}")

# Rota para deletar o conhecimento de um médico sobre alergias
@router.delete('/doctor-know-allergies/{id}', status_code=HTTPStatus.OK)
async def delete_doctor_know_allergy(id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctor_know_allergies").delete().eq("id", id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to delete doctor knowledge about allergy: {response.json()}")

        return {"message": "Doctor knowledge about allergy deleted successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting doctor knowledge about allergy: {str(e)}")
