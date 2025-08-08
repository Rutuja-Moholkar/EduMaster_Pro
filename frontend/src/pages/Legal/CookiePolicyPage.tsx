import React from 'react';
import { Container, Typography, Box, Divider } from '@mui/material';

const CookiePolicyPage: React.FC = () => {
  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
        Cookie Policy
      </Typography>
      <Typography variant="body2" color="text.secondary" gutterBottom>
        Last updated: {new Date().toLocaleDateString()}
      </Typography>
      
      <Divider sx={{ my: 3 }} />

      <Box sx={{ '& > *': { mb: 3 } }}>
        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            What Are Cookies
          </Typography>
          <Typography variant="body1" paragraph>
            Cookies are small pieces of text sent to your web browser by a website you visit. 
            A cookie file is stored in your web browser and allows the Service or a third-party 
            to recognize you and make your next visit easier.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            How We Use Cookies
          </Typography>
          <Typography variant="body1" paragraph>
            We use cookies to enhance your experience, remember your preferences, analyze site 
            traffic, and improve our services. This helps us provide a better user experience.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Types of Cookies We Use
          </Typography>
          <Typography variant="body1" paragraph>
            <strong>Essential Cookies:</strong> Required for the website to function properly.
            <br />
            <strong>Analytics Cookies:</strong> Help us understand how visitors interact with our website.
            <br />
            <strong>Functional Cookies:</strong> Enable enhanced functionality and personalization.
            <br />
            <strong>Marketing Cookies:</strong> Used to deliver relevant advertisements.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Managing Cookies
          </Typography>
          <Typography variant="body1" paragraph>
            You can control and/or delete cookies as you wish. You can delete all cookies that 
            are already on your computer and you can set most browsers to prevent them from being placed.
          </Typography>
        </Box>

        <Box>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Contact Us
          </Typography>
          <Typography variant="body1">
            If you have any questions about our use of cookies, please contact us at 
            privacy@edumaster.com.
          </Typography>
        </Box>
      </Box>
    </Container>
  );
};

export default CookiePolicyPage;