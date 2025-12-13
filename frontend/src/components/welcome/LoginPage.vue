<template>
    <div style="text-align: center;margin: 0 20px;align-content: center">
        <div>
            <div style="font-size: 25px;font-weight: bold">登录</div>
            <div style="font-size: 14px;color: grey">在进入系统之前请先输入用户名和密码进行登录</div>
        </div>
        <div style="margin-top: 50px">
            <el-input v-model="form.username" type="text" placeholder="用户名 / 邮箱">
                <template #prefix>
                    <el-icon><User /></el-icon>
                </template>
            </el-input>
            <el-input v-model="form.password" type="password" style="margin-top: 10px" placeholder="密码">
                <template #prefix>
                    <el-icon><Lock /></el-icon>
                </template>
            </el-input>
        </div>
        <el-row style="margin-top: 5px">
            <el-col :span="24" style="text-align: right">
                <el-link @click="router.push('/forget')">忘记密码？</el-link>
            </el-col>
        </el-row>
        <div style="margin-top: 40px">
            <el-button @click="login()" style="width: 270px" type="success" plain>立即登录</el-button>
        </div>
        <el-divider>
            <span style="color: grey;font-size: 13px">没有账号</span>
        </el-divider>
        <div>
            <el-button style="width: 270px" @click="router.push('/register')" type="warning" plain>注册账号</el-button>
        </div>
    </div>
</template>



<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { reactive } from "vue";
import { ElMessage } from "element-plus";
import { post } from "@/net";
import router from "@/router";
import { useStore } from "@/stores";

const store = useStore()
const form = reactive({
    username: '',
    password: ''
})

const login = async () => {
    if (!form.username || !form.password) {
        ElMessage.warning('请填写用户名和密码！')
        return
    }
    try {
        const res = await post('/api/auth/login', {
            username: form.username,
            password: form.password
        });
        if (!res || !res.data) {
            ElMessage.error('登录失败：无返回数据');
            return;
        }
        if (res.status !== 200) {
            ElMessage.error('用户名或密码错误');
            return;
        }
        const { token, user } = res.data;
        store.auth.token = token;
        store.auth.user = user;
        await router.push('/index');
    } catch (error) {
        ElMessage.error('登录失败：' + (error.response?.data?.message || error.message || '未知错误'));
    }
}
</script>



<style scoped>

</style>