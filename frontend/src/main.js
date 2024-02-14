import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import store from './store'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './style.scss'
import { modules } from './api'

const app = createApp(App)

// app.config.warnHandler = () => null
app.use(router)
app.use(ElementPlus)
app.use(store)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

console.log('here')
for (const module in modules) {
    console.log("see module", module)
}

app.mount('#app')
