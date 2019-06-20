import Vue from 'vue'
import App from './App.vue'
import router from './router/router.js'
//import axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import './utils/filter_utils.js'

Vue.use(ElementUI)
//Vue.prototype.axios = axios
Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
