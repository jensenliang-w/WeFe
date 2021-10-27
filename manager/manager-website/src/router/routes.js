/*
 * @author claude
 */

/**
 * @param {meta: hidden} Boolean false           show in menus
 * @param {meta: requiresAuth} Boolean false     no need to login
 * @param {meta: requiresLogout} Boolean true    must to login
 * @param {meta: loginAndRefresh} Boolean        login with dialog refresh current page
 * @param {meta: active} String                  highlight parent path
 * @param {meta: icon} String                    menu icon
 * @param {meta: title} String                   menu title
 * @param {meta: asmenu} Boolean                 show as a menu, no children menu
 * @param {meta: navigation} Boolean             show page fixed navigation on the right
 */
const prefixPath = process.env.NODE_ENV === 'development' ? '/' : `/${process.env.CONTEXT_ENV}/`;

// all routes
const baseRoutes = [
    {
        path: prefixPath,
        meta: {
            asmenu:         true,
            requiresLogout: false,
        },
        component: () => import('@comp/LayoutBase.vue'),
        children:  [
            {
                path: prefixPath,
                name: 'member-list',
                meta: {
                    loginAndRefresh: true,
                    title:           '成员列表',
                },
                component: () => import('../views/account/member-list'),
            },
        ],
    },
    {
        path: prefixPath,
        meta: {
            asmenu:         true,
            requiresLogout: false,
        },
        component: () => import('@comp/LayoutBase.vue'),
        children:  [
            {
                path: `${prefixPath}data-list`,
                name: 'data-list',
                meta: {
                    loginAndRefresh: true,
                    title:           '联邦数据集',
                },
                component: () => import('../views/data-center/data-list'),
            },
            {
                path: `${prefixPath}data-view`,
                name: 'data-view',
                meta: {
                    loginAndRefresh: true,
                    hidden:          true,
                    title:           '联邦数据集详情',
                    active:          `${prefixPath}data-list`,
                },
                component: () => import('../views/data-center/data-view'),
            },
        ],
    },
    {
        path: prefixPath,
        meta: {
            asmenu:         true,
            requiresLogout: false,
        },
        component: () => import('@comp/LayoutBase.vue'),
        children:  [
            {
                path: `${prefixPath}keywords`,
                name: 'keywords',
                meta: {
                    loginAndRefresh: true,
                    title:           '关键词管理',
                },
                component: () => import('../views/data-center/keywords'),
            },
        ],
    },
    {
        path: `${prefixPath}login`,
        name: 'login',
        meta: {
            title:          '登录',
            requiresAuth:   false,
            requiresLogout: true,
        },
        component: () => import('../views/sign/login.vue'),
    },
    {
        path: `${prefixPath}register`,
        name: 'register',
        meta: {
            title:          '注册',
            requiresAuth:   false,
            requiresLogout: true,
        },
        component: () => import('../views/sign/register.vue'),
    },
    {
        path: `${prefixPath}find-password`,
        name: 'find-password',
        meta: {
            title:          '找回密码',
            requiresAuth:   false,
            requiresLogout: true,
        },
        component: () => import('../views/sign/find-password.vue'),
    },
    {
        path: `${prefixPath}notfound`,
        name: 'notfound',
        meta: {
            requiresAuth: false,
            hidden:       true,
        },
        component: () => import('../views/error/404.vue'),
    },
    {
        path: `${prefixPath}forbidden`,
        name: 'forbidden',
        meta: {
            requiresAuth: false,
            hidden:       true,
        },
        component: () => import('../views/error/403.vue'),
    },
    {
        path:     `${prefixPath}:catchAll(.*)`,
        redirect: {
            path: `${prefixPath}notfound`,
        },
    },
];

export default baseRoutes;
