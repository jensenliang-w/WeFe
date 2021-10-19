const path = require('path');
const webpack = require('webpack');
const packageJson = require('../package.json');
const { original } = JSON.parse(process.env.npm_config_argv);
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const devMode = process.env.NODE_ENV !== 'production';
const context = devMode ? '/' : `${original[3] ? `/${original[3].split('=')[1]}/` : (packageJson.context ? `/${packageJson.context}/` : '/')}`;
const resolve = dir => path.resolve(__dirname, dir);

module.exports = {
    entry: {
        vendor: ['vue', 'vue-router', 'vuex'], // 'element-ui'
    },
    output: {
        path:     resolve(`../dist${context}lib`),
        filename: '[name]_dll_[hash:7].js',
        library:  '[name]_dll_[hash:7]',
    },
    plugins: [
        new CleanWebpackPlugin(),
        new webpack.DllPlugin({
            path: resolve(`../dist${context}lib/[name]-manifest.json`),
            name: '[name]_dll_[hash:7]',
        }),
    ],
};
