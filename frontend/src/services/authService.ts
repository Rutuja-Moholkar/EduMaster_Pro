import api from './api';
import { LoginRequest, RegisterRequest, JwtResponse, ApiResponse } from '../types';

export const authService = {
  // Login user
  login: async (credentials: LoginRequest): Promise<ApiResponse<JwtResponse>> => {
    const response = await api.post('/auth/login', credentials);
    return response.data;
  },

  // Register user
  register: async (userData: RegisterRequest): Promise<ApiResponse<JwtResponse>> => {
    const response = await api.post('/auth/register', userData);
    return response.data;
  },

  // Refresh access token
  refreshToken: async (refreshToken: string): Promise<ApiResponse<JwtResponse>> => {
    const response = await api.post('/auth/refresh', null, {
      params: { refreshToken }
    });
    return response.data;
  },

  // Check email availability
  checkEmailAvailability: async (email: string): Promise<ApiResponse<boolean>> => {
    const response = await api.get('/auth/check-email', {
      params: { email }
    });
    return response.data;
  },

  // Logout (client-side only)
  logout: async (): Promise<void> => {
    // Clear tokens from localStorage
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  },

  // Get current user from token
  getCurrentUser: (): any => {
    const token = localStorage.getItem('accessToken');
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload;
    } catch (error) {
      return null;
    }
  },

  // Check if user is authenticated
  isAuthenticated: (): boolean => {
    const token = localStorage.getItem('accessToken');
    if (!token) return false;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch (error) {
      return false;
    }
  },
};