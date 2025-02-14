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
#tips
app.include_router(tips.router)
#produtos
app.include_router(products.router)
app.include_router(productreview.router)
app.include_router(productallergies.router)
#alergias
app.include_router(allergies.router)
app.include_router(allergysymptoms.router)
#estabelecimentos
app.include_router(establishmentreview.router)
app.include_router(establishmentallergies.router)
app.include_router(establishment.router)
