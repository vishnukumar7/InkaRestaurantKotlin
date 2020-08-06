package com.example.carditem

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyCartActivity : AppCompatActivity(), View.OnClickListener,
    CustomOnClickListner {
    val flags = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    var backArrow: ImageView? = null
    var index = 0
    var totalCost: AppCompatTextView? = null
    var recyclerView: RecyclerView? = null
    var adapter: CartItemAdapter? = null
    var databaseHelper: DatabaseHelper? = null
    var showmore: AppCompatTextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = DatabaseHelper(this)
        index = 2
        window.decorView.systemUiVisibility = flags
        setContentView(R.layout.activity_my_cart)
        recyclerView = findViewById(R.id.recyclerViewReviewOrder)
        showmore = findViewById(R.id.show_more)
        showmore!!.setOnClickListener(this)
        adapter = CartItemAdapter(databaseHelper!!.getAllCardItem(index), this, this,1)
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        recyclerView!!.setHasFixedSize(false)
        recyclerView!!.setAdapter(adapter)
        backArrow = findViewById(R.id.back_arror)
        totalCost = findViewById(R.id.total_text_cost)
        backArrow!!.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        totalCost!!.text = "Rs. " + databaseHelper!!.totalCost
        window.decorView.systemUiVisibility = flags
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.back_arror -> onBackPressed()
            R.id.show_more -> {
                index = index + 2
                val position: Int = adapter!!.position - 1
                adapter!!.setCartItemList(databaseHelper!!.getAllCardItem(index))
                recyclerView!!.adapter = adapter
                recyclerView!!.scrollToPosition(position)
                if (index >= 20) showmore!!.visibility = View.GONE
            }
        }
    }

    override fun addItemCount(name: String?) {
        val cartItem = databaseHelper!!.getCartItem(name!!)
        if (cartItem != null) {
            cartItem.count=cartItem.count + 1
            databaseHelper!!.insertCart(cartItem)
            totalCost!!.text = "Rs. " + databaseHelper!!.totalCost
        }
    }

    override fun subItemCount(name: String?) {
        val cartItem = databaseHelper!!.getCartItem(name!!)
        if (cartItem != null) {
            cartItem.count=cartItem.count - 1
            databaseHelper!!.insertCart(cartItem)
            totalCost!!.text = "Rs. " + databaseHelper!!.totalCost
        }
    }
}
