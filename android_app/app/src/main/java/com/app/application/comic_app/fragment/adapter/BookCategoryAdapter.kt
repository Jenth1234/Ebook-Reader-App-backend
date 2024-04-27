package com.app.application.comic_app.fragment.adapter

import com.app.application.comic_app.R
import com.app.application.comic_app.model.Book
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class BookCategoryAdapter: BaseQuickAdapter<Book, BaseViewHolder>(R.layout.item_book_category) {

    init {
        addChildClickViewIds(R.id.textCategory1)
    }

    override fun convert(holder: BaseViewHolder, item: Book) {
        Glide.with(context).load(item.image).into(holder.getView(R.id.imageBook))
        holder.setText(R.id.textBookName, item.title)
            .setText(R.id.textAuthorName, item.author)
    }
}