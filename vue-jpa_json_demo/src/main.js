import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'
import './utils/filter_utils.js'

Vue.prototype.axios = axios
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
