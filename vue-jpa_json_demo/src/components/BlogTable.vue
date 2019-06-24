<template>
  <div>
    <!-- 搜索条 -->
    <div style="display: flex;justify-content: flex-start">
      <el-input placeholder="通过标题搜索该分类下的博客..." prefix-icon="el-icon-search" v-model="keyword" style="width: 400px" size="mini"></el-input>
      <el-button type="primary" icon="el-icon-search" size="mini" style="margin-left: 3px" @click="searchClick">搜索</el-button>
    </div>

    <!--<div style="width: 100%;height: 1px;background-color: #20a0ff;margin-top: 8px;margin-bottom: 0px"></div>-->
    <!-- 带选择框的表格 -->
    <el-table ref="multipleTable" :data="articles" tooltip-effect="dark" style="width: 100%;overflow-x: hidden; overflow-y: hidden;" max-height="390" @selection-change="handleSelectionChange" v-loading="loading">

      <!-- 可编辑或可删除状态下，条目前方要有一个多选框供复选 -->
      <el-table-column type="selection" width="35" align="left" v-if="showEdit || showDelete"></el-table-column>

      <!-- 标题栏 -->
      <el-table-column label="标题" width="400" align="left">
        <template slot-scope="scope">
          <span style="color: #409eff;cursor: pointer" @click="itemClick(scope.row)">{{ scope.row.title}}</span>
        </template>
      </el-table-column>

      <!-- 上次编辑时间栏 -->
      <el-table-column label="最近编辑时间" width="140" align="left">
        <template slot-scope="scope">{{ scope.row.editTime | formatDateTime}}</template>
      </el-table-column>

      <!-- 作者栏 -->
      <el-table-column prop="nickname" label="作者" width="120" align="left">
        <template slot-scope="scope">{{ scope.row.user.nickname}}</template>
      </el-table-column>

      <!-- 分类栏 -->
      <el-table-column prop="cateName" label="所属分类" width="120" align="left">
        <template slot-scope="scope">{{ scope.row.category.cateName}}</template>
      </el-table-column>

      <!-- 编辑删除按纽栏 -->
      <el-table-column label="操作" align="left" v-if="showEdit || showDelete">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)" v-if="showEdit">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)" v-if="showDelete">删除</el-button>
        </template>
      </el-table-column>

    </el-table>

    <!-- 页脚 -->
    <div class="blog_table_footer">
      <!-- 复选删除按钮 -->
      <el-button type="danger" size="mini" style="margin: 0px;" v-show="this.articles.length>0 && showDelete" :disabled="this.selItems.length==0" @click="deleteMany">批量删除</el-button>
      <span></span>
      <!-- 分页跳转导航 -->
      <el-pagination background :page-size="pageSize" layout="prev, pager, next" :total="totalCount" @current-change="currentChange" v-show="this.articles.length>0"></el-pagination>
    </div>
  </div>
</template>

<script>
import { putRequest } from "../utils/api";
import { getRequest } from "../utils/api";

export default {
  data() {
    return {
      articles: [],
      selItems: [], // 复选选中的项
      loading: false,
      currentPage: 1,
      totalCount: -1,
      pageSize: 6,
      keyword: "",
      dustbinData: []
    };
  },
  props: ["authority", "articleState", "showEdit", "showDelete", "activeName"], //authority:-2表示管理员权限
  mounted: function() {
    var _this = this;
    this.loading = true;
    this.loadBlogs(1, this.pageSize);
    _this.$bus.on("blogTableReload", function() {
      _this.loading = true;
      _this.loadBlogs(_this.currentPage, _this.pageSize);
    });
  },
  methods: {
    searchClick() {
      this.loadBlogs(1, this.pageSize);
    },
    itemClick(row) {
      this.$router.push({ path: "/blogDetail", query: { aid: row.id } });
    },
    deleteMany() {
      var selItems = this.selItems;
      for (var i = 0; i < selItems.length; i++) {
        this.dustbinData.push(selItems[i].id);
      }
      this.deleteToDustBin(selItems[0].state);
    },
    //翻页(切换页码的操作引起数据重新加载)
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.loading = true;
      this.loadBlogs(currentPage, this.pageSize);
    },
    loadBlogs(page, size) {
      var _this = this;
      var url = "";
      if (this.authority == -2) {
        url =
          "/api/admin/article/all" +
          "?page=" +
          page +
          "&size=" +
          size +
          "&keyword=" +
          this.keyword;
      } else {
        url =
          "/api/article/all?state=" +
          this.articleState +
          "&page=" +
          page +
          "&size=" +
          size +
          "&keyword=" +
          this.keyword;
      }
      getRequest(url)
        .then(
          resp => {
            _this.loading = false;
            if (resp.status == 200) {
              _this.articles = resp.data.articles;
              _this.totalCount = resp.data.totalCount;
            } else {
              _this.$message({ type: "error", message: "数据加载失败!" });
            }
          },
          resp => {
            _this.loading = false;
            if (resp.response.status == 403) {
              _this.$message({ type: "error", message: resp.response.data });
            } else {
              _this.$message({ type: "error", message: "数据加载失败!" });
            }
          }
        )
        .catch(() => {
          //压根没见到服务器
          _this.loading = false;
          _this.$message({ type: "error", message: "找不到服务器!" });
        });
    },
    handleSelectionChange(val) {
      this.selItems = val;
    },
    handleEdit(index, row) {
      this.$router.push({
        path: "/editBlog",
        query: { from: this.activeName, id: row.id }
      });
    },
    handleDelete(index, row) {
      this.dustbinData.push(row.id);
      this.deleteToDustBin(row.authority);
    },
    deleteToDustBin(state) {
      var _this = this;
      this.$confirm(
        state != 2
          ? "将该文件放入回收站，是否继续?"
          : "永久删除该文件, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          _this.loading = true;
          var url = "";
          if (_this.authority == -2) {
            url = "/admin/article/dustbin";
          } else {
            url = "/article/dustbin";
          }
          putRequest(url, { aids: _this.dustbinData, state: state }).then(
            resp => {
              if (resp.status == 200) {
                var data = resp.data;
                _this.$message({ type: data.status, message: data.msg });
                if (data.status == "success") {
                  _this.$bus.emit("blogTableReload"); //通过选项卡都重新加载数据
                }
              } else {
                _this.$message({ type: "error", message: "删除失败!" });
              }
              _this.loading = false;
              _this.dustbinData = [];
            },
            () => {
              _this.loading = false;
              _this.$message({ type: "error", message: "删除失败!" });
              _this.dustbinData = [];
            }
          );
        })
        .catch(() => {
          _this.$message({
            type: "info",
            message: "已取消删除"
          });
          _this.dustbinData = [];
        });
    }
  }
};
</script>

<style type="text/css">
.blog_table_footer {
  display: flex;
  box-sizing: content-box;
  padding-top: 10px;
  padding-bottom: 0px;
  margin-bottom: 0px;
  justify-content: space-between;
}
</style>