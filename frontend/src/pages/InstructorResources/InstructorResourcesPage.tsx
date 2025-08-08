import React from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  Button,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Avatar,
  Chip,
} from '@mui/material';
import {
  ExpandMore,
  VideoLibrary,
  Quiz,
  Assignment,
  TrendingUp,
  Support,
  School,
  PlayCircle,
  Description,
  Forum,
  Analytics,
  MonetizationOn,
} from '@mui/icons-material';

const InstructorResourcesPage: React.FC = () => {
  const resourceCategories = [
    {
      title: 'Course Creation',
      icon: <VideoLibrary />,
      resources: [
        { name: 'Course Planning Template', type: 'PDF', description: 'Step-by-step guide to plan your course structure' },
        { name: 'Video Recording Best Practices', type: 'Guide', description: 'Tips for creating professional-quality videos' },
        { name: 'Content Creation Checklist', type: 'Checklist', description: 'Ensure your course meets quality standards' },
        { name: 'Lesson Planning Worksheet', type: 'Template', description: 'Organize your lessons effectively' },
      ],
    },
    {
      title: 'Student Engagement',
      icon: <Quiz />,
      resources: [
        { name: 'Interactive Quiz Builder', type: 'Tool', description: 'Create engaging quizzes to test student knowledge' },
        { name: 'Assignment Templates', type: 'Templates', description: 'Ready-to-use assignment formats' },
        { name: 'Discussion Forum Guide', type: 'Guide', description: 'Foster meaningful student discussions' },
        { name: 'Feedback Strategies', type: 'Guide', description: 'Effective ways to provide student feedback' },
      ],
    },
    {
      title: 'Marketing & Promotion',
      icon: <TrendingUp />,
      resources: [
        { name: 'Course Marketing Kit', type: 'Kit', description: 'Promotional materials and strategies' },
        { name: 'Social Media Templates', type: 'Templates', description: 'Ready-to-post social media content' },
        { name: 'Email Marketing Guide', type: 'Guide', description: 'Build and engage your student audience' },
        { name: 'SEO for Courses', type: 'Guide', description: 'Optimize your course for search engines' },
      ],
    },
    {
      title: 'Analytics & Insights',
      icon: <Analytics />,
      resources: [
        { name: 'Performance Dashboard', type: 'Tool', description: 'Track your course metrics and student progress' },
        { name: 'Revenue Analytics', type: 'Tool', description: 'Monitor your earnings and sales trends' },
        { name: 'Student Behavior Reports', type: 'Reports', description: 'Understand how students interact with your content' },
        { name: 'Improvement Recommendations', type: 'AI Tool', description: 'AI-powered suggestions to enhance your courses' },
      ],
    },
  ];

  const faqs = [
    {
      question: 'How do I create my first course?',
      answer: 'Start by planning your course structure using our Course Planning Template. Then use our intuitive course builder to upload videos, create quizzes, and add resources. Our step-by-step guide will walk you through the entire process.',
    },
    {
      question: 'What equipment do I need to record high-quality videos?',
      answer: 'You don\'t need expensive equipment to get started. A decent webcam or smartphone camera, good lighting (natural light works well), and clear audio are the basics. Check our Video Recording Best Practices guide for detailed recommendations.',
    },
    {
      question: 'How do I price my course competitively?',
      answer: 'Research similar courses in your niche, consider your expertise level, and factor in the value you provide. Our Course Marketing Kit includes pricing strategies and market research tools to help you set the right price.',
    },
    {
      question: 'How can I increase student engagement?',
      answer: 'Use interactive elements like quizzes, assignments, and discussion forums. Provide regular feedback and create a community feeling. Our Student Engagement resources provide detailed strategies and templates.',
    },
    {
      question: 'What marketing support do you provide?',
      answer: 'We offer comprehensive marketing resources including social media templates, email marketing guides, and SEO optimization tips. Plus, our featured course program can help boost your visibility.',
    },
  ];

  const tools = [
    {
      name: 'Course Builder',
      description: 'Drag-and-drop interface for creating courses',
      icon: <VideoLibrary />,
    },
    {
      name: 'Quiz Maker',
      description: 'Create interactive quizzes and assessments',
      icon: <Quiz />,
    },
    {
      name: 'Analytics Dashboard',
      description: 'Track student progress and course performance',
      icon: <Analytics />,
    },
    {
      name: 'Revenue Tracker',
      description: 'Monitor your earnings and sales metrics',
      icon: <MonetizationOn />,
    },
    {
      name: 'Discussion Forums',
      description: 'Foster student interaction and community',
      icon: <Forum />,
    },
    {
      name: 'Assignment Manager',
      description: 'Create and grade student assignments',
      icon: <Assignment />,
    },
  ];

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Hero Section */}
      <Box sx={{ textAlign: 'center', mb: 6 }}>
        <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
          Teaching Resources
        </Typography>
        <Typography variant="h5" sx={{ color: '#374151', fontWeight: 500 }} paragraph>
          Everything you need to create, market, and manage successful online courses
        </Typography>
      </Box>

      {/* Resource Categories */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" gutterBottom fontWeight="bold">
          Resource Library
        </Typography>
        <Grid container spacing={3}>
          {resourceCategories.map((category, index) => (
            <Grid item xs={12} md={6} key={index}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Avatar sx={{ bgcolor: index % 2 === 0 ? '#3B82F6' : '#10B981', mr: 2 }}>
                      {category.icon}
                    </Avatar>
                    <Typography variant="h5" fontWeight="bold">
                      {category.title}
                    </Typography>
                  </Box>
                  <List>
                    {category.resources.map((resource, idx) => (
                      <ListItem key={idx} sx={{ px: 0 }}>
                        <ListItemIcon>
                          <Description color="action" />
                        </ListItemIcon>
                        <ListItemText
                          primary={
                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                              {resource.name}
                              <Chip label={resource.type} size="small" variant="outlined" />
                            </Box>
                          }
                          secondary={resource.description}
                        />
                      </ListItem>
                    ))}
                  </List>
                  <Button 
                    variant="contained" 
                    fullWidth 
                    sx={{ 
                      mt: 2,
                      background: index % 2 === 0 ? 'linear-gradient(135deg, #3B82F6 0%, #1E40AF 100%)' : 'linear-gradient(135deg, #10B981 0%, #047857 100%)',
                      fontWeight: 'bold',
                      py: 1.5,
                      '&:hover': {
                        transform: 'translateY(-2px)',
                        boxShadow: '0 4px 12px rgba(59, 130, 246, 0.3)',
                      }
                    }}
                  >
                    Access Resources
                  </Button>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* Tools Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" gutterBottom fontWeight="bold">
          Teaching Tools
        </Typography>
        <Typography variant="h6" color="text.secondary" paragraph>
          Powerful tools to help you create engaging courses and manage your teaching business
        </Typography>
        <Grid container spacing={3}>
          {tools.map((tool, index) => (
            <Grid item xs={12} sm={6} md={4} key={index}>
              <Card sx={{ textAlign: 'center', p: 2, height: '100%' }}>
                <CardContent>
                  <Avatar
                    sx={{
                      bgcolor: ['#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6', '#EC4899'][index % 6],
                      width: 64,
                      height: 64,
                      mx: 'auto',
                      mb: 2,
                    }}
                  >
                    {tool.icon}
                  </Avatar>
                  <Typography variant="h6" gutterBottom fontWeight="bold">
                    {tool.name}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {tool.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* FAQ Section */}
      <Box sx={{ mb: 6 }}>
        <Typography variant="h3" component="h2" gutterBottom fontWeight="bold">
          Frequently Asked Questions
        </Typography>
        {faqs.map((faq, index) => (
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
        ))}
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
          Need More Help?
        </Typography>
        <Typography variant="h6" paragraph>
          Our instructor support team is here to help you succeed
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
          <Button
            variant="contained"
            size="large"
            sx={{ 
              background: 'linear-gradient(135deg, #3B82F6 0%, #1E40AF 100%)',
              color: 'white',
              fontSize: '1.2rem',
              fontWeight: 'bold',
              px: 4,
              py: 1.5,
              '&:hover': {
                background: 'linear-gradient(135deg, #1E40AF 0%, #1E3A8A 100%)',
                boxShadow: '0 6px 25px rgba(59, 130, 246, 0.4)',
                transform: 'translateY(-2px)',
              }
            }}
            onClick={() => window.location.href = '/instructor-support'}
          >
            Contact Support
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
            onClick={() => window.location.href = '/instructor'}
          >
            Start Teaching
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default InstructorResourcesPage;