<template>
	<view class="container">
		
		<!-- 头部轮播 -->
		<view class="carousel-section">
			<!-- 标题栏和状态栏占位符 -->
			<view class="titleNview-placing"></view>
			<!-- 背景色区域 -->
			<view class="titleNview-background" :style="{backgroundColor:titleNViewBackground}"></view>
			<swiper autoplay="true" interval="3000" duration="500" class="carousel" circular @change="swiperChange" indicator-dots>
				<swiper-item v-for="(item, index) in carouselList" :key="index" class="carousel-item" @click="navigateToPage(item.url)">
					<image :src="item.imgUrl" />
				</swiper-item>
			</swiper>
		</view>
		
		<!-- 外卖 -->
		<view class="wm-section">
			<view class="container" @click="navigateToPage('/pages/category/category', 1)">
				<view class="title">门店自取</view>
				<view class="info">下单免排队</view>
				<image src="https://guoyu-admin.windymuse.cn/static/img/%E9%97%A8%E5%BA%97%E8%87%AA%E5%8F%96.jpg" class="img" mode=""></image>
			</view>
			<view class="container" @click="navigateToPage('/pages/category/category', 0)">
				<view class="title">外卖</view>
				<view class="info">无接触配送，安全到家</view>
				<image src="https://guoyu-admin.windymuse.cn/static/img/%E5%A4%96%E5%8D%96.jpg" class="img" mode=""></image>
			</view>
		</view>
		
		<!-- 底部操作菜单 -->
		<view class="page-bottom" v-if="hasLogin">
			<view class="p-b-btn">
				<view class="title">我的积分 {{userInfo.points}}</view>
				<view class="info">积分可用于抵扣价格</view>
			</view>
		</view>
		
		
		

		<!-- 分类推荐楼层 -->
		<view v-if="categoryPickList.length > 0" class="f-header m-t">
			<image src="/static/temp/h1.png"></image>
			<view class="tit-box">
				<text class="tit">分类精选</text>
				<text class="tit2">Competitive Products For You</text>
			</view>
		</view>
		<view v-if="categoryPickList.length > 0" v-for="(item, index) in categoryPickList" :key="index" class="hot-floor">
			<view class="floor-img-box">
				<image class="floor-img" :src="item.imgUrl" mode="scaleToFill"></image>
			</view>
			<scroll-view class="floor-list" scroll-x>
				<view class="scoll-wrapper">
					<view 
						v-for="(spuItem, skuIndex) in item.data" :key="skuIndex"
						class="floor-item"
						@click="navToDetailPage(spuItem.id)"
					>
						<image :src="spuItem.img + '/200px'" mode="aspectFill"></image>
						<text class="title clamp">{{spuItem.title}}</text>
						<text class="price">￥{{(isVip ? spuItem.vipPrice : spuItem.price) / 100 }}</text>
					</view>
					<view @click="naviageToPage(item.url)" class="more">
						<text>查看全部</text>
						<text>More+</text>
					</view>
				</view>
			</scroll-view>
		</view>
		
		<!-- 销量top -->
		<view class="f-header m-t">
			<image src="/static/temp/h1.png"></image>
			<view class="tit-box">
				<text class="tit">热销宝贝</text>
				<text class="tit2">Hot Sale</text>
			</view>
		</view>
		
		<view class="guess-section">
			<view 
				v-for="(item, index) in salesTop" :key="index"
				class="guess-item"
				@click="navToDetailPage(item.id)"
			>
				<view class="image-wrapper">
					<image :src="item.img + '/400px'" mode="aspectFill"></image>
				</view>
				<text class="title clamp">{{item.title}}</text>
				<text class="price">￥{{isVip ? (item.vipPrice / 100 + ' [VIP]') : item.price / 100}}</text>
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
				titleNViewBackground: '',
				swiperCurrent: 0,
				swiperLength: 0,
				carouselList: [],
				windowSpuList: [],
				categoryPickList: [],
				categoryButtomList: [],
				salesTop: [],
				banner: undefined,
				isVip: false
			};
		},
        computed: {
			...mapState(['hasLogin','userInfo']),
		},
		onShow() {
			this.isVip = this.$api.isVip()
		},
		onLoad(options) {
			//#ifdef H5
			//H5进入，有可能是回调进来的
			if (options.code && options.state) {
				const that = this
				that.logining = true
				that.$api.request('user', 'thirdPartLogin', {
					loginType: 3,
					raw: options.code
				}, failres => {
					that.logining = false
					that.$api.msg(failres.errmsg)
				}).then(res => {
					//登录成功，重定向到指定目标
					that.logining = false
					that.$store.commit('login', res.data)
					uni.setStorageSync('userInfo', res.data)
					//重定向到
					//不能重定向到tabbar页面
					if (options.state === '/pages/cart/cart' || options.state === '/pages/user/user' 
					|| options.state === '/pages/index/index' || options.state === '/pages/category/category') {
						uni.switchTab({
							url: options.state
						})
					} else {
						uni.redirectTo({
							url: options.state
						})
					}
					
				})
			}
			//#endif
			this.loadData()
		},
		onShareAppMessage() {
			return {
				title: '国渔鲜生小程序',
				desc: '全球鲜生供应商'
			}
		},
		onShareTimeline() {
			return {}
		},
		methods: {
			async loadData() {
				const that = this
				uni.showLoading({
					title: '正在加载'
				})
				that.$api.request('integral', 'getIndexData', failres => {
					that.$api.msg(failres.errmsg)
					uni.hideLoading()
				}).then(res => {
					let data = res.data
					//橱窗
					that.windowSpuList = data.windowRecommend
					//轮播
					data.advertisement.t1.forEach(item => {
						if (!item.color) {
							item.color = 'rgb(205, 215, 218)'
						}
					})
					that.carouselList = data.advertisement.t1
					that.swiperLength = data.advertisement.t1.length
					that.titleNViewBackground = data.advertisement.t1[0].color
					//分类精选
					if (data.advertisement.t2) {
						that.categoryPickList = data.advertisement.t2
					}
					//横幅
					if (data.advertisement.t3 && data.advertisement.t3.length > 0) {
						that.banner = data.advertisement.t3[0]
					}
					//热销
					if (data.salesTop) {
						that.salesTop = data.salesTop
					}
					//分类5Buttom
					if (data.advertisement.t4) {
						that.categoryButtomList = data.advertisement.t4
					}
					uni.hideLoading()
				})
			},
			//轮播图切换修改背景色
			swiperChange(e) {
				const index = e.detail.current;
				this.swiperCurrent = index;
				this.titleNViewBackground = this.carouselList[index].color;
			},
			//详情页
			navToDetailPage(id) {
				uni.navigateTo({
					url: `/pages/product/detail?id=${id}`
				})
			},
			navToWindowSuggestSpu(index) {
				const that = this
				uni.navigateTo({
					url: '/pages/product/detail?id=' + that.windowSpuList[index].spuId
				})
			},
			navigateToPage(page, deliverType=0) {
				console.log(page)
				if (page.indexOf('category') >= 0) {
					uni.setStorageSync('deliverType', deliverType);
					uni.switchTab({
						url: page
					})
				} else {
					uni.navigateTo({
						url: page
					})
				}
			}
		},
		// #ifndef MP
		// 标题栏input搜索框点击
		onNavigationBarSearchInputClicked: async function(e) {
			uni.navigateTo({
				url: '/pages/product/search'
			})
		},
		//点击导航栏 buttons 时触发
		// onNavigationBarButtonTap(e) {
		// 	const index = e.index;
		// 	if (index === 0) {
		// 		this.$api.msg('点击了扫描');
		// 	} else if (index === 1) {
		// 		// #ifdef APP-PLUS
		// 		const pages = getCurrentPages();
		// 		const page = pages[pages.length - 1];
		// 		const currentWebview = page.$getAppWebview();
		// 		currentWebview.hideTitleNViewButtonRedDot({
		// 			index
		// 		});
		// 		// #endif
		// 		uni.navigateTo({
		// 			url: '/pages/notice/notice'
		// 		})
		// 	}
		// }
		// #endif
	}
</script>

<style lang="scss">
	/* #ifdef MP */
	.mp-search-box{
		position:absolute;
		left: 0;
		top: 30upx;
		z-index: 9999;
		width: 100%;
		padding: 0 80upx;
		.ser-input{
			flex:1;
			height: 56upx;
			line-height: 56upx;
			text-align: center;
			font-size: 28upx;
			color:$font-color-base;
			border-radius: 20px;
			background: rgba(255,255,255,.6);
		}
	}
	page{
		.cate-section{
			position:relative;
			z-index:5;
			border-radius:16upx 16upx 0 0;
			margin-top:-20upx;
		}
		.carousel-section{
			padding: 0;
			.titleNview-placing {
				padding-top: 0;
				height: 0;
			}
			.carousel{
				.carousel-item{
					padding: 0;
				}
			}
			.swiper-dots{
				left:45upx;
				bottom:40upx;
			}
		}
	}
	/* #endif */
	
	.wm-section {
		background-color: white;
		display: flex;
		justify-content: space-around;
		align-items: center;
		padding: 40upx;
		.container {
			width: 320upx;
			height: 320upx;
			text-align: center;
			padding: 20upx;
			margin: 20upx;
			overflow: hidden;
			border-radius: 20upx;
			box-shadow: 0 0 5px #333333;
			
			.title {
				font-size: $font-lg;
				font-weight: bold;
				margin-bottom: 10upx;
			}
			.info {
				font-size: $font-sm;
				color: grey;
			}
			.img {
				max-width: 200upx;
				max-height: 200upx;
				
			}
		}
	}
	
	page {
		background: #f5f5f5;
	}
	.m-t{
		margin-top: 16upx;
	}
	/* 头部 轮播图 */
	.carousel-section {
		position: relative;
		padding-top: 10px;

		.titleNview-placing {
			height: var(--status-bar-height);
			padding-top: 44px;
			box-sizing: content-box;
		}

		.titleNview-background {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 426upx;
			transition: .4s;
		}
	}
	.carousel {
		width: 100%;
		height: 550upx;

		.carousel-item {
			width: 100%;
			height: 100%;
			padding: 0 28upx;
			overflow: hidden;
		}

		image {
			width: 100%;
			height: 100%;
			border-radius: 10upx;
		}
	}
	.swiper-dots {
		display: flex;
		position: absolute;
		left: 60upx;
		bottom: 15upx;
		width: 72upx;
		height: 36upx;
		background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAABkCAYAAADDhn8LAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTMyIDc5LjE1OTI4NCwgMjAxNi8wNC8xOS0xMzoxMzo0MCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OTk4MzlBNjE0NjU1MTFFOUExNjRFQ0I3RTQ0NEExQjMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OTk4MzlBNjA0NjU1MTFFOUExNjRFQ0I3RTQ0NEExQjMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6Q0E3RUNERkE0NjExMTFFOTg5NzI4MTM2Rjg0OUQwOEUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6Q0E3RUNERkI0NjExMTFFOTg5NzI4MTM2Rjg0OUQwOEUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4Gh5BPAAACTUlEQVR42uzcQW7jQAwFUdN306l1uWwNww5kqdsmm6/2MwtVCp8CosQtP9vg/2+/gY+DRAMBgqnjIp2PaCxCLLldpPARRIiFj1yBbMV+cHZh9PURRLQNhY8kgWyL/WDtwujjI8hoE8rKLqb5CDJaRMJHokC6yKgSCR9JAukmokIknCQJpLOIrJFwMsBJELFcKHwM9BFkLBMKFxNcBCHlQ+FhoocgpVwwnv0Xn30QBJGMC0QcaBVJiAMiec/dcwKuL4j1QMsVCXFAJE4s4NQA3K/8Y6DzO4g40P7UcmIBJxbEesCKWBDg8wWxHrAiFgT4fEGsB/CwIhYE+AeBAAdPLOcV8HRmWRDAiQVcO7GcV8CLM8uCAE4sQCDAlHcQ7x+ABQEEAggEEAggEEAggEAAgQACASAQQCCAQACBAAIBBAIIBBAIIBBAIABe4e9iAe/xd7EAJxYgEGDeO4j3EODp/cOCAE4sYMyJ5cwCHs4rCwI4sYBxJ5YzC84rCwKcXxArAuthQYDzC2JF0H49LAhwYUGsCFqvx5EF2T07dMaJBetx4cRyaqFtHJ8EIhK0i8OJBQxcECuCVutxJhCRoE0cZwMRyRcFefa/ffZBVPogePihhyCnbBhcfMFFEFM+DD4m+ghSlgmDkwlOgpAl4+BkkJMgZdk4+EgaSCcpVX7bmY9kgXQQU+1TgE0c+QJZUUz1b2T4SBbIKmJW+3iMj2SBVBWz+leVfCQLpIqYbp8b85EskIxyfIOfK5Sf+wiCRJEsllQ+oqEkQfBxmD8BBgA5hVjXyrBNUQAAAABJRU5ErkJggg==);
		background-size: 100% 100%;

		.num {
			width: 36upx;
			height: 36upx;
			border-radius: 50px;
			font-size: 24upx;
			color: #fff;
			text-align: center;
			line-height: 36upx;
		}

		.sign {
			position: absolute;
			top: 0;
			left: 50%;
			line-height: 36upx;
			font-size: 12upx;
			color: #fff;
			transform: translateX(-50%);
		}
	}
	/* 分类 */
	.cate-section {
		display: flex;
		justify-content: space-around;
		align-items: center;
		flex-wrap:wrap;
		padding: 30upx 22upx; 
		background: #fff;
		.cate-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			font-size: $font-sm + 2upx;
			color: $font-color-dark;
		}
		/* 原图标颜色太深,不想改图了,所以加了透明度 */
		image {
			width: 88upx;
			height: 88upx;
			margin-bottom: 14upx;
			border-radius: 50%;
			opacity: .7;
			box-shadow: 4upx 4upx 20upx rgba(250, 67, 106, 0.3);
		}
	}
	.ad-1{
		width: 100%;
		height: 210upx;
		padding: 10upx 0;
		background: #fff;
		image{
			width:100%;
			height: 100%; 
		}
	}
	/* 秒杀专区 */
	.seckill-section{
		padding: 4upx 30upx 24upx;
		background: #fff;
		.s-header{
			display:flex;
			align-items:center;
			height: 92upx;
			line-height: 1;
			.s-img{
				width: 140upx;
				height: 30upx;
			}
			.tip{
				font-size: $font-base;
				color: $font-color-light;
				margin: 0 20upx 0 40upx;
			}
			.timer{
				display:inline-block;
				width: 40upx;
				height: 36upx;
				text-align:center;
				line-height: 36upx;
				margin-right: 14upx;
				font-size: $font-sm+2upx;
				color: #fff;
				border-radius: 2px;
				background: rgba(0,0,0,.8);
			}
			.icon-you{
				font-size: $font-lg;
				color: $font-color-light;
				flex: 1;
				text-align: right;
			}
		}
		.floor-list{
			white-space: nowrap;
		}
		.scoll-wrapper{
			display:flex;
			align-items: flex-start;
		}
		.floor-item{
			width: 150upx;
			margin-right: 20upx;
			font-size: $font-sm+2upx;
			color: $font-color-dark;
			line-height: 1.8;
			image{
				width: 150upx;
				height: 150upx;
				border-radius: 6upx;
			}
			.price{
				color: $uni-color-primary;
			}
		}
	}
	
	.f-header{
		display:flex;
		align-items:center;
		height: 140upx;
		padding: 6upx 30upx 8upx;
		background: #fff;
		image{
			flex-shrink: 0;
			width: 80upx;
			height: 80upx;
			margin-right: 20upx;
		}
		.tit-box{
			flex: 1;
			display: flex;
			flex-direction: column;
		}
		.tit{
			font-size: $font-lg +2upx;
			color: #font-color-dark;
			line-height: 1.3;
		}
		.tit2{
			font-size: $font-sm;
			color: $font-color-light;
		}
		.icon-you{
			font-size: $font-lg +2upx;
			color: $font-color-light;
		}
	}
	/* 团购楼层 */
	.group-section{
		background: #fff;
		.g-swiper{
			height: 650upx;
			padding-bottom: 30upx;
		}
		.g-swiper-item{
			width: 100%;
			padding: 0 30upx;
			display:flex;
		}
		image{
			width: 100%;
			height: 460upx;
			border-radius: 4px;
		}
		.g-item{
			display:flex;
			flex-direction: column;
			overflow:hidden;
		}
		.left{
			flex: 1.2;
			margin-right: 24upx;
			.t-box{
				padding-top: 20upx;
			}
		}
		.right{
			flex: 0.8;
			flex-direction: column-reverse;
			.t-box{
				padding-bottom: 20upx;
			}
		}
		.t-box{
			height: 160upx;
			font-size: $font-base+2upx;
			color: $font-color-dark;
			line-height: 1.6;
		}
		.price{
			color:$uni-color-primary;
		}
		.m-price{
			font-size: $font-sm+2upx;
			text-decoration: line-through;
			color: $font-color-light;
			margin-left: 8upx;
		}
		.pro-box{
			display:flex;
			align-items:center;
			margin-top: 10upx;
			font-size: $font-sm;
			color: $font-base;
			padding-right: 10upx;
		}
		.progress-box{
			flex: 1;
			border-radius: 10px;
			overflow: hidden;
			margin-right: 8upx;
		}
	}
	/* 分类推荐楼层 */
	.hot-floor{
		width: 100%;
		overflow: hidden;
		margin-bottom: 20upx;
		.floor-img-box{
			width: 100%;
			height:320upx;
			position:relative;
			&:after{
				content: '';
				position:absolute;
				left: 0;
				top: 0;
				width: 100%;
				height: 100%;
				background: linear-gradient(rgba(255,255,255,.06) 30%, #f8f8f8);
			}
		}
		.floor-img{
			width: 100%;
			height: 100%;
		}
		.floor-list{
			white-space: nowrap;
			padding: 20upx;
			padding-right: 50upx;
			border-radius: 6upx;
			margin-top:-140upx;
			margin-left: 30upx;
			background: #fff;
			box-shadow: 1px 1px 5px rgba(0,0,0,.2);
			position: relative;
			z-index: 1;
		}
		.scoll-wrapper{
			display:flex;
			align-items: flex-start;
		}
		.floor-item{
			width: 180upx;
			margin-right: 20upx;
			font-size: $font-sm+2upx;
			color: $font-color-dark;
			line-height: 1.8;
			image{
				width: 180upx;
				height: 180upx;
				border-radius: 6upx;
			}
			.price{
				color: $uni-color-primary;
			}
		}
		.more{
			display:flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			flex-shrink: 0;
			width: 180upx;
			height: 180upx;
			border-radius: 6upx;
			background: #f3f3f3;
			font-size: $font-base;
			color: $font-color-light;
			text:first-child{
				margin-bottom: 4upx;
			}
		}
	}
	/* 猜你喜欢 */
	.guess-section{
		display:flex;
		flex-wrap:wrap;
		padding: 0 30upx;
		background: #fff;
		margin-bottom: 120upx;
		.guess-item{
			display:flex;
			flex-direction: column;
			width: 48%;
			padding-bottom: 40upx;
			&:nth-child(2n+1){
				margin-right: 4%;
			}
		}
		.image-wrapper{
			width: 100%;
			height: 330upx;
			border-radius: 3px;
			overflow: hidden;
			image{
				width: 100%;
				height: 100%;
				opacity: 1;
			}
		}
		.title{
			font-size: $font-lg;
			color: $font-color-dark;
			line-height: 80upx;
		}
		.price{
			font-size: $font-lg;
			color: $uni-color-primary;
			line-height: 1;
		}
	}
	
	
	/* 底部操作菜单 */
	.page-bottom {
		position: fixed;
		left: 0;
		bottom: 20upx;
		z-index: 95;
		text-align: left;
		width: 750upx;
		height: 110upx;
		background: rgba(255, 255, 255, .9);
		box-shadow: 0 0 20upx 0 rgba(0, 0, 0, .5);
	
		.p-b-btn {
			margin-left: 30upx;
			font-size: $font-sm;
			color: $font-color-base;
			height: 80upx;
			
			.title {
				font-size: $font-lg;
				font-weight: bold;
				margin: 10upx 0;
			}
			.info {
				font-size: $font-sm;
				color: $font-color-light;
			}
		}
	}

</style>
