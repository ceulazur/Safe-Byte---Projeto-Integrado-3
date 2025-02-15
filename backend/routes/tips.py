from http import HTTPStatus
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel
from typing import List
import supabase
from main import create_supabase_client

router = APIRouter()

class Tip(BaseModel):
    id: int
    message: str
    created_at: str

# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

@router.get('/tips', status_code=HTTPStatus.OK, response_model=List[Tip])
def read_tips(supabase = Depends(get_supabase_client)):
    try:
        response = supabase.table("tips").select("*").limit(10).execute()  # Limite de 10 registros para testar
        
        if not response.data:
            raise HTTPException(status_code=404, detail="No data found in 'tips' table.")
        
        # Retorna os dados como uma lista de objetos Tip
        return response.data
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error fetching tips: {str(e)}")
