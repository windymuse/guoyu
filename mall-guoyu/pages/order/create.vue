<template>
	<view>
		<!-- 地址 -->
		<navigator v-if="! orderReqeust.selfTake" url="/pages/address/list?source=1" class="address-section">
			<view class="order-content">
				<text class="yticon icon-shouhuodizhi"></text>
				<view class="cen">
					<view class="top">
						<text class="name">{{addressData.consignee}}</text>
						<text class="mobile">{{addressData.phone}}</text>
					</view>
					<text class="address">{{addressData.province}} {{addressData.city}} {{addressData.county}} {{addressData.address}} </text>
				</view>
				<text class="yticon icon-you"></text>
			</view>

			<image class="a-bg" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAu4AAAAFCAYAAAAaAWmiAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6Rjk3RjkzMjM2NzMxMTFFOUI4RkU4OEZGMDcxQzgzOEYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6Rjk3RjkzMjQ2NzMxMTFFOUI4RkU4OEZGMDcxQzgzOEYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGOTdGOTMyMTY3MzExMUU5QjhGRTg4RkYwNzFDODM4RiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpGOTdGOTMyMjY3MzExMUU5QjhGRTg4RkYwNzFDODM4RiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PrEOZlQAAAiuSURBVHjazJp7bFvV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"></image>
		</navigator>

		<view class="goods-section">
			<!-- 商品列表 -->
			<view v-for="(item, index) in orderReqeust.skuList" :key="index" class="g-item">
				<image :src="(item.skuImg?item.skuImg:item.spuImg) + '/200px'"></image>
				<view class="right">
					<text class="title clamp">{{ (item.groupShopId ? '[团购]' : '') + item.title}} {{item.deliverLimit == 1 ? '(仅同城配送)' : ''}}</text>
					<text class="spec">{{item.skuTitle}}</text>
					<view class="price-box">
						<text class="price"><text v-if="(isVip ? item.vipPrice : item.price) < item.originalPrice" 
						style="text-decoration:line-through; font-size: 25upx; color: #666666;">￥{{item.originalPrice / 100.0}}</text>￥{{(isVip ? item.vipPrice : item.price) / 100.0}}</text>
						<text class="number">x {{item.num}}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 优惠明细 -->
		<view class="yt-list">
			<view class="yt-list-cell b-b" @click="toggleMask('show')">
				<view class="cell-icon">
					券
				</view>
				<text class="cell-tit clamp">优惠券</text>
				<text class="cell-tip active">
					{{orderReqeust.coupon?orderReqeust.coupon.title : '选择优惠券'}}
				</text>
				<text class="cell-more wanjia wanjia-gengduo-d"></text>
			</view>
		</view>
		<!-- 金额明细 -->
		<view class="yt-list">
			<view class="yt-list-cell b-b">
				<text class="cell-tit clamp">商品金额</text>
				<text class="cell-tip">￥{{orderReqeust.totalOriginalPrice / 100.0}}</text>
			</view>
			<view class="yt-list-cell b-b" v-if="currVipDegree.percent < 100">
				<text class="cell-tit clamp">会员折扣</text>
				<text class="cell-tip">{{currVipDegree.percent == 100 ? '无折扣' : currVipDegree.percent + '折'}}</text>
			</view>
			<view v-if="orderReqeust.totalOriginalPrice - orderReqeust.totalPrice > 0" class="yt-list-cell b-b">
				<text class="cell-tit clamp">折扣金额</text>
				<text class="cell-tip red">-￥{{(orderReqeust.totalOriginalPrice - orderReqeust.totalPrice) / 100.0}}</text>
			</view>
			<view v-if="orderReqeust.coupon" class="yt-list-cell b-b">
				<text class="cell-tit clamp">优惠券立减</text>
				<text class="cell-tip red">-￥{{orderReqeust.coupon.discount / 100.0}}</text>
			</view>
			<view v-if="orderReqeust.freightPrice === 0" class="yt-list-cell b-b">
				<text class="cell-tit clamp">运费</text>
				<text class="cell-tip">免运费</text>
			</view>
			<view v-if="orderReqeust.freightPrice < 0" class="yt-list-cell b-b">
				<text class="cell-tit clamp">运费</text>
				<text class="cell-tip">未选择配送地址或超出配送范围</text>
			</view>
			<view v-if="orderReqeust.freightPrice > 0" class="yt-list-cell b-b">
				<text class="cell-tit clamp">运费</text>
				<text class="cell-tip">¥ {{orderReqeust.freightPrice / 100.0}}</text>
			</view>
			<view class="yt-list-cell desc-cell">
				<text class="cell-tit clamp">是否自取</text>
				<switch class="cell-tip cell-right" :checked="orderReqeust.selfTake" v-model="orderReqeust.selfTake" @change="changeSelfTake" />
			</view>
			<view class="yt-list-cell desc-cell">
				<text class="cell-tit clamp">备注</text>
				<input class="desc" type="text" v-model="orderReqeust.mono" placeholder="请填写备注信息" placeholder-class="placeholder" />
			</view>
		</view>
		

		<!-- 底部 -->
		<!-- 	营业时间，自取或非自取，可配送
				或者 非营业时间，非自取，可配送-->
		<view class="footer" v-if="(inBusinessHour && orderReqeust.freightPrice >= 0) || (!inBusinessHour && !orderReqeust.selfTake && orderReqeust.freightPrice >= 0)">
			<view class="price-content">
				<text>实付款</text>
				<text class="price-tip">￥</text>
				<text class="price">{{(orderReqeust.totalPrice - (orderReqeust.coupon ? orderReqeust.coupon.discount : 0) + orderReqeust.freightPrice) / 100.0}}</text>
			</view>
			<text class="submit" @click="submit">提交订单</text>
		</view>
		<!-- 	营业时间，自取或非自取，不可配送
				或者 非营业时间，非自取，不可配送-->
		<view class="footer" v-else-if="inBusinessHour || (!inBusinessHour && !orderReqeust.selfTake)">
			<view class="price-content">
				<text>请修改收货地址</text>
			</view>
			<text class="submit" @click="changeAddress">修改收货地址</text>
		</view>
		<!-- 	非营业时间，自取或者不可配送 -->
		<view class="footer" v-else>
			<view class="price-content">
				<text>不在营业时间</text>
			</view>
			<text class="submit" @click="backtoCart">返回购物车</text>
		</view>

		<!-- 优惠券面板 -->
		<view class="mask" :class="maskState===0 ? 'none' : maskState===1 ? 'show' : ''" @click="toggleMask">
			<view class="mask-content" @click.stop.prevent="stopPrevent">
				<!-- 优惠券页面，仿mt -->
				<view @click="selectCoupon(item)" v-if="(!item.categoryId && orderReqeust.totalPrice >= item.min) || (item.categoryId && skuCategoryPriceMap[item.categoryId] && skuCategoryPriceMap[item.categoryId] > item.min)" class="coupon-item" v-for="(item,index) in couponList" :key="index">
					<view class="con">
						<view class="left">
							<text class="title">{{item.title}}</text>
							<text class="time">有效期至{{item.gmtEnd | dateFormat}}</text>
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
		</view>

	</view>
</template>

<script>
    import {
        mapState
    } from 'vuex';
	export default {
		data() {
			return {
				currVipDegree: {},
				orderReqeust: {
					skuList: [],
					totalOriginalPrice: 0,
					totalPrice: 0, //商品折扣（仅算VIP和限时打折）后总价
					coupon: undefined,
					mono: '',
					takeWay: '',
					freightPrice: 0,
					addressId: undefined,
					selfTake: 0
				},
				skuCategoryPriceMap: {},
				maskState: 0, //优惠券面板显示状态
				couponList: [],
				isVip: false,
				addressData: {
					consignee: '',
					phone: '',
					province: '',
					city: '',
					county: '',
					address: '',
					defaultAddress: false,
				},
				submiting: false,
				merchantConfig: {}
			}
		},
		onShow() {
			this.isVip = this.$api.isVip()
		},
		async onLoad(option) {
			const that = this
			await this.loadMemberLevel()
			that.$api.request('config', 'getMerchantConfig', {}, failres => {
				that.$api.msg(failres.errmsg)
			}).then(res => {
				console.log('商户信息', res.data)
				that.merchantConfig = res.data
			})
			//商品数据
			this.isVip = this.$api.isVip()
			if (option.takeway) {
				that.orderReqeust.takeWay = option.takeway
			}
			that.orderReqeust.skuList = that.$api.globalData.skuList;
			that.orderReqeust.selfTake = that.$api.globalData.selfTake;
			let totalOriginalPrice = 0
			let totalPrice = 0
			let skuCategoryPriceMap = {}
			that.orderReqeust.skuList.forEach(item => {
				totalOriginalPrice += item.originalPrice*item.num
				totalPrice += that.isVip ? (item.vipPrice*item.num) :  (item.price*item.num)
				//构建category价格Map
				item.categoryIdList.forEach(catItem => {
					if (skuCategoryPriceMap[catItem]) {
						skuCategoryPriceMap[catItem] += that.isVip ? (item.vipPrice*item.num) :  (item.price*item.num)
					} else {
						skuCategoryPriceMap[catItem] = that.isVip ? (item.vipPrice) :  (item.price)
					}
				})
			})
			that.skuCategoryPriceMap = skuCategoryPriceMap
			that.orderReqeust.totalOriginalPrice = totalOriginalPrice
			// 会员打折
			that.orderReqeust.totalPrice = totalPrice * that.currVipDegree.percent / 100
			that.$api.request('coupon', 'getUserCoupons').then(res => {
				that.couponList = res.data
			})
			
			that.$api.request('address', 'getDefAddress').then(res => {
				that.addressData = res.data
				that.calcFreightPrice()
			})
			
			if (that.orderReqeust.skuList.length === 1 && that.orderReqeust.skuList[0].groupShopId) {
				//若是团购商品，则携带上团购信息
				that.orderReqeust.groupShopId = that.orderReqeust.skuList[0].groupShopId
			}
			console.log('order request', this.orderReqeust)
		},
		onShareAppMessage() {
			return {
				title: '国渔鲜生小程序',
				desc: '全球鲜生供应商',
				path: '/pages/index/index'
			}
		},
		onShareTimeline() {
			return {}
		},
		computed: {
			...mapState(['hasLogin','userInfo']),
			inBusinessHour() {
				// 如果营业时间数据还没有加载
				if (!this.merchantConfig.startTime) {
					return false
				}
				let nowDate = new Date()
				// 小时数 0-23
				let nowHour = nowDate.getHours()
				// 分钟数 0-59
				let nowMunite = nowDate.getMinutes()
				let startTime = this.merchantConfig.startTime.split(':')
				let endTime = this.merchantConfig.endTime.split(':')
				// 如果早于营业时间
				if (nowHour + 1 < startTime[0] || (nowHour + 1 == startTime[0] && nowMunite + 1 < startTime[1])) {
					return false
				}
				// 如果晚于营业时间
				if (nowHour + 1 > endTime[0] || (nowHour + 1 == endTime[0] && nowMunite + 1 > endTime[1])) {
					return false
				}
				// 正常营业
				return true
			}
		},
		methods: {
			async loadMemberLevel() {
				const that = this
				const res = await that.$api.request('member_level', 'getMemberLevel',{
					degree: that.userInfo.level,
					page: 1,
					limit: 1
				}, failres => {
					that.$api.msg(failres.errmsg)
				})
				let data = res.data
				console.log('curr level', data)
				if (data && data.length > 0) {
					this.currVipDegree = data[0]
					console.log('curr vip degree', this.currVipDegree)
				}
			},
			backtoCart() {
				uni.reLaunch({
					url: '/pages/cart/cart'
				})
			},
			changeSelfTake(e) {
				console.log(e.detail.value)
				this.orderReqeust.selfTake = e.detail.value ? 1 : 0
				this.calcFreightPrice()
			},
			changeAddress() {
				// 修改收货地址
				uni.navigateTo({
					url: '/pages/address/list?source=1'
				})
			},
			//显示优惠券面板
			toggleMask(type) {
				let timer = type === 'show' ? 10 : 300;
				let state = type === 'show' ? 1 : 0;
				this.maskState = 2;
				setTimeout(() => {
					this.maskState = state;
				}, timer)
			},
			calcFreightPrice() {
				const that = this
				if (that.addressData) {
					that.orderReqeust.addressId = that.addressData.id
				}
				that.$api.request('freight', 'getFreightMoney', {
					orderRequestDTO: JSON.stringify(that.orderReqeust)
				}).then(res => {
					that.orderReqeust.freightPrice = res.data
				})
			},
			numberChange(data) {
				this.number = data.number;
			},
			submit() {
				const that = this
				if (that.submiting) {
					return
				}
				// 有地址id，说明需要地址
				if (that.addressData.id) {
					that.orderReqeust.addressId = that.addressData.id
				}
				// 非自取订单，无地址，不可提交
				console.log(that.orderReqeust.selfTake, that.addressData.id)
				if (!that.orderReqeust.selfTake && !that.addressData.id) {
					that.$api.msg('非自取订单需要指定取货地址')
					return
				}
				that.submiting = true
				that.$api.request('order', 'takeOrder', {
					orderRequest : JSON.stringify(that.orderReqeust),
					channel: uni.getSystemInfoSync().platform
				}, failres => {
					that.submiting = false
					that.$api.msg(failres.errmsg)
				}).then(res => {
					//提交订单成功后，无需再让用户提交订单
					// that.submiting = false
					uni.redirectTo({
						url: '/pages/pay/pay?orderno='+ res.data + '&price=' + that.orderReqeust.totalPrice
					})
				})
				
			},
			selectCoupon(couponItem) {
				this.orderReqeust.coupon = couponItem
				this.maskState = 0
				this.calcFreightPrice()
			},
			stopPrevent() {},
		}
	}
</script>

<style lang="scss">
	page {
		background: $page-color-base;
		padding-bottom: 100upx;
	}

	.address-section {
		padding: 30upx 0;
		background: #fff;
		position: relative;

		.order-content {
			display: flex;
			align-items: center;
		}

		.icon-shouhuodizhi {
			flex-shrink: 0;
			display: flex;
			align-items: center;
			justify-content: center;
			width: 90upx;
			color: #888;
			font-size: 44upx;
		}

		.cen {
			display: flex;
			flex-direction: column;
			flex: 1;
			font-size: 28upx;
			color: $font-color-dark;
		}

		.name {
			font-size: 34upx;
			margin-right: 24upx;
		}

		.address {
			margin-top: 16upx;
			margin-right: 20upx;
			color: $font-color-light;
		}

		.icon-you {
			font-size: 32upx;
			color: $font-color-light;
			margin-right: 30upx;
		}

		.a-bg {
			position: absolute;
			left: 0;
			bottom: 0;
			display: block;
			width: 100%;
			height: 5upx;
		}
	}

	.goods-section {
		margin-top: 16upx;
		background: #fff;
		padding-bottom: 1px;

		.g-header {
			display: flex;
			align-items: center;
			height: 84upx;
			padding: 0 30upx;
			position: relative;
		}

		.logo {
			display: block;
			width: 50upx;
			height: 50upx;
			border-radius: 100px;
		}

		.name {
			font-size: 30upx;
			color: $font-color-base;
			margin-left: 24upx;
		}

		.g-item {
			display: flex;
			margin: 20upx 30upx;

			image {
				flex-shrink: 0;
				display: block;
				width: 140upx;
				height: 140upx;
				border-radius: 4upx;
			}

			.right {
				flex: 1;
				padding-left: 24upx;
				overflow: hidden;
			}

			.title {
				font-size: 30upx;
				color: $font-color-dark;
			}

			.spec {
				font-size: 26upx;
				color: $font-color-light;
			}

			.price-box {
				display: flex;
				align-items: center;
				font-size: 32upx;
				color: $font-color-dark;
				padding-top: 10upx;

				.price {
					margin-bottom: 4upx;
				}

				.number {
					font-size: 26upx;
					color: $font-color-base;
					margin-left: 20upx;
				}
			}

			.step-box {
				position: relative;
			}
		}
	}

	.yt-list {
		margin-top: 16upx;
		background: #fff;
	}

	.yt-list-cell {
		display: flex;
		align-items: center;
		padding: 10upx 30upx 10upx 40upx;
		line-height: 70upx;
		position: relative;

		&.cell-hover {
			background: #fafafa;
		}

		&.b-b:after {
			left: 30upx;
		}

		.cell-icon {
			height: 32upx;
			width: 32upx;
			font-size: 22upx;
			color: #fff;
			text-align: center;
			line-height: 32upx;
			background: #f85e52;
			border-radius: 4upx;
			margin-right: 12upx;

			&.hb {
				background: #ffaa0e;
			}

			&.lpk {
				background: #3ab54a;
			}

		}

		.cell-more {
			align-self: center;
			font-size: 24upx;
			color: $font-color-light;
			margin-left: 8upx;
			margin-right: -10upx;
		}

		.cell-tit {
			flex: 1;
			font-size: 26upx;
			color: $font-color-light;
			margin-right: 10upx;
		}

		.cell-tip {
			font-size: 26upx;
			color: $font-color-dark;

			&.disabled {
				color: $font-color-light;
			}

			&.active {
				color: $base-color;
			}

			&.red {
				color: $base-color;
			}
			
			&.cell-right {
				position: absolute;
				right: 10upx;
			}
		}

		&.desc-cell {
			.cell-tit {
				max-width: 120upx;
			}
		}

		.desc {
			flex: 1;
			font-size: $font-base;
			color: $font-color-dark;
		}
	}

	/* 支付列表 */
	.pay-list {
		padding-left: 40upx;
		margin-top: 16upx;
		background: #fff;

		.pay-item {
			display: flex;
			align-items: center;
			padding-right: 20upx;
			line-height: 1;
			height: 110upx;
			position: relative;
		}

		.icon-weixinzhifu {
			width: 80upx;
			font-size: 40upx;
			color: #6BCC03;
		}

		.icon-alipay {
			width: 80upx;
			font-size: 40upx;
			color: #06B4FD;
		}

		.icon-xuanzhong2 {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 60upx;
			height: 60upx;
			font-size: 40upx;
			color: $base-color;
		}

		.tit {
			font-size: 32upx;
			color: $font-color-dark;
			flex: 1;
		}
	}

	.footer {
		position: fixed;
		left: 0;
		bottom: 0;
		z-index: 995;
		display: flex;
		align-items: center;
		width: 100%;
		height: 90upx;
		justify-content: space-between;
		font-size: 30upx;
		background-color: #fff;
		z-index: 998;
		color: $font-color-base;
		box-shadow: 0 -1px 5px rgba(0, 0, 0, .1);

		.price-content {
			padding-left: 30upx;
		}

		.price-tip {
			color: $base-color;
			margin-left: 8upx;
		}

		.price {
			font-size: 36upx;
			color: $base-color;
		}

		.submit {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 280upx;
			height: 100%;
			color: #fff;
			font-size: 32upx;
			background-color: $base-color;
		}
	}

	/* 优惠券面板 */
	.mask {
		display: flex;
		align-items: flex-end;
		position: fixed;
		left: 0;
		top: var(--window-top);
		bottom: 0;
		width: 100%;
		background: rgba(0, 0, 0, 0);
		z-index: 9995;
		transition: .3s;

		.mask-content {
			width: 100%;
			min-height: 30vh;
			max-height: 70vh;
			background: #f3f3f3;
			transform: translateY(100%);
			transition: .3s;
			overflow-y: scroll;
		}

		&.none {
			display: none;
		}

		&.show {
			background: rgba(0, 0, 0, .4);

			.mask-content {
				transform: translateY(0);
			}
		}
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
</style>
