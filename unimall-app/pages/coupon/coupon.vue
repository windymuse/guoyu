<template>
	<view>

		<view class="mask-content" @click.stop.prevent="stopPrevent">
			<view @click="obtainCoupon(index)" class="coupon-item" v-for="(item,index) in couponList" :key="index">
				<view class="con">
					<view class="left">
						<text class="title">{{item.title}}</text>
						<text v-if="item.gmtEnd" class="time">在{{item.gmtEnd}}前有效。 可领{{item.limit}}张，已领{{item.nowCount}}张</text>
						<text v-if="!item.gmtEnd" class="time">在领取后{{item.days}}天内有效。可领{{item.limit}}张，已领{{item.nowCount}}张</text>
					</view>
					<view class="right">
						<text class="price">{{item.discount / 100.0}}</text>
						<text>满{{item.min / 100.0}}可用</text>
					</view>
					<view class="circle l"></view>
					<view class="circle r"></view>
				</view>
				<text class="tips">{{item.categoryTitle?'限' + item.categoryTitle + '可用': '全品类可用'}}</text>
			</view>
		</view>
		
		<neil-modal :show="modalShow" @close="closeModal" :show-cancel="false" :show-confirm="false">
		    <view class="product-detail">
				<view class="box">
					<input class="code" type="text" v-model="couponCode" placeholder="请输入兑换码" />
					<view class="page-bottom">
						<view class="action-btn-group btn">
							<button type="primary" class=" action-btn no-border add-cart-btn" @click="obtainCouponByCode">立即领取</button>
						</view>
					</view>
				</view>
			</view>
		</neil-modal>
	</view>
</template>

<script>
	import neilModal from '@/components/neil-modal/neil-modal.vue';
	export default {
		data() {
			return {
				goods: {
					freightTemplate: undefined,
					skuList: [],
					categoryList: [],
					appraisePage: undefined
				},
				isVip: false,
				specClass: 'none',
				specSelected: [],
				selectedSku: {},
				selectedSkuIndex: -1,
				toggleCallback: undefined,
				maskState: 1, //优惠券面板显示状态
				couponList: [],
				submiting: false,
				goodsNum: 1,
				modalShow: false,
				currCouponIndex: -1,
				couponCode: '',
			};
		},
		components: {
			neilModal
		},
		onLoad() {
			const that = this
			that.$api.request('coupon', 'getObtainableCoupon').then(res => {
				that.couponList = res.data
			})
		},
		methods: {
			//领取优惠券
			obtainCoupon(index) {
				const that = this
				const coupon = that.couponList[index]
				if (coupon.gmtType == 1) {
					// 直接领取
					that.$api.request('coupon', 'obtainCoupon', {
						couponId: that.couponList[index].id
					}).then(res => {
						that.$api.msg('领取成功')
						that.couponList[index].nowCount++
					})
				} else {
					// 优惠券领取
					this.modalShow = true
					this.currCouponIndex = index
				}
			},
			// 兑换码领取优惠券
			obtainCouponByCode() {
				const that = this
				const coupon = that.couponList[this.currCouponIndex]
				if (coupon.gmtType == 2) {
					// 优惠券领取
					that.$api.request('coupon', 'obtainCoupon', {
						couponId: coupon.id,
						code: that.couponCode,
					}).then(res => {
						console.log(res.data)
						that.$api.msg('领取成功')
						coupon.nowCount++
						that.modalShow = false
					})
				}
			},
		}
	}
</script>

<style lang="scss">

	// 顶部
	.navbar {
		display: flex;
		height: 40px;
		padding: 0 5px;
		background: #fff;
		box-shadow: 0 1px 5px rgba(0, 0, 0, .06);
		position: relative;
		z-index: 10;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;
			color: $font-color-dark;
			position: relative;

			&.current {
				color: $base-color;

				&:after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 44px;
					height: 0;
					border-bottom: 2px solid $base-color;
				}
			}
		}
	}

	/* 优惠券面板 */

	.mask-content {
		width: 100%;
		min-height: 30vh;
		max-height: 70vh;
		background: #f3f3f3;
		overflow-y: scroll;
	}

	/* 优惠券列表 */
	.coupon-item {
		display: flex;
		flex-direction: column;
		margin: 20upx 24upx;
		background: #fff;

		.con {
			display: flex;
			align-items: center;
			position: relative;
			height: 120upx;
			padding: 0 30upx;

			&:after {
				position: absolute;
				left: 0;
				bottom: 0;
				content: '';
				width: 100%;
				height: 0;
				border-bottom: 1px dashed #f3f3f3;
				transform: scaleY(50%);
			}
		}

		.left {
			display: flex;
			flex-direction: column;
			justify-content: center;
			flex: 1;
			overflow: hidden;
			height: 100upx;
		}

		.title {
			font-size: 32upx;
			color: $font-color-dark;
			margin-bottom: 10upx;
		}

		.time {
			font-size: 24upx;
			color: $font-color-light;
		}

		.right {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			font-size: 26upx;
			color: $font-color-base;
			height: 100upx;
		}

		.price {
			font-size: 44upx;
			color: $base-color;

			&:before {
				content: '￥';
				font-size: 34upx;
			}
		}

		.tips {
			font-size: 24upx;
			color: $font-color-light;
			line-height: 60upx;
			padding-left: 30upx;
		}

		.circle {
			position: absolute;
			left: -6upx;
			bottom: -10upx;
			z-index: 10;
			width: 20upx;
			height: 20upx;
			background: #f3f3f3;
			border-radius: 100px;

			&.r {
				left: auto;
				right: -6upx;
			}
		}
	}
	
	.product-detail {
		position: relative;
		text-align: center;
		
		.close {
			position: absolute;
			top: 20upx;
			right: 20upx;
			width: 60upx;
			height: 60upx;
			border: 1px solid $font-color-light;
			border-radius: 50%;
			text-align: center;
			line-height: 60upx;
			color: $font-color-light;
		}
		
		.img {
			height: 500upx;
			width: 100%;
		}
		
		.info {
			margin-left: 50upx;
			margin-right: 50upx;
			text-align: left;
		}
		
		.box {
			margin: 0 20upx;
			position: relative;
			margin-top: 80upx;
			
			.code {
				margin-bottom: 20upx;
				border: 1px solid $font-color-dark;
				height: 60upx;
				line-height: 60upx;
				border-radius: 30upx;
			}
		}
		
		
		
		.btn {
		}
		
		/* 底部操作菜单 */
		.page-bottom {
			display: flex;
			justify-content: center;
			align-items: center;
			
			position: relative;
				
			.p-b-btn {
				position: relative;
				top: -20upx;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
				font-size: $font-sm;
				color: $font-color-base;
				width: 96upx;
				height: 80upx;
			
				.yticon {
					font-size: 40upx;
					line-height: 48upx;
					color: $font-color-light;
				}
			
				&.active,
				&.active .yticon {
					color: $uni-color-primary;
				}
			
				.icon-fenxiang2 {
					font-size: 42upx;
					transform: translateY(-2upx);
				}
			
				.icon-shoucang {
					font-size: 46upx;
				}
			}
			
			.action-btn-group {
				
				height: 80upx;
				background-color: #28D7FE;
				/* width: 60%; */
				margin: 0 auto;
				margin-bottom: 40upx;
				line-height: 80upx;
				border-radius: 40upx;
				color: white;
				font-weight: bold;
				
				display: flex;
				height: 76upx;
				border-radius: 100px;
				overflow: hidden;
				box-shadow: 0 20upx 40upx -16upx #fa436a;
				box-shadow: 1px 2px 5px rgba(219, 63, 96, 0.4);
				background: linear-gradient(to right, #ffac30, #fa436a, #F56C6C);
				position: relative;
			
				&:after {
					content: '';
					position: absolute;
					top: 50%;
					right: 50%;
					transform: translateY(-50%);
					height: 28upx;
					width: 0;
					border-right: 1px solid rgba(255, 255, 255, .5);
				}
			
				.action-btn {
					display: flex;
					align-items: center;
					justify-content: center;
					width: 180upx;
					height: 100%;
					font-size: $font-base;
					padding: 0;
					border-radius: 0;
					background: transparent;
				}
			}
		}
	}
</style>
