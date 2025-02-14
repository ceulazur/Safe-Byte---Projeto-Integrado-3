from fastapi import APIRouter, Depends
from pydantic import BaseModel
from typing import Optional
from datetime import datetime
from supabase import Client
from main import create_supabase_client

# Criando o objeto APIRouter
router = APIRouter()

# Modelo Pydantic para validar os dados do usuário
class User(BaseModel):
    name: str
    email: str
    password: str
    birth_date: datetime
    phone: str
    status: Optional[bool] = True
    created_at: Optional[datetime] = None

# Função para pegar o cliente do Supabase
def get_supabase_client():
    return create_supabase_client()

# Rota para adicionar um usuário
@router.post("/users/")
async def add_user(user: User, supabase: Client = Depends(get_supabase_client)):
    # Inserindo os dados do usuário na tabela 'users'
    response = supabase.table("users").insert(user.dict()).execute()

    if response.status_code == 201:  # Sucesso
        return {"message": "User added successfully", "user": user}
    else:
        return {"error": "Failed to add user", "details": response.json()}

# Rota para pegar um usuário pelo ID
@router.get("/users/{user_id}")
async def get_user(user_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("users").select("*").eq("id", user_id).execute()

    if response.status_code == 200 and response.data:
        return {"user": response.data[0]}
    else:
        return {"error": "User not found"}

# Rota para atualizar os dados de um usuário
@router.put("/users/{user_id}")
async def update_user(user_id: int, user: User, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("users").update(user.dict()).eq("id", user_id).execute()

    if response.status_code == 200:
        return {"message": "User updated successfully"}
    else:
        return {"error": "Failed to update user"}

# Rota para deletar um usuário
@router.delete("/users/{user_id}")
async def delete_user(user_id: int, supabase: Client = Depends(get_supabase_client)):
    response = supabase.table("users").delete().eq("id", user_id).execute()

    if response.status_code == 200:
        return {"message": "User deleted successfully"}
    else:
        return {"error": "Failed to delete user"}
