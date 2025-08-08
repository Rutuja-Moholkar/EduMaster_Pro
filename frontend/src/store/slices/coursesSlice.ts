import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Course, PaginatedResponse, CourseFilters, CourseCreateRequest } from '../../types';
import { courseService } from '../../services/courseService';

interface CoursesState {
  courses: Course[];
  currentCourse: Course | null;
  totalPages: number;
  currentPage: number;
  totalElements: number;
  isLoading: boolean;
  error: string | null;
  filters: CourseFilters;
}

const initialState: CoursesState = {
  courses: [],
  currentCourse: null,
  totalPages: 0,
  currentPage: 0,
  totalElements: 0,
  isLoading: false,
  error: null,
  filters: {},
};

// Async thunks
export const fetchCourses = createAsyncThunk(
  'courses/fetchCourses',
  async (params: { page?: number; size?: number; filters?: CourseFilters }, { rejectWithValue }) => {
    try {
      const response = await courseService.getCourses(params);
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch courses');
    }
  }
);

export const fetchCourseById = createAsyncThunk(
  'courses/fetchCourseById',
  async (courseId: number, { rejectWithValue }) => {
    try {
      const response = await courseService.getCourseById(courseId);
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch course');
    }
  }
);

export const searchCourses = createAsyncThunk(
  'courses/searchCourses',
  async (params: { keyword: string; page?: number; size?: number }, { rejectWithValue }) => {
    try {
      const response = await courseService.searchCourses(params);
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Search failed');
    }
  }
);

export const createCourse = createAsyncThunk(
  'courses/createCourse',
  async (courseData: CourseCreateRequest, { rejectWithValue }) => {
    try {
      const response = await courseService.createCourse(courseData);
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to create course');
    }
  }
);

const coursesSlice = createSlice({
  name: 'courses',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    setFilters: (state, action: PayloadAction<CourseFilters>) => {
      state.filters = { ...state.filters, ...action.payload };
    },
    clearFilters: (state) => {
      state.filters = {};
    },
    clearCurrentCourse: (state) => {
      state.currentCourse = null;
    },
  },
  extraReducers: (builder) => {
    // Fetch courses
    builder
      .addCase(fetchCourses.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchCourses.fulfilled, (state, action: PayloadAction<PaginatedResponse<Course>>) => {
        state.isLoading = false;
        state.courses = action.payload.content;
        state.totalPages = action.payload.totalPages;
        state.currentPage = action.payload.number;
        state.totalElements = action.payload.totalElements;
        state.error = null;
      })
      .addCase(fetchCourses.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Fetch course by ID
    builder
      .addCase(fetchCourseById.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchCourseById.fulfilled, (state, action: PayloadAction<Course>) => {
        state.isLoading = false;
        state.currentCourse = action.payload;
        state.error = null;
      })
      .addCase(fetchCourseById.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Search courses
    builder
      .addCase(searchCourses.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(searchCourses.fulfilled, (state, action: PayloadAction<PaginatedResponse<Course>>) => {
        state.isLoading = false;
        state.courses = action.payload.content;
        state.totalPages = action.payload.totalPages;
        state.currentPage = action.payload.number;
        state.totalElements = action.payload.totalElements;
        state.error = null;
      })
      .addCase(searchCourses.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });

    // Create course
    builder
      .addCase(createCourse.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(createCourse.fulfilled, (state, action: PayloadAction<Course>) => {
        state.isLoading = false;
        state.courses.unshift(action.payload);
        state.error = null;
      })
      .addCase(createCourse.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError, setFilters, clearFilters, clearCurrentCourse } = coursesSlice.actions;
export default coursesSlice.reducer;