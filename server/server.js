const express = require('express');
const cors = require('cors');
const multer = require('multer');
const dotenv = require('dotenv');
const logRequest = require('./middlewares/logRequest');
const connectDB = require('./config/db');
const bookRoutes = require('./routes/bookRoutes');
const accountRoutes = require('./routes/accountRoutes');
const app = express();
dotenv.config();

app.use(cors());
app.use(express.json());
app.use(logRequest);

const upload = multer({ storage: multer.memoryStorage() });

app.post('/upload', upload.single('pdf'), async (req, res) => {
  try {
    const file = req.file;
    if (!file) {
      return res.status(400).json({ error: 'No file uploaded' });
    }
    const storageRef = await storage.ref();
    const fileRef = await storageRef.child(`pdfs/${file.originalname}`);
    await fileRef.put(file.buffer);
    return res.status(200).json({ message: 'File uploaded successfully' });
  } catch (error) {
    console.error('Error uploading file:', error);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

app.use('/books', bookRoutes);
app.use('/accounts', accountRoutes);

const PORT = process.env.PORT || 8080;

connectDB().then(() => {
  app.listen(PORT, () => console.log(`Server is running on port ${PORT}`));
});
