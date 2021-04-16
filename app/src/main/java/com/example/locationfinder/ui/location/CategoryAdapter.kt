package com.example.locationfinder.ui.location

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.locationfinder.R
import com.example.locationfinder.constant.McConstants.LocationCategory

/**
 * CategoryAdapter
 */
class CategoryAdapter(context: Context) :
    ArrayAdapter<LocationCategory>(context, 0, LocationCategory.values()) {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: layoutInflater.inflate(R.layout.category_item, parent, false)
        getItem(position)?.let { category -> setItemForCategory(view, category) }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (position == 0) {
            view = layoutInflater.inflate(R.layout.header_category, parent, false)
            view.setOnClickListener {

                val root = parent.rootView
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                root.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
            }
        } else {
            val category = getItem(position)
            view = layoutInflater.inflate(R.layout.category_item, parent, false)
            category?.let { it -> setItemForCategory(view, it) }
        }
        return view
    }

    override fun getItem(position: Int): LocationCategory? {
        if (position == 0) return null
        return super.getItem(position - 1)
    }

    override fun getCount(): Int = super.getCount() + 1

    override fun isEnabled(position: Int) = position != 0

    private fun setItemForCategory(view: View, category: LocationCategory) {
        //뷰와 바인딩시키기
        view.findViewById<TextView>(R.id.tv_category).text = category.categoryDescription
    }
}