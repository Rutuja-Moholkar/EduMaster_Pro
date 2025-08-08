import { useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from './redux';
import { login, register, logout, clearError } from '../store/slices/authSlice';
import { LoginRequest, RegisterRequest, UserRole } from '../types';

export const useAuth = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { 
    user, 
    isAuthenticated, 
    isLoading, 
    error,
    accessToken 
  } = useAppSelector((state) => state.auth);

  const loginUser = useCallback(async (credentials: LoginRequest) => {
    try {
      const result = await dispatch(login(credentials));
      if (login.fulfilled.match(result)) {
        navigate('/dashboard');
        return { success: true };
      } else {
        return { success: false, error: result.payload as string };
      }
    } catch (error) {
      return { success: false, error: 'Login failed' };
    }
  }, [dispatch, navigate]);

  const registerUser = useCallback(async (userData: RegisterRequest) => {
    try {
      const result = await dispatch(register(userData));
      if (register.fulfilled.match(result)) {
        navigate('/dashboard');
        return { success: true };
      } else {
        return { success: false, error: result.payload as string };
      }
    } catch (error) {
      return { success: false, error: 'Registration failed' };
    }
  }, [dispatch, navigate]);

  const logoutUser = useCallback(async () => {
    await dispatch(logout());
    navigate('/');
  }, [dispatch, navigate]);

  const clearAuthError = useCallback(() => {
    dispatch(clearError());
  }, [dispatch]);

  const hasRole = useCallback((roles: UserRole[]): boolean => {
    if (!user || !isAuthenticated) return false;
    return roles.includes(user.role);
  }, [user, isAuthenticated]);

  const isStudent = useCallback((): boolean => {
    return hasRole([UserRole.STUDENT]);
  }, [hasRole]);

  const isInstructor = useCallback((): boolean => {
    return hasRole([UserRole.INSTRUCTOR]);
  }, [hasRole]);

  const isAdmin = useCallback((): boolean => {
    return hasRole([UserRole.ADMIN]);
  }, [hasRole]);

  const canAccessInstructorFeatures = useCallback((): boolean => {
    return hasRole([UserRole.INSTRUCTOR, UserRole.ADMIN]);
  }, [hasRole]);

  const canAccessAdminFeatures = useCallback((): boolean => {
    return hasRole([UserRole.ADMIN]);
  }, [hasRole]);

  return {
    // State
    user,
    isAuthenticated,
    isLoading,
    error,
    accessToken,
    
    // Actions
    loginUser,
    registerUser,
    logoutUser,
    clearAuthError,
    
    // Role checks
    hasRole,
    isStudent,
    isInstructor,
    isAdmin,
    canAccessInstructorFeatures,
    canAccessAdminFeatures,
  };
};