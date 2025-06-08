// ✅ Mock de localStorage (antes de cualquier import)
global.localStorage = {
    getItem: vi.fn(() => 'token'),
    setItem: vi.fn(),
    removeItem: vi.fn(),
    clear: vi.fn()
}

// ✅ Mock de matchMedia (para que SweetAlert2 funcione sin errores)
global.window = global.window || {}
window.matchMedia = window.matchMedia || function () {
    return {
        matches: false,
        addEventListener: () => { },
        removeEventListener: () => { },
        addListener: () => { },
        removeListener: () => { },
        dispatchEvent: () => false,
    }
}

// ✅ Mock de Bootstrap Modal para evitar errores por modales
globalThis.__vite_ssr_import__ = globalThis.__vite_ssr_import__ || {}
globalThis.__vite_ssr_import__['bootstrap'] = {
    Modal: function () {
        return {
            show: vi.fn(),
            hide: vi.fn()
        }
    }
}

import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import PacientesView from '../../src/views/PacientesView.vue'
import Swal from 'sweetalert2'
import { createMemoryHistory, createRouter } from 'vue-router'

// ✅ Mock de la API de pacientes
vi.mock('../../src/services/api', () => ({
    getPacientes: vi.fn(),
    createPaciente: vi.fn(),
    updatePaciente: vi.fn(),
    deletePaciente: vi.fn(),
    exportPacientes: vi.fn(),
    importPacientes: vi.fn()
}))

// ✅ Mock de SweetAlert2
vi.mock('sweetalert2')

import * as api from '../../src/services/api'

const routes = [{ path: '/', component: PacientesView }]
const router = createRouter({
    history: createMemoryHistory(),
    routes
})

describe('PacientesView.vue', () => {
    beforeEach(() => {
        vi.resetAllMocks()

        // ✅ Simular el div#pacienteModal para evitar error con Bootstrap Modal
        const modalDiv = document.createElement('div')
        modalDiv.id = 'pacienteModal'
        document.body.appendChild(modalDiv)
    })

    it('renderiza el título y los botones principales', async () => {
        api.getPacientes.mockResolvedValue({ data: [] })
        const wrapper = mount(PacientesView, {
            global: { plugins: [router] }
        })
        await flushPromises()
        expect(wrapper.text()).toContain('Pacientes')
        expect(wrapper.find('button.btn-success').exists()).toBe(true)
        expect(wrapper.find('button.btn-outline-primary').exists()).toBe(true)
    })

    it('muestra pacientes en la tabla', async () => {
        api.getPacientes.mockResolvedValue({
            data: [
                { id: 1, nombreMascota: 'Firulais', especie: 'Perro', raza: 'Labrador', nombreDueno: 'Juan' }
            ]
        })
        const wrapper = mount(PacientesView, {
            global: { plugins: [router] }
        })
        await flushPromises()
        expect(wrapper.text()).toContain('Firulais')
        expect(wrapper.text()).toContain('Perro')
        expect(wrapper.text()).toContain('Juan')
    })

    it('al hacer clic en "Nuevo paciente" abre el modal', async () => {
        api.getPacientes.mockResolvedValue({ data: [] })
        const wrapper = mount(PacientesView, {
            global: { plugins: [router] }
        })
        await flushPromises()
        const btn = wrapper.find('button.btn-success')
        await btn.trigger('click')
        expect(btn.exists()).toBe(true)
    })

    it('al hacer clic en "Eliminar" llama a SweetAlert2', async () => {
        api.getPacientes.mockResolvedValue({
            data: [
                { id: 1, nombreMascota: 'Firulais', especie: 'Perro', raza: 'Labrador', nombreDueno: 'Juan' }
            ]
        })
        Swal.fire.mockResolvedValue({ isConfirmed: true })
        api.deletePaciente.mockResolvedValue({})
        const wrapper = mount(PacientesView, {
            global: { plugins: [router] }
        })
        await flushPromises()
        const btnEliminar = wrapper.find('button.btn-outline-danger')
        await btnEliminar.trigger('click')
        expect(Swal.fire).toHaveBeenCalled()
    })
})
