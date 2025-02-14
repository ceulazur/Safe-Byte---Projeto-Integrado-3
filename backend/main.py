from fastapi import FastAPI
from database import create_supabase_client
from routes import user, userallergies
from routes import tips
from routes import allergysymptoms, symptoms, allergies
from routes import doctorknowallergies, doctor, doctorreview
from routes import  productreview, productallergies, products
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
#medicos
app.include_router(doctorreview.router)
app.include_router(doctorknowallergies.router)
app.include_router(doctor.router)
#produtos
app.include_router(products.router)
app.include_router(productreview.router)
app.include_router(productallergies.router)