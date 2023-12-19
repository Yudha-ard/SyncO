const express = require('express');
const axios = require('axios');
const router = express.Router();

router.get('/', async (req, res) => {
    try {
        const externalURL = 'https://parenting-api.orami.co.id/magazine/api/v1/categories/kesehatan-umum/posts/?format=json';
        const response = await axios.get(externalURL);
        const responseData = response.data;
    
        const extractedData = responseData.results.map(post => ({
          link: `https://www.orami.co.id/magazine/${encodeURIComponent(post.slug)}`,
          title: post.title,
          slug: post.slug,
          thumbnail: post.featured_thumbnail ? post.featured_thumbnail.file : null,
          intro: post.intro,
          date: post.date
        }));
    
        res.json(extractedData);
      } catch (error) {
        console.error('Error fetching data:', error);
        res.status(500).json({ error: 'Failed to fetch data' });
      }
});

module.exports = router;