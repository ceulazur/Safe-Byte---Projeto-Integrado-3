from fastapi import FastAPI
from database import create_supabase_client
from routes import allergysymptoms, doctor, productallergies, symptoms, user, allergies,  userallergies
from routes import doctorknowallergies, doctorreview
from routes import  establishment, establishmentallergies, establishmentreview
from routes import  productreview, products
from routes import tips
app = FastAPI()

# Initialize supabase client
supabase = create_supabase_client()

#users
app.include_router(user.router)
app.include_router(userallergies.router)
#alergias
app.include_router(allergies.router)
app.include_router(allergysymptoms.router)

