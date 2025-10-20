package ${package.Controller};

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import com.wechatmall.api.common.Result;
#if(${restControllerStyle})
import org.springframework.web.bind.annotation.*;
#else
import org.springframework.stereotype.Controller;
#end
#if(${superControllerClassPackage})
import ${superControllerClassPackage};
#end
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
#if(${restControllerStyle})
@RestController
#else
@Controller
#end
@RequestMapping("#if(${package.ModuleName})/${package.ModuleName}#end/#if(${controllerMappingHyphenStyle})${controllerMappingHyphen}#else${table.entityPath}#end")
@Tag(name = "${table.comment!}", description = "${table.comment!}管理接口")
@Validated
#if(${kotlin})
class ${table.controllerName}#if(${superControllerClass}) : ${superControllerClass}()#end
#else
#if(${superControllerClass})
public class ${table.controllerName} extends ${superControllerClass} {
#else
public class ${table.controllerName} {
#end

    @Autowired
    private ${table.serviceName} ${table.entityPath}Service;

    @GetMapping("/list")
    @Operation(summary = "查询${table.comment!}列表", description = "分页查询${table.comment!}信息")
    @Parameters({
        @Parameter(name = "current", description = "当前页码", example = "1"),
        @Parameter(name = "size", description = "每页数量", example = "10")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public Result<Page<${entity}>> list(@RequestParam(defaultValue = "1") Long current,
                                        @RequestParam(defaultValue = "10") Long size) {
        Page<${entity}> page = new Page<>(current, size);
        Page<${entity}> result = ${table.entityPath}Service.page(page);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询${table.comment!}", description = "根据ID获取${table.comment!}详细信息")
    @Parameter(name = "id", description = "${table.comment!}ID", required = true, example = "1")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "数据不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public Result<${entity}> getById(@PathVariable Long id) {
        ${entity} result = ${table.entityPath}Service.getById(id);
        return Result.success(result);
    }

    @PostMapping
    @Operation(summary = "新增${table.comment!}", description = "创建新的${table.comment!}记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "创建成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public Result<Boolean> save(@RequestBody @Validated ${entity} ${table.entityPath}) {
        boolean result = ${table.entityPath}Service.save(${table.entityPath});
        return Result.success(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新${table.comment!}", description = "根据ID更新${table.comment!}信息")
    @Parameter(name = "id", description = "${table.comment!}ID", required = true, example = "1")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "更新成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "404", description = "数据不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public Result<Boolean> updateById(@PathVariable Long id, @RequestBody @Validated ${entity} ${table.entityPath}) {
        ${table.entityPath}.setId(id);
        boolean result = ${table.entityPath}Service.updateById(${table.entityPath});
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除${table.comment!}", description = "根据ID删除${table.comment!}")
    @Parameter(name = "id", description = "${table.comment!}ID", required = true, example = "1")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "404", description = "数据不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public Result<Boolean> removeById(@PathVariable Long id) {
        boolean result = ${table.entityPath}Service.removeById(id);
        return Result.success(result);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除${table.comment!}", description = "根据ID列表批量删除${table.comment!}")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "删除成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public Result<Boolean> removeByIds(@RequestBody List<Long> ids) {
        boolean result = ${table.entityPath}Service.removeByIds(ids);
        return Result.success(result);
    }
}
#end