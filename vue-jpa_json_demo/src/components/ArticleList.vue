<template>
  <el-container class="article_list">
    <el-main class="main">
      <el-tabs v-model="activeName" @tab-click="handleClick" type="card">
        <!-- authority) -2: 管理员权限 2：普通用户权限 -->
        <!-- articleState) 1: 已发表 0：草稿箱 2：回收站 -1：全部状态 -->
        <!-- 全部文章列表，不可编辑，不可删除 -->
        <el-tab-pane label="全部文章" name="all">
          <blog_table authority="2" articleState="-1" :showEdit="false" :showDelete="false" :activeName="activeName"></blog_table>
        </el-tab-pane>

        <!-- 已发表文章列表 -->
        <el-tab-pane label="已发表" name="post">
          <blog_table authority="2" articleState="1" :showEdit="true" :showDelete="true" :activeName="activeName"></blog_table>
        </el-tab-pane>

        <!-- 草稿箱列表 -->
        <el-tab-pane label="草稿箱" name="draft">
          <blog_table authority="2" articleState="0" :showEdit="true" :showDelete="true" :activeName="activeName"></blog_table>
        </el-tab-pane>

        <!-- 回收站列表，不可编辑 -->
        <el-tab-pane label="回收站" name="dustbin">
          <blog_table authority="2" articleState="2" :showEdit="false" :showDelete="true" :activeName="activeName"></blog_table>
        </el-tab-pane>

        <!-- 博客管理页，管理员权限 -->
        <el-tab-pane label="博客管理" name="blogmana" v-if="isAdmin">
          <blog_table authority="-2" articleState="-2" :showEdit="false" :showDelete="true" :activeName="activeName"></blog_table>
        </el-tab-pane>

        <!-- 博客配置页 -->
        <el-tab-pane label="博客配置" name="blogcfg">
          <blog_cfg></blog_cfg>
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </el-container>
</template>
<script>
import BlogTable from "@/components/BlogTable";
import BlogCfg from "@/components/BlogCfg";
import { getRequest } from "../utils/api";
export default {
  data() {
    return {
      activeName: "post",
      isAdmin: false
    };
  },
  mounted: function() {
    var _this = this;
    getRequest("/isAdmin").then(resp => {
      if (resp.status == 200) {
        _this.isAdmin = resp.data;
      }
    });
  },
  methods: {
    handleClick() {
      // window.console.log(tab, event);
    }
  },
  components: {
    blog_table: BlogTable,
    blog_cfg: BlogCfg
  }
};
</script>
<style>
.article_list > .header {
  background-color: #ececec;
  margin-top: 10px;
  padding-left: 5px;
  display: flex;
  justify-content: flex-start;
}

.article_list > .main {
  /*justify-content: flex-start;*/
  display: flex;
  flex-direction: column;
  padding-left: 0px;
  background-color: #fff;
  padding-top: 0px;
  margin-top: 8px;
}
</style>