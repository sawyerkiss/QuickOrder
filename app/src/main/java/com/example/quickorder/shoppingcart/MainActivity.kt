package com.example.quickorder.shoppingcart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ethieladiassa.shoppingcart.R
import com.example.ethieladiassa.shoppingcart.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {

    private lateinit var apiService: APIService
    private lateinit var productAdapter: ProductAdapter
    public lateinit var binding: ActivityMainBinding
    private var products = listOf<Product>()
//    val APIConfig = APIConfig.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        apiService = APIConfig.getRetrofitClient(this).create(APIService::class.java)


        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,
            R.color.colorPrimary
        ))

        binding.swipeRefreshLayout.isRefreshing = true

//        val layoutManager = StaggeredGridLayoutManager(this, Lin)

        binding.productsRecyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        binding.cartSize.text = ShoppingCart.getShoppingCartSize().toString()

        getProductsOffline()


        binding.showCart.setOnClickListener {

            startActivity(Intent(this, ShoppingCartActivity::class.java))
        }

    }

    private fun getProductsOffline() {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val data = "[{\"id\":1,\"name\":\"Paracetamol 500mg\",\"price\":15000,\"category\":\"Pain Relief\",\"isPrescription\":false},{\"id\":2,\"name\":\"Amoxicillin 500mg\",\"price\":45000,\"category\":\"Antibiotic\",\"isPrescription\":true},{\"id\":3,\"name\":\"Vitamin C 1000mg\",\"price\":30000,\"category\":\"Supplement\",\"isPrescription\":false},{\"id\":4,\"name\":\"Cetirizine 10mg\",\"price\":20000,\"category\":\"Allergy\",\"isPrescription\":false}]"

        val productListType: Type = object : TypeToken<List<Product>>() {}.type
        products = gson.fromJson(data, productListType)

        productAdapter = ProductAdapter(this@MainActivity, products)

        binding.productsRecyclerview.adapter = productAdapter

        productAdapter.notifyDataSetChanged()

        binding.swipeRefreshLayout.isRefreshing = false
        binding.swipeRefreshLayout.isEnabled = false
    }

    private fun getProductsOnline() {

        apiService.getProducts().enqueue(object : retrofit2.Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {

                print(t.message)
                t.message?.let { Log.d("Data error", it) }
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()



            }

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                binding.swipeRefreshLayout.isRefreshing = false
                binding.swipeRefreshLayout.isEnabled = false

                products = response.body()!!

                productAdapter = ProductAdapter(this@MainActivity, products)

                binding.productsRecyclerview.adapter = productAdapter

                productAdapter.notifyDataSetChanged()

            }

        })
    }

}
