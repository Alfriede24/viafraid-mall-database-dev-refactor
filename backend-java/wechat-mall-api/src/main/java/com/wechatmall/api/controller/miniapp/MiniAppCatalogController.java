package com.wechatmall.api.controller.miniapp;

import cn.hutool.core.util.StrUtil;
import com.wechatmall.api.pojo.dto.ApiResultResponse;
import com.wechatmall.api.pojo.dto.miniapp.BannerItemDTO;
import com.wechatmall.api.pojo.dto.miniapp.MiniAppProductDTO;
import com.wechatmall.api.pojo.dto.miniapp.ProductSearchResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序商城端演示数据接口
 */
@RestController
@RequestMapping("/miniapp")
@Validated
@Tag(name = "小程序内容", description = "为小程序端提供演示内容数据")
public class MiniAppCatalogController {

    private static final List<BannerItemDTO> BANNERS = List.of(
        BannerItemDTO.builder()
            .id(1L)
            .title("限时抢购会")
            .description("本周会员专属折扣，爆款低至7折")
            .picUrl("https://cdn.wechatmall.dev/banners/flash-sale.png")
            .targetPath("/pages/coupon/get/get")
            .popup(true)
            .build(),
        BannerItemDTO.builder()
            .id(2L)
            .title("新品上市")
            .description("Air系列全新配色同步发售")
            .picUrl("https://cdn.wechatmall.dev/banners/new-release.png")
            .targetPath("/pages/category/category?mainCategory=man&subCategory=new")
            .popup(false)
            .build(),
        BannerItemDTO.builder()
            .id(3L)
            .title("大屏广告")
            .description("关注公众号领取运费券")
            .picUrl("https://cdn.wechatmall.dev/banners/subscribe.png")
            .targetPath("/pages/profile/profile")
            .popup(false)
            .build()
    );

    private static final List<MiniAppProductDTO> PRODUCT_CATALOG = List.of(
        MiniAppProductDTO.builder()
            .id(10001L)
            .name("Air Jordan 1 High OG")
            .description("经典高帮球鞋，全新湖人限定配色")
            .price(new BigDecimal("1299"))
            .originalPrice(new BigDecimal("1499"))
            .memberPrice(new BigDecimal("1199"))
            .minPrice(new BigDecimal("1299"))
            .picUrl("https://cdn.wechatmall.dev/products/aj1-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/aj1-main.png")
            .stock(180)
            .viewCount(8650)
            .salesCount(5230)
            .mainCategory("man")
            .categoryId("shoes")
            .seriesId("air-jordan")
            .brandId("jordan")
            .tags(Arrays.asList("hot", "new", "seasonal"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10002L)
            .name("Nike Pegasus Trail 4 GTX")
            .description("防泼水越野跑鞋，雨天畅跑不怕湿")
            .price(new BigDecimal("999"))
            .originalPrice(new BigDecimal("1099"))
            .memberPrice(new BigDecimal("949"))
            .minPrice(new BigDecimal("999"))
            .picUrl("https://cdn.wechatmall.dev/products/pegasus-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/pegasus-main.png")
            .stock(220)
            .viewCount(5620)
            .salesCount(3180)
            .mainCategory("man")
            .categoryId("seasonal")
            .seriesId("pegasus")
            .brandId("nike")
            .tags(Arrays.asList("seasonal", "hot"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10003L)
            .name("Jordan Jumpman Two Trey")
            .description("复刻经典，街头休闲百搭款")
            .price(new BigDecimal("899"))
            .originalPrice(new BigDecimal("1099"))
            .memberPrice(new BigDecimal("859"))
            .minPrice(new BigDecimal("899"))
            .picUrl("https://cdn.wechatmall.dev/products/twotrey-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/twotrey-main.png")
            .stock(145)
            .viewCount(4680)
            .salesCount(2890)
            .mainCategory("man")
            .categoryId("hot")
            .seriesId("air-jordan")
            .brandId("jordan")
            .tags(Arrays.asList("hot", "sale"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10004L)
            .name("Nike Tech Fleece 套装")
            .description("秋冬加绒套装，轻盈保暖")
            .price(new BigDecimal("1099"))
            .originalPrice(new BigDecimal("1299"))
            .memberPrice(new BigDecimal("999"))
            .minPrice(new BigDecimal("1099"))
            .picUrl("https://cdn.wechatmall.dev/products/techfleece-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/techfleece-main.png")
            .stock(95)
            .viewCount(3920)
            .salesCount(2015)
            .mainCategory("man")
            .categoryId("apparel")
            .seriesId("tech")
            .brandId("nike")
            .tags(Arrays.asList("seasonal", "new"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10005L)
            .name("Adidas Ultraboost Light")
            .description("轻量缓震旗舰跑鞋，畅跑新体验")
            .price(new BigDecimal("1188"))
            .originalPrice(new BigDecimal("1399"))
            .memberPrice(new BigDecimal("1088"))
            .minPrice(new BigDecimal("1188"))
            .picUrl("https://cdn.wechatmall.dev/products/ultraboost-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/ultraboost-main.png")
            .stock(260)
            .viewCount(6230)
            .salesCount(3470)
            .mainCategory("woman")
            .categoryId("shoes")
            .seriesId("ultraboost")
            .brandId("adidas")
            .tags(Arrays.asList("hot", "brands"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10006L)
            .name("Li-Ning 音速 10")
            .description("全掌䨻缓震，国内球员上脚同款")
            .price(new BigDecimal("799"))
            .originalPrice(new BigDecimal("999"))
            .memberPrice(new BigDecimal("759"))
            .minPrice(new BigDecimal("799"))
            .picUrl("https://cdn.wechatmall.dev/products/sonic10-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/sonic10-main.png")
            .stock(180)
            .viewCount(4855)
            .salesCount(2766)
            .mainCategory("man")
            .categoryId("hot")
            .seriesId("lining-sonic")
            .brandId("li-ning")
            .tags(Arrays.asList("hot", "brands", "sale"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10007L)
            .name("New Balance 327")
            .description("法式复古风，情侣百搭小众款")
            .price(new BigDecimal("699"))
            .originalPrice(new BigDecimal("899"))
            .memberPrice(new BigDecimal("669"))
            .minPrice(new BigDecimal("699"))
            .picUrl("https://cdn.wechatmall.dev/products/nb327-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/nb327-main.png")
            .stock(140)
            .viewCount(3520)
            .salesCount(1980)
            .mainCategory("woman")
            .categoryId("hot")
            .seriesId("nb-classic")
            .brandId("new-balance")
            .tags(Arrays.asList("hot", "brands"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10008L)
            .name("Nike Kids Dynamo Free")
            .description("儿童一脚蹬运动鞋，柔软舒适")
            .price(new BigDecimal("439"))
            .originalPrice(new BigDecimal("499"))
            .memberPrice(new BigDecimal("419"))
            .minPrice(new BigDecimal("439"))
            .picUrl("https://cdn.wechatmall.dev/products/dynamo-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/dynamo-main.png")
            .stock(320)
            .viewCount(2980)
            .salesCount(1875)
            .mainCategory("kids")
            .categoryId("new")
            .seriesId("kids-dynamo")
            .brandId("nike")
            .tags(Arrays.asList("new", "kids"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10009L)
            .name("Puma Suede Classic XXI")
            .description("复古麂皮鞋，潮流常青款")
            .price(new BigDecimal("599"))
            .originalPrice(new BigDecimal("699"))
            .memberPrice(new BigDecimal("569"))
            .minPrice(new BigDecimal("599"))
            .picUrl("https://cdn.wechatmall.dev/products/puma-suede-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/puma-suede-main.png")
            .stock(160)
            .viewCount(2760)
            .salesCount(1540)
            .mainCategory("man")
            .categoryId("brands")
            .seriesId("puma-suede")
            .brandId("puma")
            .tags(Arrays.asList("brands", "sale"))
            .build(),
        MiniAppProductDTO.builder()
            .id(10010L)
            .name("Asics Gel-Kayano 30")
            .description("30周年纪念款，旗舰支撑跑鞋")
            .price(new BigDecimal("1290"))
            .originalPrice(new BigDecimal("1490"))
            .memberPrice(new BigDecimal("1220"))
            .minPrice(new BigDecimal("1290"))
            .picUrl("https://cdn.wechatmall.dev/products/kayano-cover.png")
            .mainImageUrl("https://cdn.wechatmall.dev/products/kayano-main.png")
            .stock(210)
            .viewCount(3320)
            .salesCount(1985)
            .mainCategory("man")
            .categoryId("shoes")
            .seriesId("gel-kayano")
            .brandId("asics")
            .tags(Arrays.asList("brands", "seasonal"))
            .build()
    );

    @GetMapping("/banners")
    @Operation(summary = "首页轮播图与弹窗", description = "返回小程序首页需要展示的轮播图与弹窗配置")
    public ApiResultResponse<List<BannerItemDTO>> listBanners() {
        return ApiResultResponse.ok("查询成功", BANNERS);
    }

    @GetMapping("/products/recommendations")
    @Operation(summary = "推荐商品", description = "根据销量与浏览量返回推荐商品")
    public ApiResultResponse<List<MiniAppProductDTO>> recommendProducts(
        @Parameter(description = "返回数量", schema = @Schema(type = "integer"))
        @RequestParam(value = "limit", defaultValue = "6") int limit) {

        long safeLimit = limit <= 0 ? 6L : limit;
        List<MiniAppProductDTO> data = PRODUCT_CATALOG.stream()
            .sorted(Comparator.comparing(MiniAppProductDTO::getSalesCount).thenComparing(MiniAppProductDTO::getViewCount).reversed())
            .limit(safeLimit)
            .collect(Collectors.toList());
        return ApiResultResponse.ok("查询成功", data);
    }

    @GetMapping("/products")
    @Operation(summary = "商品筛选列表", description = "根据分类、系列、品牌返回商品列表")
    public ApiResultResponse<List<MiniAppProductDTO>> queryProducts(
        @RequestParam(value = "mainCategory", required = false) String mainCategory,
        @RequestParam(value = "categoryId", required = false) String categoryId,
        @RequestParam(value = "seriesId", required = false) String seriesId,
        @RequestParam(value = "brandId", required = false) String brandId) {

        List<MiniAppProductDTO> filtered = filterProducts(mainCategory, categoryId, seriesId, brandId);
        return ApiResultResponse.ok("查询成功", filtered);
    }

    @GetMapping("/products/search")
    @Operation(summary = "商品搜索", description = "支持关键词模糊查询与分页")
    public ApiResultResponse<ProductSearchResultDTO> searchProducts(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {

        List<MiniAppProductDTO> filtered = PRODUCT_CATALOG.stream()
            .filter(item -> StrUtil.isBlank(keyword)
                || StrUtil.containsIgnoreCase(item.getName(), keyword)
                || StrUtil.containsIgnoreCase(item.getDescription(), keyword)
                || item.getTags() != null && item.getTags().stream().anyMatch(tag -> StrUtil.containsIgnoreCase(tag, keyword)))
            .sorted(Comparator.comparing(MiniAppProductDTO::getViewCount).thenComparing(MiniAppProductDTO::getSalesCount).reversed())
            .collect(Collectors.toList());

        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        int fromIndex = Math.min((safePage - 1) * safePageSize, filtered.size());
        int toIndex = Math.min(fromIndex + safePageSize, filtered.size());
        List<MiniAppProductDTO> pageItems = fromIndex >= filtered.size()
            ? Collections.emptyList()
            : filtered.subList(fromIndex, toIndex);

        ProductSearchResultDTO result = ProductSearchResultDTO.builder()
            .items(pageItems)
            .total(filtered.size())
            .hasMore(toIndex < filtered.size())
            .page(safePage)
            .pageSize(safePageSize)
            .build();

        return ApiResultResponse.ok(result);
    }

    private List<MiniAppProductDTO> filterProducts(String mainCategory, String categoryId, String seriesId, String brandId) {
        return PRODUCT_CATALOG.stream()
            .filter(item -> StrUtil.isBlank(mainCategory) || StrUtil.equalsIgnoreCase(item.getMainCategory(), mainCategory))
            .filter(item -> matchesCategory(item, categoryId))
            .filter(item -> StrUtil.isBlank(seriesId) || StrUtil.equalsIgnoreCase(seriesId, item.getSeriesId()))
            .filter(item -> StrUtil.isBlank(brandId) || StrUtil.equalsIgnoreCase(brandId, item.getBrandId()))
            .collect(Collectors.toList());
    }

    private boolean matchesCategory(MiniAppProductDTO item, String categoryId) {
        if (StrUtil.isBlank(categoryId)) {
            return true;
        }
        if (StrUtil.equalsIgnoreCase(categoryId, item.getCategoryId())) {
            return true;
        }
        return item.getTags() != null && item.getTags().stream().anyMatch(tag -> StrUtil.equalsIgnoreCase(tag, categoryId));
    }
}
