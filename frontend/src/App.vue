<script setup>
import { computed, watchEffect } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Sidebar from './components/Sidebar.vue'
import { isAuthenticated } from './store/auth'

const router = useRouter()
const route = useRoute()
const isAuth = computed(() => isAuthenticated.value && route.path !== '/login')

watchEffect(() => {
  isAuth.value
})

function logout() {
  localStorage.removeItem('auth')
  isAuthenticated.value = false
  router.push('/login')
}
</script>

<template>
  <div>
    <Sidebar v-if="isAuth" @logout="logout" />
    <main>
      <router-view />
    </main>
  </div>
</template>

<style>
.app-layout {
  display: flex;
  min-height: 100vh;
}
.sidebar {
  width: 200px;
  background: #2c3e50;
  color: #fff;
  padding: 2rem 1rem;
  min-height: 100vh;
}
.sidebar ul {
  list-style: none;
  padding: 0;
}
.sidebar li {
  margin-bottom: 1.5rem;
}
.sidebar a, .sidebar .router-link-active {
  color: #fff;
  text-decoration: none;
  font-weight: bold;
}
.sidebar a:hover {
  text-decoration: underline;
}
main.with-sidebar {
  flex: 1;
  padding: 2rem;
  background: #f5f6fa;
}
main {
  flex: 1;
  padding-top: 1.5rem;
}
</style>