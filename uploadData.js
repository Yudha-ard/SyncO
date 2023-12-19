const fs = require('fs');
const csv = require('csv-parser');
const mysql = require('mysql2');

// Create a connection to the database
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'db-synco'
});

// Connect to the database
connection.connect(error => {
  if (error) throw error;
  console.log('Successfully connected to the database.');
});

// Read the CSV file
fs.createReadStream('./symptom_penyakit.csv')
  .pipe(csv())
  .on('data', (row) => {
    console.log(row);
    // Insert the data into the database
    const query = 'INSERT INTO penyakit (nama_penyakit, deskripsi, pencegahan) VALUES (?, ?, ?)';
    const values = [row.nama_penyakit, row.deskripsi, `${row.pencegahan_1}, ${row.pencegahan_2}, ${row.pencegahan_3}, ${row.pencegahan_4}`];
    connection.query(query, values, (error, results, fields) => {
      if (error) throw error;
    });
  })
  .on('end', () => {
    console.log('CSV file successfully processed');
  });