import React from 'react';
import {
  Box,
  Container,
  Typography,
  Card,
  CardContent,
  Button,
  Grid,
  Chip,
  Avatar,
  Divider,
} from '@mui/material';
import {
  Download,
  Share,
  School,
  CalendarToday,
} from '@mui/icons-material';

const CertificatesPage: React.FC = () => {
  // Mock certificate data
  const certificates = [
    {
      id: 1,
      courseName: 'Complete React Development Course',
      instructorName: 'Dr. Sarah Johnson',
      completionDate: '2024-01-15',
      grade: 'A+',
      credentialId: 'EDU-2024-RCT-001',
      skills: ['React', 'JavaScript', 'Web Development'],
    },
    {
      id: 2,
      courseName: 'Advanced Python Programming',
      instructorName: 'Prof. Michael Chen',
      completionDate: '2024-02-20',
      grade: 'A',
      credentialId: 'EDU-2024-PYT-002',
      skills: ['Python', 'Data Structures', 'OOP'],
    },
    {
      id: 3,
      courseName: 'UI/UX Design Fundamentals',
      instructorName: 'Emma Rodriguez',
      completionDate: '2024-03-10',
      grade: 'A+',
      credentialId: 'EDU-2024-UX-003',
      skills: ['UI Design', 'UX Research', 'Figma'],
    },
  ];

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h3" component="h1" gutterBottom fontWeight="bold">
          My Certificates
        </Typography>
        <Typography variant="h6" sx={{ color: '#374151', fontWeight: 500 }}>
          Showcase your achievements and share your credentials with the world
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {certificates.map((certificate) => (
          <Grid item xs={12} md={6} lg={4} key={certificate.id}>
            <Card 
              sx={{ 
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                borderRadius: 3,
                position: 'relative',
                background: 'linear-gradient(135deg, #FFFFFF 0%, #F8FAFC 100%)',
                boxShadow: '0 4px 20px rgba(59, 130, 246, 0.1)',
                border: '1px solid rgba(59, 130, 246, 0.2)',
                '&:hover': {
                  boxShadow: '0 8px 30px rgba(59, 130, 246, 0.2)',
                  transform: 'translateY(-6px)',
                  transition: 'all 0.3s ease',
                },
              }}
            >

              <CardContent sx={{ flexGrow: 1, pt: 3 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <Avatar sx={{ 
                    bgcolor: '#3B82F6', 
                    mr: 2,
                    width: 48,
                    height: 48,
                    boxShadow: '0 2px 8px rgba(59, 130, 246, 0.3)'
                  }}>
                    <School />
                  </Avatar>
                  <Box>
                    <Typography variant="h6" fontWeight="bold" gutterBottom>
                      {certificate.courseName}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      by {certificate.instructorName}
                    </Typography>
                  </Box>
                </Box>

                <Divider sx={{ my: 2 }} />

                <Box sx={{ mb: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <CalendarToday sx={{ fontSize: 16, mr: 1, color: 'text.secondary' }} />
                    <Typography variant="body2" color="text.secondary">
                      Completed: {new Date(certificate.completionDate).toLocaleDateString()}
                    </Typography>
                  </Box>
                  <Typography variant="h5" sx={{ color: '#10B981', fontWeight: 'bold' }}>
                    Grade: {certificate.grade}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    Credential ID: {certificate.credentialId}
                  </Typography>
                </Box>

                <Box sx={{ mb: 3 }}>
                  <Typography variant="subtitle2" gutterBottom>
                    Skills Acquired:
                  </Typography>
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                    {certificate.skills.map((skill) => (
                      <Chip
                        key={skill}
                        label={skill}
                        size="small"
                        variant="outlined"
                        color="primary"
                      />
                    ))}
                  </Box>
                </Box>

                <Box sx={{ display: 'flex', gap: 1 }}>
                  <Button
                    variant="contained"
                    startIcon={<Download />}
                    size="small"
                    fullWidth
                    sx={{
                      background: 'linear-gradient(135deg, #3B82F6 0%, #1E40AF 100%)',
                      fontWeight: '600',
                      py: 1,
                      '&:hover': {
                        transform: 'translateY(-1px)',
                        boxShadow: '0 4px 12px rgba(59, 130, 246, 0.4)',
                      }
                    }}
                  >
                    Download
                  </Button>
                  <Button
                    variant="outlined"
                    startIcon={<Share />}
                    size="small"
                    fullWidth
                    sx={{
                      borderColor: '#3B82F6',
                      color: '#3B82F6',
                      fontWeight: '600',
                      py: 1,
                      '&:hover': {
                        backgroundColor: 'rgba(59, 130, 246, 0.1)',
                        transform: 'translateY(-1px)',
                      }
                    }}
                  >
                    Share
                  </Button>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {certificates.length === 0 && (
        <Box
          sx={{
            textAlign: 'center',
            py: 8,
            backgroundColor: 'grey.50',
            borderRadius: 2,
          }}
        >
          <School sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h5" gutterBottom>
            No Certificates Yet
          </Typography>
          <Typography variant="body1" color="text.secondary" paragraph>
            Complete courses to earn certificates and showcase your skills.
          </Typography>
          <Button
            variant="contained"
            onClick={() => window.location.href = '/courses'}
          >
            Browse Courses
          </Button>
        </Box>
      )}
    </Container>
  );
};

export default CertificatesPage;