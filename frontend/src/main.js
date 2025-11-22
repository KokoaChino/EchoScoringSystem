/*
 * Copyright (c) 2024-2025 KokoaChino
 * Released under the MIT License
 */

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import 'element-plus/dist/index.css'
import axios from "axios";
import { setupPersistedStore, useStore } from './stores/index.js';
import { ElMessage } from "element-plus";

axios.defaults.baseURL = 'http://localhost:4000'

const pinia = createPinia();
setupPersistedStore(pinia);

const app = createApp(App)
app.use(pinia);
axios.interceptors.request.use(config => {
    const store = useStore();
    if (store.auth.token) {
        config.headers.Authorization = `Bearer ${store.auth.token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401) {
            const store = useStore();
            store.auth.token = null;
            store.auth.user = null;
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            ElMessage.error('登录失效，请重新登录');
            router.push('/');
        }
        return Promise.reject(error);
    }
);
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

