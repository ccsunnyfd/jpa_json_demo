<template>
  <div style="display: flex;height: 500px;width: 100%;align-items: center;justify-content: center;">
    <chart ref="dschart" :options="polar" style="margin-top: 20px"></chart>
  </div>
</template>

<script>
import ECharts from "vue-echarts/components/ECharts.vue";
import "echarts/lib/chart/line";
import "echarts/lib/component/tooltip";

import "echarts/lib/component/polar";

import "echarts/lib/component/legend";
import "echarts/lib/component/title";
import "echarts/theme/dark";
import "echarts/lib/chart/bar";

import { getRequest } from "../utils/api";
export default {
  components: {
    chart: ECharts
  },
  data: function() {
    return {
      polar: {
        title: {
          text: ""
        },
        toolbox: {
          show: true,
          feature: {
            dataZoom: {
              yAxisIndex: "none"
            },
            dataView: {
              readOnly: false
            },
            magicType: {
              type: ["line", "bar"]
            },
            restore: {},
            saveAsImage: {}
          }
        },
        tooltip: {},
        legend: {
          data: ["pv"]
        },
        xAxis: {
          data: []
        },
        yAxis: {},
        series: [
          {
            name: "pv",
            type: "line",
            data: []
          }
        ],
        animationDuration: 3000
      }
    };
  },
  mounted: function() {
    var _this = this;
    getRequest("/api/article/dataStatistics").then(
      resp => {
        if (resp.status == 200) {
          _this.$refs.dschart.options.xAxis.data = resp.data.categories;
          _this.$refs.dschart.options.series[0].data = resp.data.ds;
        } else {
          _this.$message({ type: "error", message: "数据加载失败!" });
        }
      },
      () => {
        _this.$message({ type: "error", message: "数据加载失败!" });
      }
    );
  },
  methods: {}
};
</script>

<style>
</style>