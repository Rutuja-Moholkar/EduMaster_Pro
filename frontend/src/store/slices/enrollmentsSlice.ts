import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Enrollment } from '../../types';
import api from '../../services/api';

interface EnrollmentsState {
  enrollments: Enrollment[];
  isLoading: boolean;
  error: string | null;
}

const initialState: EnrollmentsState = {
  enrollments: [],
  isLoading: false,
  error: null,
};

// Async thunks
export const fetchEnrollments = createAsyncThunk(
  'enrollments/fetchEnrollments',
  async (_, { rejectWithValue }) => {
    try {
      const response = await api.get('/enrollments');
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch enrollments');
    }
  }
);

export const enrollInCourse = createAsyncThunk(
  'enrollments/enrollInCourse',
  async (courseId: number, { rejectWithValue }) => {
    try {
      const response = await api.post(`/enrollments`, { courseId });
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to enroll in course');
    }
  }
);

const enrollmentsSlice = createSlice({
  name: 'enrollments',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchEnrollments.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchEnrollments.fulfilled, (state, action) => {
        state.isLoading = false;
        state.enrollments = action.payload.data || action.payload;
      })
      .addCase(fetchEnrollments.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      .addCase(enrollInCourse.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(enrollInCourse.fulfilled, (state, action) => {
        state.isLoading = false;
        state.enrollments.push(action.payload.data || action.payload);
      })
      .addCase(enrollInCourse.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError } = enrollmentsSlice.actions;
export default enrollmentsSlice.reducer;