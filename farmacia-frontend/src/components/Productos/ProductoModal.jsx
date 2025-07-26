import React, { useState, useEffect } from 'react';
import { productoAPI } from '../../services/api';
import { XMarkIcon } from '@heroicons/react/24/outline';

const ProductoModal = ({ producto, onClose }) => {
  const [formData, setFormData] = useState({
    nombre: '',
    precio: '',
    cantidad: '',
    fechaVencimiento: '',
    descripcion: '',
    categoria: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const categorias = [
    'Analgésicos',
    'Antiinflamatorios',
    'Antibióticos',
    'Gastroenterología',
    'Antialérgicos',
    'Pediátricos',
    'Vitaminas',
    'Uso tópico',
    'Respiratorio',
    'Endocrinología',
    'Otros'
  ];

  useEffect(() => {
    if (producto) {
      setFormData({
        nombre: producto.nombre || '',
        precio: producto.precio || '',
        cantidad: producto.cantidad || '',
        fechaVencimiento: producto.fechaVencimiento || '',
        descripcion: producto.descripcion || '',
        categoria: producto.categoria || ''
      });
    }
  }, [producto]);

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
      const productData = {
        ...formData,
        precio: parseFloat(formData.precio),
        cantidad: parseInt(formData.cantidad)
      };

      if (producto) {
        await productoAPI.update(producto.id, productData);
      } else {
        await productoAPI.create(productData);
      }
      onClose();
    } catch (error) {
      console.error('Error saving producto:', error);
      setError(error.response?.data?.message || 'Error al guardar producto');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-medium text-gray-900">
            {producto ? 'Editar Producto' : 'Nuevo Producto'}
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
              Nombre *
            </label>
            <input
              type="text"
              name="nombre"
              required
              className="input-field mt-1"
              value={formData.nombre}
              onChange={handleChange}
              placeholder="Paracetamol 500mg"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Precio *
              </label>
              <input
                type="number"
                name="precio"
                required
                step="0.01"
                min="0"
                className="input-field mt-1"
                value={formData.precio}
                onChange={handleChange}
                placeholder="15.50"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">
                Stock *
              </label>
              <input
                type="number"
                name="cantidad"
                required
                min="0"
                className="input-field mt-1"
                value={formData.cantidad}
                onChange={handleChange}
                placeholder="100"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Categoría *
            </label>
            <select
              name="categoria"
              required
              className="input-field mt-1"
              value={formData.categoria}
              onChange={handleChange}
            >
              <option value="">Seleccionar categoría</option>
              {categorias.map((cat) => (
                <option key={cat} value={cat}>{cat}</option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Fecha de Vencimiento
            </label>
            <input
              type="date"
              name="fechaVencimiento"
              className="input-field mt-1"
              value={formData.fechaVencimiento}
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Descripción
            </label>
            <textarea
              name="descripcion"
              rows="3"
              className="input-field mt-1"
              value={formData.descripcion}
              onChange={handleChange}
              placeholder="Descripción del producto..."
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

export default ProductoModal;