from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel, Field
from typing import List, Optional
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()

class Establishment(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    name: str #varchar
    address: str #text
    phone: str #VARCHAR
    business_hours: str #text
    establishment_type: str #VARCHAR
    cnpj: str #VARCHAR
    status: bool = Field(default=True)
    email: str #VARCHAR -- tem um strig especifico para email tbm, mas ai tem que baixar o poettry e tal
    
 # Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar um estabelecimento
@router.post('/establishments', status_code=HTTPStatus.CREATED)
async def add_establishment(establishment: Establishment, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishments").insert(establishment.dict(exclude_unset=True)).execute()

        if response.status_code != 201:
            raise HTTPException(status_code=400, detail=f"Failed to add establishment: {response.json()}")

        return {"message": "Establishment added successfully", "establishment": establishment}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error adding establishment: {str(e)}")

# Rota para pegar um estabelecimento pelo ID
@router.get('/establishments/{establishment_id}', status_code=HTTPStatus.OK, response_model=Establishment)
async def get_establishment(establishment_id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishments").select("*").eq("id", establishment_id).execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="Establishment not found")

        return response.data[0]

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching establishment: {str(e)}")

# Rota para listar todos os estabelecimentos
@router.get('/establishments', status_code=HTTPStatus.OK, response_model=List[Establishment])
async def list_establishments(supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishments").select("*").execute()

        if not response.data:
            raise HTTPException(status_code=404, detail="No establishments found")

        return response.data

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching establishments: {str(e)}")

# Rota para atualizar um estabelecimento
@router.put('/establishments/{establishment_id}', status_code=HTTPStatus.OK)
async def update_establishment(establishment_id: int, establishment: Establishment, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishments").update(establishment.dict(exclude_unset=True)).eq("id", establishment_id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to update establishment: {response.json()}")

        return {"message": "Establishment updated successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error updating establishment: {str(e)}")

# Rota para deletar um estabelecimento
@router.delete('/establishments/{establishment_id}', status_code=HTTPStatus.OK)
async def delete_establishment(establishment_id: int, supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("establishments").delete().eq("id", establishment_id).execute()

        if response.status_code != 200:
            raise HTTPException(status_code=400, detail=f"Failed to delete establishment: {response.json()}")

        return {"message": "Establishment deleted successfully"}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error deleting establishment: {str(e)}")
