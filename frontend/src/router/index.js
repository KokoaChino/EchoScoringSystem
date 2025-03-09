import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from "@/stores";
import { nextTick } from 'vue';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/common/Welcome.vue'),
            meta: {
                title: '声骸评分系统',
                icon: '/favicon.png'
            },
            children: [
                {
                    path: '',
                    name: 'welcome-login',
                    component: () => import('@/components/welcome/LoginPage.vue'),
                }, {
                    path: 'register',
                    name: 'welcome-register',
                    component: () => import('@/components/welcome/RegisterPage.vue')
                }, {
                    path: 'forget',
                    name: 'welcome-forget',
                    component: () => import('@/components/welcome/ForgetPage.vue')
                }
            ]
        }, {
            path: '/index',
            name: 'index',
            component: () => import('@/views/echo/EchoList.vue'),
            meta: {
                title: '声骸评分系统',
                icon: '/favicon.png'
            }
        }, {
            path: '/temp_echo_list',
            name: 'temp-echo-list',
            component: () => import('@/views/echo/TempEchoList.vue'),
        }, {
            path: '/add_echo',
            name: 'add-echo',
            component: () => import('@/views/echo/AddEcho.vue'),
        }, {
            path: '/add_temp_echo',
            name: 'add-temp-echo',
            component: () => import('@/views/echo/AddTempEcho.vue'),
        }, {
            path: '/set_weight',
            name: 'set-weight',
            component: () => import('@/views/echo/SetWeight.vue'),
        }, {
            path: '/test',
            name: 'test',
            component: () => import('@/views/common/Test.vue')
        }, {
            path: '/weight_list',
            name: 'weight-list',
            component: () => import('@/views/echo/WeightList.vue')
        }, {
            path: '/echo_stats',
            name: 'echo-stats',
            component: () => import('@/views/echo/EchoStats.vue')
        }, {
            path: '/echo_collect',
            name: 'echo-collect',
            component: () => import('@/views/echo/EchoCollect.vue')
        }, {
            path: '/stats_list',
            name: 'stats-list',
            component: () => import('@/views/echo/StatsList.vue')
        }
    ]
})

router.beforeEach((to, from, next) => {
    const store = useStore()
    nextTick(() => {
        if (store.auth.user != null && to.name.startsWith('welcome-')) {
            next('/index')
        } else if (store.auth.user == null && to.fullPath.startsWith('/index')) {
            next('/')
        } else if (to.matched.length === 0) {
            next('/index')
        } else {
            next()
        }
    })
})

export default router