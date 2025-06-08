import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
})

// Interceptor para agregar Authorization a cada request
api.interceptors.request.use(config => {
    const token = localStorage.getItem('auth')
    if (token) {
        config.headers['Authorization'] = `Basic ${token}`
    }
    return config
})

// Funciones CRUD pacientes
export const getPacientes = () => api.get('/pacientes')
export const createPaciente = data => api.post('/pacientes', data)
export const updatePaciente = (id, data) => api.put(`/pacientes/${id}`, data)
export const deletePaciente = id => api.delete(`/pacientes/${id}`)
export const exportPacientes = () => api.get('/pacientes/export', { responseType: 'blob' })
export const importPacientes = file => {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/pacientes/import', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    })
} 