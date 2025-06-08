import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Sidebar from '../../src/components/Sidebar.vue'

describe('Sidebar.vue', () => {
    it('se renderiza correctamente con logo y nombre', () => {
        const wrapper = mount(Sidebar)
        expect(wrapper.text()).toContain('Veterinaria XYZ')
        expect(wrapper.find('img.nav-logo').exists()).toBe(true)
    })

    it('emite evento "logout" al hacer clic en el botón de cerrar sesión', async () => {
        const wrapper = mount(Sidebar)
        const btn = wrapper.find('button.btn-logout')
        await btn.trigger('click')

        expect(wrapper.emitted().logout).toBeTruthy()
    })
})
