<template>
  <div class="hello">
    <ul>
      <li>Post Response: {{ postResp }}</li>
      <li>Get Response: {{ getResp }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "HelloWorld",
  data: function() {
    return {
      postResp: null,
      getResp: null
    };
  },
  props: {
    msg: String
  },
  mounted() {
    this.postData();
    this.getData();
  },
  methods: {
    postData() {
      var url = "http://localhost:8080/api/person/addPerson";
      this.$http
        .post(
          url,
          {
            name: "Jack",
            age: 11
          }
          // { emulateJSON: true }
        )
        .then(
          res => {
            if (!res.ok) {
              alert("请求出错!");
            }
            this.postResp = res.body;
          },
          err => {
            window.console.log(err);
          }
        );
    },
    getData() {
      var url = "http://localhost:8080/api/person/getPerson";
      this.$http
        .get(
          url,
          {
            params: {
              name: "Mary"
            }
          }
          // { emulateJSON: true }
        )
        .then(
          res => {
            if (!res.ok) {
              alert("请求出错!");
            }
            this.getResp = res.body;
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
