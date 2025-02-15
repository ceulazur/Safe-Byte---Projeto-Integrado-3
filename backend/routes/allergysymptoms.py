from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()


class AllergySymptoms(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    allergy_id: int
    symptom_id: int
    
# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

@router.post('/allergy-symptoms', status_code=HTTPStatus.CREATED)
async def add_allergy_symptom(allergy_symptom: AllergySymptoms, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("allergy_symptoms").insert(allergy_symptom.dict(exclude_unset=True)).execute()

        if response.status_code != 201:
            raise HTTPException(status_code=400, detail=f"Failed to add allergy symptom relation: {response.json()}")

        return {"message": "Allergy symptom relation added successfully", "allergy_symptom": allergy_symptom}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error adding allergy symptom relation: {str(e)}")

# Rota para obter uma relação de alergia e sintoma pelo ID
@router.get('/allergy-symptoms/{id}', status_code=HTTPStatus.OK, response_model=AllergySymptoms)
async def get_allergy_symptom(id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("allergy_symptoms").select("*").eq("id", id).execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="Allergy symptom relation not found")

        return response.data[0]

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching allergy symptom relation: {str(e)}")

# Rota para listar todas as relações de alergia e sintoma
@router.get('/allergy-symptoms', status_code=HTTPStatus.OK, response_model=List[AllergySymptoms])
async def list_allergy_symptoms(supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("allergy_symptoms").select("*").execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="No allergy symptom relations found")

        return response.data

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching allergy symptom relations: {str(e)}")

# Rota para atualizar uma relação de alergia e sintoma
@router.put('/allergy-symptoms/{id}', status_code=HTTPStatus.OK)
async def update_allergy_symptom(id: int, allergy_symptom: AllergySymptoms, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("allergy_symptoms").update(allergy_symptom.dict(exclude_unset=True)).eq("id", id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to update allergy symptom relation: {response.json()}")

        return {"message": "Allergy symptom relation updated successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error updating allergy symptom relation: {str(e)}")

# Rota para deletar uma relação de alergia e sintoma
@router.delete('/allergy-symptoms/{id}', status_code=HTTPStatus.OK)
async def delete_allergy_symptom(id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("allergy_symptoms").delete().eq("id", id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to delete allergy symptom relation: {response.json()}")

        return {"message": "Allergy symptom relation deleted successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting allergy symptom relation: {str(e)}")