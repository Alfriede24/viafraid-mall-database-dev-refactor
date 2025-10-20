package com.wechatmall.api.controller.miniapp;

import cn.hutool.core.util.StrUtil;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.dto.logistics.LogisticsRecipientDTO;
import com.wechatmall.api.pojo.dto.logistics.LogisticsTrackNodeDTO;
import com.wechatmall.api.pojo.dto.logistics.LogisticsTrackResponseDTO;
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
import java.util.Map;

/**
 * 小程序物流查询接口
 */
@RestController
@RequestMapping("/logistics")
@Validated
@Tag(name = "物流服务", description = "提供小程序端物流轨迹查询")
public class LogisticsController {

    private static final Map<String, String> COMPANY_CODE_MAP = Map.of(
        "顺丰速运", "SF", "顺丰", "SF", "中通快递", "ZTO", "韵达速递", "YD", "圆通速递", "YTO",
        "申通快递", "STO", "京东快递", "JD", "邮政EMS", "EMS"
    );

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/track")
    @Operation(summary = "物流轨迹查询", description = "根据快递公司与运单号返回模拟物流轨迹")
    public ApiResultResponse<LogisticsTrackResponseDTO> queryLogistics(
        @RequestParam("company") String company,
        @RequestParam("trackingNo") String trackingNo) {

        if (StrUtil.isBlank(company) || StrUtil.isBlank(trackingNo)) {
            return ApiResultResponse.error("快递公司和运单号不能为空", 400);
        }

        String companyCode = COMPANY_CODE_MAP.getOrDefault(company, company.toLowerCase());
        String now = FORMATTER.format(LocalDateTime.now());

        LogisticsTrackResponseDTO responseDTO = LogisticsTrackResponseDTO.builder()
            .company(company)
            .companyCode(companyCode)
            .trackingNo(trackingNo)
            .state("5")
            .status("派件中")
            .updateTime(now)
            .recipient(LogisticsRecipientDTO.builder()
                .name("张三")
                .phone("138****8888")
                .address("广东省深圳市南山区科技园科苑大道10000号")
                .build())
            .tracks(buildMockTracks(now))
            .build();

        return ApiResultResponse.ok("查询成功", responseDTO);
    }

    private List<LogisticsTrackNodeDTO> buildMockTracks(String now) {
        LocalDateTime baseTime = LocalDateTime.parse(now, FORMATTER);
        return Arrays.asList(
            LogisticsTrackNodeDTO.builder()
                .time(FORMATTER.format(baseTime.minusHours(1)))
                .description("快件正在派送中，请保持电话畅通")
                .location("深圳市南山区")
                .status("派送中")
                .build(),
            LogisticsTrackNodeDTO.builder()
                .time(FORMATTER.format(baseTime.minusHours(5)))
                .description("快件已到达【深圳南山营业部】")
                .location("深圳市南山区")
                .status("到达网点")
                .build(),
            LogisticsTrackNodeDTO.builder()
                .time(FORMATTER.format(baseTime.minusHours(12)))
                .description("快件离开【深圳中转中心】发往【深圳南山营业部】")
                .location("深圳市宝安区")
                .status("转运中")
                .build(),
            LogisticsTrackNodeDTO.builder()
                .time(FORMATTER.format(baseTime.minusDays(1)))
                .description("包裹已揽收")
                .location("广州市白云区")
                .status("已揽收")
                .build()
        );
    }
}
