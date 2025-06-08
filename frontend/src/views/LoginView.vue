<template>
  <div class="login-bg d-flex justify-content-center align-items-center min-vh-100">
    <div class="login-card card p-4 shadow-lg border-0" style="max-width: 400px; width: 100%">
      <div class="text-center mb-3">
        <img src="../assets/logo_login.png" alt="Logo veterinaria" class="login-logo mb-2" />
      </div>
      <h2 class="text-center mb-4 fw-bold text-vet">Iniciar Sesión</h2>
      <form @submit.prevent="login">
        <div class="mb-3">
          <label for="username" class="form-label">Usuario:</label>
          <input id="username" v-model="username" type="text" class="form-control input-vet" required />
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Contraseña:</label>
          <input id="password" v-model="password" type="password" class="form-control input-vet" required />
        </div>
        <button type="submit" class="btn btn-vet w-100">Entrar</button>
      </form>
    </div>
    <!-- Huellas decorativas -->
    <div class="paw paw1"></div>
    <div class="paw paw2"></div>
    <div class="paw paw3"></div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Swal from 'sweetalert2'
import { isAuthenticated } from '../store/auth'

const username = ref('')
const password = ref('')
const router = useRouter()

async function login() {
  const basicToken = btoa(`${username.value}:${password.value}`)
  try {
    await axios.get('http://localhost:8080/api/pacientes', {
      headers: { Authorization: `Basic ${basicToken}` }
    })
    localStorage.setItem('auth', basicToken)
    isAuthenticated.value = true
    router.push('/')
  } catch (e) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'Credenciales incorrectas',
      confirmButtonColor: '#d33'
    })
  }
}
</script>

<style scoped>
.login-bg {
  background: linear-gradient(135deg, #e0f7fa 0%, #b2dfdb 100%);
  min-height: 100vh;
  width: 100vw;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: auto;
  z-index: 0;
}
.login-card {
  border-radius: 24px;
  background: #fff;
  position: relative;
  z-index: 2;
}
.text-vet {
  color: #4F8A8B;
  letter-spacing: 1px;
}
.input-vet:focus {
  border-color: #4F8A8B;
  box-shadow: 0 0 0 0.2rem rgba(79, 138, 139, 0.15);
  transition: box-shadow 0.2s;
}
.btn-vet {
  background: linear-gradient(90deg, #4F8A8B 60%, #43c6ac 100%);
  color: #fff;
  font-weight: 600;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  box-shadow: 0 2px 8px rgba(79, 138, 139, 0.08);
  transition: background 0.2s;
}
.btn-vet:hover {
  background: linear-gradient(90deg, #43c6ac 60%, #4F8A8B 100%);
}
.paw {
  position: absolute;
  width: 48px;
  height: 48px;
  background: url('data:image/svg+xml;utf8,<svg width="48" height="48" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><ellipse cx="24" cy="36" rx="12" ry="8" fill="%234F8A8B"/><ellipse cx="12" cy="20" rx="4" ry="6" fill="%234F8A8B"/><ellipse cx="36" cy="20" rx="4" ry="6" fill="%234F8A8B"/><ellipse cx="18" cy="10" rx="3" ry="4" fill="%234F8A8B"/><ellipse cx="30" cy="10" rx="3" ry="4" fill="%234F8A8B"/></svg>') no-repeat center/contain;
  opacity: 0.12;
  z-index: 1;
}
.paw1 { left: 5%; top: 10%; transform: rotate(-15deg); }
.paw2 { right: 8%; bottom: 12%; transform: rotate(10deg); }
.paw3 { left: 50%; bottom: 5%; transform: rotate(-8deg); }
@media (max-width: 600px) {
  .login-card { padding: 1.5rem 0.5rem; }
  .paw { width: 32px; height: 32px; }
}
.login-logo {
  width: 90px;
  height: 90px;
  object-fit: contain;
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(79, 138, 139, 0.10);
  background: #fff;
  border: 2px solid #4F8A8B;
}
</style> 