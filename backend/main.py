from fastapi import FastAPI
from database import create_supabase_client
from routes import user, userallergies
from routes import tips
app = FastAPI()

# Initialize supabase client
supabase = create_supabase_client()

#users
app.include_router(user.router)
app.include_router(userallergies.router)
#tips
app.include_router(tips.router)