package com.example.carditem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "cart.db", null, 1) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(cart_item_schema)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.delete(cart_item_table_name, null, null)
        onCreate(sqLiteDatabase)
    }

    fun insertCart(cartItem: CartItem) {
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(item_count, cartItem.count)
        values.put(item_description, cartItem.description)
        values.put(item_price, cartItem.price)
        values.put(item_name, cartItem.name)
        val result = database.update(
            cart_item_table_name,
            values,
            item_name + "='" + cartItem.name + "'",
            null
        )
        if (result == 0) database.insert(
            cart_item_table_name,
            null,
            values
        )
    }

    fun oneTimeInsert(cartItem: CartItem) {
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * FROM " + cart_item_table_name + " WHERE " + item_name + "='" + cartItem.name + "'",
            null
        )
        if (cursor == null || cursor.count == 0) insertCart(cartItem)
        cursor?.close()
    }

    val allCardItem: List<CartItem>
        get() {
            val cartItemList: MutableList<CartItem> = ArrayList()
            val database = this.readableDatabase
            val cursor = database.rawQuery(
                "SELECT * FROM $cart_item_table_name",
                null
            )
            if (cursor != null && cursor.count >= 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val cartItem = CartItem()
                    cartItem.count = cursor.getInt(cursor.getColumnIndex(item_count))
                    cartItem.description = cursor.getString(cursor.getColumnIndex(item_description))
                    cartItem.name = cursor.getString(cursor.getColumnIndex(item_name))
                    cartItem.price = cursor.getInt(cursor.getColumnIndex(item_price))
                    cartItemList.add(cartItem)
                    cursor.moveToNext()
                }
                cursor.close()
            }
            return cartItemList
        }

    fun getAllCardItem(index: Int): List<CartItem> {
        var index = index
        val cartItemList: MutableList<CartItem> = ArrayList()
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * FROM $cart_item_table_name",
            null
        )
        if (cursor != null && cursor.count >= 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast && index != 0) {
                val cartItem = CartItem()
                cartItem.count = cursor.getInt(cursor.getColumnIndex(item_count))
                cartItem.description = cursor.getString(cursor.getColumnIndex(item_description))
                cartItem.name = cursor.getString(cursor.getColumnIndex(item_name))
                cartItem.price = cursor.getInt(cursor.getColumnIndex(item_price))
                cartItemList.add(cartItem)
                index--
                cursor.moveToNext()
            }
            cursor.close()
        }
        return cartItemList
    }

    fun getCartItem(name: String): CartItem? {
        var cartItem: CartItem? = null
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * FROM $cart_item_table_name WHERE $item_name='$name'",
            null
        )
        if (cursor != null && cursor.count != 0) {
            cartItem = CartItem()
            cursor.moveToFirst()
            cartItem.count = cursor.getInt(cursor.getColumnIndex(item_count))
            cartItem.description = cursor.getString(cursor.getColumnIndex(item_description))
            cartItem.name = cursor.getString(cursor.getColumnIndex(item_name))
            cartItem.price = cursor.getInt(cursor.getColumnIndex(item_price))
            cursor.close()
        }
        return cartItem
    }

    fun getCartItemCount(name: String): Int {
        var count = 0
        val database = this.readableDatabase
        val cursor = database.rawQuery(
            "SELECT * FROM $cart_item_table_name WHERE $item_name='$name'",
            null
        )
        if (cursor != null && cursor.count != 0) {
            cursor.moveToFirst()
            count = cursor.getInt(cursor.getColumnIndex(item_count))
            cursor.close()
        }
        return count
    }

    val totalCount: Int
        get() {
            var total = 0
            val database = this.readableDatabase
            val cursor = database.rawQuery(
                "SELECT * FROM $cart_item_table_name",
                null
            )
            if (cursor != null && cursor.count >= 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    total += cursor.getInt(cursor.getColumnIndex(item_count))
                    cursor.moveToNext()
                }
                cursor.close()
            }
            return total
        }

    val totalCost: Int
        get() {
            var total = 0
            val database = this.readableDatabase
            val cursor = database.rawQuery(
                "SELECT * FROM $cart_item_table_name",
                null
            )
            if (cursor != null && cursor.count >= 0) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val count =
                        cursor.getInt(cursor.getColumnIndex(item_count))
                    val price =
                        cursor.getInt(cursor.getColumnIndex(item_price))
                    total += count * price
                    cursor.moveToNext()
                }
                cursor.close()
            }
            return total
        }

    companion object {
        private const val cart_item_table_name = "cart_item_table"
        private const val item_name = "name"
        private const val item_description = "description"
        private const val item_price = "price"
        private const val item_count = "count"
        private const val cart_item_schema =
            "CREATE TABLE IF NOT EXISTS " + cart_item_table_name + "(" +
                    "" + item_name + " text," +
                    "" + item_description + " text," +
                    "" + item_price + " integer," +
                    "" + item_count + " integer" +
                    ")"
    }
}
