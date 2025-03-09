<template>
    <Template>
        <div class="content">
            <div class="item1">
                <table>
                    <thead>
                    <tr>
                        <th style="border-left: none;">副词条</th>
                        <th style="border-right: none;">数值列表</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(key, i) in keys" :key="i">
                        <td style="text-align: center;">{{ key }}</td>
                        <td style="border-right: none;padding-left: 10px">
                            <el-radio-group style="gap: 10px" v-model="radios[i]" size="large">
                                <div v-for="x in map[key]" :key="x">
                                    <el-radio-button :label="x" :value="x" @click="add_list(key, x)"/>
                                </div>
                            </el-radio-group>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="item2">
                <div>
                    <table style="width: 100%;">
                        <thead>
                        <tr>
                            <th style="border-left: none;width: 30%;">角色列表</th>
                            <th style="border-right: none;width: 70%">
                                <el-cascader
                                    :options="name_options"
                                    :props="props"
                                    v-model="selectedValues"
                                    collapse-tags
                                    collapse-tags-tooltip
                                    :max-collapse-tags="2"
                                    :show-all-levels="false"
                                    @change="handleChange"
                                    style="width: 100%"
                                    placeholder="筛选角色"
                                    size="large"
                                />
                            </th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div style="width: 100%; display: flex;flex-direction: column; height: 100%;position: relative;">
                    <table style="border-bottom: none;">
                        <thead style="border: 2px solid #ccc;">
                        <tr>
                            <th style="border-left: none;width: 70%">
                                <el-select v-model="main" placeholder="Select" size="large" style="width: 100%;">
                                    <el-option v-for="item in mains[cost]" :key="item.value"
                                               :label="item.label" :value="item.value"/>
                                </el-select>
                            </th>
                            <th style="border-right: none;width: 30%">
                                <el-select v-model="cost" placeholder="Select" size="large" style="width: 100%;">
                                    <el-option v-for="item in costs" :key="item.value"
                                               :label="item.label" :value="item.value"/>
                                </el-select>
                            </th>
                        </tr>
                        </thead>
                        <tbody class="echo">
                        <tr v-for="(key, i) in list" :key="i">
                            <td>
                                <div>{{ key[0] }}</div>
                            </td>
                            <td>
                                <div>{{ key[0].includes('固定') || key[0] === '' ? key[1] : key[1] + '%' }}</div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="buttons">
                        <div style="flex: 1;display: flex;justify-content: center;align-items: center;">
                            <el-button class="bt" type="danger" plain @click="refill">重置</el-button>
                        </div>
                        <div style="flex: 1;display: flex;justify-content: center;align-items: center;">
                            <el-button v-if="store.echo.name" class="bt" type="success" plain @click="edit_echo">修改</el-button>
                            <el-button v-else class="bt" type="success" plain @click="add_temp_echo">添加</el-button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </Template>
</template>



<script setup>
import Template from "@/components/layout/Template.vue";
import { onMounted, ref, watch } from "vue";
import { get, POST, post } from "@/net/index.js";
import {ElLoading, ElMessage} from "element-plus";
import { useStore } from "@/stores/index.js";
import { onBeforeRouteLeave } from 'vue-router';

const store = useStore()

const keys = ref([])
const map = ref({})
const name_options = ref([]), name_list = ref([])
const props = { multiple: true }
const costs = [
    { value: 1, label: 1 },
    { value: 3, label: 3 },
    { value: 4, label: 4 },
], cost = ref(4)
const mains = {
    1: [
        { value: '百分比攻击 18%', label: '百分比攻击 18%' },
        { value: '百分比生命 22.8%', label: '百分比生命 22.8%' },
        { value: '百分比防御 18%', label: '百分比防御 18%' },
    ],
    3: [
        { value: '属性伤害加成 30%', label: '属性伤害加成 30%' },
        { value: '共鸣效率 32%', label: '共鸣效率 32%' },
        { value: '百分比攻击 30%', label: '百分比攻击 30%' },
        { value: '百分比生命 30%', label: '百分比生命 30%' },
        { value: '百分比防御 38%', label: '百分比防御 38%' },
    ],
    4: [
        { value: '暴击率 22%', label: '暴击率 22%' },
        { value: '暴击伤害 44%', label: '暴击伤害 44%' },
        { value: '治疗效果加成 26.4%', label: '治疗效果加成 26.4%' },
        { value: '百分比攻击 33%', label: '百分比攻击 33%' },
        { value: '百分比生命 33%', label: '百分比生命 33%' },
        { value: '百分比防御 41.8%', label: '百分比防御 41.8%' },
    ]
}, main = ref('暴击率 22%')
const radios = ref([]), selectedValues = ref([])
const list = ref([['', ''], ['', ''], ['', ''], ['', ''], ['', '']]), len = ref(0)

const handleChange = async (value) => {
    name_list.value = await value.map((path) => path[path.length - 1]);
};

watch(cost, (newVal) => {
    main.value = mains[newVal][0].value
});

const add_list = (key, x) => {
    for (let i = 0; i < 5; i++) {
        if (list.value[i][0] === key) {
            if (x === 0) {
                list.value[i] = ['', '']
                len.value--
            } else list.value[i][1] = x
            sort_list()
            return
        }
    }
    if (x === 0) {
        sort_list()
        return;
    }
    if (len.value === 5) {
        ElMessage.warning("声骸副词条个数不合法！");
        let index = keys.value.indexOf(key)
        setTimeout(() => radios.value[index] = 0, 250);
    } else {
        list.value[len.value++] = [key, x]
    }
    sort_list()
}

function refill() {
    radios.value.fill(0)
    list.value = [['', ''], ['', ''], ['', ''], ['', ''], ['', '']]
    len.value = 0
}

const edit_echo = async () => {
    if (len.value < 5) {
        ElMessage.warning("声骸副词条个数不合法！");
        return
    }
    if (name_list.value.length === 0) {
        ElMessage.warning("角色列表为空！");
        return;
    }
    await POST("echo-scoring-system/del-temp-echo", {
        username: store.auth.user.username,
        json: store.echo.name
    })
    let data = JSON.parse(store.echo.name)
    for (let i = 0; i < 5; i++) {
        data['声骸']['echo'][i][0] = list.value[i][0]
        data['声骸']['echo'][i][1] = list.value[i][1]
    }
    data['声骸']['main'] = main.value
    data['声骸']['cost'] = cost.value
    await POST("echo-scoring-system/add-temp-echo", {
        username: store.auth.user.username,
        echo: JSON.stringify(data['声骸']),
        name_list: JSON.stringify(name_list.value)
    })
    ElMessage.success("修改成功！");
    store.echo.name = null
    store.echo.index = -1
}

function sort_list() {
    for (let i = 0; i < 5; i++) {
        if (list.value[i][0]) continue
        for (let j = i + 1; j < 5; j++) {
            if (list.value[j][0]) {
                let tmp = list.value[j]
                list.value[j] = list.value[i]
                list.value[i] = tmp
            }
        }
    }
    len.value = 5
    for (let i = 0; i < 5; i++) {
        if (list.value[i][0] === '') {
            len.value = i
            return
        }
    }
}

async function add_temp_echo() {
    if (len.value < 5) {
        ElMessage.warning("声骸副词条个数不合法！");
        return
    }
    if (name_list.value.length === 0) {
        ElMessage.warning("角色列表为空！");
        return;
    }
    let echo = {
        main: main.value,
        cost: cost.value,
        score: 0,
        echo: []
    }
    for (let item of list.value) {
        item.push('')
        echo['echo'].push(item)
    }
    await POST("/echo-scoring-system/add-temp-echo", {
        username: store.auth.user.username,
        echo: JSON.stringify(echo),
        name_list: JSON.stringify(name_list.value)
    })
    refill()
}

onBeforeRouteLeave((to, from, next) => {
    store.echo.name = null
    store.echo.index = -1
    next()
})

onMounted(async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
        keys.value = await get("/echo-scoring-system/get-echo-keys")
        radios.value = new Array(keys.value.length).fill(0)
        map.value = await get("/echo-scoring-system/get-echo-values")
        for (let name of await get("echo-scoring-system/get-names")) {
            name_options.value.push({ value: name, label: name })
        }
        keys.value.forEach(key => {
            map.value[key].unshift(0);
            map.value[key].sort((a, b) => a - b);
        })
        if (store.echo.index === -2) {
            let e = JSON.parse(store.echo.name);
            for (let key of Object.keys(e)) {
                if (key !== '声骸') name_list.value.push(key)
            }
            let characters = await post("/echo-scoring-system/get-characters", store.auth.user.username)
            for (let name of name_list.value) {
                selectedValues.value.push([characters[name]['type'], name])
            }
            cost.value = e['声骸']['cost']
            main.value = e['声骸']['main']
            let echo = e['声骸']['echo']
            for (let i = 0; i < 5; i++) {
                list.value[i] = [echo[i][0], echo[i][1]]
                radios.value[keys.value.indexOf(echo[i][0])] = Number(echo[i][1])
            }
            len.value = 5
        }
        name_options.value = await store.get_options()
    } catch (e) {
        console.error("加载数据失败:", e);
    } finally {
        loading.close()
    }
})
</script>



<style scoped>
.content {
    width: 95%;
    margin: auto;
    display: flex;
}
.content .item1 {
    flex: 9;
    border: 2px solid #ccc;
    border-right: none;
    box-sizing: border-box;
}
.content .item2 {
    flex: 5;
    border: 2px solid #ccc;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
}

.content .item1 table {
    width: 100%;
    border-collapse: collapse;
}
.content .item1 td {
    padding: 9px;
    border: 1px solid #ccc;
    border-bottom: none;
    border-left: none;
}
.content th {
    padding: 10px;
    border: 2px solid #ccc;
    border-top: none;
    height: 50px;
    max-height: 50px;
    background-color: rgb(250, 250, 250);
    font-size: 23px;
    font-weight: normal;
}
.content .item2 table {
    width: 70%;
    margin: auto;
    align-items: center;
    border-collapse: collapse;
}
.content .item2 td {
    padding: 10px 20px;
    height: 40px;
    border: 1px solid #ccc;
    color: #303133;
}

.buttons {
    height: 100px;
    width: 70%;
    margin: auto;
    position: absolute;
    left: 15%;
    top: 74.5%;
    display: flex;
}
.bt {
    width: 50%;
    height: 50%;
}

.echo {
    border: 2px solid #ccc;
}
.echo td {
    line-height: 30px;
    text-align: center;
}
</style>