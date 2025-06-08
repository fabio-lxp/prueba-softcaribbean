import { createRouter, createWebHistory } from 'vue-router'
import PacientesView from '../views/PacientesView.vue'
import LoginView from '../views/LoginView.vue'
import axios from 'axios'

const routes = [
    { path: '/', component: PacientesView },
    { path: '/login', component: LoginView }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach(async (to, from, next) => {
    const token = localStorage.getItem('auth')
    if (to.path !== '/login') {
        if (!token) {
            next('/login')
        } else {
            // Validar token con una petición protegida
            try {
                await axios.get('http://localhost:8080/api/pacientes', {
                    headers: { Authorization: `Basic ${token}` }
                })
                next()
            } catch (e) {
                localStorage.removeItem('auth')
                next('/login')
            }
        }
    } else if (to.path === '/login' && token) {
        // Si ya hay token, validar también
        try {
            await axios.get('http://localhost:8080/api/pacientes', {
                headers: { Authorization: `Basic ${token}` }
            })
            next('/')
        } catch (e) {
            localStorage.removeItem('auth')
            next()
        }
    } else {
        next()
    }
})

export default router 