<template>
    <div class="title">
        <span>声骸评分系统</span>
    </div>
    <el-row class="tac">
        <el-col :span="24">
            <el-menu
                :default-active="active"
                class="el-menu-vertical-demo">
                <el-sub-menu index="1">
                    <template #title>
                        <svg id="title-icon" t="1732698818489" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="8590" width="256" height="256"><path d="M419.037 287.953h413.124c17.673 0 32-14.327 32-32s-14.327-32-32-32H419.037c-17.673 0-32 14.327-32 32s14.327 32 32 32zM419.028 543.17h411.608c17.673 0 32-14.327 32-32s-14.327-32-32-32H419.028c-17.673 0-32 14.327-32 32s14.327 32 32 32zM832.161 735.802H419.037c-17.673 0-32 14.327-32 32s14.327 32 32 32h413.124c17.673 0 32-14.327 32-32s-14.327-32-32-32z" fill="" p-id="8591"></path><path d="M256.037 255.953m-64 0a64 64 0 1 0 128 0 64 64 0 1 0-128 0Z" fill="" p-id="8592"></path><path d="M256.037 510.787m-64 0a64 64 0 1 0 128 0 64 64 0 1 0-128 0Z" fill="" p-id="8593"></path><path d="M256.037 767.621m-64 0a64 64 0 1 0 128 0 64 64 0 1 0-128 0Z" fill="" p-id="8594"></path></svg>
                        <span>声骸列表</span>
                    </template>
                        <el-menu-item index="1-1" @click="router.push('/index')">
                            <template #title>
                                <svg id="title-icon" t="1732698818489" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="8590" width="256" height="256"><path d="M419.037 287.953h413.124c17.673 0 32-14.327 32-32s-14.327-32-32-32H419.037c-17.673 0-32 14.327-32 32s14.327 32 32 32zM419.028 543.17h411.608c17.673 0 32-14.327 32-32s-14.327-32-32-32H419.028c-17.673 0-32 14.327-32 32s14.327 32 32 32zM832.161 735.802H419.037c-17.673 0-32 14.327-32 32s14.327 32 32 32h413.124c17.673 0 32-14.327 32-32s-14.327-32-32-32z" fill="" p-id="8591"></path><path d="M256.037 255.953m-64 0a64 64 0 1 0 128 0 64 64 0 1 0-128 0Z" fill="" p-id="8592"></path><path d="M256.037 510.787m-64 0a64 64 0 1 0 128 0 64 64 0 1 0-128 0Z" fill="" p-id="8593"></path><path d="M256.037 767.621m-64 0a64 64 0 1 0 128 0 64 64 0 1 0-128 0Z" fill="" p-id="8594"></path></svg>
                                <span>声骸列表</span>
                            </template>
                        </el-menu-item>
                        <el-menu-item index="1-2" @click="router.push('/temp_echo_list')">
                            <template #title>
                                <svg id="title-icon" t="1733289967449" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6845" width="256" height="256"><path d="M230.5 262.2h64v64h-64zM348.9 262.2h318.9v64H348.9zM230.5 437.3h64v64h-64zM230.5 623.3h64v64h-64z" fill="#303133" p-id="6846"></path><path d="M685 967.5c-65 0-126.1-25.3-172.1-71.3s-71.3-107.1-71.3-172.1S466.9 598 512.9 552 620 480.7 685 480.7 811.1 506 857.1 552s71.3 107.1 71.3 172.1S903 850.2 857 896.2c-45.9 46-107 71.3-172 71.3z m0-422.7c-98.9 0-179.3 80.5-179.3 179.3S586.1 903.5 685 903.5 864.3 823 864.3 724.1 783.8 544.8 685 544.8z" fill="#303133" p-id="6847"></path><path d="M667.8 437.3H348.9v64l207.1-1c34.8-19.5 71.7-32.5 111.8-35.3v-27.7z" fill="#303133" p-id="6848"></path><path d="M746.1 539.1c-1.3-0.5-2.5-0.9-3.8-1.3v1.3h3.8z" fill="#303133" p-id="6849"></path><path d="M254.3 896.2c-50.7 0-91.9-48.7-91.9-108.7V238.8c0-59.9 41.2-108.7 91.9-108.7h396c50.7 0 91.9 48.7 91.9 108.7v232.3c22.5 5.4 44 14 64 25.5V238.8c0-45.4-15.7-88.2-44.3-120.6-29.6-33.6-69.2-52-111.6-52h-396c-42.4 0-82.1 18.5-111.6 52-28.6 32.4-44.3 75.3-44.3 120.6v548.7c0 45.4 15.7 88.2 44.3 120.6 29.6 33.6 69.2 52 111.6 52h322.8c-40.7-12.9-61.1-32.4-92.7-64H254.3v0.1z" fill="#303133" p-id="6850"></path><path d="M348.9 623.3v64h79.7c3.4-22.3 9.8-43.8 19-64h-98.7zM691.1 765.8h-115c-17.7 0-32-14.3-32-32s14.3-32 32-32h115c17.7 0 32 14.3 32 32s-14.3 32-32 32z" fill="#303133" p-id="6851"></path><path d="M693.2 765.8c-17.7 0-32-14.3-32-32v-115c0-17.7 14.3-32 32-32s32 14.3 32 32v115c0 17.7-14.4 32-32 32z" fill="#303133" p-id="6852"></path></svg>
                                <span>临时列表</span>
                            </template>
                        </el-menu-item>
                </el-sub-menu>
                <el-sub-menu index="2">
                    <template #title>
                        <svg id="title-icon" t="1732698745410" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7555" width="256" height="256"><path d="M500.224 555.52L139.776 372.224l372.736-215.04L872.448 358.4l-372.224 197.12zM257.024 369.152l242.688 123.392 256-135.168-242.688-135.68-256 147.456z" fill="#000000" p-id="7556"></path><path d="M499.2 694.784L151.552 517.12l25.6-49.664 321.536 164.352 336.384-178.176 26.112 49.152z" fill="#000000" p-id="7557"></path><path d="M499.2 859.136l-347.648-183.808 26.112-49.664 321.024 170.496 335.872-184.32 27.136 48.64z" fill="#000000" p-id="7558"></path></svg>
                        <span>声骸数据</span>
                    </template>
                    <el-menu-item index="2-1" @click="router.push('/echo_stats')">
                        <template #title>
                            <svg id="title-icon" t="1732699401601" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="19165" width="256" height="256"><path d="M960 591.424V368.96c0-0.288 0.16-0.512 0.16-0.768S960 367.68 960 367.424V192a32 32 0 0 0-32-32H96a32 32 0 0 0-32 32v175.424c0 0.288-0.16 0.512-0.16 0.768s0.16 0.48 0.16 0.768v222.464c0 0.288-0.16 0.512-0.16 0.768s0.16 0.48 0.16 0.768V864a32 32 0 0 0 32 32h832a32 32 0 0 0 32-32v-271.04c0-0.288 0.16-0.512 0.16-0.768S960 591.68 960 591.424z m-560-31.232v-160H608v160h-208z m208 64V832h-208v-207.808H608z m-480-224h208v160H128v-160z m544 0h224v160h-224v-160zM896 224v112.192H128V224h768zM128 624.192h208V832H128v-207.808zM672 832v-207.808h224V832h-224z" p-id="19166"></path></svg>
                            <span>角色声骸</span>
                        </template>
                    </el-menu-item>
                    <el-menu-item index="2-2" @click="router.push('/echo_collect')">
                        <template #title>
                            <svg id="title-icon" t="1732699122737" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="12597" width="256" height="256"><path d="M945.3 960.32H38.91V104.03c0-22.47 18.23-40.7 40.7-40.7s40.7 18.23 40.7 40.7v774.88H945.3c22.47 0 40.7 18.23 40.7 40.7s-18.23 40.71-40.7 40.71z" fill="#333333" p-id="12598"></path><path d="M319.1 776.16c-22.47 0-40.7-18.23-40.7-40.7V493.08c0-22.47 18.23-40.7 40.7-40.7s40.7 18.23 40.7 40.7v242.38c0 22.46-18.23 40.7-40.7 40.7zM541.13 776.16c-22.47 0-40.7-18.23-40.7-40.7V266.04c0-22.47 18.23-40.7 40.7-40.7s40.7 18.23 40.7 40.7v469.41c0 22.47-18.23 40.71-40.7 40.71zM773.46 776.16c-22.47 0-40.7-18.23-40.7-40.7V338.28c0-22.47 18.23-40.7 40.7-40.7 22.47 0 40.7 18.23 40.7 40.7v397.18c0 22.46-18.23 40.7-40.7 40.7z" fill="#333333" p-id="12599"></path></svg>
                            <span>声骸汇总</span>
                        </template>
                    </el-menu-item>
                </el-sub-menu>
                <el-menu-item index="3" @click="router.push('/weight_list')">
                    <template #title>
                        <svg id="title-icon" t="1732698641145" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6436" width="256" height="256"><path d="M480 96v416h448c0-21.76-1.664-43.136-4.896-64C893.92 258.944 737.44 111.936 544 97.216 533.44 96.416 522.784 96 512 96h-32z m64 65.44C702.08 175.68 829.984 294.4 858.208 448H544V161.44z" fill="#303133" p-id="6437"></path><path d="M96 512C96 315.296 232.544 150.464 416 107.136v66.112C268.256 215.04 160 350.848 160 512c0 194.4 157.6 352 352 352 172.544 0 316.096-124.16 346.208-288h64.896C892.32 775.36 720 928 512 928 282.24 928 96 741.76 96 512z" fill="#303133" p-id="6438"></path></svg>
                        <span>词条权重</span>
                    </template>
                </el-menu-item>
                <el-menu-item index="4" @click="router.push('/stats_list')">
                    <svg style="padding-right: 10px;width: 18px;height: 18px" t="1732698011907" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2763" id="mx_n_1732698011907" width="256" height="256"><path d="M907.8 226.4l0.1-0.2L526 98.2l-13.4-4.5c-0.4-0.1-0.8-0.1-1.2 0l-13.3 4.5-381.8 128 0.1 0.2c-7.7 3.2-13.4 10.7-13.4 20v509.4c0 0.7 0.4 1.4 1.1 1.7l382 162.1 13.2 5.6 12.1 5.1c0.5 0.2 1 0.2 1.4 0l12.1-5.1 13.2-5.6 382-162.1c0.7-0.3 1.1-0.9 1.1-1.7V246.3c-0.1-9.2-5.8-16.7-13.4-19.9zM483.5 862L156 723c-0.7-0.3-1.1-0.9-1.1-1.7V294.9c0-1.3 1.3-2.2 2.5-1.7l327.5 139c0.7 0.3 1.1 0.9 1.1 1.7v426.4c0 1.3-1.3 2.2-2.5 1.7z m27.8-475L201.9 255.6c-1.5-0.7-1.5-2.9 0.1-3.4l310.1-103.9 310 103.9c1.6 0.5 1.7 2.7 0.1 3.4L512.7 387c-0.4 0.2-1 0.2-1.4 0zM868 723L540.5 862c-1.2 0.5-2.5-0.4-2.5-1.7V433.9c0-0.7 0.4-1.4 1.1-1.7l327.5-139c1.2-0.5 2.5 0.4 2.5 1.7v426.4c0 0.7-0.4 1.4-1.1 1.7z" fill="#303133" p-id="2764"></path></svg>
                    <span>角色基础属性</span>
                </el-menu-item>
                <el-menu-item index="5" @click="router.push('/test')">
                    <template #title>
                        <svg id="title-icon" t="1732698410047" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5275" width="256" height="256"><path d="M288 320h448a32 32 0 0 0 0-64H288a32 32 0 0 0 0 64zM288 544h448a32 32 0 0 0 0-64H288a32 32 0 0 0 0 64zM544 704H288a32 32 0 0 0 0 64h256a32 32 0 0 0 0-64z" p-id="5276" fill="#303133"></path><path d="M896 132.928C896 77.28 851.552 32 796.928 32H227.04C172.448 32 128 77.28 128 132.928v758.144C128 946.72 172.448 992 227.04 992h435.008c1.568 0 2.912-0.672 4.416-0.896 8.96 1.6 18.464-0.256 25.984-6.528l192-160a31.424 31.424 0 0 0 10.816-27.2c0.16-1.184 0.736-2.208 0.736-3.424V132.928zM192 891.072V132.928C192 112.576 207.712 96 227.04 96h569.888C816.288 96 832 112.576 832 132.928V736h-96a96 96 0 0 0-96 96v96H227.04C207.712 928 192 911.424 192 891.072zM814.016 800L704 891.68V832a32 32 0 0 1 32-32h78.016z" p-id="5277" fill="#303133"></path></svg>
                        <span>项目文档</span>
                    </template>
                </el-menu-item>
            </el-menu>
        </el-col>
    </el-row>
</template>



<script lang="ts" setup>
import router from "@/router/index.js";
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const active = ref("");
const route = useRoute();

onMounted(() => {
    let path = route.path
    if (path.includes('/index')) active.value = '1-1';
    if (path.includes('/add_echo')) active.value = '1-1';
    if (path.includes('/temp_echo_list')) active.value = '1-2';
    if (path.includes('/add_temp_echo')) active.value = '1-2';
    if (path.includes('/echo_stats')) active.value = '2-1';
    if (path.includes('/echo_collect')) active.value = '2-2';
    if (path.includes('/weight_list')) active.value = '3';
    if (path.includes('/set_weight')) active.value = '3';
    if (path.includes('/stats_list')) active.value = '4';
    if (path.includes('/test')) active.value = '5';
})
</script>



<style scoped>
.el-menu-vertical-demo {
    height: 100%;
    background-color: #fff;
    border-right: none;
}
.el-row {
    height: 100%;
}
.el-col {
    height: 100%;
}

#title-icon {
    padding-right: 10px;
    width: 18px;
    height: 18px;
}

.title {
    width: 200px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28px;
    background-color: rgb(64,158,255);
    color: white;
}
</style>