<template>
  <form @submit.prevent="handleSubmit">
    <div class="row g-3">
      <div class="col-md-6">
        <label class="form-label">Nombre Mascota</label>
        <input v-model="form.nombreMascota" type="text" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Especie</label>
        <input v-model="form.especie" type="text" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Raza</label>
        <input v-model="form.raza" type="text" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Fecha Nacimiento</label>
        <input v-model="form.fechaNacimiento" type="date" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Tipo Identificación</label>
        <input v-model="form.tipoIdentificacion" type="text" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Nro Identificación</label>
        <input v-model="form.numeroIdentificacion" type="text" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Nombre Dueño</label>
        <input v-model="form.nombreDueno" type="text" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Ciudad</label>
        <input v-model="form.ciudad" type="text" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Dirección</label>
        <input v-model="form.direccion" type="text" class="form-control" required />
      </div>
      <div class="col-md-6">
        <label class="form-label">Teléfono</label>
        <input v-model="form.telefono" type="text" class="form-control" required />
      </div>
    </div>
    <div class="mt-4 d-flex justify-content-end">
      <button type="submit" class="btn btn-primary">{{ modo === 'editar' ? 'Actualizar' : 'Guardar' }}</button>
    </div>
  </form>
</template>

<script setup>
import { reactive, watch, toRefs } from 'vue'
const props = defineProps({
  paciente: Object,
  modo: { type: String, default: 'crear' }
})
const emit = defineEmits(['submit'])
const form = reactive({
  nombreMascota: '', especie: '', raza: '', fechaNacimiento: '', tipoIdentificacion: '', numeroIdentificacion: '', nombreDueno: '', ciudad: '', direccion: '', telefono: ''
})
watch(() => props.paciente, (nuevo) => {
  if (nuevo) Object.assign(form, nuevo)
}, { immediate: true })
function handleSubmit() {
  emit('submit', { ...form })
}
</script> 