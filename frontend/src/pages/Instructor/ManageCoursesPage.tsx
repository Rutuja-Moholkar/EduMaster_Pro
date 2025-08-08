import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const ManageCoursesPage: React.FC = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ textAlign: 'center', py: 8 }}>
        <Typography variant="h4" gutterBottom>
          Manage Courses
        </Typography>
        <Typography variant="body1" sx={{ mt: 2 }}>
          Course management interface - Under development
        </Typography>
      </Box>
    </Container>
  );
};

export default ManageCoursesPage;