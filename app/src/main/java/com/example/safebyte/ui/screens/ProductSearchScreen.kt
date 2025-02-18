import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Interface da API
interface OpenFoodFactsApi {
    @GET("/api/v0/product/{barcode}.json")
    fun getProduct(@Path("barcode") barcode: String): Call<ProductResponse>
}

// Modelos de dados para resposta da API
data class ProductResponse(val product: Product?)
data class Product(
    val product_name: String?,
    val brands: String?,
    val quantity: String?,
    val ingredients_text: String?,
    val ingredients_text_pt: String?,
    val allergens : String?
)

// Tela principal para busca de produtos
class ProductSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductSearchNavigator()
        }
    }
}

@Composable
fun ProductSearchNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "search") {
        composable("search") { ProductSearchScreen(navController) }
        composable("product/{barcode}") { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
            ProductDetailsScreen(navController, barcode)
        }
    }
}

// Tela de busca de produto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchScreen(navController: NavController) {
    var barcode by remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Busca de Produtos") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = barcode,
                onValueChange = { barcode = it },
                label = { Text("Digite o código de barras") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val code = barcode.text.trim()
                    if (code.isNotEmpty()) {
                        navController.navigate("product_details/$code")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar Produto")
            }
        }
    }
}

// Tela de detalhes do produto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(navController: NavController, barcode: String) {
    var productInfo by remember { mutableStateOf("Buscando produto...") }
    var isLoading by remember { mutableStateOf(true) }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://world.openfoodfacts.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api = retrofit.create(OpenFoodFactsApi::class.java)

    // Buscar os dados do produto de forma segura
    LaunchedEffect(barcode) {
        isLoading = true
        api.getProduct(barcode).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                val product = response.body()?.product
                productInfo = if (product != null) {
                    """
                    Nome: ${product.product_name ?: "Desconhecido"}
                    Marca: ${product.brands ?: "Desconhecida"}
                    Quantidade: ${product.quantity ?: "Não informada"}
                    Ingredientes: ${product.ingredients_text_pt ?: product.ingredients_text ?: "Não informados"}
                    Alergias: ${product.allergens ?: "Não informado"}
                    """.trimIndent()
                } else {
                    "Produto não encontrado."
                }
                isLoading = false
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                productInfo = "Erro ao buscar o produto."
                isLoading = false
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Produto") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(productInfo)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Voltar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductSearchScreenPreview() {
    ProductSearchScreen(rememberNavController())
}
