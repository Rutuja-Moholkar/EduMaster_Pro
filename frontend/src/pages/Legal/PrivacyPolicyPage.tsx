import React from 'react';
import { Container, Typography, Box, Divider } from '@mui/material';

const PrivacyPolicyPage: React.FC = () => {
  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
        Privacy Policy
      </Typography>
      <Typography variant="body2" color="text.secondary" gutterBottom>
        Last updated: {new Date().toLocaleDateString()}
      </Typography>
      
      <Divider sx={{ my: 3 }} />

      <Box sx={{ '& > *': { mb: 3 } }}>
        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Information We Collect
          </Typography>
          <Typography variant="body1" paragraph>
            We collect information you provide directly to us, such as when you create an account, 
            enroll in courses, or contact us for support. This may include your name, email address, 
            payment information, and learning preferences.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            How We Use Your Information
          </Typography>
          <Typography variant="body1" paragraph>
            We use the information we collect to provide, maintain, and improve our services, 
            process transactions, send notifications, and provide customer support.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Information Sharing
          </Typography>
          <Typography variant="body1" paragraph>
            We do not sell, trade, or otherwise transfer your personal information to third parties 
            without your consent, except as described in this privacy policy.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Data Security
          </Typography>
          <Typography variant="body1" paragraph>
            We implement appropriate security measures to protect your personal information against 
            unauthorized access, alteration, disclosure, or destruction.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Contact Us
          </Typography>
          <Typography variant="body1">
            If you have any questions about this Privacy Policy, please contact us at 
            privacy@edumaster.com or through our contact form.
          </Typography>
        </Box>
      </Box>
    </Container>
  );
};

export default PrivacyPolicyPage;