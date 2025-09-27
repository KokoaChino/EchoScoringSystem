<template>
    <Template>
        <div class="screen">
            <div class="a">
                <span>筛选角色：</span>
            </div>
            <div class="b">
                <el-cascader
                    style="width: 100%;padding-right: 30px;"
                    v-model="selectedValues"
                    :options="options"
                    :props="props"
                    collapse-tags
                    collapse-tags-tooltip
                    :max-collapse-tags="15"
                    :show-all-levels="false"
                    @change="handleChange"
                    placeholder="筛选角色"
                />
            </div>
        </div>
        <el-card class="item" v-for="(name, index) in Object.keys(data)" :key="index"
                    v-show="names.length === 0 || names.includes(name)">
            <table>
                <tr>
                    <td colspan="13" class="a sub-echo">
                        词条权重
                    </td>
                </tr>
                <tr>
                    <td class="a sub-echo" style="font-size: 20px;font-weight: normal;">
                        角色
                    </td>
                    <td style="font-size: 18px;">
                        {{name}}
                    </td>
                    <td class="a sub-echo" style="font-size: 20px;font-weight: normal;">
                        词条权重峰度
                    </td>
                    <td colspan="9" style="padding: 0">
                        <div class="kurtosis">
                            <span style="flex: 1;font-size: 18px;padding: 0 10px 0 20px">
                                {{kurtosis[name]}}
                            </span>
                            <div class="slider">
                                <div class="line" style="left: 50%"></div>
                                <div class="line" style="right: 50%"></div>
                                <span style="flex: 1">
                                    平滑分布
                                </span>
                                <el-slider style="flex: 10; margin: 0 10px;" v-model="el_kurtosis[name]" range disabled :min='-2' :max="2" />
                                <span style="flex: 1">
                                    尖锐分布
                                </span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div>
                            <el-button size="large" type="success" @click="set_weight(name)" plain>设置词条权重</el-button>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td v-for="(key, index) in Object.keys(data[name])" :key="index" class="a" style="font-size: 15px;">
                        {{key}}
                    </td>
                </tr>
                <tr>
                    <td v-for="(val, index) in Object.values(data[name])" :key="index"
                        :style="`color: ${ set_color(val) }`">
                        {{val}}
                    </td>
                </tr>
                <tr>
                    <td colspan="14">
                        <div class="img">
                            <div class="sub-img">
                                <v-chart class="chart" :option="pies[name]"/>
                            </div>
                            <div class="sub-img">
                                <v-chart class="chart" :option="bars[name]"/>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </el-card>
    </Template>
</template>



<script setup>
import Template from "@/components/layout/Template.vue";
import { ref, onMounted } from "vue";
import { get, post, GET } from "@/net/index.js";
import { useStore } from "@/stores/index.js";
import VChart from "vue-echarts";
import "echarts";
import router from "@/router/index.js";
import {ElLoading} from "element-plus";

const store = useStore()
const data = ref({})
const base_pie = {
    title: {
        text: '副词条权重占比图',
        left: 'center'
    },
    tooltip: {
        trigger: 'item'
    },
    series: [
        {
            data: [],
            type: 'pie',
            radius: '60%',
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
}
const base_bar = {
    title: {
        text: '副词条权重占比图',
        left: 'center'
    },
    tooltip: {
        trigger: 'item'
    },
    xAxis: {
        type: 'category',
        data: [],
        axisLabel: {
            interval: 0,
            rotate: 30
        },
        axisTick: {
            interval: 0
        }
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            data: [],
            type: 'bar',
        }
    ]
}
const pies = ref({}), bars = ref({}), kurtosis = ref({})
const el_kurtosis = ref({})
const props = { multiple: true }
const options = ref([]), selectedValues = ref([]), names = ref([])

const handleChange = async (value) => {
    names.value = await value.map((path) => path[path.length - 1]);
};

function set_color(w) {
    let color = ''
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
    return color
}

async function set_weight(name) {
    store.echo.name = name
    store.echo.index = -3
    await router.push('/set_weight')
}

onMounted( async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
        let keys = await get("/api/echo/get-echo-keys")
        kurtosis.value = await post("/api/echo/get-weight-kurtosis", store.auth.user.username)
        for (let name of Object.keys(kurtosis.value)) {
            if (isNaN(kurtosis.value[name])) kurtosis.value[name] = 0;
            let val = kurtosis.value[name].toFixed(2)
            kurtosis.value[name] = val
            el_kurtosis.value[name] = [Math.max(-2, Math.min(0, val)), Math.min(2, Math.max(0, val))]
        }
        let weights = await GET("/api/echo/get-all-weights", {
            username: store.auth.user.username,
        })
        for (let name of await get("/api/echo/get-names")) {
            let tmp = weights[name]
            let pie = JSON.parse(JSON.stringify(base_pie)), bar = JSON.parse(JSON.stringify(base_bar))
            pies.value[name] = pie
            bars.value[name] = bar
            bars.value[name]['series'][0]['itemStyle'] = {
                normal: {
                    color: function(params) {
                        return set_color(params.value)
                    }
                }
            }
            data.value[name] = {}
            for (let key of keys) {
                data.value[name][key] = tmp[key]
                let val = tmp[key]
                if (val != 0) pie['series'][0]['data'].push({'name': key, 'value': val})
                bar['xAxis']['data'].push(key)
                bar['series'][0]['data'].push(val)
            }
            pie['series'][0]['data'].sort((a, b) => b.value - a.value)
        }
        options.value = await store.get_options()
    } catch (e) {
        console.error("加载数据失败:", e);
    } finally {
        loading.close()
    }
})
</script>



<style scoped>
.item {
    margin-top: 10px;
}

table {
    width: 100%;
    border-collapse: collapse;
}
table td {
    padding: 10px;
    border: 1px solid #ccc;
    text-align: center;
    height: 50px;
}
table .a {
    background-color: #f4f4f4;
    color: #303133;
}
table .a {
    font-size: 20px;
}
table .sub-echo {
    font-size: 28px;
    font-weight: bold;
}

.img {
    width: 100%;
    height: 500px;
    display: flex;
    justify-content: center;
    align-items: center;
}
.img .sub-img {
    flex: 1;
    height: 100%;
}

.chart {
    width: 100%;
    height: 100%;
}

.kurtosis {
    font-size: 15px;
    display: flex;
    width: 100%;
    height: 100%;
    justify-content: left;
    align-items: center;
}
.slider {
    flex: 20;
    display: flex;
    width: 100%;
    height: 100%;
    justify-content: left;
    align-items: center;
    position: relative;
}
.line {
    position: absolute;
    top: 0;
    width: 0;
    border-left: 1px dashed #303133;
    height: 100%;
}

.screen {
    display: flex;
    justify-items: center;
    align-items: center;
    height: 50px;
}
.screen .a {
    flex: 1;
    font-size: 25px;
}
.screen .b {
    flex: 10;
}
</style>