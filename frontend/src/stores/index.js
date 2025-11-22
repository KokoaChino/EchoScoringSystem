import { reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import piniaPersistedState from 'pinia-plugin-persistedstate';
import { get } from "@/net/index.js";

export const useStore = defineStore('store', () => {
    const auth = reactive({
        token: null,
        user: null,
        verificationStatus: false,
    });
    const echo = reactive({
        name: null,
        index: -1
    })
    const options = ref(null)

    const logout = () => {
        auth.token = null;
        auth.user = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    };
    const get_options = async () => {
        if (options.value === null) { // 单例模式
            let g = await get("/api/echo/get-character-groups"), res = []
            for (let key of Object.keys(g)) {
                let item = {value: key, label: key, children: []}
                for (let name of g[key]) {
                    let sub_item = {value: name, label: name}
                    item.children.push(sub_item)
                }
                res.push(item)
            }
            options.value = res
        }
        return options.value
    }
    return { auth, echo, get_options, logout };
}, {
    persist: {
        key: 'store',
        storage: localStorage,
    }
})

export function setupPersistedStore(pinia) {
    pinia.use(piniaPersistedState);
}