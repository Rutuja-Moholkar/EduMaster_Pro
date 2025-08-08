import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const InstructorDashboard: React.FC = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ textAlign: 'center', py: 8 }}>
        <Typography variant="h4" gutterBottom>
          Instructor Dashboard
        </Typography>
        <Typography variant="body1" sx={{ mt: 2 }}>
          Instructor analytics and course management - Under development
        </Typography>
      </Box>
    </Container>
  );
};

export default InstructorDashboard;