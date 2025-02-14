from fastapi import APIRouter, Depends
from pydantic import BaseModel, Field
from typing import Optional
from supabase import Client
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()

# Modelo Pydantic para validar os dados da alergia
class Allergies(BaseModel):
    id: Optional[int] = Field(default=None, primary_key=True)
    name: str
    description: str
    severity_level: str #varchar
    general_recommendations: str #campo de texto

# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar uma alergia
@router.post("/allergies/")
async def add_alergia(allergies: Allergies, supabase: Client = Depends(get_supabase_client)):
    # Inserindo os dados da alergia na tabela 'alergias'
    response = supabase.table("allergies").insert(allergies.dict()).execute()

    if response.status_code == 201:  # Sucesso
        return {"message": "Alergia adicionada com sucesso", "alergia": allergies}
    else:
        return {"error": "Falha ao adicionar alergia", "details": response.json()}

# Rota para pegar uma alergia pelo ID
@router.get("/allergies/{allergies_id}")
async def get_alergia(allergies_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("allergies").select("*").eq("id", allergies_id).execute()

    if response.status_code == 200 and response.data:
        return {"alergia": response.data[0]}
    else:
        return {"error": "Alergia não encontrada"}

# Rota para atualizar os dados de uma alergia
@router.put("/allergies/{allergies_id}")
async def update_allergies(allergies_id: int, allergies: Allergies, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("allergies").update(allergies.dict()).eq("id", allergies_id).execute()

    if response.status_code == 200:
        return {"message": "Alergia atualizada com sucesso"}
    else:
        return {"error": "Falha ao atualizar alergia"}

# Rota para deletar uma alergia
@router.delete("/allergies/{allergies_id}")
async def delete_allergies(allergies_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("allergies").delete().eq("id", allergies_id).execute()

    if response.status_code == 200:
        return {"message": "Alergia deletada com sucesso"}
    else:
        return {"error": "Falha ao deletar alergia"}
