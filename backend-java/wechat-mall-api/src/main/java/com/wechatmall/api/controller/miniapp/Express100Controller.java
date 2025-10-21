package com.wechatmall.api.controller.miniapp;

import cn.hutool.core.util.StrUtil;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.dto.express.ExpressCompanyDTO;
import com.wechatmall.api.pojo.dto.express.ExpressQueryNodeDTO;
import com.wechatmall.api.pojo.dto.express.ExpressQueryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 快递100查询模拟接口
 */
@RestController
@RequestMapping("/Express100")
@Validated
@Tag(name = "快递100", description = "为小程序端提供模拟快递查询")
public class Express100Controller {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final List<ExpressCompanyDTO> COMPANIES = List.of(
        ExpressCompanyDTO.builder().name("顺丰速运").code("shunfeng").build(),
        ExpressCompanyDTO.builder().name("中通快递").code("zhongtong").build(),
        ExpressCompanyDTO.builder().name("圆通速递").code("yuantong").build(),
        ExpressCompanyDTO.builder().name("韵达速递").code("yunda").build(),
        ExpressCompanyDTO.builder().name("申通快递").code("shentong").build(),
        ExpressCompanyDTO.builder().name("京东快递").code("jd").build(),
        ExpressCompanyDTO.builder().name("邮政EMS").code("ems").build()
    );

    @GetMapping("/companies")
    @Operation(summary = "支持的快递公司列表", description = "返回快递100可查询的快递公司编码")
    public ApiResultResponse<List<ExpressCompanyDTO>> companies() {
        return ApiResultResponse.ok("查询成功", COMPANIES);
    }

    @GetMapping("/query")
    @Operation(summary = "快递查询", description = "根据单号返回模拟快递查询结果")
    public ApiResultResponse<ExpressQueryResponseDTO> query(
        @RequestParam("trackingNumber") String trackingNumber,
        @RequestParam(value = "companyCode", required = false) String companyCode,
        @RequestParam(value = "phone", required = false) String phone) {

        if (StrUtil.isBlank(trackingNumber)) {
            return ApiResultResponse.error("快递单号不能为空", 400);
        }

        String state = "5";
        List<ExpressQueryNodeDTO> nodes = buildMockNodes();
        ExpressQueryResponseDTO responseDTO = ExpressQueryResponseDTO.builder()
            .com(StrUtil.blankToDefault(companyCode, "shunfeng"))
            .nu(trackingNumber)
            .state(state)
            .ischeck(false)
            .data(nodes)
            .build();

        return ApiResultResponse.ok("查询成功", responseDTO);
    }

    private List<ExpressQueryNodeDTO> buildMockNodes() {
        LocalDateTime now = LocalDateTime.now();
        return Arrays.asList(
            ExpressQueryNodeDTO.builder()
                .time(FORMATTER.format(now.minusHours(1)))
                .context("【深圳南山营业部】正在为您派件，请保持电话畅通")
                .status("派送中")
                .location("深圳市南山区")
                .build(),
            ExpressQueryNodeDTO.builder()
                .time(FORMATTER.format(now.minusHours(6)))
                .context("快件已到达【深圳南山营业部】")
                .status("到达网点")
                .location("深圳市南山区")
                .build(),
            ExpressQueryNodeDTO.builder()
                .time(FORMATTER.format(now.minusHours(12)))
                .context("快件离开【深圳转运中心】已发往【深圳南山营业部】")
                .status("运输中")
                .location("深圳市宝安区")
                .build(),
            ExpressQueryNodeDTO.builder()
                .time(FORMATTER.format(now.minusDays(1)))
                .context("快件已由【广州集散中心】发出")
                .status("转运中")
                .location("广州市白云区")
                .build()
        );
    }
}
