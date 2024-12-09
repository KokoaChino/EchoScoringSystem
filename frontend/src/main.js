import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import 'element-plus/dist/index.css'
import axios from "axios";
import { setupPersistedStore } from './stores/index.js';

axios.defaults.baseURL = 'http://localhost:8080'

const pinia = createPinia();
setupPersistedStore(pinia);

const app = createApp(App)
app.use(pinia);
app.use(router)
app.mount('#app')

router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title;
    }
    if (to.meta.icon) {
        let link = document.querySelector("link[rel='icon']");
        if (!link) {
            link = document.createElement('link');
            link.rel = 'icon';
            document.head.appendChild(link);
        }
        link.href = to.meta.icon;
    }
    next();
});