const bcrypt = require('bcrypt');

const plaintextPassword = 'admin1';

// Generate a salt and hash the password
bcrypt.genSalt(10, (err, salt) => {
  bcrypt.hash(plaintextPassword, salt, (err, hash) => {
    if (err) {
      console.error('Error hashing password:', err);
    } else {
      // Store the hashed password in the database or elsewhere
      console.log('Hashed Password:', hash);
    }
  });
});
