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

const router = createBrowserRouter([
  {
    Component: App,
    children: [
      {
        path: '/',
        Component: Layout,
        children: [
          {
            path: '/ca-registration',
            Component: CaRegistrationPage,
          },
          {
            path: '/ca-management',
            Component: CaManagementPage,
          },
          {
            path: 'admin-management/admin-registration',
            Component: AdminRegisterPage,
          },
          {
            path: 'admin-management/:id',
            Component: AdminDetailPage,
          },
          {
            path: 'admin-management',
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
