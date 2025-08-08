import React from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  Button,
  Stack,
  Avatar,
  Chip,
  Divider,
} from '@mui/material';
import {
  School,
  TrendingUp,
  Group,
  MonetizationOn,
  Support,
  VideoLibrary,
  Analytics,
  Star,
} from '@mui/icons-material';

const TeachPage: React.FC = () => {
  const benefits = [
    {
      icon: <MonetizationOn />,
      title: 'Earn Money',
      description: 'Set your own price and earn revenue from course sales',
      color: '#4CAF50',
    },
    {
      icon: <Group />,
      title: 'Reach Global Audience',
      description: 'Connect with students from around the world',
      color: '#2196F3',
    },
    {
      icon: <VideoLibrary />,
      title: 'Easy Course Creation',
      description: 'User-friendly tools to create engaging content',
      color: '#FF9800',
    },
    {
      icon: <Analytics />,
      title: 'Detailed Analytics',
      description: 'Track your performance and student engagement',
      color: '#9C27B0',
    },
    {
      icon: <Support />,
      title: '24/7 Support',
      description: 'Get help whenever you need it from our team',
      color: '#F44336',
    },
    {
      icon: <Star />,
      title: 'Build Your Brand',
      description: 'Establish yourself as an expert in your field',
      color: '#FFC107',
    },
  ];

  const stats = [
    { number: '50,000+', label: 'Active Students', icon: <Group /> },
    { number: '2,000+', label: 'Expert Instructors', icon: <School /> },
    { number: '$500K+', label: 'Instructor Earnings', icon: <MonetizationOn /> },
    { number: '4.8★', label: 'Average Rating', icon: <Star /> },
  ];

  const steps = [
    {
      step: '1',
      title: 'Plan Your Course',
      description: 'Choose your topic and create an engaging curriculum that helps students achieve their goals.',
    },
    {
      step: '2',
      title: 'Record Your Content',
      description: 'Use our tools to record high-quality videos, create quizzes, and add resources.',
    },
    {
      step: '3',
      title: 'Launch & Earn',
      description: 'Publish your course and start earning money while helping students learn.',
    },
  ];

  const testimonials = [
    {
      name: 'Dr. Sarah Mitchell',
      role: 'Top-Rated Instructor',
      earnings: '$25,000+',
      students: '15,000+',
      rating: 4.9,
      quote: 'Teaching on EduMaster has allowed me to reach students globally and build a sustainable income from my expertise.',
      avatar: '/images/instructors/sarah.jpg',
    },
    {
      name: 'Prof. David Chen',
      role: 'Programming Expert',
      earnings: '$18,500+',
      students: '12,000+',
      rating: 4.8,
      quote: 'The platform provides excellent tools and support. I\'ve been able to create comprehensive courses with ease.',
      avatar: '/images/instructors/david.jpg',
    },
  ];

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Hero Section */}
      <Box sx={{ textAlign: 'center', mb: 6 }}>
        <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
          Teach on EduMaster
        </Typography>
        <Typography variant="h5" color="text.secondary" paragraph>
          Share your knowledge, impact lives, and earn money doing what you love
        </Typography>
        <Button
          variant="contained"
          size="large"
          sx={{ 
            mt: 3, 
            py: 2, 
            px: 5,
            fontSize: '1.2rem',
            fontWeight: 'bold',
            boxShadow: '0 4px 15px rgba(107, 114, 128, 0.3)',
            '&:hover': {
              boxShadow: '0 6px 20px rgba(107, 114, 128, 0.4)',
              transform: 'translateY(-2px)',
            }
          }}
          onClick={() => window.location.href = '/instructor'}
        >
          Get Started Today
        </Button>
      </Box>

      {/* Stats Section */}
      <Grid container spacing={3} sx={{ mb: 6 }}>
        {stats.map((stat, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Card sx={{ textAlign: 'center', p: 2, height: '100%' }}>
              <CardContent>
                <Avatar
                  sx={{
                    bgcolor: 'primary.main',
                    width: 56,
                    height: 56,
                    mx: 'auto',
                    mb: 2,
                  }}
                >
                  {stat.icon}
                </Avatar>
                <Typography variant="h4" fontWeight="bold" color="primary.main">
                  {stat.number}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  {stat.label}
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Benefits Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" textAlign="center" gutterBottom fontWeight="bold">
          Why Teach With Us?
        </Typography>
        <Grid container spacing={3} sx={{ mt: 4 }}>
          {benefits.map((benefit, index) => (
            <Grid item xs={12} md={4} key={index}>
              <Card sx={{ height: '100%', p: 2 }}>
                <CardContent sx={{ textAlign: 'center' }}>
                  <Avatar
                    sx={{
                      bgcolor: benefit.color,
                      width: 64,
                      height: 64,
                      mx: 'auto',
                      mb: 2,
                    }}
                  >
                    {benefit.icon}
                  </Avatar>
                  <Typography variant="h6" gutterBottom fontWeight="bold">
                    {benefit.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {benefit.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* How It Works Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" textAlign="center" gutterBottom fontWeight="bold">
          How It Works
        </Typography>
        <Grid container spacing={4} sx={{ mt: 4 }}>
          {steps.map((step, index) => (
            <Grid item xs={12} md={4} key={index}>
              <Box sx={{ textAlign: 'center' }}>
                <Avatar
                  sx={{
                    bgcolor: 'primary.main',
                    width: 80,
                    height: 80,
                    mx: 'auto',
                    mb: 2,
                    fontSize: '2rem',
                    fontWeight: 'bold',
                  }}
                >
                  {step.step}
                </Avatar>
                <Typography variant="h5" gutterBottom fontWeight="bold">
                  {step.title}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  {step.description}
                </Typography>
              </Box>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* Success Stories Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" textAlign="center" gutterBottom fontWeight="bold">
          Success Stories
        </Typography>
        <Grid container spacing={3} sx={{ mt: 4 }}>
          {testimonials.map((testimonial, index) => (
            <Grid item xs={12} md={6} key={index}>
              <Card sx={{ height: '100%', p: 3 }}>
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Avatar src={testimonial.avatar} sx={{ mr: 2, width: 64, height: 64 }}>
                      {testimonial.name.charAt(0)}
                    </Avatar>
                    <Box>
                      <Typography variant="h6" fontWeight="bold">
                        {testimonial.name}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {testimonial.role}
                      </Typography>
                      <Box sx={{ display: 'flex', gap: 1, mt: 1 }}>
                        <Chip
                          label={`${testimonial.students} students`}
                          size="small"
                          variant="outlined"
                        />
                        <Chip
                          label={`${testimonial.earnings} earned`}
                          size="small"
                          color="success"
                          variant="outlined"
                        />
                      </Box>
                    </Box>
                  </Box>
                  <Divider sx={{ my: 2 }} />
                  <Typography variant="body1" paragraph>
                    "{testimonial.quote}"
                  </Typography>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Star sx={{ color: '#FFC107' }} />
                    <Typography variant="body2" fontWeight="bold">
                      {testimonial.rating}/5.0 rating
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* CTA Section */}
      <Box
        sx={{
          textAlign: 'center',
          py: 6,
          backgroundColor: 'primary.main',
          color: 'white',
          borderRadius: 2,
        }}
      >
        <School sx={{ fontSize: 64, mb: 2 }} />
        <Typography variant="h4" gutterBottom fontWeight="bold">
          Ready to Start Teaching?
        </Typography>
        <Typography variant="h6" paragraph>
          Join thousands of instructors who are already earning money sharing their knowledge
        </Typography>
        <Stack
          direction={{ xs: 'column', sm: 'row' }}
          spacing={2}
          justifyContent="center"
          sx={{ mt: 3 }}
        >
          <Button
            variant="contained"
            size="large"
            sx={{ 
              background: 'linear-gradient(135deg, #10B981 0%, #059669 100%)',
              color: 'white',
              fontWeight: 'bold',
              fontSize: '1.2rem',
              py: 1.8,
              px: 5,
              '&:hover': {
                background: 'linear-gradient(135deg, #059669 0%, #047857 100%)',
                transform: 'translateY(-3px)',
              }
            }}
            onClick={() => window.location.href = '/register'}
          >
            Start Teaching Today
          </Button>
          <Button
            variant="outlined"
            size="large"
            sx={{ 
              borderColor: 'white',
              color: 'white',
              '&:hover': {
                borderColor: 'white',
                backgroundColor: 'rgba(255,255,255,0.1)',
              }
            }}
            onClick={() => window.location.href = '/instructor-resources'}
          >
            Learn More
          </Button>
        </Stack>
      </Box>
    </Container>
  );
};

export default TeachPage;