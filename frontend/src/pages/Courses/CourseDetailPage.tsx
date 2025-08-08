import React from 'react';
import { Container, Typography, Box } from '@mui/material';
import { useParams } from 'react-router-dom';

const CourseDetailPage: React.FC = () => {
  const { id } = useParams();

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ textAlign: 'center', py: 8 }}>
        <Typography variant="h4" gutterBottom>
          Course Detail Page
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Course ID: {id}
        </Typography>
        <Typography variant="body1" sx={{ mt: 2 }}>
          This page is under development. Course details will be displayed here.
        </Typography>
      </Box>
    </Container>
  );
};

export default CourseDetailPage;