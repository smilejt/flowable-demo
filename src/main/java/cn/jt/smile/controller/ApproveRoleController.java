package cn.jt.smile.controller;

import cn.jt.smile.bean.params.role.AddAppRoleParams;
import cn.jt.smile.bean.params.role.CheckRoleNameParams;
import cn.jt.smile.bean.params.role.RoleQueryParams;
import cn.jt.smile.bean.params.role.UpdateAppRoleParams;
import cn.jt.smile.bean.vo.ApproveRoleVO;
import cn.jt.smile.service.ApproveRoleService;
import cn.jt.smile.util.Result;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 审批角色配置
 *
 * @author smile
 */
@RestController
@RequestMapping("/api/approveRole")
@Slf4j
public class ApproveRoleController {

    @Resource
    private ApproveRoleService service;

    /**
     * 新增审批角色
     *
     * @param params 参数对象
     * @return 新增结果
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "save")
    public Result<Boolean> save(@Valid @RequestBody AddAppRoleParams params) {
        log.info("[ApproveRoleController].[insert] ------> Approve Role Insert Start, params = {}", JSON.toJSONString(params));
        boolean result = service.insert(params);
        log.info("[ApproveRoleController].[insert] ------> Approve Role Insert End, result = {}", result);
        return Result.success(result);
    }

    /**
     * 修改审批角色
     *
     * @param params 参数对象 {@link UpdateAppRoleParams}
     * @return 新增结果
     */
    @SuppressWarnings("unchecked")
    @PutMapping(value = "update")
    public Result<Boolean> update(@Valid @RequestBody UpdateAppRoleParams params) {
        log.info("[ApproveRoleController].[update] ------> Approve Role Update Start, params = {}", JSON.toJSONString(params));
        boolean result = service.updateRole(params);
        log.info("[ApproveRoleController].[update] ------> Approve Role Update End, result = {}", result);
        return Result.success(result);
    }

    /**
     * 校验角色名称是否重复
     *
     * @param params 校验参数
     * @return 校验结果
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "checkRoleName")
    public Result<Boolean> checkRoleName(@Valid @RequestBody CheckRoleNameParams params) {
        log.info("[ApproveRoleController].[checkRoleName] ------> Approve Role Check Role Name Start, params = {}", JSON.toJSONString(params));
        boolean result = service.checkRoleName(params);
        log.info("[ApproveRoleController].[checkRoleName] ------> Approve Role Check Role Name End, result = {}", result);
        return Result.success(result);
    }

    /**
     * 分页查询审批角色列表
     *
     * @param params 查询参数
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "query/page")
    public Result<IPage<ApproveRoleVO>> getPage(@Valid @RequestBody RoleQueryParams params) {
        log.info("[ApproveRoleController].[getPage] ------> Approve Role Get Page Start, params = {}", JSON.toJSONString(params));
        IPage<ApproveRoleVO> result = service.getPage(params.page(), params);
        log.info("[ApproveRoleController].[getPage] ------> Approve Role Get Page End");
        return Result.success(result);
    }

    /**
     * 根据ID查询
     *
     * @param id 审批角色组ID
     * @return 查询结果对象
     */
    @SuppressWarnings("unchecked")
    @PostMapping(value = "query/{roleId}")
    public Result<ApproveRoleVO> getById(@PathVariable(value = "roleId") String id) {
        log.info("[ApproveRoleController].[getById] ------> Approve Role Get By Id Start, params = {}", id);
        ApproveRoleVO result = service.getById(id);
        log.info("[ApproveRoleController].[update] ------> Approve Role Get By Id End, result = {}", result);
        return Result.success(result);
    }

    /**
     * 删除审批权限组
     *
     * @param id 审批角色组ID
     * @return 删除结果
     */
    @SuppressWarnings("unchecked")
    @DeleteMapping(value = "{roleId}")
    public Result<Boolean> deleteById(@PathVariable(value = "roleId") String id) {
        log.info("[ApproveRoleController].[update] ------> Approve Role Delete By Id Start, params = {}", id);
        boolean result = service.deleteById(id);
        log.info("[ApproveRoleController].[update] ------> Approve Role Delete By Id End, result = {}", result);
        return Result.success(result);
    }
}
