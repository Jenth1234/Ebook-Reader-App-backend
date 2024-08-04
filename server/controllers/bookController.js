const Book = require('../models/bookModel');

const getBooks = async (req, res) => {
  try {
    const data = await Book.find({});
    res.json({ success: true, data });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

const createBook = async (req, res) => {
  try {
    const data = new Book(req.body);
    await data.save();
    res.send({ success: true, message: "Data saved successfully", data });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

const updateBook = async (req, res) => {
  try {
    const { id } = req.params;
    const { ...rest } = req.body;
    const data = await Book.findByIdAndUpdate(id, rest);
    res.send({ success: true, message: "Data updated successfully", data });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

const deleteBook = async (req, res) => {
  try {
    const { id } = req.params;
    const data = await Book.findByIdAndDelete(id);
    res.send({ success: true, message: "Data deleted successfully", data });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

module.exports = { getBooks, createBook, updateBook, deleteBook };
