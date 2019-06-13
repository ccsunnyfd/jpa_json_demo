<template>
  <div class="hello">
    <h2>Post Response: {{ postResp }}</h2>
    <h2>
      Get Response:
      <el-card class="box-card">
        <div v-for="item in getResp" :key="item.id" class="text item">
          {{ item.gifttime | formatDateTime }}
        </div>
      </el-card>
    </h2>
    <button @click="getData">点我获取数据</button>
  </div>
</template>

<script>
import Config from "@/config.js";

export default {
  name: "HelloWorld",
  data: function() {
    return {
      postResp: null,
      getResp: []
    };
  },
  props: {
    msg: String
  },
  created() {
    this.postData();
  },
  methods: {
    postData() {
      var api = Config.api + "person/addPerson";
      this.axios({
        method: "post",
        url: api,
        data: {
          name: "Michael",
          age: 15
        }
      }).then(
        response => {
          this.postResp = response.data;
        },
        err => {
          window.console.log(err);
        }
      );
    },
    getData() {
      var api = Config.api + "person/getPerson";
      this.axios({
        method: "get",
        url: api,
        params: {
          name: "Michael"
        }
      }).then(
        response => {
          this.getResp = response.data;
        },
        err => {
          window.console.log(err);
        }
      );
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.text {
  font-size: 14px;
}

.item {
  padding: 18px 0;
}

.box-card {
  width: 480px;
}
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
