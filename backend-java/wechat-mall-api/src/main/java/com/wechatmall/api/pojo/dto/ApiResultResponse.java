package com.wechatmall.api.pojo.dto;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.catalina.connector.Response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 返回值传输实体
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/20 09:35
 */
@Data
@Schema(name="ApiResultResponse",description = "统一响应格式")
public class ApiResultResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "响应信息",example="查询成功")
    private String message;
    @Schema(description = "响应码",example="200")
    private int code;
    @Schema(description = "数据实体",example = "[]")
    private T data;
    @Schema(description = "分页总数")
    private Long total;
    @Schema(description = "当前页")
    private Long current;
    @Schema(description = "当前页条数")
    private Long size;

    public static <T> ApiResultResponse<T> ok(T data){
        return new ApiResultResponse<T>("", Response.SC_OK, data);
    }
    public static <T> ApiResultResponse<T> ok(String message){
        return new ApiResultResponse<T>(message, Response.SC_OK, null);
    }

    public static <T> ApiResultResponse<T> ok(String message,T data){
        return new ApiResultResponse<T>(message, Response.SC_OK, data);
    }

    public static <T> ApiResultResponse<T> error(String message){
        return new ApiResultResponse<T>(message,Response.SC_INTERNAL_SERVER_ERROR,null);
    }

    public static <T> ApiResultResponse<T> error(String message,int code){
        return new ApiResultResponse<T>(message,code,null);
    }

    public static <T> ApiResultResponse<List<T>> list(String message, List<T> data){
      return new ApiResultResponse<List<T>>(message, Response.SC_OK, data);
    }

    public static <T> ApiResultResponse<List<T>> list(List<T> data){
        return new ApiResultResponse<List<T>>("查询成功", Response.SC_OK, data);
    }

    public static <T> ApiResultResponse<IPage<T>> page(String message, IPage<T> data){
             return new ApiResultResponse<IPage<T>>(message, Response.SC_OK, data);
    }

    public static <T> ApiResultResponse<List<T>> page(IPage<T> data){
        ApiResultResponse<List<T>> response = new ApiResultResponse<List<T>>("分页查询成功",Response.SC_OK,data.getRecords());
        BeanUtil.copyProperties(data,response);
        return response;
    }

    public ApiResultResponse(String message, int code, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }
}
