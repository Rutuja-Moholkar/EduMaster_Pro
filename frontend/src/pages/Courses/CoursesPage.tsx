import React, { useState, useEffect } from 'react';
import {
  Box,
  Container,
  Grid,
  Card,
  CardContent,
  CardMedia,
  Typography,
  Button,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  Rating,
  Pagination
} from '@mui/material';
import { Search, FilterList, Star } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

interface Course {
  id: number;
  title: string;
  instructor: string;
  price: number;
  originalPrice?: number;
  rating: number;
  reviewCount: number;
  thumbnail: string;
  category: string;
  level: string;
  duration: string;
  description: string;
}

const CoursesPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [selectedLevel, setSelectedLevel] = useState('');
  const [sortBy, setSortBy] = useState('popular');
  const [currentPage, setCurrentPage] = useState(1);
  const coursesPerPage = 12;

  // Mock data - would be fetched from API
  const [courses] = useState<Course[]>([
    {
      id: 1,
      title: 'Complete Full Stack Web Development',
      instructor: 'John Doe',
      price: 99.99,
      originalPrice: 149.99,
      rating: 4.8,
      reviewCount: 1234,
      thumbnail: '/images/courses/programming.jpg',
      category: 'Programming',
      level: 'Beginner',
      duration: '40 hours',
      description: 'Learn modern web development from scratch with HTML, CSS, JavaScript, React, and Node.js'
    },
    {
      id: 2,
      title: 'Data Science and Machine Learning',
      instructor: 'Jane Smith',
      price: 89.99,
      rating: 4.9,
      reviewCount: 987,
      thumbnail: '/images/courses/data-science.jpg',
      category: 'Data Science',
      level: 'Intermediate',
      duration: '35 hours',
      description: 'Master data science with Python, pandas, scikit-learn, and machine learning algorithms'
    },
    {
      id: 3,
      title: 'UI/UX Design Masterclass',
      instructor: 'Mike Johnson',
      price: 79.99,
      rating: 4.7,
      reviewCount: 765,
      thumbnail: '/images/courses/design.jpg',
      category: 'Design',
      level: 'Beginner',
      duration: '28 hours',
      description: 'Create stunning user interfaces and experiences with modern design principles'
    },
    {
      id: 4,
      title: 'Digital Marketing Strategy',
      instructor: 'Sarah Wilson',
      price: 69.99,
      originalPrice: 99.99,
      rating: 4.6,
      reviewCount: 543,
      thumbnail: '/images/courses/marketing.jpg',
      category: 'Marketing',
      level: 'Beginner',
      duration: '25 hours',
      description: 'Learn digital marketing strategies including SEO, social media, and content marketing'
    },
    {
      id: 5,
      title: 'Advanced JavaScript Concepts',
      instructor: 'David Brown',
      price: 119.99,
      rating: 4.9,
      reviewCount: 2156,
      thumbnail: '/images/courses/javascript.jpg',
      category: 'Programming',
      level: 'Advanced',
      duration: '30 hours',
      description: 'Deep dive into advanced JavaScript concepts, async programming, and modern ES6+ features'
    },
    {
      id: 6,
      title: 'Photography Fundamentals',
      instructor: 'Emma Davis',
      price: 59.99,
      rating: 4.5,
      reviewCount: 432,
      thumbnail: '/images/courses/photography.jpg',
      category: 'Photography',
      level: 'Beginner',
      duration: '20 hours',
      description: 'Master the fundamentals of photography including composition, lighting, and editing'
    }
  ]);

  const categories = ['Programming', 'Data Science', 'Design', 'Marketing', 'Photography', 'Business'];
  const levels = ['Beginner', 'Intermediate', 'Advanced'];

  const filteredCourses = courses.filter(course => {
    const matchesSearch = course.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         course.instructor.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = !selectedCategory || course.category === selectedCategory;
    const matchesLevel = !selectedLevel || course.level === selectedLevel;
    
    return matchesSearch && matchesCategory && matchesLevel;
  });

  const totalPages = Math.ceil(filteredCourses.length / coursesPerPage);
  const paginatedCourses = filteredCourses.slice(
    (currentPage - 1) * coursesPerPage,
    currentPage * coursesPerPage
  );

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setCurrentPage(value);
    window.scrollTo(0, 0);
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h3" component="h1" gutterBottom>
          All Courses
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Discover {courses.length} courses to advance your skills
        </Typography>
      </Box>

      {/* Search and Filters */}
      <Box sx={{ mb: 4 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} md={4}>
            <TextField
              fullWidth
              placeholder="Search courses..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              InputProps={{
                startAdornment: <Search sx={{ mr: 1, color: 'text.secondary' }} />
              }}
            />
          </Grid>
          <Grid item xs={12} sm={6} md={2}>
            <FormControl fullWidth>
              <InputLabel>Category</InputLabel>
              <Select
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value)}
                label="Category"
              >
                <MenuItem value="">All Categories</MenuItem>
                {categories.map(category => (
                  <MenuItem key={category} value={category}>{category}</MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6} md={2}>
            <FormControl fullWidth>
              <InputLabel>Level</InputLabel>
              <Select
                value={selectedLevel}
                onChange={(e) => setSelectedLevel(e.target.value)}
                label="Level"
              >
                <MenuItem value="">All Levels</MenuItem>
                {levels.map(level => (
                  <MenuItem key={level} value={level}>{level}</MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12} sm={6} md={2}>
            <FormControl fullWidth>
              <InputLabel>Sort By</InputLabel>
              <Select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                label="Sort By"
              >
                <MenuItem value="popular">Most Popular</MenuItem>
                <MenuItem value="newest">Newest</MenuItem>
                <MenuItem value="price-low">Price: Low to High</MenuItem>
                <MenuItem value="price-high">Price: High to Low</MenuItem>
                <MenuItem value="rating">Highest Rated</MenuItem>
              </Select>
            </FormControl>
          </Grid>
        </Grid>
      </Box>

      {/* Results Count */}
      <Typography variant="body1" sx={{ mb: 3 }}>
        Showing {paginatedCourses.length} of {filteredCourses.length} courses
      </Typography>

      {/* Courses Grid */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        {paginatedCourses.map((course) => (
          <Grid item xs={12} sm={6} md={4} key={course.id}>
            <Card 
              sx={{ 
                height: '100%', 
                cursor: 'pointer',
                '&:hover': { boxShadow: 6 }
              }}
              onClick={() => navigate(`/courses/${course.id}`)}
            >
              <CardMedia
                component="img"
                height="180"
                image={course.thumbnail}
                alt={course.title}
              />
              <CardContent sx={{ p: 3 }}>
                <Typography variant="h6" gutterBottom noWrap>
                  {course.title}
                </Typography>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  by {course.instructor}
                </Typography>
                
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <Rating value={course.rating} precision={0.1} size="small" readOnly />
                  <Typography variant="body2" sx={{ ml: 1 }}>
                    {course.rating} ({course.reviewCount})
                  </Typography>
                </Box>

                <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
                  <Chip label={course.category} size="small" variant="outlined" />
                  <Chip label={course.level} size="small" variant="outlined" />
                </Box>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2, display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                  {course.description}
                </Typography>

                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <Box>
                    <Typography 
                      variant="h5" 
                      component="span" 
                      sx={{ 
                        color: '#3B82F6',
                        fontWeight: 'bold',
                        fontSize: '1.5rem'
                      }}
                    >
                      ${course.price}
                    </Typography>
                    {course.originalPrice && (
                      <Typography 
                        variant="body2" 
                        component="span" 
                        sx={{ 
                          textDecoration: 'line-through', 
                          ml: 1.5, 
                          color: '#9CA3AF',
                          fontWeight: '500'
                        }}
                      >
                        ${course.originalPrice}
                      </Typography>
                    )}
                  </Box>
                  <Typography variant="body2" sx={{ color: '#374151', fontWeight: '500' }}>
                    {course.duration}
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Pagination */}
      {totalPages > 1 && (
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <Pagination
            count={totalPages}
            page={currentPage}
            onChange={handlePageChange}
            color="primary"
            size="large"
          />
        </Box>
      )}
    </Container>
  );
};

export default CoursesPage;