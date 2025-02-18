package com.example.openfoodfacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("/api/v0/product/{barcode}.json")
    fun getProduct(@Path("barcode") barcode: String): Call<ProductResponse>
}

data class ProductResponse(val product: Product?)
data class Product(val product_name: String?, val brands: String?, val quantity: String?)

class ProductSearchScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductSearchUI()
        }
    }
}

@Composable
fun ProductSearchUI() {
    var barcode by remember { mutableStateOf(TextFieldValue()) }
    var productInfo by remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl("https://world.openfoodfacts.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(OpenFoodFactsApi::class.java)

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text("Digite o código de barras") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val code = barcode.text
            if (code.isNotEmpty()) {
                api.getProduct(code).enqueue(object : Callback<ProductResponse> {
                    override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                        val product = response.body()?.product
                        productInfo = if (product != null) {
                            "Nome: ${'$'}{product.product_name}\nMarca: ${'$'}{product.brands}\nQuantidade: ${'$'}{product.quantity}"
                        } else {
                            "Produto não encontrado."
                        }
                    }

                    override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                        productInfo = "Erro ao buscar o produto."
                    }
                })
            }
        }) {
            Text("Buscar Produto")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(productInfo)
    }
}

@Preview(showBackground = true)
@Composable
fun ProductSearchUIPreview() {
    ProductSearchUI()
}
