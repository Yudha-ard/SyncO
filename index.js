const express = require("express");
const dotenv = require("dotenv");
const bcrypt = require("bcryptjs");
const mysql = require("mysql");
const jwt = require("jsonwebtoken");

// Konfigurasi dotenv
dotenv.config();

// Konfigurasi database
const db = mysql.createConnection({
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_DATABASE
});

// Koneksi ke database
db.connect((err) => {
  if (err) {
    console.error('Error connecting to database:', err);
  } else {
    console.log('Connected to database');
  }
});

// Route untuk login
const app = express();
app.use(express.json());
app.use(express.static("public"));

// Route untuk registrasi
app.post('/register', (req, res) => {
    // Validate input
    // cost id_user = req
    const email = req.body.email;
    const password = req.body.password;
    const firstName = req.body.first_name;
    const lastName = req.body.last_name;

    console.log(`${email} ${password} ${firstName} ${lastName}`)
  
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

app.get("/", (req, res) => {
  res.sendFile("index.php", { root: __dirname });
});

app.post("/auth", (req, res) => {
  // Validasi input
  const email = req.body.email;
  const password = req.body.password;
  if (!email || !password) {
    res.status(400).send("email dan password harus diisi!");
    return;
  }

  // Cek email dan password di database
  db.query("SELECT * FROM user WHERE email = ?", [email], (err, results) => {
    if (err) {
      res.status(500).send(err);
      return;
    }

    if (results.length === 0) {
      res.status(401).send("email atau password salah!");
      return;
    }

    // Bandingkan password
    if (bcrypt.compareSync(password, results[0].password)) {
      // Login berhasil
      const token = jwt.sign({ email }, process.env.JWT_SECRET, { expiresIn: "1h" });
      res.json({ token });
    } else {
      // Login gagal
      res.status(401).send("email atau password salah!");
    }
  });
});

app.use((req, res, next) => {
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


  app.post("/logout", (req, res) => {
    // Cek token di header request
    const token = req.headers["authorization"];
    if (!token) {
      return res.status(401).send("Unauthorized");
    }
  
    // Verifikasi token
    try {
      jwt.verify(token, process.env.JWT_SECRET);
    } catch (error) {
      return res.status(401).send("Invalid token");
    }
  
    // Hapus token (sesi)
    // ... implementasikan logika untuk menghapus token
  
    // Kirim response sukses
    res.json({ message: "Logout berhasil" });
  });
  

// Jalankan server
const port = process.env.PORT || 3000;
const server = app.listen(port, () => {
  console.log(`Server berjalan di port ${port}`);
});