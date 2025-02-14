from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional

from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()


class Symptoms(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    name: str
    description: str #TEXT
    severity_level: str #VARCHAR
    recomemended_actions: str #TEXT
    
#Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar um novo sintoma
@router.post('/symptoms', status_code=HTTPStatus.CREATED)
async def add_symptom(symptom: Symptoms, supabase=Depends(get_supabase_client)):
    try:
        response = supabase.table("symptoms").insert(symptom.dict(exclude_unset=True)).execute()

        if response.status_code != 201:
            raise HTTPException(status_code=400, detail=f"Failed to add symptom: {response.json()}")

        return {"message": "Symptom added successfully", "symptom": symptom}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error adding symptom: {str(e)}")

# Rota para obter um sintoma pelo ID
@router.get('/symptoms/{id}', status_code=HTTPStatus.OK, response_model=Symptoms)
async def get_symptom(id: int, supabase=Depends(get_supabase_client)):
    try:
        response = supabase.table("symptoms").select("*").eq("id", id).execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="Symptom not found")

        return response.data[0]

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching symptom: {str(e)}")

# Rota para listar todos os sintomas
@router.get('/symptoms', status_code=HTTPStatus.OK, response_model=List[Symptoms])
async def list_symptoms(supabase=Depends(get_supabase_client)):
    try:
        response = supabase.table("symptoms").select("*").execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="No symptoms found")

        return response.data

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching symptoms: {str(e)}")

# Rota para atualizar um sintoma
@router.put('/symptoms/{id}', status_code=HTTPStatus.OK)
async def update_symptom(id: int, symptom: Symptoms, supabase=Depends(get_supabase_client)):
    try:
        response = supabase.table("symptoms").update(symptom.dict(exclude_unset=True)).eq("id", id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to update symptom: {response.json()}")

        return {"message": "Symptom updated successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error updating symptom: {str(e)}")

# Rota para deletar um sintoma
@router.delete('/symptoms/{id}', status_code=HTTPStatus.OK)
async def delete_symptom(id: int, supabase=Depends(get_supabase_client)):
    try:
        response = supabase.table("symptoms").delete().eq("id", id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to delete symptom: {response.json()}")

        return {"message": "Symptom deleted successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting symptom: {str(e)}")