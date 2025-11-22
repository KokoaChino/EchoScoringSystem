<template>
    <div class="header">
        <div class="vip">
            <a v-if="!isVip"
               class="enhanced-link"
               @click="vipVisible = true"
               role="button"
               tabindex="0">
                点我成为VIP用户（沙箱环境模拟）
            </a>
            <a v-else
               class="vip-link"
               tabindex="0">
                尊敬的VIP用户，欢迎您！
            </a>
        </div>
        <div class="title"></div>
        <div class="user-info">
            <div class="username">
                <span :class="{ 'vip-username': isVip }">
                    {{ store.auth.user.username }}
                </span>
            </div>
            <el-dropdown trigger="hover" placement="bottom-start">
                <el-button type="text" style="color: white;" class="avatar" :class="{ 'vip-frame': isVip }">
                    <img src="/src/assets/avatars/user.jpg" alt="头像" class="img"/>
                </el-button>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item @click="dialogFormVisible1 = true">修改用户信息</el-dropdown-item>
                        <el-dropdown-item @click="dialogFormVisible2 = true">导入声骸数据</el-dropdown-item>
                        <el-dropdown-item @click="open">注销用户</el-dropdown-item>
                        <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>
    </div>
    <el-dialog v-model="dialogFormVisible1" title="修改用户信息" width="500" v-loading="loading3">
        <el-form :model="form1" :rules="rules1" ref="formRef1" @validate="onValidate">
            <el-form-item label="用户名称" :label-width="formLabelWidth" prop="username">
                <el-input v-model="form1.username" autocomplete="off" :placeholder="store.auth.user.username">
                    <template #prefix>
                        <el-icon><User /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="用户密码" :label-width="formLabelWidth" prop="password">
                <el-input v-model="form1.password" :maxlength="16" autocomplete="off" placeholder="我也不知道你原本的密码是什么">
                    <template #prefix>
                        <el-icon><Lock /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="用户邮箱" :label-width="formLabelWidth" prop="email">
                <el-input v-model="form1.email" autocomplete="off" :placeholder="store.auth.user.email">
                    <template #prefix>
                        <el-icon><Message /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="验证码" :label-width="formLabelWidth" prop="code">
                <el-row :gutter="10" style="width: 100%">
                    <el-col :span="17">
                        <el-input v-model="form1.code" :maxlength="6" type="text" placeholder="仅在修改邮箱时需要">
                            <template #prefix>
                                <el-icon><EditPen /></el-icon>
                            </template>
                        </el-input>
                    </el-col>
                    <el-col :span="5">
                        <el-button class="fixed-size-btn" type="success" @click="sendEmail"
                                   :disabled="!isEmailValid || coldTime > 0">
                            {{coldTime > 0 ? '请稍后 ' + coldTime + ' 秒' : '获取验证码'}}
                        </el-button>
                    </el-col>
                </el-row>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="cancel1">
                    取消
                </el-button>
                <el-button type="primary" @click="submit1">
                    提交
                </el-button>
            </div>
        </template>
    </el-dialog>
    <el-dialog v-model="dialogFormVisible2" title="导入声骸数据" width="500" v-loading="loading4">
        <el-form :model="form2" :rules="rules2" ref="formRef2">
            <el-form-item label="b-at" :label-width="formLabelWidth" prop="bAt">
                <el-input v-model="form2.bAt" autocomplete="off" placeholder="5a1b2c3d4e5f67890123456789abcdef">
                    <template #prefix>
                        <el-icon><Key /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="serverId" :label-width="formLabelWidth" prop="serverId">
                <el-input v-model="form2.serverId" autocomplete="off" placeholder="76402e5b20be2c39f095a152090afddc">
                    <template #prefix>
                        <el-icon><Monitor /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="特征码" :label-width="formLabelWidth" prop="userId">
                <el-input v-model="form2.userId" autocomplete="off" placeholder="123456789">
                    <template #prefix>
                        <el-icon><Operation /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="清空原始数据" :label-width="formLabelWidth" prop="isDelete">
                <el-switch v-model="form2.isDelete" />
            </el-form-item>
        </el-form>
        <div style="margin: 15px 0; font-size: 14px;">
            <el-link href="http://8.138.214.176:5173/ProxyPin%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B.html" target="_blank" :underline="false" type="primary">
                <el-icon style="vertical-align: middle;"><InfoFilled /></el-icon>
                <span style="margin-left: 5px; vertical-align: middle;">使用教程：如何获取b-at, serverld</span>
            </el-link>
        </div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="cancel2">
                    取消
                </el-button>
                <el-button type="primary" @click="submit2">
                    提交
                </el-button>
            </div>
        </template>
    </el-dialog>
    <el-dialog
        :close="close()"
        v-model="vipVisible"
        width="500">
        <div class="dialog" v-if="page === 1">
            <div class="vip-benefits">
                <h3 class="benefits-title">VIP专属特权</h3>
                <ul class="benefits-list">
                    <li class="benefit-item">金色昵称</li>
                    <li class="benefit-item">金色头像框</li>
                </ul>
                <div class="action-section">
                    <a class="enhanced-link upgrade-button"
                       @click="pay(); page = 2"
                       role="button"
                       tabindex="0">
                        立即升级VIP
                    </a>
                </div>
            </div>
        </div>
        <div v-else style="display: flex;align-items: center;justify-content: center;">
            <div class="card" v-loading="loading2">
                <div class="image-container" v-if="loading1" v-loading="loading1">
                    <img src="/qrcode.jpg" alt="支付二维码" class="responsive-image">
                </div>
                <div class="image-container" v-else>
                    <img :src="aliPay.qrcode" alt="支付二维码" class="responsive-image">
                </div>
                <div class="order-number">
                    订单编号：<span>{{ aliPay.id }}</span>
                </div>
                <div class="caption">￥39.99</div>
                <button class="button" @click="pay_query">我已完成支付</button>
            </div>
        </div>
    </el-dialog>
</template>



<script setup>
import { ElDropdown, ElDropdownMenu, ElDropdownItem, ElButton, ElMessage, ElMessageBox, ElLoading } from 'element-plus';
import router from "@/router";
import { getRoleData, getRoleDetail } from '@/net/kurobbsApi.js'
import { _POST, GET, post, POST } from "@/net/index.js";
import { useStore } from "@/stores";
import { reactive, ref, watch, onMounted } from 'vue'
import { EditPen, InfoFilled, Key, Lock, Message, Monitor, Operation, User } from "@element-plus/icons-vue";
import axios from "axios";

const store = useStore();
const dialogFormVisible1 = ref(false), dialogFormVisible2 = ref(false), vipVisible = ref(false)
const formLabelWidth = '100px'
const formRef1 = ref(), formRef2 = ref()
const page = ref(1)
const form1 = reactive({
    username: '',
    password: '',
    email: '',
    code: ''
})
const form2 = reactive({
    bAt: '',
    serverId: '',
    userId: '',
    isDelete: false
})
const isEmailValid = ref(false)
const coldTime = ref(0)
const aliPay = ref({
    id: "",
    username: "",
    qrcode: ""
})
const isVip = ref(false)
const loading1 = ref(true), loading2 = ref(false), loading3 = ref(false), loading4 = ref(false)

watch(dialogFormVisible1, () => {
    reset()
})

const validateUsername = (rule, value, callback) => {
    if (value === '') {
        callback()
        return
    }
    if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
        callback(new Error('用户名不能包含特殊字符，只能是中文 / 英文 / 数字'))
    } else if (value === store.auth.user.username) {
        callback(new Error('新用户名与原用户名一致'))
    } else {
        callback()
    }
}

const validateEmail = (rule, value, callback) => {
    if (value === store.auth.user.email) callback(new Error('新邮箱与原邮箱一致'))
    else callback()
}

const validateCode = (rule, value, callback) => {
    if (form1.email && !value) {
        callback(new Error('请输入新邮箱的验证码'));
    } else {
        callback(); // 验证通过
    }
}

const onValidate = (prop, isValid) => {
    if (prop === 'email')
        isEmailValid.value = isValid
}

const pay = async () => {
    aliPay.value = await POST("/api/pay/pay")
    if (aliPay.value === "") aliPay.value = null
    if (aliPay.value == null) {
        ElMessage.error('服务器繁忙，请稍后再试！')
    } else loading1.value = false
}

const rules1 = {
    username: [
        { validator: validateUsername, trigger: ['blur', 'change'] },
        { min: 2, max: 8, message: '用户名的长度必须在2-8个字符之间', trigger: ['blur', 'change', 'input'] },
    ],
    password: [
        { min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur', 'change', 'input'] }
    ],
    email: [
        { validator: validateEmail, message: '新邮箱与原邮箱一致', trigger: ['blur', 'change', 'input']},
        {  type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change', 'input']}
    ],
    code: [
        { validator: validateCode, trigger: ['blur', 'input'] },
    ],
}
const rules2 = {
    bAt: [
        { required: true, message: '请输入b-at', trigger: 'blur' },
        { min: 32, max: 32, message: 'b-at长度应为32个字符', trigger: 'blur' },
        {
            pattern: /^[a-fA-F0-9]+$/,
            message: 'b-at只能包含十六进制字符(0-9, a-f, A-F)',
            trigger: 'blur'
        }
    ],
    userId: [
        { required: true, message: '请输入特征码', trigger: 'blur' },
        { len: 9, message: '特征码长度应为9位', trigger: 'blur' },
        {
            pattern: /^\d+$/,
            message: '特征码只能包含数字',
            trigger: 'blur'
        }
    ],
    serverId: [
        { required: true, message: '请输入serverId', trigger: 'blur' },
        { min: 32, max: 32, message: 'serverId长度应为32个字符', trigger: 'blur' },
        {
            pattern: /^[a-fA-F0-9]+$/,
            message: 'serverId只能包含十六进制字符(0-9, a-f, A-F)',
            trigger: 'blur'
        }
    ]
}

const sendEmail = async () => {
    loading3.value = true
    try {
        if ((await GET("/api/auth/check-user", { username: form1.email })).data) {
            ElMessage.warning("此邮箱已被注册，请更换邮箱！")
            return
        }
        coldTime.value = 60
        _POST('/api/auth/valid-register-email', {
            email: form1.email
        }, (data) => {
            ElMessage.success(data)
            setInterval(() => coldTime.value--, 1000)
        }, (message) => {
            ElMessage.warning(message)
            coldTime.value = 0
        })
    } catch (e) {
        console.error("邮件发送失败:", e);
    } finally {
        loading3.value = false
    }
}

const open = () => {
    ElMessageBox.confirm(
        '注销用户将永久删除所有信息，操作不可恢复！<br>您确定要继续吗？',
        '警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
            dangerouslyUseHTMLString: true
        }
    )
        .then(() => {
            ElMessage({
                type: 'success',
                message: '注销用户成功',
            })
            signout()
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '注销用户取消',
            })
        })
}

const reset = () => {
    form1['username'] = ''
    form1['password'] = ''
    form1['email'] = ''
    form1['code'] = ''
}

const logout = () => {
    _POST('/api/auth/logout', {}, () => {
        ElMessage.success("退出登录成功");
        store.auth.token = null;
        store.auth.user = null;
        store.auth.verificationStatus = false;
        localStorage.removeItem('store');
        router.push('/');
    }, (err) => {
        ElMessage.error('登出失败：' + err);
    });
};


const signout = () => {
    post("/api/auth/signout")
    logout()
    setTimeout(() => location.reload(), 1000)
}

const cancel1 = () => {
    dialogFormVisible1.value = false
}
const cancel2 = () => {
    dialogFormVisible2.value = false
}

const close = () => {
    aliPay.value = {
        id: "",
        username: "",
        qrcode: ""
    }
    page.value = 1
    loading1.value = true
}

const pay_query = async () => {
    if (aliPay.value == null) {
        ElMessage.error('服务器繁忙，请稍后再试！')
        return
    }
    loading2.value = true
    let res = await POST("/api/pay/query", {
        id: aliPay.value.id
    })
    loading2.value = false
    if (res === 1) {
        isVip.value = true
        await ElMessageBox.alert('感谢您成为尊贵的VIP会员！', '🎉👑 VIP开通成功！')
    } else if (res === 0) {
        await ElMessageBox.alert('您尚未完成支付操作，请继续付款哦~', '🔄💤 支付流程待完成')
    } else {
        await ElMessageBox.alert('您的VIP开通请求已接收，请稍等~', '⏳✨ 支付状态确认中...')
    }
}

const submit1 = async () => {
    formRef1.value.validate(async (isValid) => {
        if (isValid) {
            if (form1['username']) {
                if ((await GET("/api/auth/check-user", { username: form1['username'] })).data) {
                    ElMessage.error("用户名已存在")
                    return
                }
                await POST('/api/auth/change-username', {
                    username: form1.username,
                    email: store.auth.user.email
                })
            }
            if (form1['password']) {
                await POST('/api/auth/change-password', {
                    password: form1.password,
                    email: store.auth.user.email
                })
            }
            let is_P = form1['email']
            if (form1['email']) {
                formRef1.value.validate((isValid) => {
                    if (isValid) {
                        _POST('/api/auth/validate-email', {
                            email: form1.email,
                            code: form1.code
                        },  async () => {
                            await POST("/api/auth/change-email", {
                                oldEmail: store.auth.user.email,
                                newEmail: form1.email,
                            })
                            is_P = false
                        })
                    } else {
                        is_P = false
                        ElMessage.warning('请填写电子邮件地址和验证码')
                    }
                })
            }
            if (form1.password || form1.email || form1.username) {
                const timeout = 10000;
                const startTime = Date.now();
                while (is_P) {
                    if (Date.now() - startTime > timeout) {
                        break;
                    }
                    await new Promise(resolve => setTimeout(resolve, 100)); // 每 100 毫秒检查一次
                }
                ElMessage.success("用户信息修改成功，请重新登陆")
                logout()
                setTimeout(() => location.reload(), 1000)
            } else {
                cancel1()
            }
        } else {
            ElMessage.warning('表单验证未通过')
        }
    })
}
const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms))
const submit2 = async () => {
    formRef2.value.validate(async (isValid) => {
        if (isValid) {
            const loadingInstance = ElLoading.service({
                lock: true,
                text: '数据提交中...',
                background: 'rgba(0, 0, 0, 0.7)'
            })
            try {
                loadingInstance.setText(`正在获取角色列表...`)
                const response = (await getRoleData(form2.bAt, form2.userId, form2.serverId)).data
                if (response.success) {
                    const roleList = JSON.parse(response.data)['roleList']
                    const roleDTOList = []
                    for (let i = 0; i < roleList.length; i++) {
                        let role = roleList[i]
                        loadingInstance.setText(`正在获取角色详情：${i + 1} / ${roleList.length}...`)
                        await sleep(500 + Math.random() * 500)
                        let roleDTO = JSON.parse((await getRoleDetail(form2.bAt, form2.userId, form2.serverId, role['roleId'])).data.data)
                        roleDTOList.push(roleDTO)
                    }
                    loadingInstance.setText(`批量导入声骸中...`)
                    await axios.post("/api/echo/batch-import-echo",
                        roleDTOList,
                        {
                            params: {
                                                    isDelete: form2.isDelete
                            },
                            withCredentials: true
                        }
                    );
                    ElMessage.success('请求成功')
                    if (router.currentRoute.value.path === '/index') {
                        router.go(0)
                    } else {
                        await router.push('/index')
                    }
                } else {
                    ElMessage.error(response.msg)
                }
            } catch (err) {
                ElMessage.error('网络请求失败')
            } finally {
                loadingInstance.close()
                cancel2()
            }
        } else {
            ElMessage.warning('表单验证未通过')
        }
    })
}

onMounted(async () => {
    isVip.value = (await GET("/api/auth/get-user-vip", {})).data
})
</script>



<style scoped>
.header {
    font-size: 28px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
}
.header .title {
    width: 200px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 40px;
}
.img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
}

.user-info {
    display: flex;
    align-items: center;
}
.username {
    display: flex;
    align-items: center;
    justify-content: center;
    color: rgb(50, 50, 50);
}
.username span {
    padding-right: 10px;
    font-size: 15px;
}

.fixed-size-btn {
    width: 115px;
    height: 32px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.enhanced-link {
    color: #C6FF00;
    text-decoration: underline;
    text-decoration-thickness: 1.5px;
    text-underline-offset: 3px;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
    padding: 2px 4px;
    border-radius: 3px;
    outline: transparent solid 1px;
    outline-offset: 2px;
    font-size: 20px;
}
.enhanced-link:hover {
    color: #AEEA00;
    text-decoration-color: currentColor;
}
.enhanced-link:active {
    transform: scale(0.98);
    text-decoration: none;
}
.enhanced-link:focus-visible {
    outline: 2px solid #FF6D00;
    outline-offset: 2px;
}
.enhanced-link:visited {
    color: #8E24AA;
}

.vip-link {
    color: #C6FF00;
    text-decoration: underline;
    text-decoration-thickness: 1.5px;
    text-underline-offset: 3px;
    transition: all 0.2s ease-in-out;
    padding: 2px 4px;
    border-radius: 3px;
    outline: transparent solid 1px;
    outline-offset: 2px;
    font-size: 20px;
    user-select: none;
}
.vip-link:hover {
    color: #AEEA00;
}

.vip {
    position: absolute;
    top: 50%;
    left: 50%;
    display: flex;
    align-items: center;
    transform: translate(-50%, -50%);
}
.vip-benefits {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 12px;
    padding: 2rem;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
    max-width: 500px;
}
.benefits-title {
    color: #1565C0;
    font-size: 1.8rem;
    margin-bottom: 1.5rem;
    text-align: center;
    position: relative;
    padding-bottom: 0.5rem;
}
.benefits-title::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 60px;
    height: 3px;
    background: #FF6D00;
}
.benefits-list {
    list-style: none;
    padding: 0;
    margin: 1.5rem 0;
}
.benefit-item {
    padding: 0.8rem 1.5rem;
    margin: 0.8rem 0;
    background: #f8f9fa;
    border-radius: 6px;
    position: relative;
    transition: all 0.3s ease;
    border-left: 4px solid #2196F3;
}
.benefit-item:hover {
    transform: translateX(10px);
    box-shadow: 2px 2px 8px rgba(33, 150, 243, 0.1);
}
.action-section {
    text-align: center;
    margin-top: 2rem;
}
.upgrade-button {
    font-size: 1.1rem;
    padding: 0.8rem 2rem;
    border-radius: 25px;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: 600;
}

.order-number {
    font-size: 16px;
    color: #666;
    line-height: 1.5;
    font-family: Arial, sans-serif;
    text-align: center;
}

.order-number span {
    color: #007bff;
    margin-left: 4px;
    font-weight: 500;
}

.card {
    position: relative;
    width: 360px;
    background: #ffffff;
    border-radius: 20px;
    padding: 2rem;
    box-shadow: 0 12px 30px rgba(0, 0, 0, 0.08);
    transform: translateY(0);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    overflow: hidden;
}
.card:hover {
    transform: translateY(-5px);
}
.image-container {
    position: relative;
    width: 100%;
    background: #f8f9fa;
    border-radius: 12px;
    margin-bottom: 1.5rem;
    transition: transform 0.3s;
}
.image-container::after {
    content: "";
    position: absolute;
    inset: 0;
    border: 2px dashed rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    pointer-events: none;
}
.responsive-image {
    width: 100%;
    height: auto;
    aspect-ratio: 1/1;
    object-fit: contain;
    border-radius: 8px;
}
.caption {
    text-align: center;
    font-size: 2.5rem;
    color: #2d3436;
    margin: 1rem 0;
    font-weight: 700;
    position: relative;
    display: inline-block;
    width: 100%;
}
.caption::after {
    content: "尊享会员";
    display: block;
    font-size: 1rem;
    color: #6c757d;
    font-weight: 400;
    margin-top: 0.5rem;
}
.button {
    width: 100%;
    padding: 1rem;
    background: #4a90e2;
    color: white;
    border: none;
    border-radius: 10px;
    font-size: 1.1rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s;
    position: relative;
    overflow: hidden;
}
.button::before {
    content: "";
    position: absolute;
    top: 50%;
    left: 50%;
    width: 300px;
    height: 300px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    transform: translate(-50%, -50%) scale(0);
    transition: transform 0.5s ease;
}
.button:hover {
    background: #357abd;
    box-shadow: 0 4px 12px rgba(74, 144, 226, 0.3);
}
.button:active::before {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0;
}
.button:active {
    transform: scale(0.98);
}
@keyframes pulse {
    0% { opacity: 1; }
    50% { opacity: 0.5; }
    100% { opacity: 1; }
}
.loading::after {
    content: "";
    display: inline-block;
    width: 1em;
    height: 1em;
    border: 2px solid white;
    border-radius: 50%;
    border-top-color: transparent;
    animation: spin 0.8s linear infinite;
    margin-left: 8px;
}
@keyframes spin {
    to { transform: rotate(360deg); }
}
@media (max-width: 480px) {
    .card {
        width: 90%;
        padding: 1.5rem;
    }
    .caption {
        font-size: 2rem;
    }
}

.vip-username {
    color: #FFD700;
}
@keyframes vip-glow {
    0% { text-shadow: 0 0 8px rgba(255, 215, 0, 0.6); }
    50% { text-shadow: 0 0 16px rgba(255, 215, 0, 0.8); }
    100% { text-shadow: 0 0 8px rgba(255, 215, 0, 0.6); }
}
.vip-username {
    animation: vip-glow 2s ease-in-out infinite;
}

.vip-frame {
    position: relative;
    padding: 3px !important;
    border-radius: 50%;
}
.vip-frame::before {
    content: '';
    position: absolute;
    top: -2px;
    left: -2px;
    right: -2px;
    bottom: -2px;
    background: linear-gradient(
        45deg,
        #ffd700 0%,
        #ffec8d 25%,
        #ffd700 50%,
        #ffec8d 75%,
        #ffd700 100%
    );
    background-size: 200% auto;
    border-radius: 50%;
    z-index: 1;
    animation: vip-glow 3s linear infinite;
}
.vip-frame .img {
    position: relative;
    z-index: 2;
}
@keyframes vip-glow {
    to {
        background-position: 200% center;
    }
}
</style>