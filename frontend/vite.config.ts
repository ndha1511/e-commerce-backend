import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  optimizeDeps: {

    include: [
      '@emotion/react', 
      '@emotion/styled', 
      '@mui/material/Tooltip'
    ],
    exclude: ['js-big-decimal']
  },
  plugins: [
    react({
    }),
  ],
  define: {
    global: 'window'
  },
})
