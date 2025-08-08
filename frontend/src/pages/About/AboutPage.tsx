import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const AboutPage: React.FC = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ textAlign: 'center', py: 8 }}>
        <Typography variant="h4" gutterBottom>
          About EduMaster Pro
        </Typography>
        <Typography variant="body1" sx={{ mt: 2, maxWidth: 600, mx: 'auto' }}>
          EduMaster Pro is a comprehensive e-learning platform designed to provide
          high-quality education to students worldwide. Our mission is to make learning
          accessible, engaging, and effective for everyone.
        </Typography>
      </Box>
    </Container>
  );
};

export default AboutPage;