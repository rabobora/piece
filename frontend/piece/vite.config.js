import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      eventsource:
      "./node_modules/sockjs-client/lib/transport/browser/eventsource.js",
      events: "./node_modules/sockjs-client/lib/event/emitter.js",
      crypto: "./node_modules/sockjs-client/lib/utils/browser-crypto.js",
    }
  }
})
