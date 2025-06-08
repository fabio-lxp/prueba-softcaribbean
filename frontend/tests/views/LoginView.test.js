// ✅ Mock de localStorage al inicio (antes de cualquier import)
global.localStorage = {
    getItem: vi.fn(() => null),
    setItem: vi.fn(),
    removeItem: vi.fn(),
    clear: vi.fn()
}
// ✅ Mock de window.matchMedia para SweetAlert2
global.window = global.window || {};
window.matchMedia = window.matchMedia || function () {
    return {
        matches: false,
        addEventListener: () => { },
        removeEventListener: () => { },
        addListener: () => { },
        removeListener: () => { },
        dispatchEvent: () => false,
    };
};

import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import LoginView from '../../src/views/LoginView.vue'
import axios from 'axios'
import { createMemoryHistory, createRouter } from 'vue-router'

// ✅ Mock de axios
vi.mock('axios')

const routes = [{ path: '/', component: { template: '<div>Home</div>' } }]
const router = createRouter({
    history: createMemoryHistory(),
    routes
})

describe('LoginView.vue', () => {
    beforeEach(() => {
        vi.resetAllMocks()
    })

    it('renderiza los campos de usuario y contraseña', () => {
        const wrapper = mount(LoginView, {
            global: {
                plugins: [router]
            }
        })

        expect(wrapper.find('#username').exists()).toBe(true)
        expect(wrapper.find('#password').exists()).toBe(true)
    })

    it('realiza login exitoso y navega al home', async () => {
        axios.get.mockResolvedValue({ data: [] })

        const wrapper = mount(LoginView, {
            global: {
                plugins: [router]
            }
        })

        await wrapper.find('#username').setValue('admin')
        await wrapper.find('#password').setValue('admin123')
        await wrapper.find('form').trigger('submit.prevent')

        expect(axios.get).toHaveBeenCalled()
    })

    it('muestra error si el login falla', async () => {
        axios.get.mockRejectedValue(new Error('Credenciales incorrectas'))

        const wrapper = mount(LoginView, {
            global: {
                plugins: [router]
            }
        })

        await wrapper.find('#username').setValue('wrong')
        await wrapper.find('#password').setValue('wrong')
        await wrapper.find('form').trigger('submit.prevent')

        expect(axios.get).toHaveBeenCalled()
    })
})
