import React, { useState } from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  Button,
  TextField,
  MenuItem,
  Avatar,
  Chip,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider,
  Alert,
} from '@mui/material';
import {
  Support,
  Phone,
  Email,
  Chat,
  Schedule,
  CheckCircle,
  VideoCall,
  Help,
  Person,
  Book,
} from '@mui/icons-material';

const InstructorSupportPage: React.FC = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    category: '',
    subject: '',
    message: '',
  });

  const supportOptions = [
    {
      title: 'Live Chat',
      description: 'Get instant help from our support team',
      icon: <Chat />,
      availability: 'Available 24/7',
      responseTime: 'Immediate',
      action: 'Start Chat',
      color: '#4CAF50',
    },
    {
      title: 'Email Support',
      description: 'Send us a detailed message about your issue',
      icon: <Email />,
      availability: 'Always available',
      responseTime: 'Within 2 hours',
      action: 'Send Email',
      color: '#2196F3',
    },
    {
      title: 'Video Call',
      description: 'Schedule a one-on-one call with our experts',
      icon: <VideoCall />,
      availability: 'Mon-Fri 9AM-6PM PST',
      responseTime: 'Same day booking',
      action: 'Schedule Call',
      color: '#FF9800',
    },
    {
      title: 'Phone Support',
      description: 'Call us directly for urgent matters',
      icon: <Phone />,
      availability: 'Mon-Fri 8AM-8PM PST',
      responseTime: 'Immediate',
      action: 'Call Now',
      color: '#9C27B0',
    },
  ];

  const categories = [
    'Course Creation',
    'Technical Issues',
    'Payment & Billing',
    'Marketing & Promotion',
    'Student Management',
    'Platform Features',
    'General Inquiry',
  ];

  const commonIssues = [
    {
      category: 'Course Creation',
      issues: [
        'How to upload and organize course content',
        'Video quality and formatting requirements',
        'Creating engaging quizzes and assignments',
        'Setting up course pricing and promotions',
      ],
    },
    {
      category: 'Technical Support',
      issues: [
        'Platform navigation and features',
        'Troubleshooting upload issues',
        'Mobile app functionality',
        'Browser compatibility problems',
      ],
    },
    {
      category: 'Revenue & Payments',
      issues: [
        'Understanding payment schedules',
        'Setting up payment methods',
        'Tax reporting and documentation',
        'Revenue sharing policies',
      ],
    },
  ];

  const supportTeam = [
    {
      name: 'Sarah Johnson',
      role: 'Lead Instructor Success Manager',
      expertise: 'Course Creation & Strategy',
      avatar: '/images/support/sarah.jpg',
    },
    {
      name: 'Michael Chen',
      role: 'Technical Support Specialist',
      expertise: 'Platform & Technical Issues',
      avatar: '/images/support/michael.jpg',
    },
    {
      name: 'Emily Rodriguez',
      role: 'Marketing Support Specialist',
      expertise: 'Promotion & Student Engagement',
      avatar: '/images/support/emily.jpg',
    },
  ];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle form submission
    console.log('Support request submitted:', formData);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Hero Section */}
      <Box sx={{ textAlign: 'center', mb: 6 }}>
        <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
          Instructor Support
        </Typography>
        <Typography variant="h5" color="text.secondary" paragraph>
          We're here to help you succeed. Get expert support whenever you need it.
        </Typography>
      </Box>

      {/* Support Options */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" gutterBottom fontWeight="bold">
          How Can We Help?
        </Typography>
        <Grid container spacing={3}>
          {supportOptions.map((option, index) => (
            <Grid item xs={12} sm={6} md={3} key={index}>
              <Card 
                sx={{ 
                  height: '100%', 
                  textAlign: 'center',
                  border: '2px solid transparent',
                  '&:hover': {
                    borderColor: option.color,
                    transform: 'translateY(-4px)',
                    transition: 'all 0.3s ease',
                  }
                }}
              >
                <CardContent sx={{ p: 3 }}>
                  <Avatar
                    sx={{
                      bgcolor: option.color,
                      width: 64,
                      height: 64,
                      mx: 'auto',
                      mb: 2,
                    }}
                  >
                    {option.icon}
                  </Avatar>
                  <Typography variant="h6" gutterBottom fontWeight="bold">
                    {option.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" paragraph>
                    {option.description}
                  </Typography>
                  <Chip
                    label={option.availability}
                    size="small"
                    variant="outlined"
                    sx={{ mb: 1 }}
                  />
                  <Typography variant="caption" display="block" color="text.secondary" sx={{ mb: 2 }}>
                    Response time: {option.responseTime}
                  </Typography>
                  <Button
                    variant="contained"
                    fullWidth
                    sx={{ backgroundColor: option.color }}
                  >
                    {option.action}
                  </Button>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      <Grid container spacing={4}>
        {/* Contact Form */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent sx={{ p: 4 }}>
              <Typography variant="h4" gutterBottom fontWeight="bold">
                Send Us a Message
              </Typography>
              <Typography variant="body1" color="text.secondary" paragraph>
                Describe your issue and we'll get back to you as soon as possible.
              </Typography>
              
              <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Your Name"
                      name="name"
                      value={formData.name}
                      onChange={handleInputChange}
                      required
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Email Address"
                      name="email"
                      type="email"
                      value={formData.email}
                      onChange={handleInputChange}
                      required
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      select
                      label="Category"
                      name="category"
                      value={formData.category}
                      onChange={handleInputChange}
                      required
                    >
                      {categories.map((category) => (
                        <MenuItem key={category} value={category}>
                          {category}
                        </MenuItem>
                      ))}
                    </TextField>
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="Subject"
                      name="subject"
                      value={formData.subject}
                      onChange={handleInputChange}
                      required
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="Message"
                      name="message"
                      multiline
                      rows={4}
                      value={formData.message}
                      onChange={handleInputChange}
                      required
                      placeholder="Please describe your issue in detail..."
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <Button
                      type="submit"
                      variant="contained"
                      size="large"
                      sx={{ mt: 2 }}
                    >
                      Send Message
                    </Button>
                  </Grid>
                </Grid>
              </Box>
            </CardContent>
          </Card>

          {/* Common Issues */}
          <Box sx={{ mt: 4 }}>
            <Typography variant="h4" gutterBottom fontWeight="bold">
              Common Issues & Solutions
            </Typography>
            <Grid container spacing={2}>
              {commonIssues.map((section, index) => (
                <Grid item xs={12} md={4} key={index}>
                  <Card>
                    <CardContent>
                      <Typography variant="h6" gutterBottom fontWeight="bold">
                        {section.category}
                      </Typography>
                      <List dense>
                        {section.issues.map((issue, idx) => (
                          <ListItem key={idx} sx={{ px: 0 }}>
                            <ListItemIcon>
                              <Help fontSize="small" color="action" />
                            </ListItemIcon>
                            <ListItemText 
                              primary={issue}
                              primaryTypographyProps={{ variant: 'body2' }}
                            />
                          </ListItem>
                        ))}
                      </List>
                    </CardContent>
                  </Card>
                </Grid>
              ))}
            </Grid>
          </Box>
        </Grid>

        {/* Sidebar */}
        <Grid item xs={12} md={4}>
          {/* Contact Information */}
          <Card sx={{ mb: 3 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom fontWeight="bold">
                Contact Information
              </Typography>
              <List>
                <ListItem sx={{ px: 0 }}>
                  <ListItemIcon>
                    <Email color="primary" />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Email"
                    secondary="instructor-support@edumaster.com"
                  />
                </ListItem>
                <ListItem sx={{ px: 0 }}>
                  <ListItemIcon>
                    <Phone color="primary" />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Phone"
                    secondary="+1 (555) 123-4567"
                  />
                </ListItem>
                <ListItem sx={{ px: 0 }}>
                  <ListItemIcon>
                    <Schedule color="primary" />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Support Hours"
                    secondary="Mon-Fri: 8AM-8PM PST"
                  />
                </ListItem>
              </List>
            </CardContent>
          </Card>

          {/* Support Team */}
          <Card sx={{ mb: 3 }}>
            <CardContent>
              <Typography variant="h6" gutterBottom fontWeight="bold">
                Meet Our Support Team
              </Typography>
              {supportTeam.map((member, index) => (
                <Box key={index} sx={{ mb: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <Avatar src={member.avatar} sx={{ mr: 2 }}>
                      <Person />
                    </Avatar>
                    <Box>
                      <Typography variant="subtitle2" fontWeight="bold">
                        {member.name}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        {member.role}
                      </Typography>
                    </Box>
                  </Box>
                  <Typography variant="body2" color="text.secondary">
                    Specializes in: {member.expertise}
                  </Typography>
                  {index < supportTeam.length - 1 && <Divider sx={{ my: 2 }} />}
                </Box>
              ))}
            </CardContent>
          </Card>

          {/* Quick Tips */}
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom fontWeight="bold">
                Quick Tips
              </Typography>
              <Alert severity="info" sx={{ mb: 2 }}>
                For faster support, include your course ID and screenshots when reporting technical issues.
              </Alert>
              <List>
                <ListItem sx={{ px: 0 }}>
                  <ListItemIcon>
                    <CheckCircle color="success" fontSize="small" />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Check our FAQ section first"
                    primaryTypographyProps={{ variant: 'body2' }}
                  />
                </ListItem>
                <ListItem sx={{ px: 0 }}>
                  <ListItemIcon>
                    <CheckCircle color="success" fontSize="small" />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Provide detailed issue descriptions"
                    primaryTypographyProps={{ variant: 'body2' }}
                  />
                </ListItem>
                <ListItem sx={{ px: 0 }}>
                  <ListItemIcon>
                    <CheckCircle color="success" fontSize="small" />
                  </ListItemIcon>
                  <ListItemText 
                    primary="Include browser and device information"
                    primaryTypographyProps={{ variant: 'body2' }}
                  />
                </ListItem>
              </List>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Container>
  );
};

export default InstructorSupportPage;