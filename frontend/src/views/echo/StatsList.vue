<template>
    <Template>
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
            <div class="grid-item" v-for="(character, index) in Object.values(characters)" :key="index">
                <el-card class="card">
                    <table class="basic-table">
                        <thead>
                        <tr>
                            <th width="210px">角色</th>
                            <th width="210px">武器</th>
                            <th colspan="2">三维属性</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td rowspan="3" class="img_center" style="padding: 0;">
                                <div class="img" :style="set_characters_img_and_color(character, 30)"></div>
                                <div class="txt">
                                    <span style="width: 100%;">
                                        {{ character['name'] }}
                                    </span>
                                </div>
                            </td>
                            <td rowspan="3" class="img_center" style="padding: 0;">
                                <div class="img" :style="set_weapons_img_and_color(character['weapon'], 30)"
                                     title="点击设置角色武器"
                                     @click="drawer = true;select = character['weapon'];select_character=character">
                                </div>
                                <div class="txt">
                                    <span style="width: 100%;">
                                        {{ character['weapon']['name'] }}
                                    </span>
                                </div>
                            </td>
                            <td class="a">攻击</td>
                            <td>{{ character['stats'][0] + character['weapon']['maxAtk'] }}</td>
                        </tr>
                        <tr>
                            <td class="a">生命</td>
                            <td>{{ character['stats'][1] }}</td>
                        </tr>
                        <tr>
                            <td class="a">防御</td>
                            <td>{{ character['stats'][2] }}</td>
                        </tr>
                        </tbody>
                    </table>
                </el-card>
            </div>
        </div>
    </Template>
    <el-drawer v-model="drawer" title="" :with-header="false"
               @open="open_drawer" @close="close_drawer">
        <el-divider content-position="center">筛选</el-divider>
        <div class="tag">
            <span>类型：</span>
            <el-check-tag :disabled="!(select['type'] === name && (check['类型'][name] = true))" v-for="name in Object.keys(check['类型'])"
                          :checked="check['类型'][name]">
                {{name}}
            </el-check-tag>
        </div>
        <div class="tag">
            <span>星级：</span>
            <el-check-tag v-for="key in Object.keys(check['星级'])"
                          :title="key < 4 ? `不会吧不会吧，竟然真的还有人在用 ${key} 星武器？🤔` : ''"
                          :checked="check['星级'][key]" @change="check['星级'][key] = !check['星级'][key]">
                {{key}}
            </el-check-tag>
        </div>
        <el-divider content-position="center">武器</el-divider>
        <div class="weapon_grid">
            <div class="item" v-for="(item, index) in screen_weapons" :key="index"
                 :class="{ selected: item['name'] === select_weapon }"
                 @click="select_weapon = item['name']">
                <div class="weapon_img" :style="set_weapons_img_and_color(item, 100)" :title="'攻击：' + item['maxAtk']">

                </div>
                <div class="text" >
                    {{ item['name'] }}
                </div>
            </div>
        </div>
    </el-drawer>
</template>



<script setup>
import Template from "@/components/layout/Template.vue";
import { ref, onMounted, watch } from "vue";
import { get, post, POST } from "@/net/index.js";
import { useStore } from "@/stores/index.js";
import { ElLoading, ElMessage } from "element-plus";

const store = useStore()

const drawer = ref(false)
const characters = ref({}), weapons = ref({})
const check = ref({
    '类型': {
        '长刃': false,
        '臂铠': false,
        '迅刀': false,
        '佩枪': false,
        '音感仪': false,
    },
    '星级': {
        1: false,
        2: false,
        3: false,
        4: true,
        5: true
    }
})
const select = ref({type: ''}), screen_weapons = ref([]),
    select_weapon = ref(''), select_character = ref('')
const props = { multiple: true }
const options = ref([]), selectedValues = ref([]), names = ref([])

const handleChange = async (value) => {
    names.value = await value.map((path) => path[path.length - 1]);
    characters.value = await POST("/api/echo/get-characters-by-screen", {
        names: JSON.stringify(names.value)
    })
};

watch(check,async () => {
    screen_weapons.value = await POST("/api/echo/get-weapons-by-screen", {
        json: JSON.stringify(screen())
    })
}, {deep: true})

const open_drawer = async () => {
    check.value = {
        '类型': {
            '长刃': select_character.value['weapon']['type'] === '长刃',
            '臂铠': select_character.value['weapon']['type'] === '臂铠',
            '迅刀': select_character.value['weapon']['type'] === '迅刀',
            '佩枪': select_character.value['weapon']['type'] === '佩枪',
            '音感仪': select_character.value['weapon']['type'] === '音感仪'
        },
        '星级': {
            1: false,
            2: false,
            3: false,
            4: true,
            5: true
        }
    }
    select_weapon.value = ''
    screen_weapons.value = await POST("/api/echo/get-weapons-by-screen", {
        json: JSON.stringify(screen())
    })
}

const close_drawer = async () => {
    if (select_weapon.value === '') return
    await POST("/api/echo/set-weapon", {
        name: select_character.value['name'],
        weapon: select_weapon.value
    })
    characters.value = await POST("/api/echo/get-characters-by-screen", {
        names: JSON.stringify(names.value)
    })
    await POST("/api/echo/re-weights", {
        name: select_character.value['name']
    })
    await post("/api/echo/re-data")
}

const screen = () => {
    let res = {
        '类型': [],
        '星级': []
    }
    for (let key of Object.keys(check.value)) {
        for (let k of Object.keys(check.value[key])) {
            if (check.value[key][k]) {
                res[key].push(k)
            }
        }
    }
    return res
}

function set_characters_img_and_color(item, num) {
    const starColorMap = {
        4: '201, 131, 237',
        5: '255, 246, 145'
    };
    const star = item['star'];
    const rgb = starColorMap[star] || '128, 128, 128';
    return {
        background: `linear-gradient(to top, rgba(${rgb}, 0.6) 0%, rgba(0, 0, 0, 0) ${num}%), url('${characters.value[item['name']]['avatarUrl']}')`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat'
    };
}

function set_weapons_img_and_color(item, num) {
    const starColorMap = {
        1: '222, 222, 222',
        2: '156, 243, 176',
        3: '129, 206, 255',
        4: '201, 131, 237',
        5: '255, 246, 145'
    };
    const star = item['star'];
    const rgb = starColorMap[star] || '128, 128, 128';
    return {
        background: `linear-gradient(to top, rgba(${rgb}, 0.6) 0%, rgba(0, 0, 0, 0) ${num}%), url('${weapons.value[item['name']]['imageUrl']}')`,
        backgroundSize: 'cover',
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
        options.value = await store.get_options()
        weapons.value = await get("/api/echo/get-weapons")
        characters.value = await POST("/api/echo/get-characters-by-screen", {
            names: JSON.stringify(names.value)
        })
    } catch (e) {
        ElMessage.error("加载数据失败：", e)
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
    flex-direction: row;
}
.grid-item {
    width: 97%;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    flex-shrink: 0;
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
    height: 70px;
    box-sizing: border-box;
}
.basic-table th {
    background-color: #f4f4f4;
    font-size: 25px;
    color: #303133;
    font-weight: normal;
}
tr>.a {
    color: #111;
    background-color: #f4f4f4;
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
.img_center:hover .img{
    transform: scale(1.05);
}
.img_center:hover .txt {
    opacity: 1;
}
.txt {
    position: absolute;
    width: 100%;
    height: 50px;
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

.tag {
    height: 50px;
    display: flex;
    align-items: center;
    gap: 9px;
}

.weapon_grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    justify-items: center;
    align-items: start;
    flex-direction: row;
    gap: 10px;
}
.weapon_grid .item.selected {
    border: 2px solid rgb(187,159,94);
}
.weapon_grid .item {
    width: 100%;
    height: 180px;
    display: flex;
    justify-content: center;
    align-content: center;
    flex-direction: column;
    position: relative;
    border-radius: 5px;
    transition: transform 0.3s ease;
    padding: 2px;
    box-sizing: border-box;
}
.weapon_grid .item .weapon_img {
    flex: 3;
    border: 1px solid #000;
    border-radius: 5px 5px 0 0;
    width: 100%;
    height: 100%;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    box-sizing: border-box;
}
.weapon_grid .item .text {
    flex: 1;
    border: 1px solid #000;
    border-radius: 0 0 5px 5px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgb(30,30,33);
    color: white;
}
.weapon_grid .item:hover {
    background-color: rgba(0, 0, 0, 0.2);
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
    transform: scale(1.02);
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

@media (max-width: 1000px) {
    .grid-container {
        grid-template-columns: 1fr;
    }
}
</style>