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
    <!-- 左边导航栏，右边内容区，放在一个container里面 -->
    <el-container>
      <!-- 左边垂直导航栏 -->
      <el-aside width="200px">
        <el-menu
          default-active="0"
          class="el-menu-vertical-demo"
          style="background-color: #ECECEC"
          router
        >
          <template v-for="(item, index) in this.$router.options.routes.filter(function (item) {
            return item.meta.menuShow
            })">
            <el-submenu
              :index="index + ''"
              v-if="item.children.length>1"
              :key="index"
            >
              <template slot="title">
                <i :class="item.meta.iconCls"></i>
                <span>{{item.meta.title}}</span>
              </template>
              <el-menu-item
                v-for="child in item.children.filter(function (child) {
                  return child.meta.menuShow
                  })"
                :index="child.path"
                :key="child.path"
              >
                {{child.meta.title}}
              </el-menu-item>
            </el-submenu>
            <template v-else>
              <el-menu-item
                :index="item.children[0].path"
                :key="item.children[0].path"
              >
                <i :class="item.children[0].meta.iconCls"></i>
                <span slot="title">{{item.children[0].meta.title}}</span>
              </el-menu-item>
            </template>
          </template>
        </el-menu>
      </el-aside>

      <!-- 右边显示内容 -->
      <el-container>
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{path:'/home'}">
              首页</el-breadcrumb-item>
            <el-breadcrumb-item v-text="this.$router.currentRoute.meta.title"></el-breadcrumb-item>
          </el-breadcrumb>
          <keep-alive>
            <router-view v-if="this.$route.meta.keepAlive"></router-view>
          </keep-alive>
          <router-view v-if="!this.$route.meta.keepAlive"></router-view>
        </el-main>
      </el-container>
    </el-container>

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
      function(message) {
        if (message.data.status != null) {
          _this.currentUserName = message.data.msg;
        } else {
          _this.currentUserName = message.data;
        }
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