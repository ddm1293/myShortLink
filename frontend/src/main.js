import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import store from './store'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './style.scss'
import API from '@/api'
import 'element-plus/dist/index.css'

const app = createApp(App)

app.config.globalProperties.$API = API
app.use(router)
app.use(ElementPlus)
app.use(store)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
