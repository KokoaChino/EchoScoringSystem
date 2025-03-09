<template>
    <Template>
        <div class="screen">
            <div class="sub-echo">
                <span class="item" style="align-items: flex-end;">筛选</span>
                <span class="item">条件</span>
                <el-button class="btn" type="info" @click="re_check" round>重置</el-button>
            </div>
            <div class="container">
                <div class="btn">
                    <el-button size="large" type="success" @click="router.push('/add_echo')" plain>添加声骸</el-button>
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
        <div class="content">
            <div class="sub-echo">
                <div class="a">
                    <span>角色</span>
                </div>
                <div class="b">
                    <span>声骸列表</span>
                </div>
            </div>
            <el-empty description="角色声骸列表为空，请先去添加声骸" v-if="Object.keys(data).length === 0"/>
            <div class="items" v-else>
                <div class="item" v-for="(name, index) in keys" :key="index">
                    <div class="sub-echo">
                        <div class="img_center" :style="set_background_color(characters[name], 30)" style="padding: 0;">
                            <div class="img" :style="set_img(name)"></div>
                            <div class="overlay">
                                <span style="width: 100%;">
                                    {{ name }}
                                </span>
                            </div>
                        </div>
                        <div class="score">
                            <span :style="set_colorbyw(get_total(name))">
                                {{ get_total(name) }}
                            </span>
                        </div>
                    </div>
                    <div class="echo" v-for="(echos, index) in data[name]" :key="index">
                        <table>
                            <thead>
                            <tr>
                                <th>
                                    {{ echos['main'] }}
                                </th>
                                <th style="border-left: 2px solid #ccc;font-size: 20px;">
                                    {{ echos['cost'] }}
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="(item, i) in echos['echo']" :key="i">
                                <td style="width: 80%">
                                    <div :style="set_style1(name, item[0])">
                                        <div>{{ item[0] }}</div>
                                        <div>{{ item[0].includes('固定') || item[0] === '' ? item[1] : item[1] + '%' }}</div>
                                    </div>
                                </td>
                                <td :style="set_style2(name, item[0])">
                                    {{ item[2] !== '' ? item[2] + '%' : '' }}
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" :style="set_style3(echos['score'])">
                                    <div class="fa" v-if="echos['score']">
                                        <div class="grid">
                                            <div class="btn" @click="edit_echo(name, index)">
                                                <el-button type="primary">修改声骸</el-button>
                                            </div>
                                            <div class="btn" @click="del_echo(name, index, echos['main'])">
                                                <el-button type="danger">移除声骸</el-button>
                                            </div>
                                        </div>
                                        <span>
                                            {{ echos['score'] }}
                                        </span>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </Template>
</template>



<script setup>
import Template from "@/components/layout/Template.vue";
import { ref, onMounted, watch } from "vue";
import router from "@/router/index.js";
import { useStore } from "@/stores/index.js";
import { get, post, POST } from "@/net/index.js";
import {ElLoading} from "element-plus";

const store = useStore()
const showContent = ref({}), characters = ref({})
const data = ref({}), weigths = ref({}), keys = ref([])
const scale = ref({
    '长离': '77%',
    '安可': '113%',
    '丹瑾': '90%',
    '守岸人': '67%',
    '维里奈': '98%',
    '椿': '69%',
    '秋水': '72%',
    '散华': '78%',
    '折枝': '69%',
    '忌炎': '110%',
    '今汐': '80%',
    '炽霞': '88%',
    '渊武': '80%',
    '白芷': '80%',
    '漂泊者 - 女 - 衍射': '85%',
    '漂泊者 - 男 - 衍射': '80%',
    '漂泊者 - 男 - 湮灭': '80%',
    '漂泊者 - 女 - 湮灭': '85%',
    '吟霖': '95%',
    '鉴心': '95%',
    '莫特斐': '79%',
    '釉瑚': '68%',
    '桃祈': '91%',
    '凌阳': '95%',
    '灯灯': '60%',
    '秧秧': '92%',
    '卡卡罗': '95%',
    '相里要': '77%',
    '珂莱塔': '87%',
    '洛可可': '75%',
})
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
const props = { multiple: true }
const options = ref([]), selectedValues = ref([])

const handleChange = async (value) => {
     name_check.value = await value.map((path) => path[path.length - 1]);
};

watch([name_check, cost_check, main_check, echo_check, range], () => {
    get_data_by_screen()
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
    data.value = await post("/echo-scoring-system/get-data", store.auth.user.username)
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

function set_background_color(item, num) {
    if (!item) return
    if (item['lv'] === 5) return {
        background: `linear-gradient(to top, rgba(255, 215, 0, 0.6) 0%, rgba(0, 0, 0, 0) ${num}%)`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
    };
    else return {
        background: `linear-gradient(to top, rgba(128, 0, 128, 0.6) 0%, rgba(0, 0, 0, 0) ${num}%)`,
        backgroundSize: `cover`,
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
    };
}

const get_data_by_screen = async () => {
    data.value = await POST("echo-scoring-system/get-data-by-screen", {
        json: JSON.stringify(get_check()),
        username: store.auth.user.username
    })
    keys.value = Object.keys(data.value)
    keys.value.sort((a, b) => get_total(b) - get_total(a))
}

async function edit_echo(name, index) {
    store.echo.name = name
    store.echo.index = index
    await router.push('/add_echo')
}

async function del_echo(name, index, k) {
    if (k === '') return
    await POST("/echo-scoring-system/del-echo", {
        username: store.auth.user.username,
        name: name,
        index: index
    })
    await get_data_by_screen()
    showContent.value = {}
    for (let key of keys.value) {
        showContent.value[key] = new Array(5).fill(false)
    }
}

function set_color(name, key) {
    if (key === '' || !weigths.value || Object.keys(weigths.value).length === 0) return ''
    let color = '', w = weigths.value[name][key]
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

function set_colorbyw(w) {
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

function set_img(name) {
    let sc = ''
    if (name in scale.value) sc = `background-size: ${scale.value[name]};`
    return `background-image: url("/角色立绘/${name}.png");image-rendering: smooth;` + sc
}

const set_style1 = (name, key) => {
    let res = "display: flex; justify-content: space-between;"
    res += set_color(name, key)
    return res
}
const set_style2 = (name, key) => {
    let res = "width: 20%; text-align: center;"
    res += set_color(name, key)
    return res
}
const set_style3 = (w) => {
    let res = "text-align: center;border-top: 2px solid #666;padding: 0;font-size: 20px;";
    return res + set_colorbyw(w)
}

const get_total = (name) => {
    let check = get_check(), sum = 0, cnt = 0
    for (let item of data.value[name]) {
        if (item.score !== '') {
            sum += Number(item.score)
            cnt++
        }
    }
    if (!(check['name'].length !== 0 ||
        check['main'].length !== 0 ||
        check['cost'].length !== 0 ||
        check['echo'].length !== 0 ||
        JSON.stringify(check['range']) !== JSON.stringify([0, 100]))) {
        cnt = 5
    }
    return (sum / cnt).toFixed(1)
}

onMounted(async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
        data.value = await post("/echo-scoring-system/get-data", store.auth.user.username)
        keys.value = Object.keys(data.value)
        keys.value.sort((a, b) => get_total(b) - get_total(a))
        for (let key of Object.keys(data.value)) {
            showContent.value[key] = new Array(5).fill(false)
            weigths.value[key] = await POST("/echo-scoring-system/get-weigths", {
                name: key,
                username: store.auth.user.username,
            })
        }
        characters.value = await POST("/echo-scoring-system/get-characters", store.auth.user.username)
        options.value = await store.get_options()
    } catch (e) {
        console.error("加载数据失败:", e);
    } finally {
        loading.close()
    }
})
</script>



<style scoped>
.content {
    width: 100%;
    margin: auto;
    display: flex;
    flex-direction: column;
    zoom: 0.9;
}

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

.items {
    display: flex;
    flex-direction: column;
}
.items .item {
    display: flex;
}
.items .item .sub-echo {
    flex: 1;
    align-items: center;
    justify-content: center;
    display: flex;
    flex-direction: column;
    border: 2px solid #666;
    border-top: none;
}
.items .item .echo {
    flex: 1;
    border-top: none;
}

.img_center {
    position: relative;
    flex: 5;
    width: 100%;
    box-sizing: border-box;
    overflow: hidden;
}
.img_center .img {
    width: 100%;
    height: 100%;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    transition: transform 1s;
}
.overlay {
    position: absolute;
    width: 100%;
    height: 50px;
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
    font-size: 10px;
    opacity: 0;
    transition: opacity 0.5s ease;
    left: 0;
    bottom: 0;
    text-align: center;
    line-height: 50px;
}
.img_center .img:hover {
    transform: scale(1.05);
}
.img_center:hover .overlay {
    opacity: 1;
}

.sub-echo .score {
    flex: 1;
    max-height: 50px;
    width: 100%;
    box-sizing: border-box;
    border-top: 2px solid #666;
    display: grid;
    place-items: center;
    font-size: 15px;
}
.sub-echo .score:hover {
    background-color: rgb(245, 247, 250)
}
.echo th {
    padding: 10px;
    box-sizing: border-box;
    height: 50px;
    border-bottom: 2px solid #666;
    font-size: 19px;
    font-weight: normal;
}
.echo td {
    padding: 10px;
    box-sizing: border-box;
    height: 50px;
    border: 1px solid #ccc;
    border-left: none;
    font-size: 15px;
}
.echo table {
    width: 100%;
    height: 100%;
    border-collapse: collapse;
    border-right: 2px solid #666;
    border-bottom: 2px solid #666;
}
td:hover {
    background-color: rgb(245, 247, 250);
}
th:hover {
    background-color: rgb(245, 247, 250)
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
    position: absolute;
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
</style>