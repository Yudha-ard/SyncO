const axios = require('axios');
const express = require('express');
const router = express.Router();
const mysql = require("mysql");
const dotenv = require("dotenv");
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

router.post('/', authMiddleware, async (req, res) => {
  try {
    const { symptoms } = req.body;

    if (!symptoms || !Array.isArray(symptoms)) {
      return res.status(400).json({ message: 'Invalid request body. Symptoms array is missing.' });
    }

    const data = { symptoms };

    const response = await axios.post('https://yudhaard.pythonanywhere.com/predict', data);

    const predictionArray = response.data.prediction;

    if (Array.isArray(predictionArray) && predictionArray.length > 0) {
      const predictionString = predictionArray[0];
      console.log('Extracted Prediction:', predictionString);
      
      const query = `SELECT * FROM penyakit WHERE nama_penyakit = '${predictionString}'`;

      db.query(query, async (error, results) => {
        if (error) {
          console.error('Error fetching data from database:', error);
          return res.status(500).json({ message: 'Error fetching data from database.' });
        }

        if (results.length === 0) {
          return res.status(404).json({ message: 'No matching disease found in the database.' });
        }

        const { predict, id_penyakit, nama_penyakit, deskripsi, pencegahan } = results[0];
        console.log(query);

        const userEmail = req.user.email;
        db.query(
          'SELECT id_user FROM user WHERE email = ?',
          [userEmail],
          (err, userResults) => {
              if (err) {
                  return res.status(500).json({ message: 'Internal server error' });
              }
  
              if (userResults.length === 0) {
                  return res.status(404).json({ message: 'User not found' });
              }
  
              const userId = userResults[0].id_user;
              const currentDate = new Date().toISOString().slice(0, 19).replace('T', ' ');
              const insertHistoryQuery = `INSERT INTO history (id_user, id_penyakit, tanggal) VALUES (${userId}, ${id_penyakit}, '${currentDate}')`;

              db.query(insertHistoryQuery, (insertError, insertResults) => {
                if (insertError) {
                  console.error('Error inserting data into history table:', insertError);
                  return res.status(500).json({ message: 'Error inserting data into history table' });
                }

                console.log('Data inserted into history table successfully:', insertResults);
                res.status(200).json({ 
                  message: 'Data inserted into history table successfully',
                  predict,
                  nama_penyakit,
                  deskripsi,
                  pencegahan
                });
              });
  
          }
        );
      });

    } else {
      console.log('No prediction available');
      res.status(404).json({ message: 'No prediction available.' });
    }

  } catch (error) {
    console.error('An error occurred while fetching data:', error);
    res.status(500).json({ message: 'An error occurred while fetching data.' });
  }
});

module.exports = router;
