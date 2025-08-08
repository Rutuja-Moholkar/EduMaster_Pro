import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const PaymentsPage: React.FC = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ textAlign: 'center', py: 8 }}>
        <Typography variant="h4" gutterBottom>
          Payment History
        </Typography>
        <Typography variant="body1" sx={{ mt: 2 }}>
          Your payment history and invoices will be displayed here - Under development
        </Typography>
      </Box>
    </Container>
  );
};

export default PaymentsPage;