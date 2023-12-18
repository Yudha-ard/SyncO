const express = require("express");
const dotenv = require("dotenv");
const mysql = require("mysql");
const app = express();

//IMPORT ROUTER
const authRoutes = require('./routes/auth');
const getAllUsersRoute = require('./routes/user');
const artikelRoutes = require('./routes/artikel');
const predictRoutes = require('./routes/penyakit');

// Konfigurasi dotenv
dotenv.config();

//MIDDLEWARE
app.use(express.json());

app.use('/auth', authRoutes);
app.use('/api/users', getAllUsersRoute);
app.use('/artikel', artikelRoutes);
app.use('/penyakit', predictRoutes);

// Jalankan server
const port = process.env.PORT || 3000;
const server = app.listen(port, () => {
  console.log(`Server berjalan di port ${port}`);
});