package com.wechatmall.api.controller.common;

import com.wechatmall.api.service.IProductTypeofService;
import com.wechatmall.api.pojo.entity.ProductTypeof;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  控制器Controller
 * </p>
 *
 * @author zpc
 * @since 2025-10-15 22:15:48
 */
@RestController
@RequestMapping("/common/product-typeof")
@Tag(name = "商品分类管理", description = "相关的API接口")
@Validated
public class ProductTypeofController {

    @Autowired
    private IProductTypeofService productTypeofService;

    @GetMapping("/list")
    @Operation(
            summary = "查询列表",
            description = "查询所有记录，不分页",
            responses = {
                @ApiResponse(responseCode = "200", description = "查询成功",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResultResponse.class))
                ),
                @ApiResponse(responseCode = "500", description = "服务器内部错误")
            }
    )
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<List<ProductTypeof>> list() {
        List<ProductTypeof> result = productTypeofService.list();
        return ApiResultResponse.ok(result);
    }

    @GetMapping("/page")
    @Operation(
        summary = "分页查询",
        description = "根据条件分页查询列表，支持排序和筛选",
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
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<List<ProductTypeof>> page(
            @RequestParam(defaultValue = "1") @Positive(message = "页码必须大于0") Long current,
            @RequestParam(defaultValue = "10") @Positive(message = "每页数量必须大于0") Long size) {
        Page<ProductTypeof> page = new Page<>(current, size);
        IPage<ProductTypeof> result = productTypeofService.page(page);
        return ApiResultResponse.page(result);
    }

    @GetMapping("/page/order")
    @Operation(
        summary = "高级分页查询",
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
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<List<ProductTypeof>> pageAndOrder(
            @RequestParam(defaultValue = "1") @Positive(message = "页码必须大于0") Long current,
            @RequestParam(defaultValue = "10") @Positive(message = "每页数量必须大于0") Long size,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        Page<ProductTypeof> page = new Page<>(current, size);
        
        // 设置排序
        if (sortField != null && !sortField.trim().isEmpty()) {
            if ("desc".equalsIgnoreCase(sortOrder)) {
                page.addOrder(OrderItem.desc(sortField));
            } else {
                page.addOrder(OrderItem.asc(sortField));
            }
        }
        
        IPage<ProductTypeof> result = productTypeofService.page(page);
        return ApiResultResponse.page(result);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "根据ID查询",
        description = "通过主键ID获取的详细信息",
        responses ={
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "404", description = "用户表不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<ProductTypeof> getById(@PathVariable @NotNull(message = "ID不能为空") String id) {
        ProductTypeof result = productTypeofService.getById(id);
        if (result == null) {
            return ApiResultResponse.error("不存在");
        }
        return ApiResultResponse.ok(result);
    }

    @PostMapping
    @Operation(
        summary = "新增",
        description = "创建一个新的记录",
        responses = {
            @ApiResponse(responseCode = "200", description = "创建成功",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResultResponse.class),
                            examples = @ExampleObject(value = "{\"code\":200,\"message\":\"创建成功\",\"data\":true,\"timestamp\":\"2024-01-01T12:00:00\"}"))
            ),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<Boolean> save(
            @RequestBody @Valid ProductTypeof productTypeof) {
        boolean result = productTypeofService.save(productTypeof);
        return result ? ApiResultResponse.ok("创建成功") : ApiResultResponse.error("创建失败");
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "更新",
        description = "根据ID更新信息",
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
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<Boolean> updateById(
            @PathVariable @NotNull(message = "ID不能为空") Integer id,
            @RequestBody @Valid ProductTypeof productTypeof) {
        productTypeof.setId(id);
        boolean result = productTypeofService.updateById(productTypeof);
        return result ? ApiResultResponse.ok("更新成功") : ApiResultResponse.error("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "删除",
        description = "根据ID删除记录",
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
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<Boolean> removeById(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        boolean result = productTypeofService.removeById(id);
        return result ? ApiResultResponse.ok("删除成功") : ApiResultResponse.error("删除失败");
    }

    @DeleteMapping("/{ids}")
    @Operation(
        summary = "批量删除",
        description = "根据ID列表批量删除记录",
        responses = {
            @ApiResponse(responseCode = "200", description = "批量删除成功",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResultResponse.class),
                            examples = @ExampleObject(value = "{\"code\":200,\"message\":\"批量删除成功\",\"data\":true,\"timestamp\":\"2024-01-01T12:00:00\"}"))),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
        }
    )
    @ApiOperationSupport(author = "zpc")
    public ApiResultResponse<Boolean> removeByIds(@PathVariable @NotNull(message = "ids不能为空") String ids) {
        if (StrUtil.isBlank(ids)) {
                return ApiResultResponse.error("ID列表不能为空");
        }
        boolean result = productTypeofService.removeByIds(Arrays.asList(ids.split(",")));
        return result ? ApiResultResponse.ok("批量删除成功") : ApiResultResponse.error("批量删除失败");
    }
}
