package org.jeecg.modules.iot.param.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.iot.param.entity.BsParam;
import org.jeecg.modules.iot.param.service.IBsParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


/**
 * @Description: 系统参数设置
 * @Author: jeecg-boot
 * @Date:   2024-09-18
 * @Version: V1.0
 */
@Api(tags="系统参数设置")
@RestController
@RequestMapping("/param/bsParam")
@Slf4j
public class BsParamController extends JeecgController<BsParam, IBsParamService> {
	@Autowired
	private IBsParamService bsParamService;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsParam
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "系统参数设置-分页列表查询")
	@ApiOperation(value = "系统参数设置-分页列表查询", notes = "系统参数设置-分页列表查询")
			@GetMapping(value = "/list")
			public Result<?> queryPageList(BsParam bsParam,
			@RequestParam(name="pageNo",
					defaultValue="1") Integer pageNo,
			@RequestParam(name="pageSize",
					defaultValue="10") Integer pageSize,
			HttpServletRequest req) {
//QueryWrapper<BsParam> queryWrapper =
		QueryGenerator.initQueryWrapper(bsParam, req.getParameterMap());
// 手动编写代码覆盖官方查询条件（默认官方所以查询条件都是等值判断）
		LambdaQueryWrapper<BsParam> queryWrapper = new LambdaQueryWrapper();
// 参数名称模糊查询
		String paramName = bsParam.getParamName();
		if (StringUtils.isNotEmpty(paramName)) {
			queryWrapper.like(BsParam::getParamName, paramName);
		}
// 参数编码模糊查询
		String paramCode = bsParam.getParamCode();
		if (StringUtils.isNotEmpty(paramCode)) {
			queryWrapper.like(BsParam::getParamCode, paramCode);
		}
// 其他条件都是等值判断
		Integer paramType = bsParam.getParamType();
		if (paramType!= null) {
			queryWrapper.eq(BsParam::getParamType, paramType);
		}
		Integer status = bsParam.getStatus();
		if (status!= null) {
			queryWrapper.eq(BsParam::getStatus, status);
		}
		Page<BsParam> page = new Page<BsParam>(pageNo, pageSize);
		IPage<BsParam> pageList = bsParamService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param bsParam
	 * @return
	 */
	@AutoLog(value = "系统参数设置-添加")
	@ApiOperation(value="系统参数设置-添加", notes="系统参数设置-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsParam bsParam) {
		bsParamService.save(bsParam);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsParam
	 * @return
	 */
	@AutoLog(value = "系统参数设置-编辑")
	@ApiOperation(value="系统参数设置-编辑", notes="系统参数设置-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsParam bsParam) {
		bsParamService.updateById(bsParam);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统参数设置-通过id删除")
	@ApiOperation(value="系统参数设置-通过id删除", notes="系统参数设置-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsParamService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "系统参数设置-批量删除")
	@ApiOperation(value="系统参数设置-批量删除", notes="系统参数设置-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsParamService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "系统参数设置-通过id查询")
	@ApiOperation(value="系统参数设置-通过id查询", notes="系统参数设置-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsParam bsParam = bsParamService.getById(id);
		if(bsParam==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(bsParam);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsParam
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsParam bsParam) {
        return super.exportXls(request, bsParam, BsParam.class, "系统参数设置");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BsParam.class);
    }

}
