<template>
  <nav class="navbar navbar-expand-lg navbar-dark vet-navbar shadow-sm py-2">
    <div class="container-fluid">
      <a class="navbar-brand d-flex align-items-center gap-2" href="#">
        <img src="../assets/logo_login.png" alt="Logo veterinaria" class="nav-logo" />
        <span class="fw-bold fs-4">Veterinaria Fabio</span>
      </a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <div class="flex-grow-1"></div>
        <button class="btn btn-outline-light d-flex align-items-center btn-logout" @click="$emit('logout')">
          <i class="bi bi-box-arrow-right me-2"></i>Cerrar sesión
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup>
// No necesita lógica interna, solo emite logout
</script>

<style scoped>
.vet-navbar {
  background: linear-gradient(90deg, #4F8A8B 70%, #43c6ac 100%);
  min-height: 70px;
  border-bottom: 3px solid #e0f7fa;
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  z-index: 1000;
  box-shadow: 0 4px 24px rgba(79, 138, 139, 0.10);
}
.nav-logo {
  width: 48px;
  height: 48px;
  object-fit: contain;
  border-radius: 50%;
  background: #fff;
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(79, 138, 139, 0.10);
}
.navbar-brand span {
  color: #fff;
  letter-spacing: 1px;
  text-shadow: 0 2px 8px rgba(79, 138, 139, 0.10);
}
.btn-logout {
  border-radius: 8px;
  border: 2px solid #fff;
  font-weight: 500;
  transition: background 0.2s, color 0.2s;
  margin-right: 2vw;
}
@media (max-width: 768px) {
  .btn-logout {
    margin-right: 0;
  }
}
.btn-logout:hover {
  background: #fff;
  color: #4F8A8B;
}
.nav-link.active, .nav-link.router-link-exact-active {
  color: #43c6ac !important;
  font-weight: 600;
  background: rgba(255,255,255,0.08);
  border-radius: 6px;
}
</style> 