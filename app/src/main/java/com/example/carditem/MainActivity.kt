package com.example.carditem

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), CustomOnClickListner {
    val flags = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    var viewCart: AppCompatButton? = null
    var databaseHelper: DatabaseHelper? = null
    private var recyclerView: RecyclerView? = null
    private var cartItemAdapter: CartItemAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = flags
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerViewBookScreen)
        databaseHelper = DatabaseHelper(this)
        viewCart = findViewById(R.id.view_cart_items)
        initInsert()
        viewCart!!.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MainActivity, MyCartActivity::class.java)) })
    }

    fun initInsert() {
        for (i in 1..20) {
            val cartItem = CartItem()
            cartItem.count=0
            cartItem.description="description $i"
            cartItem.name="item $i"
            cartItem.price=5
            databaseHelper!!.oneTimeInsert(cartItem)
        }
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = flags
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        cartItemAdapter = CartItemAdapter(databaseHelper!!.allCardItem, this, this,0)
        recyclerView!!.setAdapter(cartItemAdapter)
        recyclerView!!.setHasFixedSize(false)
        val count: Int = databaseHelper!!.totalCount
        if (count != 0) viewCart!!.text = "VIEW CART($count ITEMS )" else viewCart!!.text = "VIEW CART"
    }

   override fun addItemCount(name: String?) {
        val cartItem = databaseHelper!!.getCartItem(name!!)
        if (cartItem != null) {
            cartItem.count=cartItem.count + 1
            databaseHelper!!.insertCart(cartItem)
        }
        val count: Int = databaseHelper!!.totalCount
        if (count != 0) viewCart!!.text = "VIEW CART($count ITEMS )" else viewCart!!.text = "VIEW CART"
    }

   override fun subItemCount(name: String?) {
        val cartItem = databaseHelper!!.getCartItem(name!!)
        if (cartItem != null) {
            cartItem.count=cartItem.count - 1
            databaseHelper!!.insertCart(cartItem)
        }
        val count: Int = databaseHelper!!.totalCount
        if (count != 0) viewCart!!.text = "VIEW CART($count ITEMS )" else viewCart!!.text = "VIEW CART"
    }
}
