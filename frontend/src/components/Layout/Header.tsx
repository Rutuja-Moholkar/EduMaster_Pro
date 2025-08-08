import React, { useState } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  IconButton,
  Menu,
  MenuItem,
  Avatar,
  Badge,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import {
  Menu as MenuIcon,
  Notifications as NotificationsIcon,
  AccountCircle,
  Dashboard,
  School,
  ExitToApp,
  AutoStories,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const Header: React.FC = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const navigate = useNavigate();
  const { user, isAuthenticated, logoutUser, canAccessInstructorFeatures, canAccessAdminFeatures } = useAuth();
  
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [mobileMenuAnchorEl, setMobileMenuAnchorEl] = useState<null | HTMLElement>(null);

  const handleProfileMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleProfileMenuClose = () => {
    setAnchorEl(null);
  };

  const handleMobileMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setMobileMenuAnchorEl(event.currentTarget);
  };

  const handleMobileMenuClose = () => {
    setMobileMenuAnchorEl(null);
  };

  const handleLogout = async () => {
    await logoutUser();
    handleProfileMenuClose();
  };

  const menuItems = [
    { label: 'Home', path: '/' },
    { label: 'Courses', path: '/courses' },
    { label: 'About', path: '/about' },
  ];

  return (
    <AppBar position="fixed" elevation={1}>
      <Toolbar>
        {/* Logo */}
        <Box
          sx={{ 
            display: 'flex',
            alignItems: 'center',
            cursor: 'pointer',
            gap: 1
          }}
          onClick={() => navigate('/')}
        >
          <AutoStories 
            sx={{ 
              color: 'white', 
              fontSize: '2rem'
            }} 
          />
          <Typography
            variant="h5"
            component="div"
            sx={{ 
              fontWeight: 700,
              color: 'white',
              letterSpacing: '-0.5px'
            }}
          >
            EduMaster
          </Typography>
          <Typography
            variant="body2"
            component="span"
            sx={{ 
              color: 'primary.main',
              fontWeight: 600,
              fontSize: '0.75rem',
              backgroundColor: 'white',
              padding: '2px 8px',
              borderRadius: '12px',
              marginLeft: '4px'
            }}
          >
            Pro
          </Typography>
        </Box>

        {/* Desktop Navigation */}
        {!isMobile && (
          <Box sx={{ ml: 4, display: 'flex', gap: 3 }}>
            {menuItems.map((item) => (
              <Button
                key={item.path}
                color="inherit"
                onClick={() => navigate(item.path)}
                sx={{ 
                  color: 'white',
                  fontSize: '1.1rem',
                  fontWeight: '600',
                  px: 3,
                  py: 1,
                  textTransform: 'none',
                  '&:hover': {
                    backgroundColor: 'rgba(255, 255, 255, 0.1)',
                    transform: 'translateY(-1px)',
                  }
                }}
              >
                {item.label}
              </Button>
            ))}
          </Box>
        )}

        <Box sx={{ flexGrow: 1 }} />

        {/* Right side items */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          {isAuthenticated ? (
            <>
              {/* Notifications */}
              <IconButton color="inherit">
                <Badge badgeContent={3} color="error">
                  <NotificationsIcon sx={{ color: 'white' }} />
                </Badge>
              </IconButton>

              {/* Dashboard Button */}
              <Button
                color="inherit"
                startIcon={<Dashboard />}
                onClick={() => navigate('/dashboard')}
                sx={{ color: 'text.primary', display: { xs: 'none', sm: 'flex' } }}
              >
                Dashboard
              </Button>

              {/* Instructor Features */}
              {canAccessInstructorFeatures() && (
                <Button
                  color="inherit"
                  startIcon={<School />}
                  onClick={() => navigate('/instructor')}
                  sx={{ color: 'text.primary', display: { xs: 'none', md: 'flex' } }}
                >
                  Instructor
                </Button>
              )}

              {/* Profile Menu */}
              <IconButton
                size="large"
                edge="end"
                aria-label="account of current user"
                aria-controls="profile-menu"
                aria-haspopup="true"
                onClick={handleProfileMenuOpen}
                color="inherit"
              >
                <Avatar 
                  src={user?.profilePictureUrl} 
                  alt={user?.firstName}
                  sx={{ width: 32, height: 32 }}
                >
                  {user?.firstName?.charAt(0)}
                </Avatar>
              </IconButton>
            </>
          ) : (
            <>
              <Button 
                color="inherit" 
                onClick={() => navigate('/login')}
                sx={{ 
                  color: 'white',
                  fontSize: '1.1rem',
                  fontWeight: '600',
                  px: 3,
                  py: 1,
                  textTransform: 'none',
                  '&:hover': {
                    backgroundColor: 'rgba(255, 255, 255, 0.1)',
                    transform: 'translateY(-1px)',
                  }
                }}
              >
                Login
              </Button>
              <Button 
                variant="contained" 
                onClick={() => navigate('/register')}
                sx={{ 
                  ml: 2,
                  fontSize: '1.1rem',
                  fontWeight: '600',
                  px: 3,
                  py: 1.2,
                  textTransform: 'none',
                  backgroundColor: '#10B981',
                  '&:hover': {
                    backgroundColor: '#059669',
                    transform: 'translateY(-1px)',
                    boxShadow: '0 4px 12px rgba(16, 185, 129, 0.3)',
                  }
                }}
              >
                Sign Up
              </Button>
            </>
          )}

          {/* Mobile Menu */}
          {isMobile && (
            <IconButton
              size="large"
              edge="start"
              color="inherit"
              aria-label="menu"
              onClick={handleMobileMenuOpen}
            >
              <MenuIcon sx={{ color: 'white' }} />
            </IconButton>
          )}
        </Box>

        {/* Profile Menu */}
        <Menu
          id="profile-menu"
          anchorEl={anchorEl}
          open={Boolean(anchorEl)}
          onClose={handleProfileMenuClose}
          onClick={handleProfileMenuClose}
        >
          <MenuItem onClick={() => navigate('/profile')}>
            <AccountCircle sx={{ mr: 1 }} />
            Profile
          </MenuItem>
          <MenuItem onClick={() => navigate('/my-courses')}>
            <School sx={{ mr: 1 }} />
            My Courses
          </MenuItem>
          {canAccessAdminFeatures() && (
            <MenuItem onClick={() => navigate('/admin')}>
              <Dashboard sx={{ mr: 1 }} />
              Admin Panel
            </MenuItem>
          )}
          <MenuItem onClick={handleLogout}>
            <ExitToApp sx={{ mr: 1 }} />
            Logout
          </MenuItem>
        </Menu>

        {/* Mobile Menu */}
        <Menu
          id="mobile-menu"
          anchorEl={mobileMenuAnchorEl}
          open={Boolean(mobileMenuAnchorEl)}
          onClose={handleMobileMenuClose}
        >
          {menuItems.map((item) => (
            <MenuItem
              key={item.path}
              onClick={() => {
                navigate(item.path);
                handleMobileMenuClose();
              }}
            >
              {item.label}
            </MenuItem>
          ))}
        </Menu>
      </Toolbar>
    </AppBar>
  );
};

export default Header;