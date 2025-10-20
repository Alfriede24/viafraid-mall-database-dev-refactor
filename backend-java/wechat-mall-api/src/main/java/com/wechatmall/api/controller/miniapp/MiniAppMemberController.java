package com.wechatmall.api.controller.miniapp;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppCouponDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppFavoriteCreateRequest;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppFavoriteProductDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppMemberProfileDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppMemberProfileUpdateRequest;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppPointsHistoryItemDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppPointsHistoryPageDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppPointsSummaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 小程序会员中心模拟接口
 */
@RestController
@RequestMapping("/miniapp/member")
@Validated
@Tag(name = "小程序会员中心", description = "为小程序端提供会员、积分、优惠券等演示接口")
public class MiniAppMemberController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final AtomicReference<MiniAppMemberProfileDTO> MEMBER_PROFILE = new AtomicReference<>(
        MiniAppMemberProfileDTO.builder()
            .userId(95270001L)
            .nickname("耐撕小王")
            .avatarUrl("https://cdn.wechatmall.dev/avatar/default.png")
            .phone("138****8888")
            .gender(1)
            .genderLabel("男")
            .level("黑金会员")
            .joinDate(DATE_FORMATTER.format(LocalDate.now().minusYears(1)))
            .tags(List.of("线下购卡", "高活跃", "积分达人"))
            .build()
    );

    private static final MiniAppPointsSummaryDTO POINTS_SUMMARY = MiniAppPointsSummaryDTO.builder()
        .availablePoints(1250)
        .frozenPoints(120)
        .totalEarned(5280)
        .totalSpent(4030)
        .build();

    private static final CopyOnWriteArrayList<MiniAppPointsHistoryItemDTO> POINTS_HISTORY = new CopyOnWriteArrayList<>(List.of(
        MiniAppPointsHistoryItemDTO.builder().id(501L).description("订单完成，返还积分").createdAt("2025-05-20 10:30").points(199).type("earn").build(),
        MiniAppPointsHistoryItemDTO.builder().id(500L).description("积分兑换运动手环").createdAt("2025-05-18 15:00").points(-500).type("consume").build(),
        MiniAppPointsHistoryItemDTO.builder().id(499L).description("签到奖励积分").createdAt("2025-05-17 09:00").points(30).type("earn").build(),
        MiniAppPointsHistoryItemDTO.builder().id(498L).description("订单完成，返还积分").createdAt("2025-05-15 12:10").points(88).type("earn").build(),
        MiniAppPointsHistoryItemDTO.builder().id(497L).description("积分兑换运费券").createdAt("2025-05-12 18:45").points(-200).type("consume").build()
    ));

    private static final CopyOnWriteArrayList<MiniAppCouponDTO> COUPONS = new CopyOnWriteArrayList<>(List.of(
        MiniAppCouponDTO.builder().id(301L).name("新人专享券").description("满100元减20元").value(20).validPeriod("2025.10.31")
            .status("unused").threshold("满100元可用").type("满减券").build(),
        MiniAppCouponDTO.builder().id(302L).name("全场通用券").description("满200元减50元").value(50).validPeriod("2025.12.31")
            .status("unused").threshold("满200元可用").type("满减券").build(),
        MiniAppCouponDTO.builder().id(303L).name("会员生日礼券").description("无门槛立减").value(100).validPeriod("2025.08.31")
            .status("used").threshold("无门槛").type("礼遇券").build(),
        MiniAppCouponDTO.builder().id(304L).name("活动体验券").description("满50元减10元").value(10).validPeriod("2025.07.31")
            .status("expired").threshold("满50元可用").type("活动券").build()
    ));

    private static final CopyOnWriteArrayList<MiniAppFavoriteProductDTO> FAVORITES = new CopyOnWriteArrayList<>(List.of(
        MiniAppFavoriteProductDTO.builder().id(2001L).productId(10001L).name("Air Jordan 1 High OG")
            .image("https://cdn.wechatmall.dev/products/aj1-cover.png")
            .price(new BigDecimal("1299"))
            .available(true)
            .build(),
        MiniAppFavoriteProductDTO.builder().id(2002L).productId(10005L).name("Adidas Ultraboost Light")
            .image("https://cdn.wechatmall.dev/products/ultraboost-cover.png")
            .price(new BigDecimal("1188"))
            .available(true)
            .build(),
        MiniAppFavoriteProductDTO.builder().id(2003L).productId(10009L).name("New Balance 327 复古跑鞋")
            .image("https://cdn.wechatmall.dev/products/nb327-cover.png")
            .price(new BigDecimal("799"))
            .available(false)
            .build()
    ));

    private static final AtomicLong FAVORITE_ID_GENERATOR = new AtomicLong(3000L);

    @GetMapping("/profile")
    @Operation(summary = "查询会员资料", description = "返回当前登录用户的会员资料（模拟数据）")
    public ApiResultResponse<MiniAppMemberProfileDTO> getProfile() {
        return ApiResultResponse.ok("查询成功", MEMBER_PROFILE.get());
    }

    @PutMapping("/profile")
    @Operation(summary = "更新会员资料", description = "更新昵称、头像、手机号及性别")
    public ApiResultResponse<MiniAppMemberProfileDTO> updateProfile(
        @Valid @RequestBody MiniAppMemberProfileUpdateRequest request) {

        MiniAppMemberProfileDTO current = MEMBER_PROFILE.get();
        MiniAppMemberProfileDTO updated = MiniAppMemberProfileDTO.builder()
            .userId(current.getUserId())
            .nickname(StrUtil.blankToDefault(request.getNickname(), current.getNickname()))
            .avatarUrl(StrUtil.blankToDefault(request.getAvatarUrl(), current.getAvatarUrl()))
            .phone(StrUtil.blankToDefault(request.getPhone(), current.getPhone()))
            .gender(request.getGender() == null ? current.getGender() : request.getGender())
            .genderLabel(resolveGenderLabel(request.getGender() == null ? current.getGender() : request.getGender()))
            .level(current.getLevel())
            .joinDate(current.getJoinDate())
            .tags(current.getTags())
            .build();

        MEMBER_PROFILE.set(updated);
        return ApiResultResponse.ok("保存成功", updated);
    }

    @GetMapping("/assets/points/summary")
    @Operation(summary = "积分汇总", description = "返回当前会员的积分统计")
    public ApiResultResponse<MiniAppPointsSummaryDTO> getPointsSummary() {
        return ApiResultResponse.ok("查询成功", POINTS_SUMMARY);
    }

    @GetMapping("/assets/points/history")
    @Operation(summary = "积分明细列表", description = "分页返回积分明细记录")
    public ApiResultResponse<MiniAppPointsHistoryPageDTO> getPointsHistory(
        @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize) {

        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = pageSize == null || pageSize < 1 ? 20 : Math.min(pageSize, 100);
        int fromIndex = (safePage - 1) * safeSize;

        List<MiniAppPointsHistoryItemDTO> sorted = POINTS_HISTORY.stream()
            .sorted(Comparator.comparing(MiniAppPointsHistoryItemDTO::getId).reversed())
            .collect(Collectors.toList());

        if (fromIndex >= sorted.size()) {
            return ApiResultResponse.ok("查询成功", MiniAppPointsHistoryPageDTO.builder()
                .page(safePage)
                .pageSize(safeSize)
                .hasNext(false)
                .items(List.of())
                .build());
        }

        int toIndex = Math.min(fromIndex + safeSize, sorted.size());
        List<MiniAppPointsHistoryItemDTO> pageItems = new ArrayList<>(sorted.subList(fromIndex, toIndex));
        boolean hasNext = toIndex < sorted.size();

        MiniAppPointsHistoryPageDTO pageDTO = MiniAppPointsHistoryPageDTO.builder()
            .page(safePage)
            .pageSize(safeSize)
            .hasNext(hasNext)
            .items(pageItems)
            .build();

        return ApiResultResponse.ok("查询成功", pageDTO);
    }

    @GetMapping("/assets/coupons")
    @Operation(summary = "优惠券列表", description = "根据状态返回优惠券")
    public ApiResultResponse<List<MiniAppCouponDTO>> getCoupons(
        @Parameter(description = "状态过滤，unused/used/expired") @RequestParam(required = false) String status) {

        String normalizedStatus = StrUtil.isBlank(status) ? null : status.trim().toLowerCase(Locale.ROOT);
        List<MiniAppCouponDTO> filtered = COUPONS.stream()
            .filter(coupon -> normalizedStatus == null || StrUtil.equalsIgnoreCase(normalizedStatus, coupon.getStatus()))
            .sorted(Comparator.comparing(MiniAppCouponDTO::getId))
            .collect(Collectors.toList());

        return ApiResultResponse.ok("查询成功", filtered);
    }

    @GetMapping("/favorites")
    @Operation(summary = "收藏列表", description = "返回当前会员收藏的商品")
    public ApiResultResponse<List<MiniAppFavoriteProductDTO>> getFavorites() {
        return ApiResultResponse.ok("查询成功", List.copyOf(FAVORITES));
    }

    @PostMapping("/favorites")
    @Operation(summary = "新增收藏", description = "根据商品ID新增收藏")
    public ApiResultResponse<MiniAppFavoriteProductDTO> addFavorite(
        @Valid @RequestBody MiniAppFavoriteCreateRequest request) {

        MiniAppFavoriteProductDTO existing = FAVORITES.stream()
            .filter(item -> item.getProductId().equals(request.getProductId()))
            .findFirst()
            .orElse(null);

        if (existing != null) {
            return ApiResultResponse.ok("已在收藏夹中", existing);
        }

        BigDecimal price = NumberUtil.isNumber(request.getPrice())
            ? new BigDecimal(request.getPrice())
            : new BigDecimal("0");

        MiniAppFavoriteProductDTO favorite = MiniAppFavoriteProductDTO.builder()
            .id(FAVORITE_ID_GENERATOR.incrementAndGet())
            .productId(request.getProductId())
            .name(StrUtil.blankToDefault(request.getName(), "商品" + request.getProductId()))
            .image(StrUtil.blankToDefault(request.getImage(), "https://cdn.wechatmall.dev/products/default.png"))
            .price(price)
            .available(true)
            .build();

        FAVORITES.add(favorite);
        return ApiResultResponse.ok("收藏成功", favorite);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    @Operation(summary = "取消收藏", description = "根据收藏ID移除收藏")
    public ApiResultResponse<Boolean> removeFavorite(@PathVariable Long favoriteId) {
        boolean removed = FAVORITES.removeIf(item -> item.getId().equals(favoriteId));
        return removed
            ? ApiResultResponse.ok("取消收藏成功", Boolean.TRUE)
            : ApiResultResponse.error("未找到对应收藏记录", 404);
    }

    private String resolveGenderLabel(Integer gender) {
        if (gender == null) {
            return "未知";
        }
        return switch (gender) {
            case 1 -> "男";
            case 2 -> "女";
            default -> "未知";
        };
    }
}
