<template>
    <Template>
        <div class="screen">
            <div class="sub-echo">
                <span class="item" style="align-items: flex-end;">筛选</span>
                <span class="item">条件</span>
                <el-button class="btn" type="info" @click="re_check" round>重置</el-button>
            </div>
            <div class="container" >
                <div class="btn">
                    <el-button size="large" type="success" @click="router.push('/add_temp_echo')" plain>添加声骸</el-button>
                </div>
                <div class="item">
                    <div class="a">
                        Cost：
                    </div>
                    <div class="b">
                        <el-check-tag v-for="cost in Object.keys(cost_check)"
                                      :checked="cost_check[cost]" @change="cost_check[cost] = !cost_check[cost]">
                            {{cost}}
                        </el-check-tag>
                    </div>
                </div>
                <div class="item">
                    <div class="a">
                        主词条：
                    </div>
                    <div class="b">
                        <el-check-tag v-for="main in Object.keys(main_check)"
                                      :checked="main_check[main]" @change="main_check[main] = !main_check[main]">
                            {{main}}
                        </el-check-tag>
                    </div>
                </div>
                <div class="item">
                    <div class="a">
                        副词条：
                    </div>
                    <div class="b">
                        <el-check-tag v-for="echo in Object.keys(echo_check)"
                                      :checked="echo_check[echo]" @change="echo_check[echo] = !echo_check[echo]">
                            {{echo}}
                        </el-check-tag>
                    </div>
                </div>
                <div class="item">
                    <div class="a">
                        角色：
                    </div>
                    <div class="b">
                        <el-cascader
                            v-model="selectedValues"
                            :options="options"
                            :props="props"
                            collapse-tags
                            collapse-tags-tooltip
                            :max-collapse-tags="15"
                            :show-all-levels="false"
                            @change="handleChange"
                            style="width: 100%;padding-right: 15px"
                            placeholder="筛选角色"
                        />
                    </div>
                </div>
                <div class="item">
                    <div class="a">
                        分数：
                    </div>
                    <div class="b" style="display: flex">
                        <span style="flex: 1">{{range[0]}} - {{range[1]}}</span>
                        <el-slider style="flex: 18; padding-right: 25px" v-model="range" range />
                    </div>
                </div>
            </div>
        </div>
        <el-empty description="临时声骸列表为空，请先去添加声骸" v-if="Object.keys(data).length === 0"/>
        <div class="echo-list" v-else>
            <div class="item" v-for="(item, index) in data" :key="index">
                <div class="container">
                    <el-card class="sub-echo head main-echo" body-style="padding: 0;" style="width: 300px">
                        <table>
                            <tr>
                                <td class="title" style="width: 75%;border-top: 2px solid #666;border-bottom: 2px solid #666;">
                                    {{item['声骸']['main']}}
                                </td>
                                <td class="title" style="width: 25%;border-top: 2px solid #666;border-bottom: 2px solid #666;">
                                    {{'Cost ' + item['声骸']['cost']}}
                                </td>
                            </tr>
                            <tr v-for="li in item['声骸']['echo']">
                                <td>{{li[0]}}</td>
                                <td>
                                    {{ li[0].includes('固定') || li[0] === '' ? li[1] : li[1] + '%'}}
                                </td>
                            </tr>
                            <tr>
                                <td :style="set_score_color(item['声骸']['score'])" class="score" colspan="2"
                                    style="border-top: 2px solid #666;border-bottom: 2px solid #666;">
                                    <div class="fa">
                                        <div class="grid">
                                            <div class="btn" @click="edit_echo(index)">
                                                <el-button type="primary">修改声骸</el-button>
                                            </div>
                                            <div class="btn" @click="del_echo(index)">
                                                <el-button type="danger">移除声骸</el-button>
                                            </div>
                                        </div>
                                        <span>
                                            {{item['声骸']['score']}}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </el-card>
                    <el-card class="sub-echo body" v-for="name in Object.keys(item).filter(name => name !== '声骸')"
                             body-style="padding: 0" >
                        <table>
                            <tr>
                                <td class="title" style="width: 80%">{{name}}</td>
                                <td class="title" style="width: 20%;" :title="characters[name]['type']"
                                    :style="set_img(characters[name]['type'])"></td>
                            </tr>
                            <tr v-for="(li, index) in item[name]['echo']" :key="index">
                                <td>
                                    <div style="display: flex; justify-content: space-between;">
                                        <div :style="set_color(name, li[0])">{{ li[0] }}</div>
                                        <div :style="set_color(name, li[0])">
                                            {{ li[0].includes('固定') || li[0] === '' ? li[1] : li[1] + '%' }}
                                        </div>
                                    </div>
                                </td>
                                <td :style="set_color(name, li[0])">{{li[2] + '%'}}</td>
                            </tr>
                            <tr>
                                <td :style="set_score_color(item[name]['score'])" class="score" colspan="2">
                                    <div class="fa">
                                        <div class="grid">
                                            <div class="btn" @click="add_echo(name, data[index], index)">
                                                <el-button type="success">添加声骸</el-button>
                                            </div>
                                            <div class="btn" @click="del_sub_echo(name, index)">
                                                <el-button type="danger">移除声骸</el-button>
                                            </div>
                                        </div>
                                        <span>
                                            {{ item[name]['score'] }}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </el-card>
                </div>
            </div>
        </div>
    </Template>
    <div class="data-size">
        <table>
            <tr>
                <td class="head">临时声骸个数</td>
                <td class="body">{{data.length}}</td>
            </tr>
        </table>
    </div>
</template>



<script setup>
import Template from "@/components/layout/Template.vue";
import { ref, onMounted, watch } from "vue";
import router from "@/router/index.js";
import { useStore } from "@/stores/index.js";
import { post, POST } from "@/net/index.js";
import {ElLoading, ElMessage} from "element-plus";

const store = useStore(), loading = ref(false)
const name_check = ref([]), cost_check = ref({1: false, 3: false, 4: false}), main_check = ref({
    '百分比攻击': false,
    '百分比生命': false,
    '百分比防御': false,
    '属性伤害加成': false,
    '共鸣效率': false,
    '暴击率': false,
    '暴击伤害': false,
    '治疗效果加成': false
}), echo_check = ref({
    "固定攻击": false,
    "固定生命": false,
    "固定防御": false,
    "百分比攻击": false,
    "百分比生命": false,
    "百分比防御": false,
    "暴击率": false,
    "暴击伤害": false,
    "共鸣效率": false,
    "普攻伤害加成": false,
    "重击伤害加成": false,
    "共鸣技能伤害加成": false,
    "共鸣解放伤害加成": false
}), range = ref([0, 100])
const data = ref([]), weights = ref({}), characters = ref({})
const props = { multiple: true }
const options = ref([]), selectedValues = ref([])

const handleChange = async (value) => {
    name_check.value = await value.map((path) => path[path.length - 1]);
};

watch([name_check, cost_check, main_check, echo_check, range], () => {
    get_temp_data_by_screen()
}, { deep: true });

const re_check = async () => {
    name_check.value = []
    selectedValues.value = []
    for (let key of Object.keys(cost_check.value)) {
        cost_check.value[key] = false
    }
    for (let key of Object.keys(main_check.value)) {
        main_check.value[key] = false
    }
    for (let key of Object.keys(echo_check.value)) {
        echo_check.value[key] = false
    }
    range.value = [0, 100]
}

function set_color(name, key) {
    if (key === '') return ''
    let color = '', w = weights.value[name][key]
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

function set_score_color(w) {
    let color = ''
    if (w >= 80) {
        color = "red"
    } else if (w >= 70) {
        color = "orange"
    } else if (w >= 60) {
        color = "blue"
    } else if (w >= 50) {
        color = "green"
    } else {
        color = "gray"
    }
    return `color: ${ color }`
}


const get_check = () => {
    let check = {
        'name': name_check.value,
        'cost': [],
        'main': [],
        'echo': [],
        'range': range.value
    }
    for (let key of Object.keys(cost_check.value)) {
        if (cost_check.value[key]) check['cost'].push(key)
    }
    for (let key of Object.keys(main_check.value)) {
        if (main_check.value[key]) check['main'].push(key)
    }
    for (let key of Object.keys(echo_check.value)) {
        if (echo_check.value[key]) check['echo'].push(key)
    }
    return check
}

const flush_data = async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    data.value = await POST("/echo-scoring-system/get-temp-data-by-screen", {
        json: JSON.stringify(get_check()),
        username: store.auth.user.username
    })
    loading.close()
}

const del_sub_echo = async (name, index) => {
    await POST("/echo-scoring-system/del-temp-sub-echo", {
        username: store.auth.user.username,
        json: JSON.stringify(data.value[index]),
        name: name
    })
    await flush_data()
}

async function add_echo(name, d, index) {
    let data = await post("/echo-scoring-system/get-data", store.auth.user.username)
    let echo = d['声骸']
    if (Object.keys(data).includes(name)) {
        let len = 0, sum = 0
        for (let item of data[name]) {
            if (item.main !== '') {
                len++
                sum += Number(item.cost)
            }
        }
        if (len === 5) {
            ElMessage.warning("该角色的声骸列表已满！");
            return
        }
        if (sum + Number(echo['cost']) > 12) {
            ElMessage.warning("该角色声骸的 Cost 不合法！");
            return
        }
    }
    data.value = await POST("/echo-scoring-system/add-data", {
        name: name,
        username: store.auth.user.username,
        json: JSON.stringify(d[name])
    })
    ElMessage.success("添加成功！");
    await del_echo(index)
}

const del_echo = async (index) => {
    await POST("/echo-scoring-system/del-temp-echo", {
        username: store.auth.user.username,
        json: JSON.stringify(data.value[index]),
    })
    await flush_data()
}

const edit_echo = async (index) => {
    store.echo.name = JSON.stringify(data.value[index])
    store.echo.index = -2
    await router.push('/add_temp_echo')
}

const set_img = (name) => {
    return {
        background: `url("/角色属性/${name}.png")`,
        backgroundColor: "rgb(250, 250, 250)",
        backgroundSize: `cover`,
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
    }
}

const get_temp_data_by_screen = async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    data.value = await POST("/echo-scoring-system/get-temp-data-by-screen", {
        json: JSON.stringify(get_check()),
        username: store.auth.user.username
    })
    loading.close()
}

onMounted(async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
        characters.value = await post("/echo-scoring-system/get-characters", store.auth.user.username)
        for (let name of Object.keys(characters.value)) {
            weights.value[name] = await POST("/echo-scoring-system/get-weigths", {
                username: store.auth.user.username,
                name: name
            })
        }
        options.value = await store.get_options()
        await get_temp_data_by_screen()
    } catch (e) {
        console.error("加载数据失败:", e);
    } finally {
        loading.close()
    }
})
</script>



<style scoped>
.screen {
    width: 100%;
    height: 250px;
    display: flex;
    padding-bottom: 10px;
    zoom: 0.9;
}
.screen .sub-echo {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    border: 1px solid #ccc;
    position: relative;
    background-color: rgb(250,250,250);
}
.screen .sub-echo .btn {
    position: absolute;
    bottom: 30px;
}
.screen .sub-echo .item {
    flex: 1;
    display: flex;
    justify-content: center;
}
.screen .container {
    flex: 18;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    border: 1px solid #ccc;
    border-left: none;
    color: #303133;
    position: relative;
}
.screen .container .btn {
    position: absolute;
    right: 10px;
    top: 10px;
}
.screen .container .item {
    flex: 1;
    display: flex;
    width: 100%;
    justify-content: center;
    align-items: center;
}
.screen .container .item .a {
    flex: 1;
    height: 100%;
    display: flex;
    justify-content: right;
    align-items: center;
    padding-right: 10px;
}
.screen .container .item .b {
    flex: 20;
    border-top: none;
    height: 100%;
    display: flex;
    align-items: center;
    gap: 7.7px;
}

.sub-echo {
    display: flex;
    color: #303133;
}
.sub-echo .a {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 1;
    border: 2px solid #666;
    height: 100px;
    border-right: none;
    background-color: rgb(250, 250, 250);
}
.sub-echo .b {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 5;
    border: 2px solid #666;
    height: 100px;
    background-color: rgb(250, 250, 250);
}
.sub-echo span {
    font-size: 2em;
}

.echo-list {
    display: flex;
    flex-direction: column;
}
.echo-list .item {
    height: 465px;
    display: flex;
    margin: 25px 0;
}
.echo-list .item .container {
    height: 100%;
    display: flex;
    justify-items: center;
    align-items: center;
    flex-grow: 1;
    flex-shrink: 0;
    border-bottom: 1px solid #ccc;
    border-top: 1px solid #ccc;
    padding: 0 5px;
}
.echo-list .item .container .sub-echo {
    width: 295px;
    height: 95%;
    margin: 5px;
}

table {
    width: 100%;
    height: 100%;
    border-collapse: collapse;
    table-layout: fixed;
}
table td {
    width: 60px;
    padding: 10px;
    border: 1px solid #ccc;
    border-left: 2px solid #ccc;
    border-right: 2px solid #ccc;
    text-align: center;
    vertical-align: middle;
    font-size: 16px;
}
table .title {
    background-color: rgb(250, 250, 250);
    font-size: 20px;
    font-weight: normal;
    border: 2px solid #ccc;
}
.main-echo td {
    border: 1px solid #666;
    border-left: 2px solid #666;
    border-right: 2px solid #666;
}
td:hover {
    background-color: rgb(245, 247, 250);
}
th:hover {
    background-color: rgb(245, 247, 250)
}

.score {
    font-size: 12px;
    padding: 5px 0;
    border: 2px solid #ccc;
}
.fa {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}
.fa span {
    opacity: 1;
    transition: opacity 0.5s ease;
}
.fa .grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    justify-content: center;
    width: 100%;
    height: 100%;
    position: absolute;
    opacity: 0;
    transition: opacity 0.5s ease;
}
.fa .grid .btn {
    display: flex;
    justify-content: center;
    align-items: center;
}
.fa:hover span {
    opacity: 0;
}
.fa:hover .grid {
    opacity: 1;
}

.data-size {
    position: fixed;
    bottom: 10px;
    left: 10px;
    width: 180px;
    height: 50px;
}
.data-size td {
    background-color: rgb(245, 247, 250);
    border: 1px solid #666;
}
.data-size .head {
    width: 80%;
    color: #303133;
}
.data-size .body {
    width: 20%;
    font-size: large;
}
</style>