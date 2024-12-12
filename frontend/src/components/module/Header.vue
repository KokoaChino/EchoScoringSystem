<template>
    <div class="header">
        <div class="title"></div>
        <div class="user-info">
            <div class="username">
                <span>
                    {{ store.auth.user.username }}
                </span>
            </div>
            <el-dropdown trigger="hover" placement="bottom-start">
                <el-button type="text" style="color: white;" class="avatar">
                    <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="头像" class="img"/>
                </el-button>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item @click="dialogFormVisible = true">修改用户信息</el-dropdown-item>
                        <el-dropdown-item @click="open">注销用户</el-dropdown-item>
                        <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
        </div>
    </div>
    <el-dialog v-model="dialogFormVisible" title="修改用户信息" width="500">
        <el-form :model="form" :rules="rules" ref="formRef" @validate="onValidate">
            <el-form-item label="用户名称" :label-width="formLabelWidth" prop="username">
                <el-input v-model="form.username" autocomplete="off" :placeholder="store.auth.user.username">
                    <template #prefix>
                        <el-icon><User /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="用户密码" :label-width="formLabelWidth" prop="password">
                <el-input v-model="form.password" :maxlength="16" autocomplete="off" placeholder="我也不知道你原本的密码是什么">
                    <template #prefix>
                        <el-icon><Lock /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="用户邮箱" :label-width="formLabelWidth" prop="email">
                <el-input v-model="form.email" autocomplete="off" :placeholder="store.auth.user.email">
                    <template #prefix>
                        <el-icon><Message /></el-icon>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="验证码" :label-width="formLabelWidth" prop="code">
                <el-row :gutter="10" style="width: 100%">
                    <el-col :span="17">
                        <el-input v-model="form.code" :maxlength="6" type="text" placeholder="仅在修改邮箱时需要">
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
                <el-button @click="cancel">
                    取消
                </el-button>
                <el-button type="primary" @click="submit">
                    提交
                </el-button>
            </div>
        </template>
    </el-dialog>
</template>



<script setup>
import { ElDropdown, ElDropdownMenu, ElDropdownItem, ElButton, ElMessage, ElMessageBox } from 'element-plus';
import router from "@/router";
import { _GET, _POST, post, POST } from "@/net/index.js";
import { useStore } from "@/stores";
import { reactive, ref, watch } from 'vue'
import { EditPen, Lock, Message, User } from "@element-plus/icons-vue";

const store = useStore();
const dialogFormVisible = ref(false)
const formLabelWidth = '70px'
const formRef = ref()
const form = reactive({
    username: '',
    password: '',
    email: '',
    code: ''
})
const isEmailValid = ref(false)
const coldTime = ref(0)

watch(dialogFormVisible, () => {
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
    if (form.email && !value) {
        callback(new Error('请输入新邮箱的验证码'));
    } else {
        callback(); // 验证通过
    }
}

const onValidate = (prop, isValid) => {
    if(prop === 'email')
        isEmailValid.value = isValid
}

const rules = {
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

const sendEmail = () => {
    coldTime.value = 60
    _POST('/api/auth/valid-register-email', {
        email: form.email
    }, (message) => {
        ElMessage.success(message)
        setInterval(() => coldTime.value--, 1000)
    }, (message) => {
        ElMessage.warning(message)
        coldTime.value = 0
    })
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
    form['username'] = ''
    form['password'] = ''
    form['email'] = ''
    form['code'] = ''
}

const logout = () => {
    _GET('/api/auth/logout', (message) => {
        ElMessage.success(message);
        store.auth.user = null;
        router.push('/');
    });
};

const signout = () => {
    post("/api/auth/signout", store.auth.user.username)
    logout()
    setTimeout(() => location.reload(), 1000)
}

const cancel = () => {
    dialogFormVisible.value = false
}

const submit = async () => {
    formRef.value.validate(async (isValid) => {
        if (isValid) {
            if (form['username']) {
                await POST('/api/auth/change-username', {
                    username: form.username,
                    email: store.auth.user.email
                })
            }
            if (form['password']) {
                await POST('/api/auth/change-password', {
                    password: form.password,
                    email: store.auth.user.email
                })
            }
            let is_P = form['email']
            if (form['email']) {
                formRef.value.validate( (isValid) => {
                    if (isValid) {
                        _POST('/api/auth/validate-email', {
                            email: form.email,
                            code: form.code
                        },  async () => {
                            await POST("/api/auth/change-email", {
                                oldEmail: store.auth.user.email,
                                newEmail: form.email,
                            })
                            is_P = false
                        })
                    } else {
                        is_P = false
                        ElMessage.warning('请填写电子邮件地址和验证码')
                    }
                })
            }
            if (form.password || form.email || form.username) {
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
                cancel()
            }
        } else {
            ElMessage.warning('表单验证未通过')
        }
    })
}
</script>



<style scoped>
.header {
    font-size: 28px;
    display: flex;
    justify-content: space-between;
    align-items: center;
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
</style>