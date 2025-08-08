import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { Payment } from '../../types';
import api from '../../services/api';

interface PaymentsState {
  payments: Payment[];
  isLoading: boolean;
  error: string | null;
}

const initialState: PaymentsState = {
  payments: [],
  isLoading: false,
  error: null,
};

// Async thunks
export const fetchPayments = createAsyncThunk(
  'payments/fetchPayments',
  async (_, { rejectWithValue }) => {
    try {
      const response = await api.get('/payments');
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch payments');
    }
  }
);

export const processPayment = createAsyncThunk(
  'payments/processPayment',
  async (paymentData: { courseId: number; paymentMethodId: string }, { rejectWithValue }) => {
    try {
      const response = await api.post('/payments/process', paymentData);
      return response.data;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Payment processing failed');
    }
  }
);

const paymentsSlice = createSlice({
  name: 'payments',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchPayments.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(fetchPayments.fulfilled, (state, action) => {
        state.isLoading = false;
        state.payments = action.payload.data || action.payload;
      })
      .addCase(fetchPayments.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      })
      .addCase(processPayment.pending, (state) => {
        state.isLoading = true;
        state.error = null;
      })
      .addCase(processPayment.fulfilled, (state, action) => {
        state.isLoading = false;
        state.payments.push(action.payload.data || action.payload);
      })
      .addCase(processPayment.rejected, (state, action) => {
        state.isLoading = false;
        state.error = action.payload as string;
      });
  },
});

export const { clearError } = paymentsSlice.actions;
export default paymentsSlice.reducer;