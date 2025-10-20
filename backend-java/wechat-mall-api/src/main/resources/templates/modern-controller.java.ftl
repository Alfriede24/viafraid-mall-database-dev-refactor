package ${package.Controller};

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import com.wechatmall.api.pojo.dto.ApiResultResponse;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.*;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.entity.Users;
import com.wechatmall.api.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 控制器Controller
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@Tag(name = "${table.comment!}管理", description = "${table.comment!}相关的API接口")
@Validated
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} ${table.entityPath}Service;

    @GetMapping("/list")
    @Operation(
            summary = "查询${table.comment!}列表",
            description = "查询所有${table.comment!}记录，不分页",
            responses = {
                @ApiResponse(responseCode = "200", description = "查询成功",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResultResponse.class))
                ),
                @ApiResponse(responseCode = "500", description = "服务器内部错误")
            }
    )
    @ApiOperationSupport(author = "${author}")
    public ApiResultResponse<List<${entity}>> list() {
        List<${entity}> result = ${table.entityPath}Service.list();
        return ApiResultResponse.ok(result);
    }

    @GetMapping("/page")
    @Operation(
        summary = "分页查询${table.comment!}",
        description = "根据条件分页查询${table.comment!}列表，支持排序和筛选",
        parameters = {
            @Parameter(name = "current", description = "当前页码", example = "1",
            schema = @Schema(type = "integer", minimum = "1")),
            @Parameter(name = "size", description = "每页数量", example = "10",
            schema = @Schema(type = "integer", minimum = "1", maximum = "100"))
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "${author}")
    public ApiResultResponse<IPage<${entity}>> page(
            @RequestParam(defaultValue = "1") @Positive(message = "页码必须大于0") Long current,
            @RequestParam(defaultValue = "10") @Positive(message = "每页数量必须大于0") Long size) {
        Page<${entity}> page = new Page<>(current, size);
        IPage<${entity}> result = ${table.entityPath}Service.page(page);
        return ApiResultResponse.page(result);
    }

    @GetMapping("/page/order")
    @Operation(
        summary = "高级分页查询${table.comment!}",
        description = "支持条件筛选、排序的高级分页查询",
        parameters = {
            @Parameter(name = "current", description = "当前页码", example = "1", schema = @Schema(type = "integer", minimum = "1")),
            @Parameter(name = "size", description = "每页数量", example = "10", schema = @Schema(type = "integer", minimum = "1", maximum = "100")),
            @Parameter(name = "sortField", description = "排序字段", example = "id", schema = @Schema(type = "string")),
            @Parameter(name = "sortOrder", description = "排序方式", example = "asc", schema = @Schema(type = "string", allowableValues = {"asc", "desc"}))
        },
        responses={
                @ApiResponse(responseCode = "200", description = "查询成功"),
                @ApiResponse(responseCode = "400", description = "请求参数错误")
        }
    )
    @ApiOperationSupport(author = "${author}")
    public ApiResultResponse<IPage<${entity}>> pageAndOrder(
            @RequestParam(defaultValue = "1") @Positive(message = "页码必须大于0") Long current,
            @RequestParam(defaultValue = "10") @Positive(message = "每页数量必须大于0") Long size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        Page<${entity}> page = new Page<>(current, size);
        
        // 设置排序
        if (sortField != null && !sortField.trim().isEmpty()) {
            if ("desc".equalsIgnoreCase(sortOrder)) {
                page.addOrder(OrderItem.desc(sortField));
            } else {
                page.addOrder(OrderItem.asc(sortField));
            }
        }
        
        IPage<${entity}> result = ${table.entityPath}Service.page(page);
        return ApiResultResponse.page(result);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "根据ID查询${table.comment!}",
        description = "通过主键ID获取${table.comment!}的详细信息",
        responses ={
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "404", description = "用户表不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "${author}")
    public ApiResultResponse<${entity}> getById(@PathVariable @NotNull(message = "ID不能为空") String id) {
        ${entity} result = ${table.entityPath}Service.getById(id);
        if (result == null) {
            return ApiResultResponse.error("${table.comment!}不存在");
        }
        return ApiResultResponse.ok(result);
    }

    @PostMapping
    @Operation(
        summary = "新增${table.comment!}",
        description = "创建一个新的${table.comment!}记录",
        responses = {
            @ApiResponse(responseCode = "200", description = "创建成功",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResultResponse.class),
                            examples = @ExampleObject(value = "{\"code\":200,\"message\":\"创建成功\",\"data\":true,\"timestamp\":\"2024-01-01T12:00:00\"}"))
            ),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "${author}")
    public ApiResultResponse<Boolean> save(
            @RequestBody @Valid ${entity} ${table.entityPath}) {
        boolean result = ${table.entityPath}Service.save(${table.entityPath});
        return result ? ApiResultResponse.ok("创建成功") : ApiResultResponse.error("创建失败");
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "更新${table.comment!}",
        description = "根据ID更新${table.comment!}信息",
        parameters = {
                 @Parameter(name = "id", description = "用户表ID", required = true, example = "1", schema = @Schema(type = "integer", format = "int64"))
        },
        responses = {
                @ApiResponse(responseCode = "200", description = "更新成功",
                                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResultResponse.class))
                ),
                @ApiResponse(responseCode = "400", description = "请求参数错误"),
                @ApiResponse(responseCode = "404", description = "用户表不存在"),
                @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "${author}")
    public ApiResultResponse<Boolean> updateById(
            @PathVariable @NotNull(message = "ID不能为空") String id,
            @RequestBody @Valid ${entity} ${table.entityPath}) {
        ${table.entityPath}.setId(id);
        boolean result = ${table.entityPath}Service.updateById(${table.entityPath});
        return result ? ApiResultResponse.ok("更新成功") : ApiResultResponse.error("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "删除${table.comment!}",
        description = "根据ID删除${table.comment!}记录",
        parameters = {
            @Parameter(name = "id", description = "用户表ID", required = true, example = "1", schema = @Schema(type = "integer", format = "int64"))
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "删除成功",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResultResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "用户表不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "${author}")
    public ApiResultResponse<Boolean> removeById(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        boolean result = ${table.entityPath}Service.removeById(id);
        return result ? ApiResultResponse.ok("删除成功") : ApiResultResponse.error("删除失败");
    }

    @DeleteMapping("/{ids}")
    @Operation(
        summary = "批量删除${table.comment!}",
        description = "根据ID列表批量删除${table.comment!}记录",
        responses = {
            @ApiResponse(responseCode = "200", description = "批量删除成功",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResultResponse.class),
                            examples = @ExampleObject(value = "{\"code\":200,\"message\":\"批量删除成功\",\"data\":true,\"timestamp\":\"2024-01-01T12:00:00\"}"))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "${author}")
    public ApiResultResponse<Boolean> removeByIds(@PathVariable @NotNull(message = "ids不能为空") String ids) {
        if (StrUtil.isNotBlank(ids)) {
                return ApiResultResponse.error("ID列表不能为空");
        }
        boolean result = ${table.entityPath}Service.removeByIds(Arrays.asList(ids.split(",")));
        return result ? ApiResultResponse.ok("批量删除成功") : ApiResultResponse.error("批量删除失败");
    }
}
</#if>