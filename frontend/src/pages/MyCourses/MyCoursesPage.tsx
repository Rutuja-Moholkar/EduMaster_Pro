import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const MyCoursesPage: React.FC = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ textAlign: 'center', py: 8 }}>
        <Typography variant="h4" gutterBottom>
          My Courses
        </Typography>
        <Typography variant="body1" sx={{ mt: 2 }}>
          Your enrolled courses will be displayed here - Under development
        </Typography>
      </Box>
    </Container>
  );
};

export default MyCoursesPage;