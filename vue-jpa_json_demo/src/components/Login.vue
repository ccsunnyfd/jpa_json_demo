<template>

  <Form ref="loginForm" :model="loginForm" :rules="rules" class="login-container">
    <h3 class="login_title">系统登录</h3>
    <FormItem prop="username">
      <iInput type="text" v-model="loginForm.username" placeholder="账号">
        <Icon type="ios-person-outline" slot="prepend"></Icon>
      </iInput>
    </FormItem>
    <FormItem prop="password">
      <iInput type="password" v-model="loginForm.password" placeholder="密码">
        <Icon type="ios-lock-outline" slot="prepend"></Icon>
      </iInput>
    </FormItem>
    <Checkbox class="login_remember" v-model="checked">记住密码</Checkbox>
    <FormItem>
      <Button type="primary" @click.native.prevent="submitClick">登录</Button>
    </FormItem>
  </Form>

</template>

<script>
import { postRequest } from "@/utils/api";

export default {
  name: "Login",
  data: function() {
    return {
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },
      checked: true,
      loginForm: {
        username: "",
        password: ""
      },
      loading: false
    };
  },
  methods: {
    submitClick: function() {
      var _this = this;
      this.loading = true;
      postRequest("/login", {
        username: this.loginForm.username,
        password: this.loginForm.password
      }).then(
        resp => {
          _this.loading = false;
          if (resp.status == 200) {
            //成功
            var json = resp.data;
            if (json.status == "success") {
              _this.$router.replace({ path: "/home" });
            } else {
              _this.$alert("登录认证失败!", "失败!");
            }
          } else {
            //失败
            _this.$alert("登录失败!", "失败!");
          }
        },
        () => {
          _this.loading = false;
          _this.$alert("找不到服务器⊙﹏⊙∥!", "失败!");
        }
      );
    }
  }
};
</script>

<style scoped>
.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #303030;
  border: 1px solid #e5e5e5;
  box-shadow: 0 0 25px #cac6c6;
}

.login_title {
  font-size: 20px;
  margin: 0px auto 40px auto;
  text-align: center;
  color: #ffffff;
}

.login_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
  color: #ffffff;
}
</style>