import * as React from 'react';
import * as ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router';
import App from './App';
import Layout from './layout/Layout';
import AdminManagementPage from './pages/admins/AdminManagementPage';
import SignInPage from './pages/auth/SignIn';
import ErrorPage from './pages/ErrorPage';
import CaManagementPage from './pages/ca-service/CaManagementPage';
import CaRegistrationPage from './pages/ca-service/CaRegistrationPage';
import AdminDetailPage from './pages/admins/AdminDetailPage';
import AdminRegisterPage from './pages/admins/AdminRegisterPage';
import UserManagementPage from './pages/users/UserManagementPage';
import UserDetailPage from './pages/users/UserDetailPage';
import DashboardPage from './pages/dashboard/DashboardPage';
import ServerConfigurationPage from './pages/server-configuration/ServerConfigurationPage';
import PasswordPolicyManagementPage from './pages/admins/password-policy/PasswordPolicyManagementPage';

const router = createBrowserRouter([
  {
    Component: App,
    children: [
      {
        path: '/',
        Component: Layout,
        children: [
          {
            path: '/',
            Component: DashboardPage
          },
          {
            path: '/ca-registration',
            Component: CaRegistrationPage,
          },
          {
            path: '/ca-management',
            Component: CaManagementPage,
          },
          {
            path: '/user-management/:id',
            Component: UserDetailPage,
          },
          {
            path: '/user-management',
            Component: UserManagementPage,
          },
          {
            path: 'server-configuration',
            Component: ServerConfigurationPage,
          },
          {
            path: 'admins/admin-management/admin-registration',
            Component: AdminRegisterPage,
          },
          {
            path: 'admins/admin-management/:id',
            Component: AdminDetailPage,
          },
          {
            path: 'admins/admin-management',
            Component: AdminManagementPage,
          },
          {
            path: 'admins/password-policy',
            Component: PasswordPolicyManagementPage,
          },
          {
            path: 'admins',
            Component: AdminManagementPage,
          },
        ],
      },
      {
        path: '/sign-in',
        Component: SignInPage,
      },
      {
        path: '/error',
        Component: ErrorPage,
      },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
);
