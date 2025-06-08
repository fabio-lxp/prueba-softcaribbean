<template>
  <div class="main-bg py-4">
    <div class="container main-card py-4 px-3 px-md-5">
      <div class="d-flex flex-wrap justify-content-between align-items-center mb-3 gap-2 header-pacientes">
        <div class="d-flex gap-2 flex-wrap">
          <button class="btn btn-outline-primary d-flex align-items-center btn-action" @click="exportarPacientes">
            <i class="bi bi-file-earmark-excel me-2"></i>Exportar Excel
          </button>
          <label class="btn btn-outline-secondary d-flex align-items-center btn-action mb-0">
            <i class="bi bi-upload me-2"></i>Importar Excel
            <input type="file" accept=".xlsx" @change="importarPacientes" hidden />
          </label>
          <button class="btn btn-success d-flex align-items-center btn-action" @click="abrirModalCrear">
            <i class="bi bi-plus-lg me-2"></i>Nuevo paciente
          </button>
        </div>
      </div>
      <h2 class="display-6 fw-bold mb-4 mt-2 text-center">Pacientes</h2>
      <PacienteTable :pacientes="pacientes" @edit="onEdit" @delete="onDelete" />
    </div>
    <!-- Huellas decorativas -->
    <div class="paw paw-main1"></div>
    <div class="paw paw-main2"></div>
  </div>
  <!-- Modal Crear/Editar Paciente -->
  <div class="modal fade" id="pacienteModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ modoModal === 'editar' ? 'Editar paciente' : 'Nuevo paciente' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <PacienteForm :paciente="pacienteActual" :modo="modoModal" @submit="guardarPaciente" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PacienteTable from '../components/PacienteTable.vue'
import PacienteForm from '../components/PacienteForm.vue'
import { getPacientes, createPaciente, updatePaciente, deletePaciente, exportPacientes, importPacientes } from '../services/api'
import Swal from 'sweetalert2'
import * as XLSX from 'xlsx'
let modalInstance = null
const pacientes = ref([])
const pacienteActual = ref(null)
const modoModal = ref('crear')
async function cargarPacientes() {
  const { data } = await getPacientes()
  pacientes.value = data
}
onMounted(() => {
  cargarPacientes()
  import('bootstrap').then(({ Modal }) => {
    modalInstance = new Modal(document.getElementById('pacienteModal'))
  })
})
function abrirModalCrear() {
  pacienteActual.value = null
  modoModal.value = 'crear'
  modalInstance.show()
}
function onEdit(paciente) {
  pacienteActual.value = { ...paciente }
  modoModal.value = 'editar'
  modalInstance.show()
}
async function onDelete(paciente) {
  const res = await Swal.fire({
    title: '¿Eliminar paciente?',
    text: `Esta acción no se puede deshacer.`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Sí, eliminar',
    cancelButtonText: 'Cancelar'
  })
  if (res.isConfirmed) {
    try {
      await deletePaciente(paciente.id)
      Swal.fire({ icon: 'success', title: 'Paciente eliminado', timer: 1200, showConfirmButton: false })
      cargarPacientes()
    } catch (e) {
      Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo eliminar el paciente' })
    }
  }
}
async function guardarPaciente(data) {
  try {
    if (modoModal.value === 'crear') {
      await createPaciente(data)
      Swal.fire({ icon: 'success', title: 'Paciente creado', timer: 1200, showConfirmButton: false })
    } else if (modoModal.value === 'editar') {
      await updatePaciente(pacienteActual.value.id, data)
      Swal.fire({ icon: 'success', title: 'Paciente actualizado', timer: 1200, showConfirmButton: false })
    }
    modalInstance.hide()
    cargarPacientes()
  } catch (e) {
    Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo guardar el paciente' })
  }
}
async function exportarPacientes() {
  try {
    const { data } = await exportPacientes()
    const url = window.URL.createObjectURL(new Blob([data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', 'pacientes.xlsx')
    document.body.appendChild(link)
    link.click()
    link.remove()
    Swal.fire({ icon: 'success', title: 'Exportación exitosa', timer: 1200, showConfirmButton: false })
  } catch (e) {
    Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo exportar el archivo' })
  }
}
async function importarPacientes(e) {
  const file = e.target.files[0]
  if (!file) return
  // Validar archivo Excel antes de enviar
  try {
    const data = await file.arrayBuffer()
    const workbook = XLSX.read(data, { type: 'array' })
    const sheet = workbook.Sheets[workbook.SheetNames[0]]
    const rows = XLSX.utils.sheet_to_json(sheet, { defval: '' })
    const obligatorios = [
      'Nombre Mascota', 'Especie', 'Raza', 'Fecha Nacimiento',
      'Tipo ID', 'Nro ID', 'Nombre Dueño', 'Ciudad', 'Dirección', 'Teléfono'
    ]
    const incompletas = rows.filter((row, idx) =>
      obligatorios.some(campo => !row[campo] || row[campo].toString().trim() === '')
    )
    if (incompletas.length > 0) {
      Swal.fire({
        icon: 'error',
        title: 'Archivo inválido',
        html: `Hay ${incompletas.length} fila(s) con campos obligatorios vacíos. Corrige el archivo antes de importar.`
      })
      e.target.value = ''
      return
    }
    await importPacientes(file)
    Swal.fire({ icon: 'success', title: 'Importación exitosa', timer: 1200, showConfirmButton: false })
    cargarPacientes()
  } catch (e) {
    Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo importar el archivo' })
  }
  e.target.value = '' // Limpiar input
}
</script>

<style scoped>
.main-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #e0f7fa 0%, #b2dfdb 100%);
  position: relative;
  overflow-x: hidden;
}
.main-card {
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 4px 32px rgba(79, 138, 139, 0.10);
  margin-top: 2rem;
  margin-bottom: 2rem;
  position: relative;
  z-index: 2;
}
.header-pacientes {
  border-bottom: none;
  padding-bottom: 0;
  margin-bottom: 0 !important;
}
.btn-action {
  border-radius: 8px;
  font-weight: 500;
  font-size: 1.05rem;
  box-shadow: 0 2px 8px rgba(79, 138, 139, 0.06);
  transition: background 0.2s, color 0.2s;
}
.btn-action:hover {
  background: #43c6ac;
  color: #fff;
  border-color: #43c6ac;
}
.paw {
  position: absolute;
  width: 64px;
  height: 64px;
  background: url('data:image/svg+xml;utf8,<svg width="64" height="64" viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><ellipse cx="32" cy="48" rx="16" ry="10" fill="%234F8A8B"/><ellipse cx="16" cy="28" rx="6" ry="8" fill="%234F8A8B"/><ellipse cx="48" cy="28" rx="6" ry="8" fill="%234F8A8B"/><ellipse cx="24" cy="14" rx="4" ry="6" fill="%234F8A8B"/><ellipse cx="40" cy="14" rx="4" ry="6" fill="%234F8A8B"/></svg>') no-repeat center/contain;
  opacity: 0.10;
  z-index: 1;
}
.paw-main1 { left: 2%; top: 8%; transform: rotate(-10deg); }
.paw-main2 { right: 3%; bottom: 10%; transform: rotate(12deg); }
@media (max-width: 900px) {
  .main-card { padding: 1.5rem 0.5rem; }
  .paw { width: 40px; height: 40px; }
}
@media (max-width: 600px) {
  .main-card { margin-top: 0.5rem; margin-bottom: 0.5rem; }
  .header-pacientes { padding-bottom: 0.5rem; }
}
</style> 