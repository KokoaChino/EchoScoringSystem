<template>
    <div style="text-align: center;margin: 0 20px;align-content: center">
        <div>
            <div style="font-size: 25px;font-weight: bold">注册新用户</div>
            <div style="font-size: 14px;color: grey">欢迎注册声骸评分系统，请在下方填写相关信息</div>
        </div>
        <div style="margin-top: 50px" v-loading="loading">
            <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
                <el-form-item prop="username">
                    <el-input v-model="form.username" :maxlength="8" type="text" placeholder="用户名">
                        <template #prefix>
                            <el-icon><User /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input v-model="form.password" :maxlength="16" type="password" placeholder="密码">
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password_repeat">
                    <el-input v-model="form.password_repeat" :maxlength="16" type="password" placeholder="重复密码">
                        <template #prefix>
                            <el-icon><Lock /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input v-model="form.email" type="email" placeholder="电子邮件地址">
                        <template #prefix>
                            <el-icon><Message /></el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="code">
                    <el-row :gutter="10" style="width: 100%">
                        <el-col :span="17">
                            <el-input v-model="form.code" :maxlength="6" type="text" placeholder="请输入验证码">
                                <template #prefix>
                                    <el-icon><EditPen /></el-icon>
                                </template>
                            </el-input>
                        </el-col>
                        <el-col :span="5">
                            <el-button class="fixed-size-btn" type="success" @click="validateEmail"
                                       :disabled="!isEmailValid || coldTime > 0">
                                {{coldTime > 0 ? '请稍后 ' + coldTime + ' 秒' : '获取验证码'}}
                            </el-button>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>
        </div>
        <div style="margin-top: 80px">
            <el-button style="width: 270px" type="warning" @click="register" plain>立即注册</el-button>
        </div>
        <div style="margin-top: 20px">
            <span style="font-size: 14px;line-height: 15px;color: grey">已有账号? </span>
            <el-link type="primary" style="translate: 0 -2px" @click="router.push('/')">立即登录</el-link>
        </div>
        <div id="hcaptcha-container"></div>
    </div>
</template>



<script setup>
import { EditPen, Lock, Message, User } from "@element-plus/icons-vue";
import router from "@/router";
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElNotification } from "element-plus";
import { _POST, GET } from "@/net";
import { useStore } from "@/stores/index.js";

const store = useStore()

const onVerify = (token) => {
    ElMessage.success("验证成功！")
    store.auth.verificationStatus = true
}

const onError = (err) => {
    ElMessage.error("验证失败！")
}

const initHcaptcha = () => {
    if (window.hcaptcha) {
        window.hcaptcha.render('hcaptcha-container', {
            sitekey: 'eca80d76-ea54-4954-bfae-39ae3dd86776',
            callback: onVerify,
            'error-callback': onError
        })
    }
}

const form = reactive({
    username: '',
    password: '',
    password_repeat: '',
    email: '',
    code: ''
})

const validateUsername = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入用户名'))
    } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
        callback(new Error('用户名不能包含特殊字符，只能是中文 / 英文 / 数字'))
    } else {
        callback()
    }
}

const validatePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== form.password) {
        callback(new Error("两次输入的密码不一致"))
    } else {
        callback()
    }
}

const rules = {
    username: [
        { validator: validateUsername, trigger: ['blur', 'change'] },
        { min: 2, max: 8, message: '用户名的长度必须在2-8个字符之间', trigger: ['blur', 'change'] },
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur', 'change'] }
    ],
    password_repeat: [
        { validator: validatePassword, trigger: ['blur', 'change'] },
    ],
    email: [
        { required: true, message: '请输入邮件地址', trigger: 'blur' },
        {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
    ],
    code: [
        { required: true, message: '请输入获取的验证码', trigger: 'blur' },
    ]
}

const formRef = ref()
const isEmailValid = ref(false)
const coldTime = ref(0), loading = ref(false)

const onValidate = (prop, isValid) => {
    if (prop === 'email')
        isEmailValid.value = isValid
}

const register = () => {
    formRef.value.validate((isValid) => {
        if (isValid) {
            _POST('/api/auth/register', {
                username: form.username,
                password: form.password,
                email: form.email,
                code: form.code
            }, (data) => {
                ElMessage.success(data)
                router.push("/")
            })
        } else {
            ElMessage.warning('请完整填写注册表单内容！')
        }
    })
}

const validateEmail = async () => {
    loading.value = true
    try {
        if ((await GET("/api/auth/check-user", { username: form.email })).data) {
            ElMessage.warning("此邮箱已被注册，请更换邮箱！")
            return
        }
        if (store.auth.verificationStatus) {
            coldTime.value = 60
            _POST('/api/auth/valid-register-email', {
                email: form.email
            }, (data) => {
                ElMessage.success(data)
                setInterval(() => coldTime.value--, 1000)
            }, (data) => {
                ElMessage.warning(data)
                coldTime.value = 0
            })
        } else {
            ElNotification({
                title: '(＃°Д°)',
                message: '请先完成下面的"我是人类"验证！',
                type: 'error',
            })
        }
    } catch (e) {
        ElMessage.error("邮件发送失败：", e)
    } finally {
        loading.value = false
    }
}

onMounted(() => {
    useStore().auth.verificationStatus = false
    const script = document.createElement('script')
    script.src = 'https://js.hcaptcha.com/1/api.js?onload=onloadCallback&render=explicit'
    script.async = true
    script.defer = true
    window.onloadCallback = () => {
        initHcaptcha()
    }
    document.head.appendChild(script)
})
</script>



<style scoped>
.fixed-size-btn {
    width: 105px;
    height: 32px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

#hcaptcha-container {
    margin: 20px 0;
}
</style>