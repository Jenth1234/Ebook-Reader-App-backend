package com.app.application.comic_app.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.app.application.comic_app.R
import com.app.application.comic_app.model.Book
import com.bumptech.glide.Glide

class SlideAdapter(
    val context: Context,
    val listBook: MutableList<Book>
): PagerAdapter() {
    override fun getCount(): Int = listBook.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = (view == `object`)

    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.item_image_top_5_book, container, false)
        val imgView = view.findViewById<ImageView>(R.id.imageBook)
        val bookCurrent = listBook[position]
        Glide.with(context).load(bookCurrent.image).into(imgView)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}