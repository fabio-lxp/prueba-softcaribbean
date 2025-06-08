import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import PacienteTable from '../../src/components/PacienteTable.vue'

describe('PacienteTable.vue', () => {
    const pacientesMock = [
        {
            id: 1,
            nombreMascota: 'Firulais',
            especie: 'Perro',
            raza: 'Labrador',
            nombreDueno: 'Juan'
        }
    ]

    it('Muestra los pacientes', () => {
        const wrapper = mount(PacienteTable, {
            props: { pacientes: pacientesMock }
        })

        expect(wrapper.text()).toContain('Firulais')
        expect(wrapper.text()).toContain('Perro')
        expect(wrapper.text()).toContain('Juan')
    })

    it('Muestra mensaje si no hay pacientes', () => {
        const wrapper = mount(PacienteTable, {
            props: { pacientes: [] }
        })

        expect(wrapper.text()).toContain('No hay pacientes registrados.')
    })

    it('cuando se hace clic en el botón de editar,abre el modal para editar', async () => {
        const wrapper = mount(PacienteTable, {
            props: { pacientes: pacientesMock }
        })

        const btnEditar = wrapper.find('button.btn-outline-primary')
        await btnEditar.trigger('click')

        expect(wrapper.emitted().edit).toBeTruthy()
        expect(wrapper.emitted().edit[0][0].id).toBe(1)
    })

    it('cuando se hace clic en el botón de eliminar, elimina(redundancia) el registro', async () => {
        const wrapper = mount(PacienteTable, {
            props: { pacientes: pacientesMock }
        })

        const btnEliminar = wrapper.find('button.btn-outline-danger')
        await btnEliminar.trigger('click')

        expect(wrapper.emitted().delete).toBeTruthy()
        expect(wrapper.emitted().delete[0][0].id).toBe(1)
    })
})
