import React from 'react';
import { Container, Typography, Box, Divider } from '@mui/material';

const TermsOfServicePage: React.FC = () => {
  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
        Terms of Service
      </Typography>
      <Typography variant="body2" color="text.secondary" gutterBottom>
        Last updated: {new Date().toLocaleDateString()}
      </Typography>
      
      <Divider sx={{ my: 3 }} />

      <Box sx={{ '& > *': { mb: 3 } }}>
        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Acceptance of Terms
          </Typography>
          <Typography variant="body1" paragraph>
            By accessing and using EduMaster Pro, you accept and agree to be bound by the terms 
            and provision of this agreement.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Use of the Service
          </Typography>
          <Typography variant="body1" paragraph>
            You may use our service for lawful purposes only. You agree not to use the service 
            in any way that could damage, disable, or impair the service or interfere with any 
            other party's use of the service.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            User Accounts
          </Typography>
          <Typography variant="body1" paragraph>
            You are responsible for maintaining the confidentiality of your account and password. 
            You agree to accept responsibility for all activities that occur under your account.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Intellectual Property
          </Typography>
          <Typography variant="body1" paragraph>
            The service and its original content, features, and functionality are and will remain 
            the exclusive property of EduMaster Pro and its licensors.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Refund Policy
          </Typography>
          <Typography variant="body1" paragraph>
            We offer a 30-day money-back guarantee for all courses. If you're not satisfied with 
            your purchase, you may request a full refund within 30 days.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Contact Information
          </Typography>
          <Typography variant="body1">
            If you have any questions about these Terms of Service, please contact us at 
            legal@edumaster.com.
          </Typography>
        </Box>
      </Box>
    </Container>
  );
};

export default TermsOfServicePage;