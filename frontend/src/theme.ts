import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#6B7280', // Light grey (modern, clean)
      light: '#9CA3AF',
      dark: '#4B5563',
      contrastText: '#ffffff',
    },
    secondary: {
      main: '#9CA3AF', // Soft light grey (gentle accent)
      light: '#D1D5DB',
      dark: '#6B7280',
      contrastText: '#1F2937',
    },
    background: {
      default: '#FFFFFF', // Clean white background
      paper: '#FFFFFF',
    },
    text: {
      primary: '#1F2937', // Dark grey (excellent contrast)
      secondary: '#6B7280', // Medium grey
    },
    success: {
      main: '#10B981', // Fresh green (modern success color)
      light: '#34D399',
      dark: '#059669',
    },
    warning: {
      main: '#ff9800',
      light: '#ffb74d',
      dark: '#f57c00',
    },
    error: {
      main: '#f44336',
      light: '#e57373',
      dark: '#d32f2f',
    },
    info: {
      main: '#2196f3',
      light: '#64b5f6',
      dark: '#1976d2',
    },
  },
  typography: {
    fontFamily: [
      'Roboto',
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
    ].join(','),
    h1: {
      fontSize: '3rem',
      fontWeight: 600,
      lineHeight: 1.2,
    },
    h2: {
      fontSize: '2.5rem',
      fontWeight: 600,
      lineHeight: 1.3,
    },
    h3: {
      fontSize: '2.125rem',
      fontWeight: 600,
      lineHeight: 1.3,
    },
    h4: {
      fontSize: '1.75rem',
      fontWeight: 500,
      lineHeight: 1.4,
    },
    h5: {
      fontSize: '1.5rem',
      fontWeight: 500,
      lineHeight: 1.4,
    },
    h6: {
      fontSize: '1.25rem',
      fontWeight: 500,
      lineHeight: 1.5,
    },
    body1: {
      fontSize: '1rem',
      lineHeight: 1.6,
    },
    body2: {
      fontSize: '0.875rem',
      lineHeight: 1.5,
    },
  },
  shape: {
    borderRadius: 8,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: 500,
          borderRadius: 8,
          padding: '8px 16px',
        },
        contained: {
          boxShadow: '0 3px 12px rgba(107, 114, 128, 0.15)',
          '&:hover': {
            boxShadow: '0 6px 24px rgba(107, 114, 128, 0.25)',
            transform: 'translateY(-2px)',
          },
          '&.MuiButton-containedPrimary': {
            background: 'linear-gradient(135deg, #6B7280 0%, #4B5563 100%)',
          },
          '&.MuiButton-containedSecondary': {
            background: 'linear-gradient(135deg, #9CA3AF 0%, #6B7280 100%)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          boxShadow: '0 4px 20px rgba(107, 114, 128, 0.08)',
          border: '1px solid rgba(156, 163, 175, 0.1)',
          '&:hover': {
            boxShadow: '0 8px 32px rgba(107, 114, 128, 0.15)',
            transform: 'translateY(-6px)',
            transition: 'all 0.3s ease',
            borderColor: 'rgba(156, 163, 175, 0.25)',
          },
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 8,
          },
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          background: 'linear-gradient(135deg, #6B7280 0%, #4B5563 100%)',
          color: '#ffffff',
          boxShadow: '0 2px 16px rgba(107, 114, 128, 0.2)',
        },
      },
    },
    MuiDrawer: {
      styleOverrides: {
        paper: {
          borderRight: 'none',
          boxShadow: '2px 0 8px rgba(0, 0, 0, 0.1)',
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 16,
          fontWeight: 500,
        },
      },
    },
  },
  breakpoints: {
    values: {
      xs: 0,
      sm: 600,
      md: 900,
      lg: 1200,
      xl: 1536,
    },
  },
});

export default theme;