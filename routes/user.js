const express = require("express");
const router = express.Router();
const jwt = require("jsonwebtoken");
const dotenv = require("dotenv");
const mysql = require("mysql");
const authMiddleware = require('../middlewares/authMiddleware');



// Konfigurasi dotenv
dotenv.config();

// Buat koneksi ke database
const db = mysql.createConnection({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_DATABASE,
});

// ...

// Route untuk mengambil data user berdasarkan id
router.get("/:id", authMiddleware, (req, res) => {  
    // Query database
    db.query(
        "SELECT id_user, first_name, last_name, email, dateofbirth, height, weight FROM user WHERE id_user = ?",
        [req.params.id],
        (err, results) => {
            if (err) {
                res.status(500).send(err);
                return;
            }
  
            const date = results[0].dateofbirth;
            const formattedDate = [date.getFullYear(), ('0' + (date.getMonth() + 1)).slice(-2), ('0' + date.getDate()).slice(-2)].join('-');
            res.json({ user: { ...results[0], dateofbirth: formattedDate } });

            // Kirim response
            // res.json({ user: results[0] });
        }
    );
});

// Route untuk mengedit data user berdasarkan id
router.put("/:id", authMiddleware, (req, res) => {
    const dateofbirth = req.body.dateofbirth;
    const height = req.body.height;
    const weight = req.body.weight;
    
    // Pastikan dateofbirth, height, dan weight ada dalam request body
    if (!dateofbirth || !height || !weight) {
        res.status(400).json({ message: 'Mohon isi semua field yang diperlukan' });
        return;
    }

    // Query database untuk mengupdate data
    db.query(
        "UPDATE user SET dateofbirth = ?, height = ?, weight = ? WHERE id_user = ?",
        [dateofbirth, height, weight, req.params.id],
        (err, results) => {
            if (err) {
                res.status(500).send(err);
                return;
            }

            // Kirim response
            res.json({ message: 'Data berhasil diupdate' });
        }
    );
});

module.exports = router;