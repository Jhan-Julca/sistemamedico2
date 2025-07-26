import React, { useState } from 'react';
import { clienteAPI } from '../../services/api';
import { XMarkIcon, MagnifyingGlassIcon } from '@heroicons/react/24/outline';

const ClienteDniModal = ({ onClose }) => {
  const [dni, setDni] = useState('');
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');
  const [showManualForm, setShowManualForm] = useState(false);
  const [manualData, setManualData] = useState({
    nombres: '',
    apellidoPaterno: '',
    apellidoMaterno: '',
    telefono: ''
  });

  const handleSearch = async () => {
    if (!dni || dni.length !== 8) {
      setError('El DNI debe tener exactamente 8 dígitos');
      return;
    }

    setLoading(true);
    setError('');
    setResult(null);
    setShowManualForm(false);

    try {
      const response = await clienteAPI.getByDni(dni);
      if (response.data.success) {
        setResult({
          success: true,
          message: response.data.message,
          cliente: response.data.cliente
        });
      } else {
        setResult({
          success: false,
          message: response.data.message
        });
        setShowManualForm(true);
      }
    } catch (error) {
      console.error('Error searching DNI:', error);
      setError('Error al consultar DNI');
    } finally {
      setLoading(false);
    }
  };

  const handleManualSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const clienteData = {
        dni,
        ...manualData
      };
      
      const response = await clienteAPI.createManual(clienteData);
      if (response.data.success) {
        setResult({
          success: true,
          message: 'Cliente registrado manualmente',
          cliente: response.data.cliente
        });
        setShowManualForm(false);
      }
    } catch (error) {
      console.error('Error creating manual cliente:', error);
      setError('Error al registrar cliente manualmente');
    } finally {
      setLoading(false);
    }
  };

  const handleManualChange = (e) => {
    setManualData({
      ...manualData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-medium text-gray-900">
            Buscar Cliente por DNI
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

        {/* DNI Search */}
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">
              DNI
            </label>
            <div className="mt-1 flex space-x-2">
              <input
                type="text"
                maxLength="8"
                pattern="[0-9]{8}"
                className="input-field flex-1"
                value={dni}
                onChange={(e) => setDni(e.target.value)}
                placeholder="12345678"
              />
              <button
                onClick={handleSearch}
                disabled={loading}
                className="btn-primary flex items-center"
              >
                <MagnifyingGlassIcon className="h-5 w-5 mr-1" />
                {loading ? '...' : 'Buscar'}
              </button>
            </div>
          </div>

          {/* Result */}
          {result && (
            <div className={`p-4 rounded-lg ${
              result.success ? 'bg-green-50 border border-green-200' : 'bg-yellow-50 border border-yellow-200'
            }`}>
              <p className={`text-sm ${
                result.success ? 'text-green-700' : 'text-yellow-700'
              }`}>
                {result.message}
              </p>
              
              {result.cliente && (
                <div className="mt-2 text-sm text-gray-600">
                  <p><strong>Nombre:</strong> {result.cliente.nombres} {result.cliente.apellidoPaterno} {result.cliente.apellidoMaterno}</p>
                  <p><strong>DNI:</strong> {result.cliente.dni}</p>
                  <p><strong>Fuente:</strong> {result.cliente.fuenteDatos}</p>
                  {result.cliente.telefono && (
                    <p><strong>Teléfono:</strong> {result.cliente.telefono}</p>
                  )}
                </div>
              )}
            </div>
          )}

          {/* Manual Form */}
          {showManualForm && (
            <div className="border-t pt-4">
              <h4 className="text-md font-medium text-gray-900 mb-3">
                Registro Manual
              </h4>
              <form onSubmit={handleManualSubmit} className="space-y-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Nombres *
                  </label>
                  <input
                    type="text"
                    name="nombres"
                    required
                    className="input-field mt-1"
                    value={manualData.nombres}
                    onChange={handleManualChange}
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
                    value={manualData.apellidoPaterno}
                    onChange={handleManualChange}
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
                    value={manualData.apellidoMaterno}
                    onChange={handleManualChange}
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
                    value={manualData.telefono}
                    onChange={handleManualChange}
                    placeholder="987654321"
                  />
                </div>

                <button
                  type="submit"
                  disabled={loading}
                  className="w-full btn-primary"
                >
                  {loading ? 'Registrando...' : 'Registrar Cliente'}
                </button>
              </form>
            </div>
          )}

          <div className="flex justify-end space-x-3 pt-4">
            <button
              onClick={onClose}
              className="btn-secondary"
            >
              Cerrar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ClienteDniModal;