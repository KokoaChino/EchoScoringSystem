<template>
    <Template>
        <table>
            <tr>
                <td class="sub-echo">角色名称</td>
                <td style="width: 67%">
                    <el-cascader
                        v-model="selectedValues"
                        :options="options"
                        collapse-tags
                        collapse-tags-tooltip
                        :show-all-levels="false"
                        style="width: 100%"
                        placeholder="筛选角色"
                        size="large"
                    />
                </td>
                <td style="width: 30%;padding: 0 20px;">
                    <div style="display: flex; justify-content: space-between; align-items: center; width: 100%;">
                        <span>是否精确到 0.01：</span>
                        <el-switch
                            size="large"
                            v-model="is_one_percent"
                            inline-prompt
                            active-text="是"
                            inactive-text="否"
                        />
                    </div>
                </td>
                <td style="width: 10%">
                    <el-button size="large" type="info" plain @click="re_fill">重置权重</el-button>
                </td>
                <td style="width: 10%">
                    <el-button size="large" type="primary" plain @click="re_weights">恢复默认</el-button>
                </td>
                <td style="width: 10%">
                    <el-button size="large" type="success" plain @click="set_weights">提交更改</el-button>
                </td>
            </tr>
            <tr>
                <td class="sub-echo" style="width: 15%;">副词条名称</td>
                <td class="sub-echo" colspan="5" style="width: 85%;">权重</td>
            </tr>
            <tr v-for="(key, i) in keys" :key="i">
                <td :style="set_style3(key)">{{ key }}</td>
                <td colspan="5" :style="set_style3(key)">
                    <div class="slider-demo-block">
                        <el-slider v-model="weights[key]" show-input size="large" :max="100"
                                   :step="is_one_percent ? 0.01 : 1"/>
                    </div>
                </td>
            </tr>
        </table>
    </Template>
</template>



<script setup>
import Template from "@/components/layout/Template.vue";
import { ref, onMounted, watch } from "vue";
import { get, post, POST } from "@/net/index.js";
import { useStore } from "@/stores/index.js";
import {ElLoading, ElMessage} from "element-plus";


const store = useStore()
const keys = ref([])
const map = ref({})
const weights = ref({})
const name = ref('')
const is_one_percent = ref(false)
const options = ref([]), selectedValues = ref([])

watch(selectedValues, (newVal) => {
    name.value = newVal[1]
})

watch(name, async (newVal) => {
    weights.value = await POST("/api/echo/get-weights", {
        name: newVal,
        username: store.auth.user.username,
    })
});

watch(is_one_percent, async (newVal) => {
    weights.value = await POST("/api/echo/get-weights", {
        name: name.value,
        username: store.auth.user.username,
    })
    if (!newVal) {
        for (let key of keys.value) {
            weights.value[key] = Math.round(weights.value[key])
        }
    }
});

function set_color(key) {
    let color, w = weights.value[key]
    if (w >= 75) {
        color = "red"
    } else if (w >= 40) {
        color = "orange"
    } else if (w >= 20) {
        color = "blue"
    } else if (w >= 10) {
        color = "green"
    } else {
        color = "gray"
    }
    return `color: ${ color }`
}

const set_weights = async () => {
    await POST("/api/echo/set-weights", {
        name: name.value,
        username: store.auth.user.username,
        json: JSON.stringify(weights.value)
    })
    await post("/api/echo/re-data", store.auth.user.username)
    ElMessage.success("权重更新成功！")
}

const re_weights = async () => {
    await POST("/api/echo/re-weights", {
        name: name.value,
        username: store.auth.user.username
    })
    weights.value = await POST("/api/echo/get-weights", {
        name: name.value,
        username: store.auth.user.username
    })
    if (!is_one_percent.value) {
        for (let key of keys.value) {
            weights.value[key] = Math.round(weights.value[key])
        }
    }
}

const re_fill = () => {
    for (let key of keys.value) {
        weights.value[key] = 0
    }
}

const set_style3 = (key) => {
    let res = "text-align: center;"
    res += set_color(key)
    return res
}


onMounted(async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
        options.value = await store.get_options()
        keys.value = await get("/api/echo/get-echo-keys")
        for (let key of keys.value) {
            map.value[key] = []
            weights.value[key] = 0
        }
        if (store.echo.index === -3) {
            name.value = store.echo.name
            let characters = await post("/api/echo/get-characters", store.auth.user.username)
            selectedValues.value = [characters[name.value]['type'], name.value]
        }
        map.value = await get("/api/echo/get-echo-values")
        keys.value.forEach(key => {
            map.value[key].unshift(0);
            map.value[key].sort((a, b) => a - b);
        })
        weights.value = await POST("/api/echo/get-weights", {
            name: name.value,
            username: store.auth.user.username,
        })
    } catch (e) {
        console.error("加载数据失败:", e);
    } finally {
        loading.close()
    }
})
</script>



<style scoped>
.slider-demo-block {
    max-width: 100%;
}

table {
    zoom: 0.9;
    width: 100%;
    border-collapse: collapse;
}
table td {
    padding: 10px;
    border: 1px solid #ccc;
    text-align: center;
    height: 40px;
}
table .sub-echo {
    background-color: #f4f4f4;
    color: #303133;
    font-size: 20px;
    font-weight: normal;
}
</style>