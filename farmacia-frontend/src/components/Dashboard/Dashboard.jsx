import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { 
  UsersIcon, 
  ShoppingBagIcon, 
  CubeIcon, 
  ChartBarIcon 
} from '@heroicons/react/24/outline';
import { clienteAPI, productoAPI, ventaAPI, usuarioAPI } from '../../services/api';

const Dashboard = () => {
  const { user, hasRole } = useAuth();
  const [stats, setStats] = useState({
    clientes: 0,
    productos: 0,
    ventas: 0,
    usuarios: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const promises = [];
      
      if (hasRole('ADMIN') || hasRole('VENDEDOR')) {
        promises.push(clienteAPI.getAll());
      }
      
      if (hasRole('ADMIN') || hasRole('ALMACENERO')) {
        promises.push(productoAPI.getAll());
      }
      
      if (hasRole('ADMIN') || hasRole('VENDEDOR')) {
        promises.push(ventaAPI.getAll());
      }
      
      if (hasRole('ADMIN')) {
        promises.push(usuarioAPI.getAll());
      }

      const results = await Promise.allSettled(promises);
      
      let index = 0;
      const newStats = { ...stats };
      
      if (hasRole('ADMIN') || hasRole('VENDEDOR')) {
        if (results[index]?.status === 'fulfilled') {
          newStats.clientes = results[index].value.data.length;
        }
        index++;
      }
      
      if (hasRole('ADMIN') || hasRole('ALMACENERO')) {
        if (results[index]?.status === 'fulfilled') {
          newStats.productos = results[index].value.data.length;
        }
        index++;
      }
      
      if (hasRole('ADMIN') || hasRole('VENDEDOR')) {
        if (results[index]?.status === 'fulfilled') {
          newStats.ventas = results[index].value.data.length;
        }
        index++;
      }
      
      if (hasRole('ADMIN')) {
        if (results[index]?.status === 'fulfilled') {
          newStats.usuarios = results[index].value.data.length;
        }
      }
      
      setStats(newStats);
    } catch (error) {
      console.error('Error loading stats:', error);
    } finally {
      setLoading(false);
    }
  };

  const cards = [
    {
      title: 'Clientes',
      value: stats.clientes,
      icon: UsersIcon,
      color: 'bg-blue-500',
      show: hasRole('ADMIN') || hasRole('VENDEDOR')
    },
    {
      title: 'Productos',
      value: stats.productos,
      icon: CubeIcon,
      color: 'bg-green-500',
      show: hasRole('ADMIN') || hasRole('ALMACENERO')
    },
    {
      title: 'Ventas',
      value: stats.ventas,
      icon: ShoppingBagIcon,
      color: 'bg-purple-500',
      show: hasRole('ADMIN') || hasRole('VENDEDOR')
    },
    {
      title: 'Usuarios',
      value: stats.usuarios,
      icon: ChartBarIcon,
      color: 'bg-orange-500',
      show: hasRole('ADMIN')
    }
  ].filter(card => card.show);

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
        <div className="text-sm text-gray-500">
          Bienvenido, {user?.nombreCompleto || user?.email}
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {cards.map((card, index) => (
          <div key={index} className="card">
            <div className="flex items-center">
              <div className={`${card.color} p-3 rounded-lg`}>
                <card.icon className="h-6 w-6 text-white" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">{card.title}</p>
                <p className="text-2xl font-semibold text-gray-900">
                  {loading ? '...' : card.value}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Welcome Card */}
      <div className="card">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">
          Sistema de Gestión de Farmacia
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h3 className="font-medium text-gray-900 mb-2">Funcionalidades Disponibles:</h3>
            <ul className="space-y-1 text-sm text-gray-600">
              {hasRole('ADMIN') && (
                <>
                  <li>• Gestión completa de usuarios</li>
                  <li>• Administración de productos</li>
                  <li>• Control de ventas y reportes</li>
                  <li>• Gestión de clientes con RENIEC</li>
                </>
              )}
              {hasRole('VENDEDOR') && (
                <>
                  <li>• Registro de ventas</li>
                  <li>• Gestión de clientes</li>
                  <li>• Consulta de productos</li>
                </>
              )}
              {hasRole('ALMACENERO') && (
                <>
                  <li>• Gestión de inventario</li>
                  <li>• Control de productos</li>
                  <li>• Actualización de stock</li>
                </>
              )}
            </ul>
          </div>
          <div>
            <h3 className="font-medium text-gray-900 mb-2">Tu Información:</h3>
            <div className="space-y-1 text-sm text-gray-600">
              <p><strong>Email:</strong> {user?.email}</p>
              <p><strong>Nombre:</strong> {user?.nombreCompleto}</p>
              <p><strong>Roles:</strong> {user?.roles?.join(', ')}</p>
              {user?.sedeNombre && (
                <p><strong>Sede:</strong> {user.sedeNombre}</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;