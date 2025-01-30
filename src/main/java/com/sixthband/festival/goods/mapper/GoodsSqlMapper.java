package com.sixthband.festival.goods.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.goods.dto.GoodsCategoryDto;
import com.sixthband.festival.goods.dto.GoodsDetailImageDto;
import com.sixthband.festival.goods.dto.GoodsDiscountDto;
import com.sixthband.festival.goods.dto.GoodsDto;
import com.sixthband.festival.goods.dto.GoodsGptReviewDto;
import com.sixthband.festival.goods.dto.GoodsOrderDto;
import com.sixthband.festival.goods.dto.GoodsReviewDetailImageDto;
import com.sixthband.festival.goods.dto.GoodsReviewDto;
import com.sixthband.festival.goods.dto.GoodsReviewLikeDto;
import com.sixthband.festival.goods.dto.GoodsReviewRecommendDto;
import com.sixthband.festival.goods.dto.GoodsShopperbagDto;
import com.sixthband.festival.goods.dto.GoodsWishDto;

@Mapper
public interface GoodsSqlMapper {

    //Category
    public List<GoodsCategoryDto> goodsCategory();

    //Image
    public void insertGoods(GoodsDto goodsDto);
    public void goodsDetailImage(GoodsDetailImageDto goodsDetailImageDto);

    //Discount
    public List<GoodsDiscountDto> selectDiscountsByGoodsId(int goods_id);
    public List<GoodsDto> selectDisCountAndGoods();
    public void insertDiscount(GoodsDiscountDto goodsDiscountDto);
    public void updateDiscount(GoodsDiscountDto goodsDiscountDto);
    public void deleteDiscount(@Param("id") int id);

    //List
    public List<GoodsDto> selectGoodsListAll();
    public List<GoodsDto> selectGoodsList(@Param("pageIndex") int pageIndex);
    public int getGoodsListTotalCount();
    public GoodsCategoryDto selectIdFromCategory(int id);

    public GoodsDto getGoodsDetailById(@Param("id") int id);
    public void updateGoodsProduct(GoodsDto goodsDto);
    public void deleteGoodsId(int id);
    public List<GoodsDetailImageDto> selectImageListByGoodsId(int goods_id);

    //WishList
    public void insertWishList(GoodsWishDto goodsWishDto);
    public int deleteWishList(GoodsWishDto goodsWishDto);
    public int getWishLiked(GoodsWishDto goodsWishDto);
    public int totalWishLikes(@RequestParam("goods_id") int goods_id);
    public int WhishLikesByGoodsIdAndUserId(GoodsWishDto goodsWishDto);
    public List<GoodsWishDto> getWishListAllByUserId(@Param("user_id") int user_id);

    //Cart
    public void insertGoodsShopperbag(GoodsShopperbagDto goodsShopperbagDto);
    public List<GoodsShopperbagDto> selectGoodsCartList();
    public List<GoodsShopperbagDto> selectGoodsCartListByUserId(int user_id);
    public void deleteCartByGoods( @Param("goods_id") int goods_id, @Param("user_id") int user_id);

    //Order
    public void insertGoodsOrder(GoodsOrderDto goodsOrderDto);
    public void updateOrderStatus(@Param("id") int id, @Param("order_status") String order_status);
    public List<Map<String, Object>> countOrdersByStatus();
    public List<Map<String, Object>> countOrdersByStatusByUserId(@Param("user_id") int user_id);
    public List<GoodsOrderDto> selectGoodsOrderList(@Param("user_id") int user_id);
    public GoodsOrderDto selectGoodsOrderDetail(@Param("id") int id, @Param("user_id") int user_id);
    public GoodsOrderDto selectGoodsOrderDetailById(@Param("id") int id);

    //Review
    public void insertGoodsReview(GoodsReviewDto goodsReviewDto);
    public void insertGptReview(GoodsGptReviewDto goodsGptReviewDto);
    public GoodsGptReviewDto goodsGptReview(@Param("comment_id") int comment_id);
    public int countReviewsByOrderId(@Param("order_id") int order_id);
    public List<GoodsReviewDto> selectGoodsReviewListAll();
    public List<GoodsReviewDto> selectGoodsReviewList(@Param("goods_id") int goods_id);

    public void insertGoodsReviewDetailImage(GoodsReviewDetailImageDto goodsReviewDetailImageDto);
    public List<GoodsReviewDetailImageDto> selectImageListByReviewId(int review_id);
    public GoodsReviewDto getReviewDetailById(@Param("id") int id);

    public int countGoodsReviewByGoodsId(@Param("goods_id") int goods_id);
    public Double avgGoodsReviewRatingByGoodsId(@Param("goods_id") int goods_id);
    public void deleteReview(int id);

    public void insertGoodsReviewLike(GoodsReviewLikeDto goodsReviewLikeDto);
    public int deleteGoodsReviewLike(GoodsReviewLikeDto goodsReviewLikeDto);
    public int getReviewLiked(GoodsReviewLikeDto goodsReviewLikeDto);
    public int totalReviewLikes(@RequestParam("review_id") int review_id);

    public UserDto getUserDetailById(@Param("id") int id);

    //Admin
    public List<GoodsOrderDto> getGoodsOrderList(@Param("pageIndex") int pageIndex);
    public int getOrderListTotalCount();
    public GoodsOrderDto getGoodsOrderListByOrderId(@Param("order_id") int order_id);
    public List<GoodsReviewDto> getGoodsReviewListByGoodsAndUser(@Param("limitStart") int limitStart, @Param("pageIndex") int pageIndex);
    public int getReviewListTotalCount();
    public GoodsReviewDto getReviewDetailAll(@Param("id") int id);
    public int countReviewAll();
    public int countOrderAll();
    public int countGoodsAll();
    public void insertReviewRecommend(GoodsReviewRecommendDto goodsReviewRecommendDto);
    public void deleteReviewRecommend(@Param("id") int id);
    public void deleteReviewRecommendationsByReviewId(int goodsReviewId);
    public List<GoodsDto> getGoodsDetailAll(@Param("id") int id);

    //Main
    public List<GoodsDto> selectOrderTop4();
    public List<GoodsDto> selectNewItem6();

}
