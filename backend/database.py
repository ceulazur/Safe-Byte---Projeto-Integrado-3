from supabase import Client, create_client
from dotenv import load_dotenv
import os

# Load environment variables
load_dotenv()

# Fetch variables
url = os.getenv("SUPABASE_URL")
key = os.getenv("SUPABASE_KEY")

def create_supabase_client():
    supabase: Client = create_client(url, key)
    return supabase


