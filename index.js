const express = require("express");
const dotenv = require("dotenv");
const mysql = require("mysql");
const app = express();

//IMPORT ROUTER
const authRoutes = require('./routes/auth');

// Konfigurasi dotenv
dotenv.config();

//MIDDLEWARE
app.use(express.json());

app.use('/auth', authRoutes)

// Jalankan server
const port = process.env.PORT || 3000;
const server = app.listen(port, () => {
  console.log(`Server berjalan di port ${port}`);
});