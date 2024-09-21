package org.jeecg.modules.iot.inv.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;

/**
 * @Description: inv_task
 * @Author: jeecg-boot
 * @Date:   2024-09-21
 * @Version: V1.0
 */
@Data
@TableName("inv_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="inv_task对象", description="inv_task")
public class InvTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序号*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "序号")
    private Integer id;
	/**盘点编号*/
	@Excel(name = "盘点编号", width = 15)
    @ApiModelProperty(value = "盘点编号")
    private Integer num;
	/**盘点人*/
	@Excel(name = "盘点人", width = 15)
    @ApiModelProperty(value = "盘点人")
    private String creator;
	/**状态(0-未开始，1-盘点中)*/
	@Excel(name = "状态(0-未开始，1-盘点中)", width = 15)
    @ApiModelProperty(value = "状态(0-未开始，1-盘点中)")
    private String status;
	/**盘点时间*/
	@Excel(name = "盘点时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "盘点时间")
    private Date plannedTime;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
	/**盘点范围(0-指定设备)*/
	@Excel(name = "盘点范围(0-指定设备)", width = 15)
    @ApiModelProperty(value = "盘点范围(0-指定设备)")
    private Integer invScope;
	/**盘点情况*/
	@Excel(name = "盘点情况", width = 15)
    @ApiModelProperty(value = "盘点情况")
    private String invSituation;
	/**盘点方式(0-手动盘点,1-扫码盘点,2-REID盘点)*/
	@Excel(name = "盘点方式(0-手动盘点,1-扫码盘点,2-REID盘点)", width = 15)
    @ApiModelProperty(value = "盘点方式(0-手动盘点,1-扫码盘点,2-REID盘点)")
    private Integer invMethod;
	/**是否自盘(0-允许责任人自盘)*/
	@Excel(name = "是否自盘(0-允许责任人自盘)", width = 15)
    @ApiModelProperty(value = "是否自盘(0-允许责任人自盘)")
    private Integer isSelfinv;
	/**是否审批*/
	@Excel(name = "是否审批", width = 15)
    @ApiModelProperty(value = "是否审批")
    private Integer reviewApproved;
}
