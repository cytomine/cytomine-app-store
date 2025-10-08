import { fileURLToPath, URL } from 'node:url';

import { ConfigEnv, defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueDevTools from 'vite-plugin-vue-devtools';
import { VitePluginNode } from 'vite-plugin-node';
// https://vite.dev/config/
export default defineConfig(({ mode }: ConfigEnv) => {
  // https://vite.dev/config/#using-environment-variables-in-config
  const env = loadEnv(mode, process.cwd(), '');
  return {
    plugins: [
      vue(),
      vueDevTools(),
      // VitePluginNode({ adapter: 'express', appPath: './App.vue', })
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    server: {
      host: '0.0.0.0',
      port: env.APP_PORT ? Number(env.APP_PORT) : 5173,
      proxy: {
        '/api': {
          target: process.env.VITE_API_BASE_URL || 'http://localhost:8082',
          changeOrigin: true,
        },
      },
      watch: {
        usePolling: true,
      },
    },
  };
});


