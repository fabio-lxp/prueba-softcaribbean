import { ref } from 'vue'
let initialAuth = false
try {
    initialAuth = !!localStorage.getItem('auth')
} catch (e) {
    initialAuth = false
}
export const isAuthenticated = ref(initialAuth) 