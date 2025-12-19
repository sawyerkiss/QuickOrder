package com.example.quickorder.shoppingcart

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ethieladiassa.shoppingcart.R
import com.example.ethieladiassa.shoppingcart.databinding.ActivityShoppingCartBinding

class ShoppingCartActivity : AppCompatActivity() {

    lateinit var adapter: ShoppingCartAdapter
    lateinit var binding: ActivityShoppingCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCartBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_shopping_cart)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        upArrow?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)


        adapter = ShoppingCartAdapter(this, ShoppingCart.getInstance().getCart())
        adapter.notifyDataSetChanged()

        binding.shoppingCartRecyclerView.adapter = adapter

        binding.shoppingCartRecyclerView.layoutManager = LinearLayoutManager(this)


        var totalPrice = ShoppingCart.getInstance().getCart()
            .fold(0.toDouble()) { acc, cartItem -> acc + cartItem.quantity.times(cartItem.product.price!!.toDouble()) }


        binding.totalPrice.text = "$${totalPrice}"
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()

            }
        }

        return super.onOptionsItemSelected(item)
    }
}
