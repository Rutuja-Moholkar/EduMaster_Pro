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
  Rating,
  Chip,
} from '@mui/material';
import {
  Smartphone,
  Download,
  Star,
  Wifi,
  VideoLibrary,
  NotificationsActive,
  Speed,
  Security,
  CloudDownload,
} from '@mui/icons-material';

const MobileAppPage: React.FC = () => {
  const features = [
    {
      icon: <VideoLibrary />,
      title: 'Offline Learning',
      description: 'Download courses and learn without internet connection',
    },
    {
      icon: <NotificationsActive />,
      title: 'Smart Notifications',
      description: 'Never miss a lesson with personalized reminders',
    },
    {
      icon: <Speed />,
      title: 'Fast Performance',
      description: 'Optimized for smooth learning on any device',
    },
    {
      icon: <Security />,
      title: 'Secure Learning',
      description: 'Your progress and data are always protected',
    },
    {
      icon: <CloudDownload />,
      title: 'Cloud Sync',
      description: 'Seamlessly sync across all your devices',
    },
    {
      icon: <Wifi />,
      title: 'Adaptive Streaming',
      description: 'Adjusts video quality based on your connection',
    },
  ];

  const testimonials = [
    {
      name: 'Sarah Johnson',
      rating: 5,
      comment: 'Love learning on the go! The app is so user-friendly.',
      avatar: '/images/avatars/sarah.jpg',
    },
    {
      name: 'Mike Chen',
      rating: 5,
      comment: 'Offline mode is a game-changer for my daily commute.',
      avatar: '/images/avatars/mike.jpg',
    },
    {
      name: 'Emily Rodriguez',
      rating: 4,
      comment: 'Great app with excellent course quality.',
      avatar: '/images/avatars/emily.jpg',
    },
  ];

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Hero Section */}
      <Box sx={{ textAlign: 'center', mb: 6 }}>
        <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
          Learn Anywhere with EduMaster Mobile App
        </Typography>
        <Typography variant="h5" sx={{ color: '#374151', fontWeight: 500 }} paragraph>
          Take your learning journey wherever you go. Download our mobile app for iOS and Android.
        </Typography>
        
        <Stack
          direction={{ xs: 'column', sm: 'row' }}
          spacing={2}
          justifyContent="center"
          alignItems="center"
          sx={{ mt: 4 }}
        >
          <Button
            variant="contained"
            size="large"
            startIcon={<Download />}
            sx={{ 
              minWidth: 200,
              py: 1.5,
              background: 'linear-gradient(45deg, #000 30%, #333 90%)',
              '&:hover': {
                background: 'linear-gradient(45deg, #333 30%, #666 90%)',
              }
            }}
          >
            Download for iOS
          </Button>
          <Button
            variant="contained"
            size="large"
            startIcon={<Download />}
            sx={{ 
              minWidth: 200,
              py: 1.5,
              background: 'linear-gradient(45deg, #3DDC84 30%, #00C851 90%)',
              '&:hover': {
                background: 'linear-gradient(45deg, #00C851 30%, #007E33 90%)',
              }
            }}
          >
            Download for Android
          </Button>
        </Stack>

        <Box sx={{ mt: 3, display: 'flex', justifyContent: 'center', alignItems: 'center', gap: 2 }}>
          <Rating value={4.8} precision={0.1} readOnly />
          <Typography variant="body2" color="text.secondary">
            4.8/5 rating from 50,000+ users
          </Typography>
        </Box>
      </Box>

      {/* Features Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" textAlign="center" gutterBottom fontWeight="bold">
          Why Choose Our Mobile App?
        </Typography>
        <Typography variant="h6" textAlign="center" color="text.secondary" paragraph>
          Packed with features to enhance your learning experience
        </Typography>

        <Grid container spacing={3} sx={{ mt: 4 }}>
          {features.map((feature, index) => (
            <Grid item xs={12} md={4} key={index}>
              <Card sx={{ height: '100%', textAlign: 'center', p: 2 }}>
                <CardContent>
                  <Avatar
                    sx={{
                      bgcolor: index % 2 === 0 ? '#3B82F6' : '#10B981',
                      width: 64,
                      height: 64,
                      mx: 'auto',
                      mb: 2,
                    }}
                  >
                    {feature.icon}
                  </Avatar>
                  <Typography variant="h6" gutterBottom fontWeight="bold">
                    {feature.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {feature.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* Stats Section */}
      <Box sx={{ mb: 6, textAlign: 'center' }}>
        <Grid container spacing={4}>
          <Grid item xs={12} sm={3}>
            <Typography variant="h3" fontWeight="bold" sx={{ color: '#3B82F6' }}>
              1M+
            </Typography>
            <Typography variant="h6" sx={{ color: '#374151', fontWeight: 500 }}>
              Downloads
            </Typography>
          </Grid>
          <Grid item xs={12} sm={3}>
            <Typography variant="h3" fontWeight="bold" sx={{ color: '#10B981' }}>
              50K+
            </Typography>
            <Typography variant="h6" sx={{ color: '#374151', fontWeight: 500 }}>
              Active Users
            </Typography>
          </Grid>
          <Grid item xs={12} sm={3}>
            <Typography variant="h3" fontWeight="bold" sx={{ color: '#F59E0B' }}>
              4.8★
            </Typography>
            <Typography variant="h6" sx={{ color: '#374151', fontWeight: 500 }}>
              App Rating
            </Typography>
          </Grid>
          <Grid item xs={12} sm={3}>
            <Typography variant="h3" fontWeight="bold" sx={{ color: '#EF4444' }}>
              99%
            </Typography>
            <Typography variant="h6" sx={{ color: '#374151', fontWeight: 500 }}>
              Uptime
            </Typography>
          </Grid>
        </Grid>
      </Box>

      {/* Testimonials Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" textAlign="center" gutterBottom fontWeight="bold">
          What Our Users Say
        </Typography>
        <Grid container spacing={3} sx={{ mt: 4 }}>
          {testimonials.map((testimonial, index) => (
            <Grid item xs={12} md={4} key={index}>
              <Card sx={{ height: '100%', p: 2 }}>
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Avatar src={testimonial.avatar} sx={{ mr: 2 }}>
                      {testimonial.name.charAt(0)}
                    </Avatar>
                    <Box>
                      <Typography variant="subtitle1" fontWeight="bold">
                        {testimonial.name}
                      </Typography>
                      <Rating value={testimonial.rating} size="small" readOnly />
                    </Box>
                  </Box>
                  <Typography variant="body2" color="text.secondary">
                    "{testimonial.comment}"
                  </Typography>
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
        <Smartphone sx={{ fontSize: 64, mb: 2 }} />
        <Typography variant="h4" gutterBottom fontWeight="bold">
          Ready to Start Learning?
        </Typography>
        <Typography variant="h6" paragraph>
          Download the EduMaster app today and join millions of learners worldwide
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
              background: 'linear-gradient(135deg, #F59E0B 0%, #D97706 100%)',
              color: 'white',
              fontSize: '1.3rem',
              fontWeight: 'bold',
              px: 5,
              py: 2,
              border: '2px solid rgba(255,255,255,0.3)',
              '&:hover': {
                background: 'linear-gradient(135deg, #D97706 0%, #B45309 100%)',
                transform: 'translateY(-3px)',
                border: '2px solid rgba(255,255,255,0.5)',
              }
            }}
          >
            Get Started Now
          </Button>
        </Stack>
      </Box>
    </Container>
  );
};

export default MobileAppPage;