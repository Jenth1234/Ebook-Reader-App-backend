const mongoose = require('mongoose');

const bookSchema = mongoose.Schema({
  title: String,
  author: String,
  genre: String,
  description: String,
  image: String,
  file: String,
}, { timestamps: true });

const Book = mongoose.model("Book", bookSchema);

module.exports = Book;
