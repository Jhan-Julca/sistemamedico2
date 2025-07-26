import axios from 'axios';

const API_BASE_URL = 'http://localhost:8010/api';

// Crear instancia de axios
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para agregar token a las requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar respuestas y errores
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (credentials) => api.post('/auth/login', credentials),
  validateToken: () => api.post('/auth/validate'),
  refreshToken: () => api.post('/auth/refresh'),
};

// Cliente API
export const clienteAPI = {
  getAll: () => api.get('/cliente'),
  getById: (id) => api.get(`/cliente/${id}`),
  getByDni: (dni) => api.get(`/cliente/dni/${dni}`),
  getByDniQuery: (dni) => api.get(`/cliente/dni-query?dni=${dni}`),
  create: (cliente) => api.post('/cliente', cliente),
  createManual: (cliente) => api.post('/cliente/manual', cliente),
  update: (id, cliente) => api.put(`/cliente/${id}`, cliente),
  delete: (id) => api.delete(`/cliente/${id}`),
  search: (termino) => api.get(`/cliente/buscar?termino=${termino}`),
  getCacheStats: () => api.get('/cliente/cache/stats'),
  clearCache: () => api.delete('/cliente/cache'),
};

// Producto API
export const productoAPI = {
  getAll: () => api.get('/producto'),
  getById: (id) => api.get(`/producto/${id}`),
  create: (producto) => api.post('/producto', producto),
  update: (id, producto) => api.put(`/producto/${id}`, producto),
  delete: (id) => api.delete(`/producto/${id}`),
  searchByName: (nombre) => api.get(`/producto/buscar/nombre?nombre=${nombre}`),
  searchByCategory: (categoria) => api.get(`/producto/buscar/categoria?categoria=${categoria}`),
};

// Venta API
export const ventaAPI = {
  getAll: () => api.get('/venta'),
  getById: (id) => api.get(`/venta/${id}`),
  create: (venta) => api.post('/venta', venta),
  update: (id, venta) => api.put(`/venta/${id}`, venta),
  delete: (id) => api.delete(`/venta/${id}`),
};

// VentaDetalle API
export const ventaDetalleAPI = {
  getAll: () => api.get('/ventadetalle'),
  getById: (id) => api.get(`/ventadetalle/${id}`),
  getByVenta: (idventa) => api.get(`/ventadetalle/detalle/${idventa}`),
  create: (detalle) => api.post('/ventadetalle', detalle),
  delete: (id) => api.delete(`/ventadetalle/${id}`),
};

// Usuario API
export const usuarioAPI = {
  getAll: () => api.get('/usuario'),
  getById: (id) => api.get(`/usuario/${id}`),
  create: (usuario) => api.post('/usuario', usuario),
  update: (id, usuario) => api.put(`/usuario/${id}`, usuario),
  delete: (id) => api.delete(`/usuario/${id}`),
  activate: (id) => api.put(`/usuario/${id}/activar`),
  deactivate: (id) => api.put(`/usuario/${id}/desactivar`),
  changePassword: (id, password) => api.put(`/usuario/${id}/password`, { password }),
  checkEmail: (email) => api.get(`/usuario/verificar-email?email=${email}`),
  getStats: () => api.get('/usuario/estadisticas'),
};

// Sede API
export const sedeAPI = {
  getAll: () => api.get('/sede'),
  getById: (id) => api.get(`/sede/${id}`),
  create: (sede) => api.post('/sede', sede),
  delete: (id) => api.delete(`/sede/${id}`),
};

// Empresa API
export const empresaAPI = {
  getByRuc: (ruc) => api.get(`/empresa/ruc/${ruc}`),
};

export default api;