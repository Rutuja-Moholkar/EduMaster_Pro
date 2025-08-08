import React, { useEffect } from 'react';
import {
  Box,
  Container,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  LinearProgress,
  Avatar,
  Chip
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { School, PlayArrow, TrendingUp, Assignment } from '@mui/icons-material';
import { useAppSelector } from '../../hooks/redux';

const DashboardPage: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAppSelector((state) => state.auth);

  // Mock data - would be fetched from API
  const enrolledCourses = [
    {
      id: 1,
      title: 'Full Stack Development',
      instructor: 'John Doe',
      progress: 75,
      thumbnail: '/images/courses/programming.jpg',
      nextLesson: 'Building REST APIs'
    },
    {
      id: 2,
      title: 'Data Science Fundamentals',
      instructor: 'Jane Smith',
      progress: 45,
      thumbnail: '/images/courses/data-science.jpg',
      nextLesson: 'Machine Learning Basics'
    },
    {
      id: 3,
      title: 'UI/UX Design',
      instructor: 'Mike Johnson',
      progress: 30,
      thumbnail: '/images/courses/design.jpg',
      nextLesson: 'User Research Methods'
    }
  ];

  const stats = [
    { icon: School, label: 'Enrolled Courses', value: '3', color: 'primary' },
    { icon: Assignment, label: 'Completed', value: '1', color: 'success' },
    { icon: TrendingUp, label: 'In Progress', value: '2', color: 'warning' },
    { icon: PlayArrow, label: 'Hours Watched', value: '24', color: 'info' }
  ];

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 18) return 'Good afternoon';
    return 'Good evening';
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      {/* Welcome Section */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" gutterBottom>
          {getGreeting()}, {user?.firstName}! 👋
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Ready to continue your learning journey?
        </Typography>
      </Box>

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        {stats.map((stat, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <Avatar sx={{ bgcolor: `${stat.color}.main`, mr: 2 }}>
                    <stat.icon />
                  </Avatar>
                  <Box>
                    <Typography variant="h4" component="div">
                      {stat.value}
                    </Typography>
                    <Typography color="text.secondary" variant="body2">
                      {stat.label}
                    </Typography>
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Current Courses */}
      <Typography variant="h5" gutterBottom sx={{ mb: 3 }}>
        Continue Learning
      </Typography>

      <Grid container spacing={3}>
        {enrolledCourses.map((course) => (
          <Grid item xs={12} md={6} lg={4} key={course.id}>
            <Card sx={{ height: '100%' }}>
              <Box
                component="img"
                sx={{
                  height: 140,
                  width: '100%',
                  objectFit: 'cover'
                }}
                src={course.thumbnail}
                alt={course.title}
              />
              <CardContent sx={{ p: 2.5 }}>
                <Typography variant="h6" gutterBottom>
                  {course.title}
                </Typography>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  by {course.instructor}
                </Typography>
                
                <Box sx={{ mt: 2, mb: 2 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography variant="body2">Progress</Typography>
                    <Typography variant="body2">{course.progress}%</Typography>
                  </Box>
                  <LinearProgress variant="determinate" value={course.progress} sx={{ height: 8 }} />
                </Box>

                <Chip
                  label={`Next: ${course.nextLesson}`}
                  size="small"
                  variant="outlined"
                  sx={{ mb: 2 }}
                />

                <Button
                  variant="contained"
                  fullWidth
                  startIcon={<PlayArrow />}
                  onClick={() => navigate(`/courses/${course.id}`)}
                >
                  Continue Learning
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Browse More Courses */}
      <Box sx={{ textAlign: 'center', mt: 4 }}>
        <Typography variant="h6" gutterBottom>
          Want to learn something new?
        </Typography>
        <Button
          variant="outlined"
          size="large"
          onClick={() => navigate('/courses')}
        >
          Browse All Courses
        </Button>
      </Box>
    </Container>
  );
};

export default DashboardPage;