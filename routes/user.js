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

router.get('/me', authMiddleware, (req, res) => {
    const userEmail = req.user.email;

    db.query(
        'SELECT id_user, CONCAT(first_name," ",last_name) AS fullname, email, dateofbirth, height, weight FROM user WHERE email = ?',
        [userEmail],
        (err, results) => {
            if (err) {
                return res.status(500).json({ message: 'Internal server error' });
            }

            if (results.length === 0) {
                return res.status(404).json({ message: 'User not found' });
            }

            res.json({ user: results[0] });
        }
    );
});


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

// Route untuk mengambil email dan username berdasarkan id
router.get("/data/:id", authMiddleware, (req, res) => {
    // Query database
    db.query(
        "SELECT id_user, email, CONCAT(first_name, ' ', last_name) AS name FROM user WHERE id_user = ?",
        [req.params.id],
        (err, results) => {
            if (err) {
                res.status(500).send(err);
                return;
            }

            if (results.length > 0) {
                res.json({ 
                            id: results[0].id_user,
                            name: results[0].name,
                            email: results[0].email
                        });
            } else {
                res.status(404).json({ message: 'No user found with the provided ID.' });
            }
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

// mendapatakan umur user berdasarkan id
router.get("/age/:id", authMiddleware, (req, res) => {
    // Query database
    db.query(
        "SELECT dateofbirth FROM user WHERE id_user = ?",
        [req.params.id],
        (err, results) => {
            if (err) {
                res.status(500).send(err);
                return;
            }

            const birthDate = new Date(results[0].dateofbirth);
            const today = new Date();
            let age = today.getFullYear() - birthDate.getFullYear();
            const month = today.getMonth() - birthDate.getMonth();

            if (month < 0 || (month === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }

            res.json({ age: age });
        }
    );
});

// Route untuk mengambil berat badan user berdasarkan id
router.get("/weight/:id", authMiddleware, (req, res) => {
    // Query database
    db.query(
        "SELECT weight FROM user WHERE id_user = ?",
        [req.params.id],
        (err, results) => {
            if (err) {
                res.status(500).send(err);
                return;
            }

            if (results.length > 0) {
                res.json({ weight: results[0].weight });
            } else {
                res.status(404).json({ message: 'No user found with the provided ID.' });
            }
        }
    );
});

// Route untuk mengambil tinggi badan user berdasarkan id
router.get("/height/:id", authMiddleware, (req, res) => {
    // Query database
    db.query(
        "SELECT height FROM user WHERE id_user = ?",
        [req.params.id],
        (err, results) => {
            if (err) {
                res.status(500).send(err);
                return;
            }

            if (results.length > 0) {
                res.json({ height: results[0].height });
            } else {
                res.status(404).json({ message: 'No user found with the provided ID.' });
            }
        }
    );
});

module.exports = router;