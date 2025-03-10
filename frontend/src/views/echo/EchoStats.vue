<template>
    <Template>
        <el-empty description="角色声骸属性为空，请先去添加声骸" v-if="Object.keys(data).length === 0"/>
        <div v-else>
            <div class="screen">
                <div class="a">
                    <span>筛选角色：</span>
                </div>
                <div class="b">
                    <el-cascader
                        style="width: 100%;padding-right: 30px;z-index: 999"
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
            <div class="grid-container">
                <div class="grid-item" v-for="(item, index) in data" :key="index"
                     v-show="names.length === 0 || names.includes(item['角色'])">
                    <el-card class="card">
                        <table class="basic-table">
                            <thead>
                            <tr>
                                <th width="250px">角色</th>
                                <th colspan="4">声骸属性</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td rowspan="5" class="img_center" style="padding: 0;">
                                    <div class="img" :style="set_background_color(characters[item['角色']], 30)"></div>
                                    <div class="txt">
                                    <span style="width: 100%;">
                                        {{item['角色']}}
                                    </span>
                                    </div>
                                </td>
                                <td class="a">攻击</td>
                                <td class="b">{{item['攻击']}}</td>
                                <td class="a">共鸣效率</td>
                                <td class="b">{{item['共鸣效率']}}</td>
                            </tr>
                            <tr>
                                <td class="a">生命</td>
                                <td class="b">{{item['生命']}}</td>
                                <td class="a">普攻伤害加成</td>
                                <td class="b">{{item['普攻伤害加成']}}</td>
                            </tr>
                            <tr>
                                <td class="a">防御</td>
                                <td class="b">{{item['防御']}}</td>
                                <td class="a">重击伤害加成</td>
                                <td class="b">{{item['重击伤害加成']}}</td>
                            </tr>
                            <tr>
                                <td class="a">暴击率</td>
                                <td class="b">{{item['暴击率']}}</td>
                                <td class="a">共鸣技能伤害加成</td>
                                <td class="b">{{item['共鸣技能伤害加成']}}</td>
                            </tr>
                            <tr>
                                <td class="a">暴击伤害</td>
                                <td class="b">{{item['暴击伤害']}}</td>
                                <td class="a">共鸣解放伤害加成</td>
                                <td class="b">{{item['共鸣解放伤害加成']}}</td>
                            </tr>
                            </tbody>
                            <tr>
                                <td colspan="5" class="img_data" style="height: 470px;">
                                    <el-radio-group class="select" v-model="select_show[item['角色']]">
                                        <el-radio-button label="加点次数" value="加点次数"/>
                                        <el-radio-button label="标准化偏差" value="标准化偏差"/>
                                    </el-radio-group>
                                    <v-chart v-if="select_show[item['角色']] === '加点次数'" class="chart" :option="pies[item['角色']]"/>
                                    <v-chart v-else class="chart" :option="bars[item['角色']]"/>
                                </td>
                            </tr>
                        </table>
                    </el-card>
                </div>
            </div>
        </div>
    </Template>
</template>



<script setup>
import Template from "@/components/layout/Template.vue";
import { ref, onMounted } from "vue";
import { get, post, POST } from "@/net/index.js";
import "echarts";
import VChart from "vue-echarts";
import { useStore } from "@/stores/index.js";
import {ElLoading} from "element-plus";

const store = useStore()
const base_pie = {
    title: {
        text: '副词条平均加点次数图',
        subtext: '以副词条基准值为参考',
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
        text: '副词条标准化偏差图',
        left: 'center'
    },
    tooltip: {
        trigger: 'item',
    },
    xAxis: {
        type: 'category',
        data: [],
        axisLabel: {
            interval: 0,
            rotate: 30
        }
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            data: [],
            type: 'bar'
        }
    ],
    grid: {
        left: '5%',
        right: '5%',
    }
}
const pies = ref({}), bars = ref({})
const data = ref([]), select_show = ref({}), characters = ref({})
const props = { multiple: true }
const options = ref([]), selectedValues = ref([]), names = ref([])

const handleChange = async (value) => {
    names.value = await value.map((path) => path[path.length - 1]);
};

function set_background_color(item, num) {
    if (item['lv'] === 5) return {
        background: `linear-gradient(to top, rgba(255, 215, 0, 0.6) 0%, rgba(0, 0, 0, 0) ${num}%), url("/角色头像/${item['name']}.png")`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
    };
    else return {
        background: `linear-gradient(to top, rgba(128, 0, 128, 0.6) 0%, rgba(0, 0, 0, 0) ${num}%), url("/角色头像/${item['name']}.png")`,
        backgroundSize: `cover`,
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
    };
}

onMounted( async () => {
    const loading = ElLoading.service({
        lock: true,
        text: 'Loading',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    try {
        let average = await get("/echo-scoring-system/get-echo-average"),
            basics = await get("/echo-scoring-system/get-character-stats")
        characters.value = await POST("/echo-scoring-system/get-characters", store.auth.user.username)
        data.value = await post("/echo-scoring-system/get-echo-stats", store.auth.user.username)
        for (let item of Object.values(data.value)) {
            let pie = JSON.parse(JSON.stringify(base_pie)), bar = JSON.parse(JSON.stringify(base_bar)), list = []
            bar['series'][0]['itemStyle'] = {
                normal: {
                    color: function(params) {
                        return params.value < 0 ? 'red' : 'green';
                    }
                }
            }
            for (let key of Object.keys(item)) {
                if (key !== '角色' && item[key] != 0) {
                    let val = (item[key] / average[key]).toFixed(1)
                    list.push({'name': key, 'value': val})
                }
            }
            pie['series'][0]['data'] = list.sort((a, b) => b.value - a.value)
            pies.value[item['角色']] = pie
            let rdmap = await POST("/echo-scoring-system/get-echo-relative-deviation-by-name", {
                username: store.auth.user.username,
                name: item['角色']
            })
            for (let key of Object.keys(rdmap)) {
                let val = rdmap[key]
                bar['xAxis']['data'].push(key)
                bar['series'][0]['data'].push(val)
            }
            bars.value[item['角色']] = bar
            select_show.value[item['角色']] = '加点次数'
            item['攻击'] = (Number(item['固定攻击']) + Number(item['百分比攻击'] * Number(basics[item['角色']][0]) * 0.01)).toFixed(1)
            item['生命'] = (Number(item['固定生命']) + Number(item['百分比生命']) * Number(basics[item['角色']][1]) * 0.01).toFixed(1)
            item['防御'] = (Number(item['固定防御']) + Number(item['百分比防御'] * Number(basics[item['角色']][2]) * 0.01)).toFixed(1)
            for (let key of Object.keys(item)) {
                if (item[key] == 0) {
                    item[key] = 0
                }
                if (!new Set(['攻击', '生命', '防御', '角色']).has(key))
                    item[key] += '%'
            }
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
.grid-container {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    justify-items: center;
    align-items: start;
}
.grid-item {
    width: 97%;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
}

.card {
    width: 100%;
    height: 100%;
    margin: 10px;
}

.basic-table {
    width: 100%;
    border-collapse: collapse;
}
.basic-table th,
.basic-table td {
    border: 1px solid #ccc;
    padding: 8px;
    text-align: center;
    height: 55px;
    box-sizing: border-box;
}
.basic-table th {
    background-color: #f4f4f4;
    font-size: 25px;
    font-weight: normal;
}
tr>.a {
    color: #111;
    background-color: #f4f4f4;
    font-weight: lighter;
}
tr>.b {
    color: #606266;
    font-weight: lighter;
}

.img_center {
    position: relative;
    overflow: hidden;
}
.img {
    width: 100%;
    height: 100%;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    transition: all 1s;
}
.img_center:hover .txt {
    opacity: 1;
}
.txt {
    position: absolute;
    width: 100%;
    height: 55px;
    background-color: rgba(0, 0, 0, 0.7);
    color: white;
    opacity: 0;
    transition: opacity 0.5s ease;
    justify-items: center;
    align-items: center;
    display: flex;
    left: 0;
    bottom: 0;
}

.img_data {
    padding: 10px;
    margin: 10px;
    width: 100%;
    box-sizing: border-box;
    position: relative;
}
.select {
    position: absolute;
    right: 8px;
    top: 8px;
    z-index: 10;
}

.screen {
    display: flex;
    justify-items: center;
    align-items: center;
    height: 50px;
    padding-left: 20px;
}
.screen .a {
    flex: 1;
    font-size: 25px;
}
.screen .b {
    flex: 10;
}
</style>