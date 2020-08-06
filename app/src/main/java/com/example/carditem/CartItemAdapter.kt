package com.example.carditem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class CartItemAdapter(private var cartItemList: List<CartItem>, customOnClickListner: CustomOnClickListner, context: Context?,type :Int) :
    RecyclerView.Adapter<CartItemAdapter.CustomViewHolder?>() {
    private val customOnClickListner: CustomOnClickListner
    private val databaseHelper: DatabaseHelper
    private val type : Int
    var position = 0
        private set


    fun setCartItemList(cartItemList: List<CartItem>) {
        this.cartItemList = cartItemList
    }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val cartItem = cartItemList[position]
        buttonView(holder, cartItem.name)
        if (type == 1) holder.message!!.visibility = View.VISIBLE else holder.message!!.visibility = View.GONE
        this.position = position
        // holder.count.setText("" + cartItem.getCount());
        holder.description.setText(cartItem.description)
        holder.price.text = "Rs." + cartItem.price
        holder.name.setText(cartItem.name)
        holder.add.setOnClickListener {
            customOnClickListner.addItemCount(cartItem.name)
            buttonView(holder, cartItem.name)
        }
        holder.sub.setOnClickListener {
            customOnClickListner.subItemCount(cartItem.name)
            buttonView(holder, cartItem.name)
        }
    }

    private fun buttonView(holder: CustomViewHolder, name: String?) {
        val count: Int = databaseHelper.getCartItemCount(name!!)
        holder.count.text = "" + count
        if (count == 0) {
            holder.sub.isEnabled = false
            holder.sub.isClickable = false
        } else if (count == 20) {
            holder.add.isEnabled = false
            holder.add.isClickable = false
        } else {
            holder.sub.isEnabled = true
            holder.sub.isClickable = true
            holder.add.isEnabled = true
            holder.add.isClickable = true
        }
    }


    class CustomViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: AppCompatTextView
        var description: AppCompatTextView
        var price: AppCompatTextView
        var count: AppCompatTextView
        var add: AppCompatButton
        var sub: AppCompatButton
        var message: ImageView? = null

        init {
            name = itemView.findViewById(R.id.card_text_name)
            description = itemView.findViewById(R.id.card_text_description)
            price = itemView.findViewById(R.id.card_text_price)
            count = itemView.findViewById(R.id.card_text_count)
            add = itemView.findViewById(R.id.card_text_add)
            sub = itemView.findViewById(R.id.card_text_sub)
            message = itemView.findViewById(R.id.message)
        }
    }

    init {
        databaseHelper = DatabaseHelper(context)
        this.customOnClickListner = customOnClickListner
        this.type=type
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }
}
