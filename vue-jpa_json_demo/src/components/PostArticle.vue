<template>
  <el-container v-loading="loading" class="post-article">
    <el-header class="header">
      <el-select v-model="article.category.id" placeholder="请选择文章栏目" style="width: 150px;">
        <el-option v-for="item in categories" :key="item.id" :label="item.cateName" :value="item.id">
        </el-option>
      </el-select>
      <el-input v-model="article.title" placeholder="请输入标题..." style="width: 400px;margin-left: 10px"></el-input>
      <el-tag :key="tag.id" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)" style="margin-left: 10px">
        {{tag.tagName}}
      </el-tag>
      <el-input class="input-new-tag" v-if="tagInputVisible" v-model="tagValue" ref="saveTagInput" size="small" @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
      </el-input>
      <el-button v-else class="button-new-tag" type="primary" size="small" @click="showInput">+Tag</el-button>
    </el-header>
    <el-main class="main">
      <div id="editor">
        <mavon-editor style="height: 100%;width: 100%;" ref=md @imgAdd="imgAdd" @imgDel="imgDel" v-model="article.mdContent"></mavon-editor>
      </div>
      <div style="display: flex;align-items: center;margin-top: 15px;justify-content: flex-end">
        <el-button @click="cancelEdit" v-if="from!=undefined">放弃修改</el-button>
        <template v-if="from==undefined || from=='draft'">
          <el-button @click="saveBlog(0)">保存到草稿箱</el-button>
          <el-button type="primary" @click="saveBlog(1)">发表文章</el-button>
        </template>
        <template v-else-if="from=='post'">
          <el-button type="primary" @click="saveBlog(1)">保存修改</el-button>
        </template>
      </div>
    </el-main>
  </el-container>
</template>
<script>
//import { postRequest } from "@/utils/api";
import { jPostRequest } from "@/utils/api";
//import { putRequest } from "@/utils/api";
//import { deleteRequest } from "@/utils/api";
import { getRequest } from "@/utils/api";
import { uploadFileRequest } from "@/utils/api";
// Local Registration
import { mavonEditor } from "mavon-editor";
// 可以通过 mavonEditor.markdownIt 获取解析器markdown-it对象
import "mavon-editor/dist/css/index.css";
import { isNotNullORBlank } from "@/utils/utils";

export default {
  data() {
    return {
      categories: [],
      tagInputVisible: false,
      tagValue: "",
      loading: false,
      from: "",
      article: {
        id: "-1", // -1表示默认是新增操作，如果是编辑，那么会在mount时用获取的aid覆盖这里的-1
        tags: "",
        title: "",
        mdContent: "",
        category: {
          id: "",
          cateName: "",
          modifiedTime: null
        }
      },
      dynamicTags: []
    };
  },
  mounted: function() {
    this.getCategories();
    var from = this.$route.query.from;
    this.from = from;
    var _this = this;
    // 如果是从点击编辑按钮导航过来的，需要调用API获取原来的内容展示
    if (isNotNullORBlank(from)) {
      var id = this.$route.query.id;
      this.id = id;
      this.loading = true;
      getRequest("/api/article/" + id).then(
        resp => {
          _this.loading = false;
          if (resp.status == 200) {
            _this.article = resp.data;
            _this.article.category = resp.data.category;
            var tagList = [];
            if (_this.article.tags) {
              tagList = _this.article.tags.split(",");
            }
            for (var i = 0; i < tagList.length; i++) {
              _this.dynamicTags.push({
                id: i,
                tagName: tagList[i]
              });
            }
          } else {
            _this.$message({ type: "error", message: "页面加载失败!" });
          }
        },
        () => {
          _this.loading = false;
          _this.$message({ type: "error", message: "页面加载失败!" });
        }
      );
    }
  },
  components: {
    mavonEditor
  },
  methods: {
    cancelEdit() {
      this.$router.go(-1);
    },
    saveBlog(state) {
      if (
        !isNotNullORBlank(
          this.article.title,
          this.article.mdContent,
          this.article.category
        )
      ) {
        this.$message({ type: "error", message: "数据不能为空!" });
        return;
      }
      var _this = this;
      _this.loading = true;
      var currTagString = "";
      for (var i = 0; i < _this.dynamicTags.length; i++) {
        if (i == _this.dynamicTags.length - 1) {
          currTagString = currTagString + _this.dynamicTags[i].tagName;
        } else {
          currTagString = currTagString + _this.dynamicTags[i].tagName + ",";
        }
      }
      jPostRequest("/api/article/addArticle/", {
        id: _this.article.id,
        title: _this.article.title,
        mdContent: _this.article.mdContent,
        htmlContent: _this.$refs.md.d_render,
        category: {
          id: _this.article.category.id,
          cateName: _this.article.cateName,
          modifiedTime: _this.article.modifiedTime
        },
        state: state,
        tags: currTagString
      }).then(
        resp => {
          _this.loading = false;
          if (resp.status == 200 && resp.data.status == "success") {
            _this.article.id = resp.data.msg;
            _this.$message({
              type: "success",
              message: state == 0 ? "保存成功!" : "发布成功!"
            });
            //            if (_this.from != undefined) {
            this.$bus.emit("blogTableReload");
            //            }
            if (state == 1) {
              _this.$router.replace({ path: "/articleList" });
            }
          }
        },
        () => {
          _this.loading = false;
          _this.$message({
            type: "error",
            message: state == 0 ? "保存草稿失败!" : "博客发布失败!"
          });
        }
      );
    },
    imgAdd(pos, $file) {
      var _this = this;
      // 第一步.将图片上传到服务器.
      var formdata = new FormData();
      formdata.append("image", $file);
      uploadFileRequest("/api/article/uploadimg", formdata).then(resp => {
        var json = resp.data;
        if (json.status == "success") {
          //            _this.$refs.md.$imgUpdateByUrl(pos, json.msg)
          _this.$refs.md.$imglst2Url([[pos, json.msg]]);
        } else {
          _this.$message({ type: json.status, message: json.msg });
        }
      });
    },
    imgDel(pos) {
      window.console.log(pos);
    },
    getCategories() {
      let _this = this;
      getRequest("/api/admin/category/all").then(resp => {
        _this.categories = resp.data;
      });
    },
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
    },
    showInput() {
      this.tagInputVisible = true;
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleInputConfirm() {
      let tagValue = this.tagValue;
      if (isNotNullORBlank(tagValue)) {
        this.dynamicTags.push({
          id: tagValue,
          tagName: tagValue
        });
      }
      this.tagInputVisible = false;
      this.tagValue = "";
    }
  }
};
</script>
<style>
.post-article > .main > #editor {
  width: 100%;
  height: 450px;
  text-align: left;
}

.post-article > .header {
  background-color: #ececec;
  margin-top: 10px;
  padding-left: 5px;
  display: flex;
  justify-content: flex-start;
}

.post-article > .main {
  /*justify-content: flex-start;*/
  display: flex;
  flex-direction: column;
  padding-left: 5px;
  background-color: #ececec;
  padding-top: 0px;
}

.post-article > .header > .el-tag + .el-tag {
  margin-left: 10px;
}

.post-article > .header > .button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.post-article > .header > .input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

/* .post-article {
} */
</style>