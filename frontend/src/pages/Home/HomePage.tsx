import React from 'react';
import { Box, Container, Typography, Button, Grid, Card, CardContent, CardMedia } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { School, TrendingUp, People, Star } from '@mui/icons-material';

const HomePage: React.FC = () => {
  const navigate = useNavigate();

  const featuredCourses = [
    {
      id: 1,
      title: 'Full Stack Development',
      instructor: 'John Doe',
      price: 99.99,
      rating: 4.8,
      image: '/images/courses/programming.jpg'
    },
    {
      id: 2,
      title: 'Data Science Fundamentals',
      instructor: 'Jane Smith',
      price: 89.99,
      rating: 4.9,
      image: '/images/courses/data-science.jpg'
    },
    {
      id: 3,
      title: 'UI/UX Design Masterclass',
      instructor: 'Mike Johnson',
      price: 79.99,
      rating: 4.7,
      image: '/images/courses/design.jpg'
    }
  ];

  const stats = [
    { icon: School, value: '500+', label: 'Courses' },
    { icon: People, value: '10K+', label: 'Students' },
    { icon: TrendingUp, value: '98%', label: 'Success Rate' },
    { icon: Star, value: '4.8', label: 'Average Rating' }
  ];

  return (
    <Box>
      {/* Hero Section */}
      <Box
        sx={{
          background: 'linear-gradient(135deg, #dbeafe 0%, #bfdbfe 50%, #93c5fd 100%)',
          color: '#1e40af',
          py: 8,
          mb: 6
        }}
      >
        <Container maxWidth="lg">
          <Grid container spacing={4} alignItems="center">
            <Grid item xs={12} md={6}>
              <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
                Learn Without Limits
              </Typography>
              <Typography variant="h5" paragraph>
                Discover thousands of courses from expert instructors and advance your career
              </Typography>
              <Box sx={{ mt: 4 }}>
                <Button
                  variant="contained"
                  size="large"
                  sx={{ mr: 2 }}
                  onClick={() => navigate('/courses')}
                >
                  Browse Courses
                </Button>
                <Button
                  variant="outlined"
                  size="large"
                  sx={{ color: '#1e40af', borderColor: '#1e40af' }}
                  onClick={() => navigate('/register')}
                >
                  Get Started
                </Button>
              </Box>
            </Grid>
            <Grid item xs={12} md={6}>
              <Box
                component="img"
                src="/images/hero/learning-hero.jpg"
                alt="Learning illustration"
                sx={{ width: '100%', height: 'auto', borderRadius: 2 }}
              />
            </Grid>
          </Grid>
        </Container>
      </Box>

      {/* Stats Section */}
      <Container maxWidth="lg" sx={{ mb: 6 }}>
        <Grid container spacing={4}>
          {stats.map((stat, index) => (
            <Grid item xs={6} md={3} key={index}>
              <Card sx={{ textAlign: 'center', p: 2 }}>
                <CardContent>
                  <stat.icon sx={{ fontSize: 48, color: 'primary.main', mb: 1 }} />
                  <Typography variant="h3" component="div" fontWeight="bold">
                    {stat.value}
                  </Typography>
                  <Typography variant="body1" color="text.secondary">
                    {stat.label}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Featured Courses */}
      <Container maxWidth="lg" sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" textAlign="center" gutterBottom>
          Featured Courses
        </Typography>
        <Typography variant="h6" textAlign="center" color="text.secondary" paragraph sx={{ mb: 4 }}>
          Start learning with our most popular courses
        </Typography>
        
        <Grid container spacing={4}>
          {featuredCourses.map((course) => (
            <Grid item xs={12} sm={6} md={4} key={course.id}>
              <Card 
                sx={{ 
                  height: '100%', 
                  cursor: 'pointer',
                  border: '1px solid rgba(59, 130, 246, 0.1)',
                  '&:hover': {
                    boxShadow: '0 8px 25px rgba(59, 130, 246, 0.15)',
                    transform: 'translateY(-6px)',
                    transition: 'all 0.3s ease',
                  }
                }} 
                onClick={() => navigate(`/courses/${course.id}`)}
              >
                <CardMedia
                  component="img"
                  height="160"
                  image={course.image}
                  alt={course.title}
                />
                <CardContent sx={{ p: 2.5 }}>
                  <Typography variant="h6" gutterBottom fontWeight="bold">
                    {course.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    by {course.instructor}
                  </Typography>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mt: 2 }}>
                    <Typography variant="h5" sx={{ color: '#3B82F6', fontWeight: 'bold' }}>
                      ${course.price}
                    </Typography>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <Star sx={{ color: '#F59E0B', fontSize: 18, mr: 0.5 }} />
                      <Typography variant="body1" fontWeight="500">
                        {course.rating}
                      </Typography>
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Call to Action */}
      <Box sx={{ bgcolor: 'grey.100', py: 6 }}>
        <Container maxWidth="md" sx={{ textAlign: 'center' }}>
          <Typography variant="h4" component="h2" gutterBottom>
            Ready to Start Learning?
          </Typography>
          <Typography variant="h6" color="text.secondary" paragraph>
            Join thousands of students already learning on EduMaster Pro
          </Typography>
          <Button
            variant="contained"
            size="large"
            sx={{
              fontSize: '1.2rem',
              fontWeight: 'bold',
              px: 4,
              py: 1.5,
              background: 'linear-gradient(135deg, #10B981 0%, #059669 100%)',
              '&:hover': {
                background: 'linear-gradient(135deg, #059669 0%, #047857 100%)',
                transform: 'translateY(-2px)',
                boxShadow: '0 6px 20px rgba(16, 185, 129, 0.3)',
              }
            }}
            onClick={() => navigate('/register')}
          >
            Sign Up Now
          </Button>
        </Container>
      </Box>
    </Box>
  );
};

export default HomePage;