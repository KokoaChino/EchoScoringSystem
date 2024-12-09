<template>
    <router-view/>
</template>



<script setup>
import { _GET } from "@/net";
import { useStore } from "@/stores";
import router from "@/router";

const store = useStore()

if (store.auth.user == null) {
    _GET('/api/user/me', (message) => {
        store.auth.user = message
        router.push('/index')
    }, () => {
        store.auth.user = null
    })
}
</script>



<style scoped>
* {
    margin: 0;
    padding: 0;
}
</style>