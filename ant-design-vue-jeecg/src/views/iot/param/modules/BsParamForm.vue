<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="参数名称" :labelCol="labelCol"
                         :wrapperCol="wrapperCol">
              <a-input v-decorator="['paramName', validatorRules.paramName]"
                       placeholder="请输入参数名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="参数类型" :labelCol="labelCol"
                         :wrapperCol="wrapperCol">
              <a-select v-decorator="['paramType', validatorRules.paramType]"
                        @change="paramTypeChange" placeholder="请选择参数类型">
                <a-select-option :value="1">图片</a-select-option>
                <a-select-option :value="2">文本</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="参数编码" :labelCol="labelCol"
                         :wrapperCol="wrapperCol">
              <a-input v-decorator="['paramCode', validatorRules.paramCode]"
                       placeholder="请输入参数编码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="参数值" :labelCol="labelCol"
                         :wrapperCol="wrapperCol">
              <a-input v-if='paramType === 2' v-decorator="['paramValue',
validatorRules.paramValue]" placeholder="请输入参数值"></a-input>
              <!--<j-image-upload v-decorator="['paramValue',
              validatorRules.paramValue]"/>-->
              <j-image-upload v-else-if='paramType === 1' isMultiple vdecorator="['paramValue', validatorRules.paramValue]"/>
              <span v-else style="font-size: 12px;font-style: italic;">请选择参数类
型，生成对应的表单组件</span>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol"
                         :wrapperCol="wrapperCol">
              <a-textarea rows="3" v-decorator="['content']" placeholder="请输入备
注"></a-textarea>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="状态" :labelCol="labelCol"
                         :wrapperCol="wrapperCol">
              <a-radio-group buttonStyle="solid" v-decorator="['status',
{initialValue: 1}]">
                <a-radio-button :value="1">可用</a-radio-button>
                <a-radio-button :value="0">禁用</a-radio-button>
              </a-radio-group>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align:
center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>


</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
// 引入
import JImageUpload from '@/components/jeecg/JImageUpload'
import ATextarea from "ant-design-vue/es/input/TextArea";
  export default {
    name: 'BsParamForm',
    components: {
      ATextarea,
      JFormContainer,
      JImageUpload
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          paramName: {
            rules: [
              { required: true, message: '请输入参数名称!'},
            ]
          },
          paramType: {
            rules: [
              { required: true, message: '请输入参数类型（1-图片 2-文本）!'},
            ]
          },
          paramCode: {
            rules: [
              { required: true, message: '请输入参数编码!'},
            ]
          },
          paramValue: {
            rules: [
              { required: true, message: '请输入参数值!'},
            ]
          },
        },
        url: {
          add: "/param/bsParam/add",
          edit: "/param/bsParam/edit",
          queryById: "/param/bsParam/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'paramName','paramType','paramCode','paramValue','content','status','createBy','createTime','updateBy','updateTime'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'paramName','paramType','paramCode','paramValue','content','status','createBy','createTime','updateBy','updateTime'))
      },
    }
  }
</script>