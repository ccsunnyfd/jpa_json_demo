<template>

  <div class="login">
    <div class="login-con">
      <Card icon="log-in" title="欢迎登录" :bordered="false">
        <div class="form-con">

          <Form ref="loginForm" :model="form" :rules="rules" class="login-container" @keydown.enter.native="handleSubmit">
            <FormItem prop="username">
              <iInput type="text" v-model="form.username" placeholder="请输入用户名">
                <span slot="prepend">
                  <Icon :size="16" type="ios-person"></Icon>
                </span>
              </iInput>
            </FormItem>
            <FormItem prop="password">
              <iInput type="password" v-model="form.password" placeholder="请输入密码">
                <span slot="prepend">
                  <Icon :size="14" type="md-lock"></Icon>
                </span>
              </iInput>
            </FormItem>
            <Checkbox class="login_remember" v-model="checked">记住密码</Checkbox>
            <FormItem>
              <Button type="primary" @click.native.prevent="handleSubmit">登录</Button>
            </FormItem>
          </Form>

        </div>
      </Card>
    </div>
  </div>

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
      form: {
        username: "",
        password: ""
      },
      loading: false
    };
  },
  methods: {
    handleSubmit: function() {
      var _this = this;
      this.loading = true;
      postRequest("/login", {
        username: this.form.username,
        password: this.form.password
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

<style lang="less" scoped>
.login {
  width: 100%;
  height: 100%;
  background-image: url("../assets/images/login-bg.jpg");
  background-size: cover;
  background-position: center;
  position: relative;
  &-con {
    position: absolute;
    right: 30%;
    top: 50%;
    transform: translateY(-40%);
    width: 300px;
    &-header {
      font-size: 16px;
      font-weight: 300;
      text-align: center;
      padding: 30px 0;
    }
    .form-con {
      padding: 10px 0 0;
    }
    .log-in {
      .form-con {
        .loginForm {
          .login_remember {
            font-size: 10px;
            text-align: center;
            color: #c3c3c3;
          }
        }
      }
    }
  }
}
</style>