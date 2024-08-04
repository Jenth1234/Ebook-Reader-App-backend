const Account = require('../models/accountModel');

const getAccounts = async (req, res) => {
  try {
    const data = await Account.find({});
    res.json({ success: true, data });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

const createAccount = async (req, res) => {
  try {
    const data = new Account(req.body);
    await data.save();
    res.send({ success: true, message: "Data saved successfully", data });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

const updateAccount = async (req, res) => {
  try {
    const { id } = req.params;
    const { ...rest } = req.body;
    const data = await Account.findByIdAndUpdate(id, rest);
    res.send({ success: true, message: "Data updated successfully", data });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

const deleteAccount = async (req, res) => {
  try {
    const { id } = req.params;
    const data = await Account.findByIdAndDelete(id);
    res.send({ success: true, message: "Data deleted successfully", data });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
};

module.exports = { getAccounts, createAccount, updateAccount, deleteAccount };
