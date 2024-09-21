<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="参数名称">
              <a-input placeholder="请输入参数名称" v-model="queryParam.paramName">
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="参数类型">
              <a-select placeholder="请选择参数类型" vmodel="queryParam.paramType">
                <a-select-option value="1">图片</a-select-option>
                <a-select-option value="2">文本</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="参数编码">
                <a-input placeholder="请输入参数编码" vmodel="queryParam.paramCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <a-select placeholder="请选择状态" v-model="queryParam.status">
                  <a-select-option value="1">可用</a-select-option>
                  <a-select-option value="0">禁用</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
<span style="float: left;overflow: hidden;" class="table-pagesearch-submitButtons">
<a-button type="primary" @click="searchQuery" icon="search">查询
</a-button>
<a-button type="primary" @click="searchReset" icon="reload"
          style="margin-left: 8px">重置</a-button>
<a @click="handleToggleSearch" style="margin-left: 8px">
{{ toggleSearchStatus ? '收起' : '展开' }}
<a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
</a>
</span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->


    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('系统参数设置')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bs-param-modal ref="modalForm" @ok="modalFormOk"></bs-param-modal>
  </a-card>
</template>

<script>

  import '@assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import BsParamModal from './modules/BsParamModal.vue'

  export default {
    name: 'BsParamList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      BsParamModal
    },
    data () {
      return {
        description: '系统参数设置管理页面',
        // 表头
        columns: [
          {
            title:'参数名称',
            align:"center",
            dataIndex: 'paramName'
          },
          {
            title:'参数类型',
            align:"center",
            dataIndex: 'paramType',
            scopedSlots: {
              customRender: "paramTypeString"
            }
          },
          {
            title:'参数编码',
            align:"center",
            dataIndex: 'paramCode'
          },
          {
            title:'参数值',
            align:"center",
            dataIndex: 'paramValue'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'content'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: {
              customRender: "statusString"
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/param/bsParam/list",
          delete: "/param/bsParam/delete",
          deleteBatch: "/param/bsParam/deleteBatch",
          exportXlsUrl: "/param/bsParam/exportXls",
          importExcelUrl: "param/bsParam/importExcel",
          
        },
        dictOptions:{},
      }
    },
    created() {
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>