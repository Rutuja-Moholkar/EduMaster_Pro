import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Box } from '@mui/material';

import Layout from './components/Layout/Layout';
import ProtectedRoute from './components/Auth/ProtectedRoute';
import LoadingSpinner from './components/Common/LoadingSpinner';

// Public Pages
import HomePage from './pages/Home/HomePage';
import LoginPage from './pages/Auth/LoginPage';
import RegisterPage from './pages/Auth/RegisterPage';
import CoursesPage from './pages/Courses/CoursesPage';
import CourseDetailPage from './pages/Courses/CourseDetailPage';
import AboutPage from './pages/About/AboutPage';

// Protected Pages
import DashboardPage from './pages/Dashboard/DashboardPage';
import ProfilePage from './pages/Profile/ProfilePage';
import MyCoursesPage from './pages/MyCourses/MyCoursesPage';
import PaymentsPage from './pages/Payments/PaymentsPage';

// Instructor Pages
import InstructorDashboard from './pages/Instructor/InstructorDashboard';
import CreateCoursePage from './pages/Instructor/CreateCoursePage';
import ManageCoursesPage from './pages/Instructor/ManageCoursesPage';

// Admin Pages
import AdminDashboard from './pages/Admin/AdminDashboard';
import ManageUsersPage from './pages/Admin/ManageUsersPage';
import ManageCategoriesPage from './pages/Admin/ManageCategoriesPage';

// Error Pages
import NotFoundPage from './pages/Error/NotFoundPage';

// Additional Pages
import CertificatesPage from './pages/Certificates/CertificatesPage';
import MobileAppPage from './pages/MobileApp/MobileAppPage';
import TeachPage from './pages/Teach/TeachPage';
import InstructorResourcesPage from './pages/InstructorResources/InstructorResourcesPage';
import InstructorSupportPage from './pages/InstructorSupport/InstructorSupportPage';
import ContactPage from './pages/Contact/ContactPage';
import HelpPage from './pages/Help/HelpPage';
import PrivacyPolicyPage from './pages/Legal/PrivacyPolicyPage';
import TermsOfServicePage from './pages/Legal/TermsOfServicePage';
import CookiePolicyPage from './pages/Legal/CookiePolicyPage';

import { useAppSelector } from './hooks/redux';
import { UserRole } from './types';

const App: React.FC = () => {
  const { isLoading } = useAppSelector((state) => state.auth);

  if (isLoading) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        <LoadingSpinner size={60} />
      </Box>
    );
  }

  return (
    <Layout>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/courses" element={<CoursesPage />} />
        <Route path="/courses/:id" element={<CourseDetailPage />} />
        <Route path="/about" element={<AboutPage />} />

        {/* Protected Student Routes */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute roles={[UserRole.STUDENT, UserRole.INSTRUCTOR, UserRole.ADMIN]}>
              <DashboardPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute roles={[UserRole.STUDENT, UserRole.INSTRUCTOR, UserRole.ADMIN]}>
              <ProfilePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/my-courses"
          element={
            <ProtectedRoute roles={[UserRole.STUDENT, UserRole.INSTRUCTOR, UserRole.ADMIN]}>
              <MyCoursesPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/payments"
          element={
            <ProtectedRoute roles={[UserRole.STUDENT, UserRole.INSTRUCTOR, UserRole.ADMIN]}>
              <PaymentsPage />
            </ProtectedRoute>
          }
        />

        {/* Instructor Routes */}
        <Route
          path="/instructor"
          element={
            <ProtectedRoute roles={[UserRole.INSTRUCTOR, UserRole.ADMIN]}>
              <InstructorDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/instructor/courses/create"
          element={
            <ProtectedRoute roles={[UserRole.INSTRUCTOR, UserRole.ADMIN]}>
              <CreateCoursePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/instructor/courses"
          element={
            <ProtectedRoute roles={[UserRole.INSTRUCTOR, UserRole.ADMIN]}>
              <ManageCoursesPage />
            </ProtectedRoute>
          }
        />

        {/* Admin Routes */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute roles={[UserRole.ADMIN]}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/users"
          element={
            <ProtectedRoute roles={[UserRole.ADMIN]}>
              <ManageUsersPage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/categories"
          element={
            <ProtectedRoute roles={[UserRole.ADMIN]}>
              <ManageCategoriesPage />
            </ProtectedRoute>
          }
        />

        {/* Additional Public Routes */}
        <Route path="/certificates" element={<CertificatesPage />} />
        <Route path="/mobile-app" element={<MobileAppPage />} />
        <Route path="/teach" element={<TeachPage />} />
        <Route path="/instructor-resources" element={<InstructorResourcesPage />} />
        <Route path="/instructor-support" element={<InstructorSupportPage />} />
        <Route path="/contact" element={<ContactPage />} />
        <Route path="/help" element={<HelpPage />} />
        
        {/* Legal Pages */}
        <Route path="/privacy" element={<PrivacyPolicyPage />} />
        <Route path="/terms" element={<TermsOfServicePage />} />
        <Route path="/cookies" element={<CookiePolicyPage />} />

        {/* Redirect and Error Routes */}
        <Route path="/404" element={<NotFoundPage />} />
        <Route path="*" element={<Navigate to="/404" replace />} />
      </Routes>
    </Layout>
  );
};

export default App;