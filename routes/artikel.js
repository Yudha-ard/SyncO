const express = require('express');
const router = express.Router();
const mysql = require("mysql");
const dotenv = require("dotenv");

// Konfigurasi dotenv
dotenv.config();

// Buat koneksi ke database
const db = mysql.createConnection({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_DATABASE,
});


// GET route to fetch data from the artikel table
router.get('/', (req, res) => {
    db.query('SELECT id_artikel, tanggal, judul, author, isi_artikel, kode_penyakit FROM artikel', (err, results) => {
        if (err) {
            console.error(err);
            res.status(500).send('Error fetching data from database');
        } else {
            // Array of month names and day names
            const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
            const dayNames = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

            // Format the date for each artikel
            results.forEach(artikel => {
                const date = new Date(artikel.tanggal);
                artikel.tanggal = `${dayNames[date.getDay()]}, ${date.getDate()} ${monthNames[date.getMonth()]} ${date.getFullYear()}`;
            });
            
            // Send response
            res.json(results);
        }
    });
});

module.exports = router;