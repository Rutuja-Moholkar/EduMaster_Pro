import api from './api';
import { 
  Course, 
  CourseCreateRequest, 
  ApiResponse, 
  PaginatedResponse, 
  CourseFilters 
} from '../types';

export const courseService = {
  // Get all published courses
  getCourses: async (params: {
    page?: number;
    size?: number;
    filters?: CourseFilters;
  }): Promise<ApiResponse<PaginatedResponse<Course>>> => {
    const { page = 0, size = 12, filters = {} } = params;
    
    const queryParams = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      sortBy: filters.sortBy || 'createdAt',
      sortDir: filters.sortDir || 'desc',
    });

    if (filters.searchTerm) queryParams.append('searchTerm', filters.searchTerm);
    if (filters.categoryId) queryParams.append('categoryId', filters.categoryId.toString());
    if (filters.level) queryParams.append('level', filters.level);
    if (filters.minPrice) queryParams.append('minPrice', filters.minPrice.toString());
    if (filters.maxPrice) queryParams.append('maxPrice', filters.maxPrice.toString());

    const response = await api.get(`/courses/public?${queryParams}`);
    return response.data;
  },

  // Get course by ID
  getCourseById: async (courseId: number): Promise<ApiResponse<Course>> => {
    const response = await api.get(`/courses/public/${courseId}`);
    return response.data;
  },

  // Search courses
  searchCourses: async (params: {
    keyword: string;
    page?: number;
    size?: number;
  }): Promise<ApiResponse<PaginatedResponse<Course>>> => {
    const { keyword, page = 0, size = 12 } = params;
    
    const response = await api.get('/courses/public/search', {
      params: { keyword, page, size }
    });
    return response.data;
  },

  // Filter courses
  filterCourses: async (filters: CourseFilters & {
    page?: number;
    size?: number;
  }): Promise<ApiResponse<PaginatedResponse<Course>>> => {
    const response = await api.get('/courses/public/filter', {
      params: filters
    });
    return response.data;
  },

  // Get popular courses
  getPopularCourses: async (limit: number = 10): Promise<ApiResponse<Course[]>> => {
    const response = await api.get('/courses/public/popular', {
      params: { limit }
    });
    return response.data;
  },

  // Get recent courses
  getRecentCourses: async (limit: number = 10): Promise<ApiResponse<Course[]>> => {
    const response = await api.get('/courses/public/recent', {
      params: { limit }
    });
    return response.data;
  },

  // Get free courses
  getFreeCourses: async (limit: number = 10): Promise<ApiResponse<Course[]>> => {
    const response = await api.get('/courses/public/free', {
      params: { limit }
    });
    return response.data;
  },

  // Create course (Instructor/Admin)
  createCourse: async (courseData: CourseCreateRequest): Promise<ApiResponse<Course>> => {
    const response = await api.post('/courses', courseData);
    return response.data;
  },

  // Update course (Instructor/Admin)
  updateCourse: async (courseId: number, courseData: Partial<CourseCreateRequest>): Promise<ApiResponse<Course>> => {
    const response = await api.put(`/courses/${courseId}`, courseData);
    return response.data;
  },

  // Publish course (Instructor/Admin)
  publishCourse: async (courseId: number): Promise<ApiResponse<Course>> => {
    const response = await api.post(`/courses/${courseId}/publish`);
    return response.data;
  },

  // Submit for approval (Instructor)
  submitForApproval: async (courseId: number): Promise<ApiResponse<Course>> => {
    const response = await api.post(`/courses/${courseId}/submit-approval`);
    return response.data;
  },

  // Approve course (Admin)
  approveCourse: async (courseId: number): Promise<ApiResponse<Course>> => {
    const response = await api.post(`/courses/${courseId}/approve`);
    return response.data;
  },

  // Suspend course (Admin)
  suspendCourse: async (courseId: number, reason: string): Promise<ApiResponse<Course>> => {
    const response = await api.post(`/courses/${courseId}/suspend`, null, {
      params: { reason }
    });
    return response.data;
  },

  // Delete course (Instructor/Admin)
  deleteCourse: async (courseId: number): Promise<ApiResponse<string>> => {
    const response = await api.delete(`/courses/${courseId}`);
    return response.data;
  },

  // Get instructor courses
  getInstructorCourses: async (instructorId: number, params: {
    page?: number;
    size?: number;
  } = {}): Promise<ApiResponse<PaginatedResponse<Course>>> => {
    const { page = 0, size = 10 } = params;
    
    const response = await api.get(`/courses/instructor/${instructorId}`, {
      params: { page, size }
    });
    return response.data;
  },

  // Get total published courses count
  getTotalPublishedCourses: async (): Promise<ApiResponse<number>> => {
    const response = await api.get('/courses/stats/total-published');
    return response.data;
  },
};