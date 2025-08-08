import React, { useState } from 'react';
import {
  Box,
  Container,
  Typography,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Grid,
  Card,
  CardContent,
  TextField,
  Button,
  Chip,
  Avatar,
  InputAdornment,
} from '@mui/material';
import {
  ExpandMore,
  Search,
  Help,
  School,
  Payment,
  Settings,
  Support,
  VideoLibrary,
} from '@mui/icons-material';

const HelpPage: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('all');

  const categories = [
    { id: 'all', label: 'All Topics', icon: <Help /> },
    { id: 'courses', label: 'Courses', icon: <School /> },
    { id: 'payments', label: 'Payments', icon: <Payment /> },
    { id: 'technical', label: 'Technical', icon: <Settings /> },
    { id: 'account', label: 'Account', icon: <Support /> },
  ];

  const faqs = [
    {
      category: 'courses',
      question: 'How do I enroll in a course?',
      answer: 'To enroll in a course, browse our course catalog, select a course you\'re interested in, and click the "Enroll Now" button. You\'ll need to create an account and complete the payment process if it\'s a paid course.',
    },
    {
      category: 'courses',
      question: 'Can I access courses offline?',
      answer: 'Yes! With our mobile app, you can download course content and watch videos offline. Simply download the course materials when you have an internet connection, and access them anytime, anywhere.',
    },
    {
      category: 'courses',
      question: 'Do I get a certificate after completing a course?',
      answer: 'Yes, you receive a certificate of completion for every course you finish. These certificates can be downloaded and shared on your LinkedIn profile or resume.',
    },
    {
      category: 'payments',
      question: 'What payment methods do you accept?',
      answer: 'We accept all major credit cards (Visa, MasterCard, American Express), PayPal, and bank transfers. All payments are processed securely through our payment partners.',
    },
    {
      category: 'payments',
      question: 'Can I get a refund?',
      answer: 'Yes, we offer a 30-day money-back guarantee. If you\'re not satisfied with a course, you can request a full refund within 30 days of purchase.',
    },
    {
      category: 'technical',
      question: 'What browsers are supported?',
      answer: 'Our platform works best on the latest versions of Chrome, Firefox, Safari, and Edge. We also support mobile browsers on iOS and Android devices.',
    },
    {
      category: 'technical',
      question: 'I\'m having trouble playing videos. What should I do?',
      answer: 'Try refreshing the page, clearing your browser cache, or switching to a different browser. If the problem persists, contact our technical support team.',
    },
    {
      category: 'account',
      question: 'How do I reset my password?',
      answer: 'Click on "Forgot Password" on the login page and enter your email address. We\'ll send you a link to reset your password.',
    },
    {
      category: 'account',
      question: 'Can I change my email address?',
      answer: 'Yes, you can update your email address in your account settings. Go to Profile > Settings > Account Information to make changes.',
    },
  ];

  const quickLinks = [
    { title: 'Getting Started Guide', description: 'New to EduMaster? Start here!' },
    { title: 'Course Catalog', description: 'Browse all available courses' },
    { title: 'Mobile App', description: 'Download our mobile app' },
    { title: 'Community Forum', description: 'Connect with other learners' },
    { title: 'Instructor Resources', description: 'Resources for teaching' },
    { title: 'Contact Support', description: 'Get personalized help' },
  ];

  const filteredFaqs = faqs.filter((faq) => {
    const matchesCategory = selectedCategory === 'all' || faq.category === selectedCategory;
    const matchesSearch = faq.question.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         faq.answer.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Hero Section */}
      <Box sx={{ textAlign: 'center', mb: 6 }}>
        <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
          Help Center
        </Typography>
        <Typography variant="h5" color="text.secondary" paragraph>
          Find answers to your questions and get the help you need
        </Typography>
        
        {/* Search Bar */}
        <Box sx={{ maxWidth: 600, mx: 'auto', mt: 4 }}>
          <TextField
            fullWidth
            placeholder="Search for help articles..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <Search />
                </InputAdornment>
              ),
            }}
            sx={{ 
              '& .MuiOutlinedInput-root': {
                borderRadius: 3,
                backgroundColor: 'white',
              }
            }}
          />
        </Box>
      </Box>

      {/* Categories */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" gutterBottom fontWeight="bold">
          Browse by Category
        </Typography>
        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
          {categories.map((category) => (
            <Chip
              key={category.id}
              icon={category.icon}
              label={category.label}
              clickable
              color={selectedCategory === category.id ? 'primary' : 'default'}
              onClick={() => setSelectedCategory(category.id)}
              sx={{ mb: 1 }}
            />
          ))}
        </Box>
      </Box>

      <Grid container spacing={4}>
        {/* FAQ Section */}
        <Grid item xs={12} md={8}>
          <Typography variant="h4" gutterBottom fontWeight="bold">
            Frequently Asked Questions
          </Typography>
          
          {filteredFaqs.length === 0 ? (
            <Box sx={{ textAlign: 'center', py: 4 }}>
              <Typography variant="h6" color="text.secondary">
                No articles found for your search.
              </Typography>
              <Button 
                variant="outlined" 
                onClick={() => setSearchQuery('')}
                sx={{ mt: 2 }}
              >
                Clear Search
              </Button>
            </Box>
          ) : (
            filteredFaqs.map((faq, index) => (
              <Accordion key={index} sx={{ mb: 1 }}>
                <AccordionSummary expandIcon={<ExpandMore />}>
                  <Typography variant="h6" fontWeight="bold">
                    {faq.question}
                  </Typography>
                </AccordionSummary>
                <AccordionDetails>
                  <Typography variant="body1" color="text.secondary">
                    {faq.answer}
                  </Typography>
                </AccordionDetails>
              </Accordion>
            ))
          )}
        </Grid>

        {/* Sidebar */}
        <Grid item xs={12} md={4}>
          {/* Quick Links */}
          <Card sx={{ mb: 3 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom fontWeight="bold">
                Quick Links
              </Typography>
              {quickLinks.map((link, index) => (
                <Box 
                  key={index}
                  sx={{ 
                    p: 2, 
                    mb: 1, 
                    backgroundColor: 'grey.50',
                    borderRadius: 1,
                    cursor: 'pointer',
                    '&:hover': {
                      backgroundColor: 'grey.100',
                    }
                  }}
                >
                  <Typography variant="subtitle2" fontWeight="bold">
                    {link.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {link.description}
                  </Typography>
                </Box>
              ))}
            </CardContent>
          </Card>

          {/* Contact Support */}
          <Card>
            <CardContent sx={{ textAlign: 'center' }}>
              <Avatar sx={{ bgcolor: 'primary.main', width: 64, height: 64, mx: 'auto', mb: 2 }}>
                <Support />
              </Avatar>
              <Typography variant="h6" gutterBottom fontWeight="bold">
                Still Need Help?
              </Typography>
              <Typography variant="body2" color="text.secondary" paragraph>
                Can't find what you're looking for? Our support team is here to help.
              </Typography>
              <Button 
                variant="contained" 
                fullWidth 
                onClick={() => window.location.href = '/contact'}
              >
                Contact Support
              </Button>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
};

export default HelpPage;