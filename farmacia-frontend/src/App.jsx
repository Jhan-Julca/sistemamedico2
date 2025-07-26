import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Layout from './components/Layout/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './components/Auth/Login';
import Dashboard from './components/Dashboard/Dashboard';
import ClienteList from './components/Clientes/ClienteList';
import ProductoList from './components/Productos/ProductoList';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          
          <Route path="/" element={
            <ProtectedRoute>
              <Layout>
                <Dashboard />
              </Layout>
            </ProtectedRoute>
          } />
          
          <Route path="/clientes" element={
            <ProtectedRoute roles={['ADMIN', 'VENDEDOR']}>
              <Layout>
                <ClienteList />
              </Layout>
            </ProtectedRoute>
          } />
          
          <Route path="/productos" element={
            <ProtectedRoute roles={['ADMIN', 'ALMACENERO']}>
              <Layout>
                <ProductoList />
              </Layout>
            </ProtectedRoute>
          } />
          
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;