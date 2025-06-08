import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'url'

// ✅ Configuración completa con cobertura
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  test: {
    globals: true,
    environment: 'jsdom',
    coverage: {
      reporter: ['text', 'html'],
      all: true,
      include: ['src/**/*.{js,vue}'], // Archivos que se incluirán en cobertura
      exclude: ['**/main.js', '**/router/**', '**/store/**']
    }
  }
})
