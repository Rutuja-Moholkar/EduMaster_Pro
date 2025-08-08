import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { Box, Typography, Button } from '@mui/material';
import { useAuth } from '../../hooks/useAuth';
import { ProtectedRouteProps } from '../../types';
import LoadingSpinner from '../Common/LoadingSpinner';

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, roles = [] }) => {
  const { isAuthenticated, isLoading, user, hasRole } = useAuth();
  const location = useLocation();

  // Show loading spinner while checking authentication
  if (isLoading) {
    return <LoadingSpinner fullScreen message="Checking authentication..." />;
  }

  // Redirect to login if not authenticated
  if (!isAuthenticated) {
    return (
      <Navigate 
        to="/login" 
        state={{ from: location.pathname }} 
        replace 
      />
    );
  }

  // Check role permissions if roles are specified
  if (roles.length > 0 && !hasRole(roles)) {
    return (
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        minHeight="60vh"
        textAlign="center"
        p={4}
      >
        <Typography variant="h4" color="error" gutterBottom>
          Access Denied
        </Typography>
        <Typography variant="body1" color="text.secondary" paragraph>
          You don't have permission to access this page.
        </Typography>
        <Typography variant="body2" color="text.secondary" paragraph>
          Required roles: {roles.join(', ')}
        </Typography>
        <Typography variant="body2" color="text.secondary" paragraph>
          Your role: {user?.role}
        </Typography>
        <Button 
          variant="contained" 
          onClick={() => window.history.back()}
          sx={{ mt: 2 }}
        >
          Go Back
        </Button>
      </Box>
    );
  }

  return <>{children}</>;
};

export default ProtectedRoute;