import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import PacienteForm from '../../src/components/PacienteForm.vue'

describe('PacienteForm.vue', () => {
    it('Se prueba el botón de al enviar datos', async () => {
        const wrapper = mount(PacienteForm, {
            props: {
                paciente: {
                    nombreMascota: 'Firulais',
                    especie: 'Perro',
                    raza: 'Labrador',
                    fechaNacimiento: '2022-01-01',
                    tipoIdentificacion: 'CC',
                    numeroIdentificacion: '123456789',
                    nombreDueno: 'Juan',
                    ciudad: 'Bogotá',
                    direccion: 'Calle 123',
                    telefono: '3001234567'
                }
            }
        })

        await wrapper.find('form').trigger('submit.prevent')

        expect(wrapper.emitted().submit).toBeTruthy()
        expect(wrapper.emitted().submit[0][0].nombreMascota).toBe('Firulais')
    })
})
