const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const dotenv = require("dotenv");
const mysql = require("mysql");
const authMiddleware = require('../middlewares/authMiddleware');

dotenv.config();

const db = mysql.createConnection({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_DATABASE
});

db.connect((err) => {
  if (err) {
    console.error('Error connecting to database:', err);
    process.exit(1);
  } else {
    console.log('Connected to database');
  }
});

// Route untuk registrasi
router.post('/register', (req, res) => {
  // Validate input
  // cost id_user = req
  const email = req.body.email;
  const password = req.body.password;
  const firstName = req.body.first_name;
  const lastName = req.body.last_name;

  // Check if all required fields are provided
  if (!email || !password || !firstName || !lastName) {
    res.status(400).json({ message: "Input tidak boleh kosong!" });
    return;
  }

  // Check if all fields are of the correct type
  if (typeof email !== 'string' || typeof password !== 'string' || typeof firstName !== 'string' || typeof lastName !== 'string') {
    res.status(400).json({ message: "Semua input harus string!" });
    return;
  }

  // Hash password
  const hashedPassword = bcrypt.hashSync(password, 10);

  // Cek apakah email sudah terdaftar
  db.query("SELECT * FROM user WHERE email = ?", [email], (err, results) => {
    if (err) {
      res.status(500).send(err);
      return;
    }

    if (results.length > 0) {
      res.status(400).json({ message: "Email sudah terdaftar!" });
      return;
    }

    // Daftarkan user baru
    db.query("INSERT INTO user (email, password, first_name, last_name) VALUES (?, ?, ?, ?)", [email, hashedPassword, firstName,lastName], (err) => {
      if (err) {
        res.status(500).send(err);
        return;
      }

      // Registrasi berhasil
      res.json({ message: "Registrasi berhasil!" });
    });
  });
});

router.get("/", (req, res) => {
  res.sendFile("index.php", { root: __dirname });
});

router.post("/login", (req, res) => {
  // Validasi input
  const email = req.body.email; 
  const password = req.body.password;
  if (!email || !password) {
    res.status(400).json({ message: "email dan password harus diisi!" }); //send("email dan password harus diisi!");
    return;
  }

  // Cek email dan password di database
  db.query("SELECT * FROM user WHERE email = ?", [email], (err, results) => {
    if (err) {
      res.status(500).send(err);
      return;
    }

    if (results.length === 0) {
      res.status(400).json({
        error: true,
        message: "Invalid email or password"
      });
      return;
    }

    // Bandingkan password
    if (bcrypt.compareSync(password, results[0].password)) {
      // Login berhasil
      const token = jwt.sign({ email }, process.env.JWT_SECRET, { expiresIn: "1h" });
      const uid = results[0].id_user;
      const userId = uid.toString();
      const fname = results[0].first_name;
      const lname = results[0].last_name;
      res.json({
        error: false,
        message: "success",
        loginResult: {
          userId: userId, // replace with the actual user ID from the database
          firstName: fname, // replace with the actual name from the database
          lastName: lname, // replace with the actual name from the database
          token: token
        }
      });
    } else { 
      // Login gagal
      res.status(400).json({
        error: true,
        message: "Invalid email or password"
      });
    }
  });
});

router.use((req, res, next) => {
    const token = req.headers["authorization"];
    if (!token) {
      return res.status(401).send("Unauthorized");
    }
  
    try {
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      req.user = decoded;
      next();
    } catch (error) {
      return res.status(401).send("Invalid token");
    }
  });

router.post("/logout", authMiddleware, (req, res) => {
  res.json({ message: 'Logout berhasil' });
});

module.exports = router;