const express = require('express');
const { getAccounts, createAccount, updateAccount, deleteAccount } = require('../controllers/accountController');
const router = express.Router();

router.get('/', getAccounts);
router.post('/', createAccount);
router.put('/:id', updateAccount);
router.delete('/:id', deleteAccount);

module.exports = router;
