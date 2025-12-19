package com.example.quickorder.shoppingcart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ethieladiassa.shoppingcart.R

class ShoppingCartAdapter(var context: Context, var cartItems: List<CartItem>) :
    RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {

        val layout = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false)

        return ViewHolder(layout)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bindItem(cartItems[position])
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var productName : TextView = itemView.findViewById(R.id.product_name)
        private var productPrice : TextView = itemView.findViewById(R.id.product_price)
        private var productQuantity : TextView = itemView.findViewById(R.id.product_quantity)

        fun bindItem(cartItem: CartItem) {

            productName.text = cartItem.product.name

            productPrice.text = "$${cartItem.product.price}"

            productQuantity.text = cartItem.quantity.toString()

        }
    }

}