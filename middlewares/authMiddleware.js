const dotenv = require("dotenv");
const jwt = require('jsonwebtoken');

function authMiddleware(req, res, next) {
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
  }

module.exports = authMiddleware;
