<template>
    <div style="display: flex;flex-direction: column;align-content: center;position: relative">
        <div style="position: absolute;top: 50px;width: 100%;">
            <el-steps :active="active" finish-status="success" align-center>
                <el-step title="验证电子邮件" />
                <el-step title="重新设定密码" />
            </el-steps>
        </div>
        <transition name="el-fade-in-linear" mode="out-in">
            <div style="text-align: center;margin: 0 20px;height: 100%;align-content: center;" v-if="active === 0" v-loading="loading">
                <div>
                    <div style="font-size: 25px;font-weight: bold">重置密码</div>
                    <div style="font-size: 14px;color: grey">请输入需要重置密码的电子邮件地址</div>
                </div>
                <div style="margin-top: 50px">
                    <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
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
                <div style="margin-top: 70px">
                    <el-button @click="startReset()" style="width: 270px;" type="danger" plain>开始重置密码</el-button>
                </div>
                <div id="hcaptcha-container"></div>
            </div>
        </transition>
        <transition name="el-fade-in-linear" mode="out-in">
            <div style="text-align: center;margin: 0 20px;height: 100%;align-content: center;" v-if="active === 1">
                <div>
                    <div style="font-size: 25px;font-weight: bold">重置密码</div>
                    <div style="font-size: 14px;color: grey">请填写您的新密码，务必牢记，防止丢失</div>
                </div>
                <div style="margin-top: 50px">
                    <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
                        <el-form-item prop="password">
                            <el-input v-model="form.password" :maxlength="16" type="password" placeholder="新密码">
                                <template #prefix>
                                    <el-icon><Lock /></el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password_repeat">
                            <el-input v-model="form.password_repeat" :maxlength="16" type="password" placeholder="重复新密码">
                                <template #prefix>
                                    <el-icon><Lock /></el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                    </el-form>
                </div>
                <div style="margin-top: 70px">
                    <el-button @click="doReset()" style="width: 270px;" type="danger" plain>立即重置密码</el-button>
                </div>
            </div>
        </transition>
    </div>
</template>



<script setup>
import { onMounted, reactive, ref } from "vue";
import { EditPen, Lock, Message } from "@element-plus/icons-vue";
import { _POST, GET } from "@/net";
import { ElMessage, ElNotification } from "element-plus";
import router from "@/router";
import { useStore } from "@/stores/index.js";

const active = ref(0)
const form = reactive({
    email: '',
    code: '',
    password: '',
    password_repeat: '',
})

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
    email: [
        { required: true, message: '请输入邮件地址', trigger: 'blur' },
        {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
    ],
    code: [
        { required: true, message: '请输入获取的验证码', trigger: 'blur' },
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur', 'change'] }
    ],
    password_repeat: [
        { validator: validatePassword, trigger: ['blur', 'change'] },
    ],
}

const formRef = ref()
const isEmailValid = ref(false)
const coldTime = ref(0)
const store = useStore(), loading = ref(false)

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

const onValidate = (prop, isValid) => {
    if (prop === 'email')
        isEmailValid.value = isValid
}

const validateEmail = async () => {
    loading.value = true
    try {
        if (!(await GET("/api/auth/check-user", { username: form.email })).data) {
            ElMessage.warning("邮箱账户不存在！")
            return
        }
        if (store.auth.verificationStatus) {
            coldTime.value = 60
            _POST('/api/auth/valid-reset-email', {
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

const startReset = () => {
    formRef.value.validate((isValid) => {
        if (isValid) {
            _POST('/api/auth/start-reset', {
                email: form.email,
                code: form.code
            }, () => {
                active.value++
            }, (data) => {
                ElMessage.warning(data)
            })
        } else { // 如果表单验证未通过
            ElMessage.warning('请填写电子邮件地址和验证码')
        }
    })
}

const doReset = () => {
    formRef.value.validate((isValid) => {
        if (isValid) {
            _POST('/api/auth/do-reset', {
                password: form.password
            }, (message) => {
                ElMessage.success(message)
                router.push('/')
            })
        } else {
            ElMessage.warning('请填写新的密码')
        }
    })
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