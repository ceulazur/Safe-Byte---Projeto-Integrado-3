from fastapi import FastAPI
from database import create_supabase_client
from routes import user, userallergies
from routes import tips
from routes import allergysymptoms, symptoms, allergies
app = FastAPI()

# Initialize supabase client
supabase = create_supabase_client()

#users
app.include_router(user.router)
app.include_router(userallergies.router)
#tips
app.include_router(tips.router)
#alergias
app.include_router(allergies.router)
app.include_router(allergysymptoms.router)
#simtomas
app.include_router(symptoms.router)