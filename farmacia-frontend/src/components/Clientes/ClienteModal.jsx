import React, { useState, useEffect } from 'react';
import { clienteAPI } from '../../services/api';
import { XMarkIcon } from '@heroicons/react/24/outline';

const ClienteModal = ({ cliente, onClose }) => {
  const [formData, setFormData] = useState({
    dni: '',
    nombres: '',
    apellidoPaterno: '',
    apellidoMaterno: '',
    telefono: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (cliente) {
      setFormData({
        dni: cliente.dni || '',
        nombres: cliente.nombres || '',
        apellidoPaterno: cliente.apellidoPaterno || '',
        apellidoMaterno: cliente.apellidoMaterno || '',
        telefono: cliente.telefono || ''
      });
    }
  }, [cliente]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      if (cliente) {
        await clienteAPI.update(cliente.id, formData);
      } else {
        await clienteAPI.createManual(formData);
      }
      onClose();
    } catch (error) {
      console.error('Error saving cliente:', error);
      setError(error.response?.data?.message || 'Error al guardar cliente');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-medium text-gray-900">
            {cliente ? 'Editar Cliente' : 'Nuevo Cliente'}
          </h3>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <XMarkIcon className="h-6 w-6" />
          </button>
        </div>

        {error && (
          <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">
              DNI *
            </label>
            <input
              type="text"
              name="dni"
              required
              maxLength="8"
              pattern="[0-9]{8}"
              className="input-field mt-1"
              value={formData.dni}
              onChange={handleChange}
              placeholder="12345678"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Nombres *
            </label>
            <input
              type="text"
              name="nombres"
              required
              className="input-field mt-1"
              value={formData.nombres}
              onChange={handleChange}
              placeholder="Juan Carlos"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Apellido Paterno *
            </label>
            <input
              type="text"
              name="apellidoPaterno"
              required
              className="input-field mt-1"
              value={formData.apellidoPaterno}
              onChange={handleChange}
              placeholder="Pérez"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Apellido Materno *
            </label>
            <input
              type="text"
              name="apellidoMaterno"
              required
              className="input-field mt-1"
              value={formData.apellidoMaterno}
              onChange={handleChange}
              placeholder="García"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Teléfono
            </label>
            <input
              type="tel"
              name="telefono"
              className="input-field mt-1"
              value={formData.telefono}
              onChange={handleChange}
              placeholder="987654321"
            />
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="btn-secondary"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading}
              className="btn-primary"
            >
              {loading ? 'Guardando...' : 'Guardar'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ClienteModal;