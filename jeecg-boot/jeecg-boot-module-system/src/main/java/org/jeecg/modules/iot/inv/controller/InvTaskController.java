package org.jeecg.modules.iot.inv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.iot.inv.entity.InvTask;
import org.jeecg.modules.iot.inv.service.IInvTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: inv_task
 * @Author: jeecg-boot
 * @Date:   2024-09-21
 * @Version: V1.0
 */
@Api(tags="inv_task")
@RestController
@RequestMapping("/inv/invTask")
@Slf4j
public class InvTaskController extends JeecgController<InvTask, IInvTaskService> {
	@Autowired
	private IInvTaskService invTaskService;
	
	/**
	 * 分页列表查询
	 *
	 * @param invTask
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "inv_task-分页列表查询")
	@ApiOperation(value="inv_task-分页列表查询", notes="inv_task-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(InvTask invTask,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<InvTask> queryWrapper = QueryGenerator.initQueryWrapper(invTask, req.getParameterMap());
		Page<InvTask> page = new Page<InvTask>(pageNo, pageSize);
		IPage<InvTask> pageList = invTaskService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param invTask
	 * @return
	 */
	@AutoLog(value = "inv_task-添加")
	@ApiOperation(value="inv_task-添加", notes="inv_task-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody InvTask invTask) {
		invTaskService.save(invTask);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param invTask
	 * @return
	 */
	@AutoLog(value = "inv_task-编辑")
	@ApiOperation(value="inv_task-编辑", notes="inv_task-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody InvTask invTask) {
		invTaskService.updateById(invTask);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "inv_task-通过id删除")
	@ApiOperation(value="inv_task-通过id删除", notes="inv_task-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		invTaskService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "inv_task-批量删除")
	@ApiOperation(value="inv_task-批量删除", notes="inv_task-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.invTaskService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "inv_task-通过id查询")
	@ApiOperation(value="inv_task-通过id查询", notes="inv_task-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		InvTask invTask = invTaskService.getById(id);
		if(invTask==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(invTask);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param invTask
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, InvTask invTask) {
        return super.exportXls(request, invTask, InvTask.class, "inv_task");
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
        return super.importExcel(request, response, InvTask.class);
    }

}
