package com.example.quickorder.shoppingcart

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ethieladiassa.shoppingcart.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

class ProductAdapter(var context: Context, var products: List<Product> = arrayListOf()) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_row_item, parent, false)
        return ViewHolder(view)

    }

    private var recyclerView: RecyclerView? = null

    private val heightListener = View.OnLayoutChangeListener { v, _, top, _, bottom, _, oldTop, _, oldBottom ->
        if (bottom - top != oldBottom - oldTop) v.post { notifyDataSetChanged() }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        recyclerView.addOnLayoutChangeListener(heightListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnLayoutChangeListener(heightListener)
        this.recyclerView = null
    }

    private fun applyRowHeight(view: View) {
        val parent = recyclerView ?: return
        val availableHeight = parent.height - parent.paddingTop - parent.paddingBottom
        if (availableHeight <= 0) return
        val params = view.layoutParams
        // StaggeredGridLayoutManager subtracts the item margins from an exact height itself.
        val rowHeight = availableHeight / ROWS_PER_SCREEN
        if (params.height != rowHeight) {
            params.height = rowHeight
            view.layoutParams = params
        }
    }

    override fun getItemCount(): Int = products.size

    companion object {
        private const val ROWS_PER_SCREEN = 3
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        applyRowHeight(viewHolder.itemView)
        viewHolder.bindProduct(products[position])


//        (context as MainActivity).coordinator

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var productName: TextView = itemView.findViewById(R.id.product_name)
        private var productPrice: TextView = itemView.findViewById(R.id.product_price)
        private var productCategory: TextView = itemView.findViewById(R.id.product_category)
        private var addToCart: ImageButton = itemView.findViewById(R.id.addToCart)
        private var removeItem: ImageButton = itemView.findViewById(R.id.removeItem)

        @SuppressLint("CheckResult")
        fun bindProduct(product: Product) {
            productName.text = product.name
            productPrice.text = "$${product.price.toString()}"
            productCategory.text = product.category
            //                val products = mutableListOf<Product>()
//                products.add(product)
//

//                ShoppingCart.addItem(item)


            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {

                addToCart.setOnClickListener { view ->

                    val item = CartItem(product)

                    ShoppingCart.addItem(item)
                    //notify users
                    Snackbar.make(
                        (itemView.context as MainActivity).binding.coordinator,
                        "${product.name} added to your cart",
                        Snackbar.LENGTH_LONG
                    ).show()

                    it.onNext(ShoppingCart.getInstance().getCart())

                }

                removeItem.setOnClickListener { view ->

                    val item = CartItem(product)

                    ShoppingCart.removeItem(item, itemView.context)

                    Snackbar.make(
                        (itemView.context as MainActivity).binding.coordinator,
                        "${product.name} removed from your cart",
                        Snackbar.LENGTH_LONG
                    ).show()
                    it.onNext(ShoppingCart.getInstance().getCart())

                }


            }).subscribe { cart ->

                var quantity = 0

                cart.forEach { cartItem ->

                    quantity += cartItem.quantity
                }

                (itemView.context as MainActivity).binding.cartSize.text = quantity.toString()
                Toast.makeText(itemView.context, "Cart size $quantity", Toast.LENGTH_SHORT).show()

//            }
//                Toast.makeText(itemView.context, "${product.name} added to your cart", Toast.LENGTH_SHORT).show()


            }

        }
    }
}