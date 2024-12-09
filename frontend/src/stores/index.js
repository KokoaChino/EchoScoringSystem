import { reactive } from 'vue'
import { defineStore } from 'pinia'
import piniaPersistedState from 'pinia-plugin-persistedstate';

export const useStore = defineStore('store', () => {
    const auth = reactive({
        user: null
    })
    const echo = reactive({
        name: null,
        index: -1
    })
    return { auth, echo }
}, {
    persist: true // 持久化
})

export function setupPersistedStore(pinia) {
    pinia.use(piniaPersistedState);
}

