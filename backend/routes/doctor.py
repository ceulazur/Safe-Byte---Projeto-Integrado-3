from datetime import datetime
from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()


class Doctor(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    name: str #VARCHAR
    crm: str #VARCHAR
    specialty: str #VARCHAR
    address: str #TEXT
    phone: str #VARCHAR
    email: str #VARCHAR
    status: bool
    
# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar um médico
@router.post('/doctors', status_code=HTTPStatus.CREATED)
async def add_doctor(doctor: Doctor, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctors").insert(doctor.dict(exclude_unset=True)).execute()

        if response.status_code != 201:
            raise HTTPException(status_code=400, detail=f"Failed to add doctor: {response.json()}")

        return {"message": "Doctor added successfully", "doctor": doctor}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error adding doctor: {str(e)}")

# Rota para pegar um médico pelo ID
@router.get('/doctors/{id}', status_code=HTTPStatus.OK, response_model=Doctor)
async def get_doctor(id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctors").select("*").eq("id", id).execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="Doctor not found")

        return response.data[0]

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching doctor: {str(e)}")

# Rota para listar todos os médicos
@router.get('/doctors', status_code=HTTPStatus.OK, response_model=List[Doctor])
async def list_doctors(supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctors").select("*").execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="No doctors found")

        return response.data

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching doctors: {str(e)}")

# Rota para atualizar um médico
@router.put('/doctors/{id}', status_code=HTTPStatus.OK)
async def update_doctor(id: int, doctor: Doctor, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctors").update(doctor.dict(exclude_unset=True)).eq("id", id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to update doctor: {response.json()}")

        return {"message": "Doctor updated successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error updating doctor: {str(e)}")

# Rota para deletar um médico
@router.delete('/doctors/{id}', status_code=HTTPStatus.OK)
async def delete_doctor(id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("doctors").delete().eq("id", id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to delete doctor: {response.json()}")

        return {"message": "Doctor deleted successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting doctor: {str(e)}")