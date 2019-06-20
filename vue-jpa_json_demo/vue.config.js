module.exports = {
    publicPath: '/',
    outputDir: 'dist',
    chainWebpack: () => { },
    devServer: {
        open: process.platform === 'darwin',
        host: '127.0.0.1',
        port: 9090,
        https: false,
        hotOnly: false,
        // proxy: {
        //     '/login': {
        //         target: 'http://localhost:8080',
        //         changeOrigin: true,
        //         ws: true,
        //         // pathRewrite: {
        //         //     '^/api': ''
        //         // }
        //     }
        // }
    }

}