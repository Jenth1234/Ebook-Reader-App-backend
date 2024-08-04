
const mongoose = require('mongoose');

const accountSchema = mongoose.Schema({
  account: String,
  password: String,
  ten: String,
  email: String,
  sdt: String,
}, { timestamps: true });

const Account = mongoose.model("Account", accountSchema);

module.exports = Account;
