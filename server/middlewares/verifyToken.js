const logRequest = (req, res, next) => {
    console.log(`Received request from: ${req.connection.remoteAddress} - ${req.method} ${req.url}`);
    next();
  };
  
  module.exports = logRequest;
  