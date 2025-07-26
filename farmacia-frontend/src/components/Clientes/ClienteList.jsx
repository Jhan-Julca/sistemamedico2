import React, { useState, useEffect } from 'react';
import { clienteAPI } from '../../services/api';
import { 
  MagnifyingGlassIcon, 
  PlusIcon, 
  UserIcon,
  IdentificationIcon 
} from '@heroicons/react/24/outline';
import ClienteModal from './ClienteModal';
import ClienteDniModal from './ClienteDniModal';

const ClienteList = () => {
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [showDniModal, setShowDniModal] = useState(false);
  const [selectedCliente, setSelectedCliente] = useState(null);

  useEffect(() => {
    loadClientes();
  }, []);

  const loadClientes = async () => {
    try {
      const response = await clienteAPI.getAll();
      setClientes(response.data);
    } catch (error) {
      console.error('Error loading clientes:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      loadClientes();
      return;
    }

    try {
      setLoading(true);
      const response = await clienteAPI.search(searchTerm);
      setClientes(response.data);
    } catch (error) {
      console.error('Error searching clientes:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (cliente) => {
    setSelectedCliente(cliente);
    setShowModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Estás seguro de eliminar este cliente?')) {
      try {
        await clienteAPI.delete(id);
        loadClientes();
      } catch (error) {
        console.error('Error deleting cliente:', error);
        alert('Error al eliminar cliente');
      }
    }
  };

  const handleModalClose = () => {
    setShowModal(false);
    setShowDniModal(false);
    setSelectedCliente(null);
    loadClientes();
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('es-PE');
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Clientes</h1>
        <div className="flex space-x-3">
          <button
            onClick={() => setShowDniModal(true)}
            className="btn-primary flex items-center"
          >
            <IdentificationIcon className="h-5 w-5 mr-2" />
            Buscar por DNI
          </button>
          <button
            onClick={() => setShowModal(true)}
            className="btn-primary flex items-center"
          >
            <PlusIcon className="h-5 w-5 mr-2" />
            Nuevo Cliente
          </button>
        </div>
      </div>

      {/* Search */}
      <div className="card">
        <div className="flex space-x-4">
          <div className="flex-1">
            <input
              type="text"
              placeholder="Buscar por nombre o apellido..."
              className="input-field"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
          </div>
          <button
            onClick={handleSearch}
            className="btn-primary flex items-center"
          >
            <MagnifyingGlassIcon className="h-5 w-5 mr-2" />
            Buscar
          </button>
        </div>
      </div>

      {/* Clientes Table */}
      <div className="card">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Cliente
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  DNI
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Teléfono
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Fuente
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Fecha Registro
                </th>
                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Acciones
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {clientes.map((cliente) => (
                <tr key={cliente.id} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-shrink-0 h-10 w-10">
                        <div className="h-10 w-10 rounded-full bg-gray-200 flex items-center justify-center">
                          <UserIcon className="h-5 w-5 text-gray-500" />
                        </div>
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          {cliente.nombres} {cliente.apellidoPaterno} {cliente.apellidoMaterno}
                        </div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {cliente.dni}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {cliente.telefono || '-'}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                      cliente.fuenteDatos === 'RENIEC' 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-blue-100 text-blue-800'
                    }`}>
                      {cliente.fuenteDatos}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {formatDate(cliente.fechaRegistro)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button
                      onClick={() => handleEdit(cliente)}
                      className="text-primary-600 hover:text-primary-900 mr-3"
                    >
                      Editar
                    </button>
                    <button
                      onClick={() => handleDelete(cliente.id)}
                      className="text-red-600 hover:text-red-900"
                    >
                      Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          
          {clientes.length === 0 && (
            <div className="text-center py-8">
              <UserIcon className="mx-auto h-12 w-12 text-gray-400" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">No hay clientes</h3>
              <p className="mt-1 text-sm text-gray-500">
                Comienza agregando un nuevo cliente.
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Modals */}
      {showModal && (
        <ClienteModal
          cliente={selectedCliente}
          onClose={handleModalClose}
        />
      )}

      {showDniModal && (
        <ClienteDniModal
          onClose={handleModalClose}
        />
      )}
    </div>
  );
};

export default ClienteList;