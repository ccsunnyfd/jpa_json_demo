import Vue from 'vue'
import App from './App.vue'
import router from './router/router.js'
import VueBus from 'vue-bus'
//import axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.min.css'

import iView from 'iview'
import 'iview/dist/styles/iview.css'

import './utils/filter_utils.js'

Vue.use(ElementUI)
Vue.use(iView)
Vue.use(VueBus)
//Vue.prototype.axios = axios
Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
