<template>
    <Template>
        <el-card class="score">
            <table>
                <tr>
                    <td class="a sub-echo" colspan="4">
                        声骸汇总
                    </td>
                </tr>
                <tr>
                    <td class="a" width="10%">
                        声骸总数
                    </td>
                    <td class="b" width="10%">
                        {{ total }}
                    </td>
                    <td class="a" width="10%">
                        声骸平均分
                    </td>
                    <td class="b" style="width: 70%">
                        <div style="display: flex;justify-content: center;align-items: center;">
                            <span style="flex: 1;padding-right: 10px">{{ (sx / total).toFixed(1) }}</span>
                            <el-progress style="flex: 18;" :text-inside="true" :stroke-width="26"
                                         :color="set_colorbyw(sx / total)"
                                         :percentage="(sx / total).toFixed(1)"
                                         :format="(percentage) => percentage"
                                         striped />
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <v-chart class="chart" :option="score"/>
                    </td>
                </tr>
            </table>
        </el-card>
        <el-card class="sss">
            <table>
                <tr>
                    <td colspan="8" class="a  sub-echo">
                        声骸评级
                    </td>
                </tr>
                <tr>
                    <td v-for="(key, index) in Object.keys(rate)" :key="index" class="a" style="width: 12.5%">
                        {{key}}
                    </td>
                </tr>
                <tr>
                    <td v-for="(val, index) in Object.values(rate)" :key="index">
                        {{val}}
                    </td>
                </tr>
                <tr>
                    <td colspan="8">
                        <v-chart class="chart" :option="rates"/>
                    </td>
                </tr>
            </table>
        </el-card>
        <el-card style="margin-top: 10px;" class="relative-deviation">
            <table>
                <tr>
                    <td colspan="13" class="a sub-echo">
                        声骸词条标准化偏差
                    </td>
                </tr>
                <tr>
                    <td v-for="(key, index) in Object.keys(rd)" :key="index" class="a" style="font-size: 16px;">
                        {{key}}
                    </td>
                </tr>
                <tr>
                    <td v-for="(val, index) in Object.values(rd)" :key="index" :style="{ color: val < 0 ? 'red' : 'green' }">
                        {{val + '%'}}
                    </td>
                </tr>
                <tr>
                    <td colspan="13">
                        <v-chart class="chart" :option="rds"/>
                    </td>
                </tr>
            </table>
        </el-card>
    </Template>
</template>



<script setup>
import Template from "@/components/layout/Template.vue";
import { ref, onMounted } from "vue";
import { post } from "@/net/index.js";
import { useStore } from "@/stores/index.js";
import VChart from "vue-echarts";
import "echarts";
import {ElLoading} from "element-plus";

const store = useStore()
const color = {
    "F": 'rgb(204,194,194)',
    "D": 'rgb(143,139,139)',
    "C": 'rgb(117,166,59)',
    "B": 'rgb(44,118,200)',
    "A": 'rgb(255, 204, 0)',
    "S": 'rgb(255, 165, 0)',
    "SS": 'rgb(237,98,98)',
    "SSS": 'rgb(232,42,42)',
}
const cnt = ref([]), rate = ref({
    "F": 0,
    "D": 0,
    "C": 0,
    "B": 0,
    "A": 0,
    "S": 0,
    "SS": 0,
    "SSS": 0
}), total = ref(0), sx = ref(0), rd = ref({})
const base_bar = {
    title: {
        text: '',
        left: 'center'
    },
    tooltip: {
        trigger: 'item'
    },
    xAxis: {
        type: 'category',
        data: [],
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            data: [],
            type: 'bar',
        }
    ],
    grid: {
        left: '3%',
        right: '3%',
    }
}
const score = ref({}), rates = ref({}), rds = ref({})

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
    return color
}

onMounted(async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
        cnt.value = await post("/echo-scoring-system/get-echo-cnts", store.auth.user.username)
        rd.value = await post("/echo-scoring-system/get-echo-relative-deviation", store.auth.user.username)
        rds.value = JSON.parse(JSON.stringify(base_bar))
        score.value = JSON.parse(JSON.stringify(base_bar))
        rates.value = JSON.parse(JSON.stringify(base_bar))
        score.value['title']['text'] = '声骸评分分布柱状图'
        score.value['dataZoom'] = [{
            type: 'inside'
        },{
            type: 'slider'
        }]
        rds.value['xAxis']['axisLabel'] = {
            interval: 0,
            rotate: 10
        }
        rds.value['series'][0]['itemStyle'] = {
            normal: {
                color: function(params) {
                    return params.value < 0 ? 'red' : 'green';
                }
            }
        }
        rates.value['title']['text'] = '声骸评级分布柱状图'
        rds.value['title']['text'] = '声骸词条标准化偏差柱状图'
        for (let i = 0; i < cnt.value.length; i++) {
            score.value['xAxis']['data'].push(i)
            score.value['series'][0]['data'].push(cnt.value[i])
            total.value += cnt.value[i]
            sx.value += cnt.value[i] * i
            if (i >= 90) {
                rate.value["SSS"] += cnt.value[i]
            } else if (i >= 80) {
                rate.value["SS"] += cnt.value[i]
            } else if (i >= 70) {
                rate.value["S"] += cnt.value[i]
            } else if (i >= 60) {
                rate.value["A"] += cnt.value[i]
            } else if (i >= 50) {
                rate.value["B"] += cnt.value[i]
            } else if (i >= 40) {
                rate.value["C"] += cnt.value[i]
            } else if (i >= 30) {
                rate.value["D"] += cnt.value[i]
            } else {
                rate.value["F"] += cnt.value[i]
            }
        }
        for (let key of Object.keys(rate.value)) {
            rates.value['xAxis']['data'].push(key)
            rates.value['series'][0]['data'].push(rate.value[key])
        }
        for (let key of Object.keys(rd.value)) {
            rds.value['xAxis']['data'].push(key)
            rds.value['series'][0]['data'].push(rd.value[key])
        }
        rates.value['series'][0]['itemStyle'] = {
            color: function (params) {
                return color[params.name];
            }
        }
    } catch (e) {
        console.error("加载数据失败:", e);
    } finally {
        loading.close()
    }
})
</script>



<style scoped>
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
table .a,.b {
    font-size: 20px;
}
table .sub-echo {
    font-size: 25px;
    font-weight: bold;
}

.score {
    width: 100%;
}

.sss {
    margin-top: 10px;
}

.chart {
    width: 100%;
    height: 500px;
}
</style>