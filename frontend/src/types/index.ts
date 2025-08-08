// API Response Types
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: Date;
}

// User Types
export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  isVerified: boolean;
  isActive: boolean;
  profilePictureUrl?: string;
  phone?: string;
  bio?: string;
  createdAt: Date;
  updatedAt: Date;
}

export enum UserRole {
  STUDENT = 'STUDENT',
  INSTRUCTOR = 'INSTRUCTOR',
  ADMIN = 'ADMIN'
}

// Authentication Types
export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword?: string;
  role?: UserRole;
  phone?: string;
  bio?: string;
}

export interface JwtResponse {
  accessToken: string;
  refreshToken: string;
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  isVerified: boolean;
  isActive: boolean;
  profilePictureUrl?: string;
}

// Course Types
export interface Course {
  id: number;
  title: string;
  description: string;
  shortDescription?: string;
  price: number;
  status: CourseStatus;
  thumbnailUrl?: string;
  durationHours?: number;
  level: CourseLevel;
  language: string;
  requirements?: string;
  learningOutcomes?: string;
  createdAt: Date;
  updatedAt: Date;
  instructor: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
  };
  category: {
    id: number;
    name: string;
    description?: string;
  };
  totalLessons: number;
  totalEnrollments: number;
  averageRating: number;
  totalReviews: number;
  isFree: boolean;
}

export enum CourseStatus {
  DRAFT = 'DRAFT',
  PENDING_APPROVAL = 'PENDING_APPROVAL',
  PUBLISHED = 'PUBLISHED',
  SUSPENDED = 'SUSPENDED'
}

export enum CourseLevel {
  BEGINNER = 'BEGINNER',
  INTERMEDIATE = 'INTERMEDIATE',
  ADVANCED = 'ADVANCED'
}

export interface CourseCreateRequest {
  title: string;
  description: string;
  shortDescription?: string;
  price: number;
  categoryId: number;
  instructorId: number;
  level: CourseLevel;
  language: string;
  requirements?: string;
  learningOutcomes?: string;
  thumbnailUrl?: string;
  durationHours?: number;
}

// Category Types
export interface Category {
  id: number;
  name: string;
  description?: string;
  createdAt: Date;
  updatedAt: Date;
}

// Enrollment Types
export interface Enrollment {
  id: number;
  enrollmentDate: Date;
  completionStatus: CompletionStatus;
  completionDate?: Date;
  progressPercentage: number;
  user: {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
  };
  course: {
    id: number;
    title: string;
    description: string;
    thumbnailUrl?: string;
    price: number;
    durationHours?: number;
  };
  totalLessons: number;
  completedLessons: number;
  isCompleted: boolean;
  isInProgress: boolean;
}

export enum CompletionStatus {
  ENROLLED = 'ENROLLED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED'
}

// Payment Types
export interface Payment {
  id: number;
  amount: number;
  currency: string;
  status: PaymentStatus;
  paymentDate: Date;
  refundAmount?: number;
  refundDate?: Date;
  refundReason?: string;
  user: {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
  };
  course: {
    id: number;
    title: string;
    thumbnailUrl?: string;
  };
  stripePaymentIntentId?: string;
  isSuccessful: boolean;
  isRefunded: boolean;
  isFailed: boolean;
  netAmount: number;
}

export enum PaymentStatus {
  PENDING = 'PENDING',
  SUCCEEDED = 'SUCCEEDED',
  FAILED = 'FAILED',
  REFUNDED = 'REFUNDED',
  CANCELED = 'CANCELED'
}

export interface PaymentIntentResponse {
  paymentIntentId: string;
  clientSecret: string;
  amount: number;
  currency: string;
}

// Notification Types
export interface Notification {
  id: number;
  title: string;
  message: string;
  type: NotificationType;
  isRead: boolean;
  relatedEntityType?: string;
  relatedEntityId?: number;
  createdAt: Date;
}

export enum NotificationType {
  INFO = 'INFO',
  SUCCESS = 'SUCCESS',
  WARNING = 'WARNING',
  ERROR = 'ERROR'
}

// Lesson Types
export interface Lesson {
  id: number;
  title: string;
  description?: string;
  videoUrl?: string;
  durationMinutes: number;
  orderIndex: number;
  isFreePreview: boolean;
  contentType: ContentType;
  contentUrl?: string;
  createdAt: Date;
  updatedAt: Date;
}

export enum ContentType {
  VIDEO = 'VIDEO',
  TEXT = 'TEXT',
  PDF = 'PDF',
  QUIZ = 'QUIZ'
}

// Review Types
export interface Review {
  id: number;
  rating: number;
  comment?: string;
  isApproved: boolean;
  createdAt: Date;
  updatedAt: Date;
  user: {
    id: number;
    firstName: string;
    lastName: string;
  };
  course: {
    id: number;
    title: string;
  };
}

// Statistics Types
export interface UserStats {
  totalEnrollments: number;
  completedEnrollments: number;
  inProgressEnrollments: number;
  learningProgress: number;
}

export interface CourseStats {
  totalEnrollments: number;
  completedEnrollments: number;
  completionRate: number;
}

export interface PaymentAnalytics {
  totalRevenue: number;
  totalRefunds: number;
  netRevenue: number;
  totalSuccessfulPayments: number;
  totalFailedPayments: number;
  totalRefundedPayments: number;
}

// Pagination Types
export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface PaginationParams {
  page: number;
  size: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}

// Filter Types
export interface CourseFilters {
  searchTerm?: string;
  categoryId?: number;
  level?: CourseLevel;
  minPrice?: number;
  maxPrice?: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}

// Form Types
export interface FormErrors {
  [key: string]: string;
}

// Common UI Types
export interface SelectOption {
  value: string | number;
  label: string;
}

export interface TabItem {
  label: string;
  value: string;
  icon?: React.ReactNode;
}

// Route Types
export interface ProtectedRouteProps {
  children: React.ReactNode;
  roles?: UserRole[];
}

// Theme Types
export interface ThemeMode {
  mode: 'light' | 'dark';
}

// Error Types
export interface ErrorResponse {
  message: string;
  errors?: { [key: string]: string };
  timestamp: Date;
  status: number;
}

export interface ValidationError {
  field: string;
  message: string;
}