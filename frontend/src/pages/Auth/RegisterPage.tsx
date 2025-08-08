import React, { useState } from 'react';
import {
  Box,
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Link,
  Alert,
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormHelperText
} from '@mui/material';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from '../../hooks/redux';
import { register as registerUser } from '../../store/slices/authSlice';
import { UserRole } from '../../types';

interface RegisterFormData {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword: string;
  role: UserRole;
}

const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { isLoading, error } = useAppSelector((state) => state.auth);
  const [submitError, setSubmitError] = useState<string>('');

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm<RegisterFormData>({
    defaultValues: {
      role: UserRole.STUDENT
    }
  });

  const password = watch('password');

  const onSubmit = async (data: RegisterFormData) => {
    try {
      setSubmitError('');
      const { confirmPassword, ...registerData } = data;
      await dispatch(registerUser(registerData)).unwrap();
      
      // Show success message and redirect to login
      navigate('/login', { 
        state: { 
          message: 'Registration successful! Please check your email to verify your account.' 
        }
      });
    } catch (error: any) {
      setSubmitError(error.message || 'Registration failed. Please try again.');
    }
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 8, mb: 4 }}>
      <Paper elevation={3} sx={{ p: 4 }}>
        <Box sx={{ textAlign: 'center', mb: 3 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            Sign Up
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Create your EduMaster Pro account
          </Typography>
        </Box>

        {(error || submitError) && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error || submitError}
          </Alert>
        )}

        <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
          <Box sx={{ display: 'flex', gap: 2 }}>
            <TextField
              {...register('firstName', {
                required: 'First name is required',
                minLength: {
                  value: 2,
                  message: 'First name must be at least 2 characters'
                }
              })}
              fullWidth
              label="First Name"
              autoComplete="given-name"
              autoFocus
              margin="normal"
              error={!!errors.firstName}
              helperText={errors.firstName?.message}
            />

            <TextField
              {...register('lastName', {
                required: 'Last name is required',
                minLength: {
                  value: 2,
                  message: 'Last name must be at least 2 characters'
                }
              })}
              fullWidth
              label="Last Name"
              autoComplete="family-name"
              margin="normal"
              error={!!errors.lastName}
              helperText={errors.lastName?.message}
            />
          </Box>

          <TextField
            {...register('email', {
              required: 'Email is required',
              pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: 'Invalid email address'
              }
            })}
            fullWidth
            label="Email Address"
            type="email"
            autoComplete="email"
            margin="normal"
            error={!!errors.email}
            helperText={errors.email?.message}
          />

          <FormControl fullWidth margin="normal" error={!!errors.role}>
            <InputLabel>I want to</InputLabel>
            <Select
              {...register('role', { required: 'Please select your role' })}
              label="I want to"
              defaultValue={UserRole.STUDENT}
            >
              <MenuItem value={UserRole.STUDENT}>Learn (Student)</MenuItem>
              <MenuItem value={UserRole.INSTRUCTOR}>Teach (Instructor)</MenuItem>
            </Select>
            {errors.role && (
              <FormHelperText>{errors.role.message}</FormHelperText>
            )}
          </FormControl>

          <TextField
            {...register('password', {
              required: 'Password is required',
              minLength: {
                value: 8,
                message: 'Password must be at least 8 characters'
              },
              pattern: {
                value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/,
                message: 'Password must contain at least one uppercase letter, one lowercase letter, and one number'
              }
            })}
            fullWidth
            label="Password"
            type="password"
            autoComplete="new-password"
            margin="normal"
            error={!!errors.password}
            helperText={errors.password?.message}
          />

          <TextField
            {...register('confirmPassword', {
              required: 'Please confirm your password',
              validate: value =>
                value === password || 'Passwords do not match'
            })}
            fullWidth
            label="Confirm Password"
            type="password"
            autoComplete="new-password"
            margin="normal"
            error={!!errors.confirmPassword}
            helperText={errors.confirmPassword?.message}
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            size="large"
            disabled={isLoading}
            sx={{ mt: 3, mb: 2 }}
          >
            {isLoading ? (
              <CircularProgress size={24} color="inherit" />
            ) : (
              'Create Account'
            )}
          </Button>

          <Box sx={{ textAlign: 'center' }}>
            <Link
              component={RouterLink}
              to="/login"
              variant="body2"
              underline="hover"
            >
              Already have an account? Sign In
            </Link>
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default RegisterPage;