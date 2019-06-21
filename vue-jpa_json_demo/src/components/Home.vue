<template>
  <el-container class="home_container">
    <el-header>
      <div class="home_title">V部落博客管理平台</div>
      <div class="home_userinfoContainer">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link home_userinfo">
            {{currentUserName}}<i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="sysMsg">系统消息</el-dropdown-item>
            <el-dropdown-item command="MyArticle">我的文章</el-dropdown-item>
            <el-dropdown-item command="MyHome">个人主页</el-dropdown-item>
            <el-dropdown-item
              command="logout"
              divided
            >退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
  </el-container>
</template>

<script>
import { getRequest } from "@/utils/api";

export default {
  name: "Home",
  data() {
    return {
      currentUserName: ""
    };
  },
  methods: {
    handleCommand(command) {
      var _this = this;
      if (command == "logout") {
        this.$confirm("注销登录吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(
          function() {
            getRequest("/logout");
            _this.currentUserName = "游客";
            _this.$router.replace({ path: "/" });
          },
          function() {
            //取消
          }
        );
      }
    }
  },
  mounted: function() {
    this.$alert("这是David做的博客系统！", {
      confirmButtonText: "确定",
      callback: () => {}
    });
    var _this = this;
    getRequest("/currentUserNickname").then(
      function(msg) {
        _this.currentUserName = msg.data;
      },
      function() {
        _this.currentUserName = "游客";
      }
    );
  }
};
</script>
<style>
.home_container {
  height: 100%;
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100%;
}

.el-header {
  background-color: #20a0ff;
  color: #333;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-aside {
  background-color: #ececec;
}

.el-main {
  background-color: #fff;
  color: #000;
  text-align: center;
}

.home_title {
  color: #fff;
  font-size: 22px;
  display: inline;
}

.home_userinfo {
  color: #fff;
  cursor: pointer;
}

.home_userinfoContainer {
  display: inline;
  margin-right: 20px;
}
</style>