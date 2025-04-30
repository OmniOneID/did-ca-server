import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    host: '0.0.0.0',
    proxy: {
      // /cas/admin/v1 → http://localhost:8094/cas/admin/v1
      '/cas/admin/v1': {
        target: 'http://localhost:8094',
        changeOrigin: true,
      },
    },
  },
});
