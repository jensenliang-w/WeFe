/*!
 * @author claude
 * 用户认证相关
 */

const prefixPath = process.env.NODE_ENV === 'development' ? '/' : `/${process.env.CONTEXT_ENV}/`;

export const setStorage = () => {
    return localStorage;
};

/**
 * 清空用户信息
 */
export const clearUserInfo = () => {
    const { baseUrl } = window.api;

    setStorage().removeItem(`${baseUrl}_userInfo`);
};

/**
 * 检测登录状态
 */
export const baseIsLogin = () => {
    const { baseUrl } = window.api;
    const userInfo = setStorage().getItem(`${baseUrl}_userInfo`);

    if (userInfo) {
        // 同步存储信息
        syncLogin(userInfo);
    } else {
        clearUserInfo();
    }
    return Boolean(userInfo);
};

/**
 * 同步登录
 */
export const syncLogin = async (userInfo = {}) => {

    if (userInfo.token) {
        const { $route, $router } = window.$app;

        if ($route.meta.requiresLogout) {
            const { href } = window.location;
            const url = window.decodeURIComponent(href);
            const target = url.split('?redirect=')[1];

            $router.replace(target || prefixPath);
        } else {
            window.location.reload();
        }
    }
};

/**
 * 强制用户下线, 清除登录信息并跳转到登录页面
 */
export const baseLogout = () => {

    const { $router } = window.$app;
    const { location: { href, pathname } } = window;
    const { baseUrl } = window.api;
    const userInfo = setStorage().getItem(`${baseUrl}_userInfo`);
    // 重置 store 和 localstorage
    clearUserInfo();
    let json = eval('[' + userInfo + ']');
    window.$app.$http.get({
        url: '/logout?token=' + json[0].token,
    });

    let query = {};

    if ($router.currentRoute.path !== prefixPath && $router.currentRoute.path !== pathname && !href.includes('?redirect=')) {
        query = {
            redirect: $router.currentRoute.fullPath,
        };
    }

    $router.replace({
        name: 'login',
        query,
    });
};

/**
 * 同步多标签用户状态
 */
export const syncTabsUserState = async () => {
    const { baseUrl } = window.api;

    // 未登录
    window.addEventListener('storage', (e) => {

        if (e.key === setStorage().getItem(`${baseUrl}_userInfo`)) {

            if (e.newValue) {
                // 已登录
                syncLogin(e.newValue);

            } else {
                // 未登录或已过期
                baseLogout();
            }
        }
    });
};
