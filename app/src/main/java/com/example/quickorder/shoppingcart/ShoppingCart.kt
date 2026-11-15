package com.example.quickorder.shoppingcart

import android.content.Context

class ShoppingCart private constructor() {
    var cartList: ArrayList<CartItem> = ArrayList()
    init {
        // define in constructor
    }

    private object Holder { val INSTANCE = ShoppingCart() }

    companion object {
        @JvmStatic
        fun getInstance(): ShoppingCart{
            return Holder.INSTANCE
        }

        fun addItem(cartItem: CartItem) {
            val cart = getInstance().getCart()

            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }

            if (targetItem == null) {
                cartItem.quantity++
                cart.add(cartItem)
            } else {

                targetItem.quantity++
            }
            getInstance().saveCart(cart)

        }

        fun removeItem(cartItem: CartItem, context: Context) {

            val cart = getInstance().getCart()


            val targetItem = cart.singleOrNull { it.product.id == cartItem.product.id }

            if (targetItem != null) {

                targetItem.quantity--
                if (targetItem.quantity <= 0) {
                    cart.remove(targetItem)
                }

            }

            getInstance().saveCart(cart)

        }



        fun getShoppingCartSize(): Int {

            var cartSize = 0
            getInstance().getCart().forEach {
                cartSize += it.quantity;
            }

            return cartSize
        }
    }

    fun saveCart(cart: MutableList<CartItem>) {
        if(!cartList.containsAll(cart))
            cartList.addAll(cart)
    }

    fun getCart(): MutableList<CartItem> {
        return cartList
    }

}