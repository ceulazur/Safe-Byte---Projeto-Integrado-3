from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()

class EstablishmentAllergies(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    establishment_id: int
    allergy_id: int
    risk_level: str #varchar
    notes: str #text
    
# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()


@router.get('/establishment_allergies', status_code=HTTPStatus.OK, response_model=List[EstablishmentAllergies])
def read_establishment_allergies(supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_allergies").select("*").limit(10).execute()  # Limite de 10 registros para teste
        
        if not response.data:
            raise HTTPException(status_code=404, detail="No data found in 'establishment_allergies' table.")
        
        # Retorna os dados como uma lista de objetos EstablishmentAllergy
        return response.data
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching establishment allergies: {str(e)}")


@router.get('/establishment_allergies/{allergy_id}', status_code=HTTPStatus.OK, response_model=EstablishmentAllergies)
def get_establishment_allergy(allergy_id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_allergies").select("*").eq("id", allergy_id).execute()
        
        if not response.data:
            raise HTTPException(status_code=404, detail="Establishment allergy not found.")
        
        # Retorna os dados da alergia do estabelecimento específica
        return response.data[0]
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching establishment allergy: {str(e)}")


@router.post('/establishment_allergies', status_code=HTTPStatus.CREATED, response_model=EstablishmentAllergies)
def add_establishment_allergy(allergy: EstablishmentAllergies, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_allergies").insert(allergy.dict(exclude_unset=True)).execute()
        
        if response.status_code != 201:
            raise HTTPException(status_code=400, detail="Failed to add establishment allergy")
        
        # Retorna a alergia do estabelecimento adicionada
        return response.data[0]
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error adding establishment allergy: {str(e)}")


@router.put('/establishment_allergies/{allergy_id}', status_code=HTTPStatus.OK, response_model=EstablishmentAllergies)
def update_establishment_allergy(allergy_id: int, allergy: EstablishmentAllergies, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_allergies").update(allergy.dict(exclude_unset=True)).eq("id", allergy_id).execute()
        
        if response.status_code != 200:
            raise HTTPException(status_code=400, detail="Failed to update establishment allergy")
        
        # Retorna a alergia do estabelecimento atualizada
        return response.data[0]
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error updating establishment allergy: {str(e)}")


@router.delete('/establishment_allergies/{allergy_id}', status_code=HTTPStatus.NO_CONTENT)
def delete_establishment_allergy(allergy_id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishment_allergies").delete().eq("id", allergy_id).execute()
        
        if response.status_code != 200:
            raise HTTPException(status_code=400, detail="Failed to delete establishment allergy")
        
        # Retorna uma mensagem de sucesso
        return {"message": "Establishment allergy deleted successfully"}
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting establishment allergy: {str(e)}")
