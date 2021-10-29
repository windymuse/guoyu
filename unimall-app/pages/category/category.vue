<template>
	<view class="content">
	
		<!-- 小程序头部兼容 -->
		<!-- #ifdef MP -->
		<view class="info-box">
			<view class="title">{{merchantConfig.title}}</view>
			<view class="info">距离您 {{distance}}</view>
			<view class="switch" >
				<view class="switchNums" :class="deliverType == 0 ? 'currentNum' : ''" @click="switchChange(0)">{{selectOpen[0]}}</view>
				<view class="switchNums" :class="deliverType == 1 ? 'currentNum' : ''" @click="switchChange(1)">{{selectOpen[1]}}</view>
			</view>

			<input @click="navigateToPage('/pages/product/search')" class="ser-input" type="text" value="搜索" disabled />
		</view>
		<!-- #endif -->
		
		<view class="container">
			<scroll-view scroll-y class="left-aside">
				<view v-for="item in flist" :key="item.id" class="f-item b-b" :class="{active: item.id === currentId}" @click="tabtap(item)">
					{{item.title}}
				</view>
			</scroll-view>
			<scroll-view scroll-with-animation scroll-y class="right-aside" :scroll-top="tabScrollTop">
				<view v-for="item in slist" :key="item.id" class="s-list" :id="'main-'+item.id">
					<!-- <text class="s-item" >{{item.title}}</text> -->
					<view class="t-list" v-if="titem.parentId === item.id && titem.goods.length > 0" v-for="titem in item.childrenList" :key="titem.id">
						<text class="s-item title" >{{item.title}} > {{titem.title}}</text>
						<view class="t-item" v-for="good in titem.goods" :key="good.id">
							<image @click="navToDetailPage(good)" class="img" :src="good.img + '?x-oss-process=style/200px'"></image>
							<text @click="navToDetailPage(good)" class="title">{{good.title}}</text>
							<view class="price-box" @click="navToDetailPage(good)">
								<text class="price-tip">¥</text>
								<text class="price">{{isVip ? (good.vipPrice ? good.vipPrice : goods.vipPrice) / 100.0  + ' [VIP]': (good.price ? good.price : goods.price) / 100.0 }}</text>
								<text v-if="(isVip ? (good.vipPrice ? good.vipPrice : goods.vipPrice) : (good.price ? good.price : goods.price)) < (good.price ? good.originalPrice : goods.originalPrice)"
								 class="m-price">¥{{(good.price ? good.originalPrice : goods.originalPrice) / 100}}</text>
							</view>
							<view class="chooseBtn" @click="chooseThis(good)">选规格</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
		<neil-modal :show="modalShow" @close="closeModal" :show-cancel="false" :show-confirm="false">
		    <view class="product-detail">
		        <view class="close" @click="closeModal">X</view>
		        <image class="img" :src="goods.img" mode="aspectFit"></image>
		        <view class="info">{{goods.title}}</view>
				<view class="box">
					<view class="price-box">
						<text class="price-tip">¥</text>
						<text class="price">{{isVip ? (selectedSku.vipPrice ? selectedSku.vipPrice : goods.vipPrice) / 100.0  + ' [VIP]': (selectedSku.price ? selectedSku.price : goods.price) / 100.0 }}</text>
						<text v-if="(isVip ? (selectedSku.vipPrice ? selectedSku.vipPrice : goods.vipPrice) : (selectedSku.price ? selectedSku.price : goods.price)) < (selectedSku.price ? selectedSku.originalPrice : goods.originalPrice)"
						 class="m-price">¥{{(selectedSku.price ? selectedSku.originalPrice : goods.originalPrice) / 100}}</text>
						<text v-if="(isVip ? (selectedSku.vipPrice ? selectedSku.vipPrice : goods.vipPrice) : (selectedSku.price ? selectedSku.price : goods.price)) < (selectedSku.price ? selectedSku.originalPrice : goods.originalPrice)"
						 class="coupon-tip">{{parseInt((isVip? (selectedSku.vipPrice ? selectedSku.vipPrice : goods.vipPrice): (selectedSku.price ? selectedSku.price : goods.price)) / (selectedSku.originalPrice ? selectedSku.originalPrice : goods.originalPrice) * 100) / 10}}折</text>
					</view>
					<view class="sub" @click="numSub">-</view>
					<view class="num">{{goodsNum}}</view>
					<view class="add" @click="numAdd">+</view>
					
					<view class="attr-list">
						<!-- <text>规格</text> -->
						<view class="item-list">
							<text v-for="(skuItem, skuIndex) in goods.skuList" :key="skuIndex" class="tit" :class="{selected: skuIndex === selectedSkuIndex}"
							 @click="selectSpec(skuItem, skuIndex)">
								{{skuItem.title}}
							</text>
						</view>
					</view>
					<view class="page-bottom">
						<view class="p-b-btn" @click="navToDetailPage(goods)">
							<text class="yticon icon-gouwuche"></text>
							<text>详情</text>
						</view>
						<view class="p-b-btn" :class="{active: goods.collect}" @click="toFavorite">
							<text class="yticon icon-shoucang"></text>
							<text>收藏</text>
						</view>
						<view class="action-btn-group btn">
							<button type="primary" class=" action-btn no-border buy-now-btn" @click="buy">立即购买</button>
							<button type="primary" class=" action-btn no-border add-cart-btn" @click="addCart">加入购物车</button>
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
				modalShow: false,
				cateMaskState: 0, //分类面板展开状态
				headerPosition: "fixed",
				headerTop: "0px",
				loadingType: 'more', //加载更多状态
				filterIndex: 0,
				priceOrder: 0, //1 价格从低到高 2价格从高到低
				goodsList: [],
				cateId: 0,
				keywords: '',
				pageNo: 1,
				isVip: false,
				
				sizeCalcState: false,
				tabScrollTop: 0,
				currentId: 1,
				flist: [],
				slist: [],
				tlist: [],
				rawData: [],
				selectOpen:["自取","外卖"],
				deliverType: 1,
				goods: {},
				goodsNum: 1,
				selectedSku: {},
				selectedSkuIndex: -1,
				merchantConfig: {},
				userConfig: {}
			}
		},
		components: {
			neilModal
		},
		computed: {
			distance() {
				// 计算用户与商家的距离
				if (!(this.userConfig.longitude && this.merchantConfig.longitude)) {
					return 0
				} else {
					return this.getDistance(this.userConfig.latitude, this.userConfig.longitude,
											this.merchantConfig.latitude, this.merchantConfig.longitude)
				}
			}
		},
		onShow() {
			let deliverType = uni.getStorageSync('deliverType');
			this.deliverType = deliverType || 0
		},
		async onLoad(e){
			await this.loadData()
			console.log('onload', e)
			const that = this
			uni.getLocation({
				type: 'wgs84',
				success: function (res) {
					console.log('当前位置的经度：' + res.longitude);
					console.log('当前位置的纬度：' + res.latitude);
					that.userConfig = res
					
					that.$api.request('config', 'getMerchantConfig', {}, failres => {
						that.$api.msg(failres.errmsg)
					}).then(res => {
						console.log('商户信息', res.data)
						that.merchantConfig = res.data
					})
				}
			})
		},
		methods: {
			// 计算距离函数
			Rad(d) { 
				//根据经纬度判断距离
				return d * Math.PI / 180.0;
			},
			getDistance(lat1, lng1, lat2, lng2) {
				// lat1用户的纬度
				// lng1用户的经度
				// lat2商家的纬度
				// lng2商家的经度
				var radLat1 = this.Rad(lat1);
				var radLat2 = this.Rad(lat2);
				var a = radLat1 - radLat2;
				var b = this.Rad(lng1) - this.Rad(lng2);
				var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
				s = s * 6378.137;
				s = Math.round(s * 10000) / 10000;
				s = s.toFixed(1) + 'km' //保留两位小数
				console.log('经纬度计算的距离:' + s)
				return s
			},
			chooseThis(good) {
				console.log('good', good)
				const that = this
				uni.showLoading({
					title: '正在加载'
				})
				that.$api.request('goods', 'getGoods', {
					spuId: good.id,
					groupShopId: ''
				}, failres => {
					that.$api.msg(failres.errmsg)
					uni.hideLoading()
				}).then(res => {
					that.goods = res.data
					if (that.goods.groupShop) {
						//若存在团购信息，将价格更新到团购价格
						that.goods.price = that.goods.groupShop.minPrice
						that.goods.vipPrice = that.goods.groupShop.minPrice
						//更新各个SKU的价格
						for (let i = 0; i < that.goods.skuList.length; i++) {
							for (let j = 0; j < that.goods.groupShop.groupShopSkuList.length; j++) {
								if (that.goods.skuList[i].id === that.goods.groupShop.groupShopSkuList[j].skuId) {
									that.goods.skuList[i].price = that.goods.groupShop.groupShopSkuList[j].skuGroupShopPrice
									that.goods.skuList[i].vipPrice = that.goods.groupShop.groupShopSkuList[j].skuGroupShopPrice
								}
							}
						}
					}
					uni.hideLoading()
				})
				
				this.goods = good
				this.goodsNum = 1
				this.modalShow = true
			},
			//选择规格
			selectSpec(skuItem, skuIndex) {
				this.selectedSku = skuItem
				this.selectedSkuIndex = skuIndex
			},
			numSub() {
				if (this.goodsNum > 1)
					this.goodsNum --
			},
			numAdd() {
				this.goodsNum ++
			},
			closeModal() {
				this.modalShow = false
				this.selectedSku = {}
				this.selectedSkuIndex = -1
			},
			//规格弹窗开关
			toggleSpec(e) {
				if (this.specClass === 'show') {
					this.specClass = 'hide';

					setTimeout(() => {
						this.specClass = 'none';
						if (this.toggleCallback) {
							this.toggleCallback()
							this.toggleCallback = undefined
						}
					}, 150);
				} else if (this.specClass === 'none') {
					this.specClass = 'show';
					if (!this.selectedSku.title) {
						this.selectedSku = this.goods.skuList[0]
						this.selectedSkuIndex = 0
					}
				}
			},
			//详情
			navToDetailPage(item) {
				//测试数据没有写id，用title代替
				let id = item.id;
				uni.navigateTo({
					url: `/pages/product/detail?id=${id}`
				})
			},
			//收藏
			toFavorite() {
				const that = this
				if (that.goods.collect) {
					//取消收藏
					that.goods.collect = false
					this.$api.request('collect', 'deleteCollect', {
						spuId: that.goods.id
					}).then(res => {

					})
				} else {
					//添加收藏
					that.goods.collect = true
					this.$api.request('collect', 'addCollect', {
						spuId: that.goods.id
					})
				}
			},
			// 直接购买
			buy() {
				const that = this
				if (!that.selectedSku.id) {
					that.specClass = 'none'
					that.toggleSpec()
					that.toggleCallback = that.buy
				} else {
					let skuItem = {
						skuId: that.selectedSku.id,
						num: that.goodsNum,
						title: that.goods.title,
						originalPrice: that.selectedSku.originalPrice,
						price: that.selectedSku.price,
						vipPrice: that.selectedSku.vipPrice,
						skuTitle: that.selectedSku.title,
						spuImg: that.goods.img,
						skuImg: that.selectedSku.img,
						stock: that.selectedSku.stock,
						spuId: that.goods.id,
						categoryId: that.goods.categoryId,
						categoryIdList: that.goods.categoryIds
					}
					if (that.goods.groupShop) {
						skuItem['groupShopId'] = that.goods.groupShop.id
					}
					let skuList = [1]
					skuList[0] = skuItem
					that.$api.globalData.skuList = skuList
					uni.navigateTo({
						url: `/pages/order/create?takeway=buy`
					})
				}
			},
			//加入购物车
			addCart(e) {
				const that = this
				if (!that.selectedSku.id) {
					that.specClass = 'none'
					that.toggleSpec()
					that.toggleCallback = that.addCart
				} else {
					//添加到购物车
					that.$api.request('cart', 'addCartItem', {
						skuId: that.selectedSku.id,
						num: that.goodsNum
					}).then(res => {
						that.$api.msg('添加购物车成功')
						that.closeModal()
					})
				}
			},
			switchChange(tab) {
				console.log(tab)
				this.deliverType = tab
			},
			// 加载分类
			async loadData(){
				const that = this
				this.$api.request('category', 'categoryList').then(async res => {
					that.rawData = res.data
					that.flist = res.data
					that.currentId = res.data[0].id
					that.slist = res.data[0].childrenList
					
					for (let i = 0; i < that.flist.length; i++) {
						let curr_i_data = that.flist[i].childrenList
						for (let j = 0; j < curr_i_data.length; j++) {
							let curr_j_data = curr_i_data[j].childrenList
							for (let k = 0; k < curr_j_data.length; k++) {
								let cate3 = curr_j_data[k]
								res = await that.loadGoods(cate3.id)
								cate3.goods = res
								
							}
						}
					}
					console.log('flist 2')
					console.log(that.flist)
					console.log(that.flist[0].childrenList[0].childrenList[0])
					that.slist = that.flist[0].childrenList
					that.$forceUpdate()
				})
				
			},
			//一级分类点击
			tabtap(item){
				this.currentId = item.id;
				this.currentId = item.id
				this.slist = item.childrenList
				this.tabScrollTop = this.tabScrollTop === 0 ? 1 : 0
			},
			navToList(tid){
				uni.navigateTo({
					url: `/pages/product/list?tid=${tid}`
				})
			},
			navigateToPage(url) {
				uni.navigateTo({
					url: url
				})
			},
			
			//加载商品 ，带下拉刷新和上滑加载
			async loadGoods(cateId) {
		
				let orderByInfo = {}
				if (this.filterIndex === 0) {
					//销量排序
					orderByInfo = {
						orderBy: 'sales',
						isAsc: false
					}
				}
				if (this.filterIndex === 1) {
					//价格排序 需要从新获取Page
					orderByInfo = {
						orderBy: 'price',
						isAsc: this.priceOrder === 1
					}
				}
				let res = await this.$api.request('goods', 'getGoodsPage', {
					categoryId: cateId,
					title: '',
					pageNo : 1,
					...orderByInfo
				})
				
				return res.data.items
				
			},
			
			//详情
			navToDetailPage(item) {
				//测试数据没有写id，用title代替
				let id = item.id;
				uni.navigateTo({
					url: `/pages/product/detail?id=${id}`
				})
			},
		}
	}
</script>

<style lang='scss'>
	/* #ifdef MP */
	.info-box{
		/* position:absolute; */
		/* left: 0; */
		top: 30upx;
		width: 100%;
		padding: 0;
		background-color: white;
		
		.title {
			font-size: 50upx;
			line-height: 80upx;
			height: 80upx;
			margin-top: 40upx;
			margin-left: 80upx;
		}
		
		.info {
			font-size: $font-sm;
			color: $font-color-light;
			margin: 20upx 0;
			margin-left: 80upx;
		}
		
		.switch {
			position: absolute;
			top: 40upx;
			right: 30upx;
			display: flex;
			align-items: center;
			justify-content: flex-end;
			height: 54rpx;
			width: 180upx;
			border-radius: 54rpx;
			background: #c1c1c1;
		}
		.switchNums {
		  width: 112rpx;
		  height: 54rpx;
		  line-height: 54rpx;
		  border-radius: 54rpx;
		  text-align: center;
		  color: white;
		  font-size: 28rpx;
		  font-weight: bold;
		  &.currentNum {
		    background: #174787;
		    color: #ffffff;
		  }
		}
		.ser-input{
			flex:1;
			width: 100%;
			height: 64upx;
			line-height: 56upx;
			text-align: left;
			padding-left: 30upx;
			font-size: 28upx;
			color:$font-color-base;
			background: rgba(255,255,255,.6);
			border: 1px solid grey;
			box-sizing: border-box;
		}
	}
	/* #endif */

	page,
	.content {
		height: 100%;
		background-color: #f8f8f8;
	}

	.content {
		display: flex;
		align-items: center;
		flex-flow: column;
	}
	
	.container {
		display: flex;
		align-items: center;
		flex-flow: row;
	}
	.left-aside {
		flex-shrink: 0;
		width: 200upx;
		height: 100%;
		background-color: #f8f8f8;
	}
	.f-item {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: 100upx;
		font-size: 28upx;
		color: $font-color-base;
		position: relative;
		&.active{
			color: $base-color;
			background: white;
			&:before{
				content: '';
				position: absolute;
				left: 0;
				top: 50%;
				transform: translateY(-50%);
				height: 100upx;
				width: 16upx;
				background-color: $base-color;
				/* border-radius: 0 4px 4px 0; */
				opacity: .8;
			}
		}
	}

	.right-aside{
		flex: 1;
		overflow: hidden;
		padding-left: 20upx;
		width: 550upx;
		background-color: white;
		min-height: 1000upx;
		padding-bottom: 40upx;
	}
	.s-item{
		display: flex;
		align-items: center;
		height: 70upx;
		padding-top: 8upx;
		font-size: 28upx;
		color: $font-color-dark;
		width: 100%;
		display: inline-block;
		clear: both;
	}
	.t-list{
		display: flex;
		flex-wrap: wrap;
		width: 100%;
		background: #fff;
		padding-top: 12upx;
		&:after{
			content: '';
			flex: 99;
			height: 0;
		}
	}
	.price-box {
		display: flex;
		align-items: baseline;
		height: 64upx;
		padding: 10upx 0;
		font-size: 26upx;
		color: $uni-color-primary;
	
		.price {
			font-size: $font-lg + 2upx;
		}
		
		.m-price {
			margin: 0 12upx;
			color: $font-color-light;
			text-decoration: line-through;
		}
		
		.coupon-tip {
			align-items: center;
			padding: 4upx 10upx;
			background: $uni-color-primary;
			font-size: $font-sm;
			color: #fff;
			border-radius: 6upx;
			line-height: 1;
			transform: translateY(-4upx);
		}
	}
	.t-item{
		flex-shrink: 0;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		width: 500upx;
		font-size: 26upx;
		color: #666;
		padding-bottom: 20upx;
		position: relative;
		height: 200upx;
		
		.img{
			position: absolute;
			left: 20upx;
			width: 140upx;
			height: 140upx;
		}
		.title {
			position: absolute;
			right: 20upx;
			width: 300upx;
			top: 25upx;
		}
/* 		.price {
			position: absolute;
			right: 160upx;
			bottom: 40upx;
		} */
		.price-box {
			position: absolute;
			left: 180upx;
			bottom: 40upx;
		}
		.chooseBtn {
			position: absolute;
			right: 0upx;
			bottom: 40upx;
			padding: 5upx 20upx;
			background-color: #174787;
			border-radius: 20upx;
			color: white;
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
			border-top: 2upx solid $font-color-light;
			margin: 0 20upx;
			position: relative;
			margin-top: 80upx;
			
			
			.sub {
				position: absolute;
				right: 100upx;
				top: 20upx;
				border: 1upx solid $font-color-light;
				color: $font-color-light;
				box-sizing: border-box;
				height: 40upx;
				width: 40upx;
				border-radius: 50%;
				line-height: 30upx;
				font-size: 40upx;
			}
			
			.num {
				position: absolute;
				right: 60upx;
				top: 20upx;
				color: $font-color-light;
				box-sizing: border-box;
				height: 40upx;
				width: 40upx;
			}
			
			.add {
				position: absolute;
				right: 20upx;
				top: 20upx;
				color: $font-color-light;
				box-sizing: border-box;
				height: 40upx;
				width: 40upx;
				border-radius: 50%;
				line-height: 30upx;
				font-size: 40upx;
				color: white;
				background-color: #28D7FE;
			}
			
			
			.attr-list {
				display: flex;
				flex-direction: column;
				font-size: $font-base + 2upx;
				color: $font-color-base;
				padding-top: 30upx;
				padding-left: 10upx;
				margin: 40upx auto;
			}
			
			.item-list {
				padding: 20upx 0 0;
				display: flex;
				flex-wrap: wrap;
			
				text {
					display: flex;
					align-items: center;
					justify-content: center;
					background: #eee;
					margin-right: 20upx;
					margin-bottom: 20upx;
					border-radius: 100upx;
					min-width: 60upx;
					height: 60upx;
					padding: 0 20upx;
					font-size: $font-base;
					color: $font-color-dark;
				}
			
				.selected {
					background: #fbebee;
					color: $uni-color-primary;
				}
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
				background-color: #174787;
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
				box-shadow: 0 20upx 40upx -16upx #174787;
				box-shadow: 1px 2px 5px rgba(219, 63, 96, 0.4);
				/* background: linear-gradient(to right, #ffac30, #fa436a, #F56C6C); */
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
