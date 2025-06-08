<template>
  <div>
    <table v-if="pacientes.length" class="table table-hover align-middle">
      <thead class="table-light">
        <tr>
          <th>ID</th>
          <th>Nombre Mascota</th>
          <th>Especie</th>
          <th>Raza</th>
          <th>Dueño</th>
          <th class="text-end">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="paciente in pacientes" :key="paciente.id">
          <td>{{ paciente.id }}</td>
          <td :title="paciente.nombreMascota">{{ paciente.nombreMascota.length > 18 ? paciente.nombreMascota.slice(0, 18) + '…' : paciente.nombreMascota }}</td>
          <td :title="paciente.especie">{{ paciente.especie.length > 14 ? paciente.especie.slice(0, 14) + '…' : paciente.especie }}</td>
          <td :title="paciente.raza">{{ paciente.raza.length > 14 ? paciente.raza.slice(0, 14) + '…' : paciente.raza }}</td>
          <td :title="paciente.nombreDueno">{{ paciente.nombreDueno.length > 18 ? paciente.nombreDueno.slice(0, 18) + '…' : paciente.nombreDueno }}</td>
          <td class="acciones-cell text-end">
            <div class="acciones-stack">
              <button class="btn btn-sm btn-outline-primary me-2 d-inline-flex align-items-center" @click="$emit('edit', paciente)">
                <i class="bi bi-pencil-square me-1"></i>Editar
              </button>
              <button class="btn btn-sm btn-outline-danger d-inline-flex align-items-center" @click="$emit('delete', paciente)">
                <i class="bi bi-trash me-1"></i>Eliminar
              </button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-else class="alert alert-info text-center">
      No hay pacientes registrados.
    </div>
  </div>
</template>

<script setup>
defineProps({ pacientes: Array })
</script>

<style scoped>
@media (max-width: 600px) {
  .acciones-stack {
    display: flex;
    flex-direction: column;
    align-items: stretch;
  }
  .acciones-stack .btn {
    width: 100%;
    font-size: 0.92rem;
    padding: 0.35rem 0.3rem;
    min-height: 36px;
    margin-bottom: 0.5rem;
  }
  .acciones-stack .btn:last-child {
    margin-bottom: 0;
  }
}

@media (min-width: 601px) {
  .acciones-stack {
    display: flex;
    flex-direction: row;
    gap: 0.5rem;
    justify-content: flex-end;
    align-items: center;
  }
  .acciones-stack .btn {
    width: auto;
    min-width: 90px;
    margin-bottom: 0;
  }
}
</style> 