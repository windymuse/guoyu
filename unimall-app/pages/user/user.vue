<template>  
    <view class="container">
		<!-- 头部轮播 -->
		<view class="carousel-section">
			<!-- 标题栏和状态栏占位符 -->
			<!-- <view class="titleNview-placing"></view> -->
			<!-- 背景色区域 -->
			<!-- <view class="titleNview-background" :style="{backgroundColor:titleNViewBackground}"></view> -->
			<swiper autoplay="true" interval="3000" duration="500" class="carousel" circular @change="swiperChange" indicator-dots="true">
				<swiper-item v-for="(item, index) in carouselList" :key="index" class="carousel-item" @click="naviageToPage(item.url)">
					<image :src="item.imgUrl" />
				</swiper-item>
			</swiper>
		</view>
		<view class="user-section">
			<!-- <image class="bg" src="/static/user-bg.jpg"></image> -->
			<view class="user-info-box">
				<view class="info-box">
					<text @click="toLogin" class="username">{{ hasLogin? ('Hey，' + userInfo.nickname || '未设置昵称') : '立即登录' }}</text>
					<text v-if="hasLogin" class="level">{{currVipDegree.name}}</text>
					
				</view>
				<view class="portrait-box">
					<image class="portrait" :src="userInfo.avatarUrl || '/static/missing-face.png'"></image>
				</view>
			</view>
			<view v-if="hasLogin" class="vip-card-box">
				<view class="b-btn">
					当前等级折扣 {{currVipDegree.percent == 100 ? '无折扣' : currVipDegree / 10 + '折'}}
				</view>
				<view class="tit">
					Lv{{userInfo.level || 0}}
				</view>
				<view class="progress-box">
				  <progress :percent="upgradePercent" active stroke-width='10' border-radius='10' color="#478FFF" />
				</view>
				<text class="e-b">再消费{{moneyToNext || 0}}元，即可升级为Lv{{(userInfo.level || 0) + 1}}</text>
			</view>
		</view>
		
		<view 
			class="cover-container"
		>
			
			<!-- <view class="tj-sction">
				<view class="tj-item">
					<text class="num">128.8</text>
					<text>余额</text>
				</view>
				<view class="tj-item">
					<text class="num">0</text>
					<text>优惠券</text>
				</view>
				<view class="tj-item">
					<text class="num">20</text>
					<text>积分</text>
				</view>
			</view> -->
			<view class="order-section">
				<view class="order-item" @click="navTo('/pages/order/list?state=0')" hover-class="common-hover"  :hover-stay-time="50">
					<text class="yticon icon-shouye"></text>
					<text>全部订单</text>
				</view>
				<view class="order-item" @click="navTo('/pages/order/list?state=10')"  hover-class="common-hover" :hover-stay-time="50">
					<text class="yticon icon-daifukuan"></text>
					<text>待付款</text>
				</view>
				<view class="order-item" @click="navTo('/pages/order/list?state=30')" hover-class="common-hover"  :hover-stay-time="50">
					<text class="yticon icon-yishouhuo"></text>
					<text>待收货</text>
				</view>
				<view class="order-item" @click="navTo('/pages/order/list?state=60')" hover-class="common-hover"  :hover-stay-time="50">
					<text class="yticon icon-shouhoutuikuan"></text>
					<text>退款/售后</text>
				</view>
			</view>
			
			 <view class="history-section icon">
				<view v-if="footprintList.length > 0" class="sec-header">
					<text class="yticon icon-lishijilu"></text>
					<text>浏览历史</text>
				</view>
				<scroll-view v-if="footprintList.length > 0" scroll-x class="h-list">
					<image v-for="(item, index ) in footprintList" :key="index" @longpress="deleteFootprint(item)" @click="navTo('/pages/product/detail?id=' + item.spuId)" :src="item.spuImg + '?x-oss-process=style/200px'" mode="aspectFill"></image>
				</scroll-view>
				<list-cell icon="icon-dizhi" iconColor="#5fcda2" title="地址管理" @eventClick="navTo('/pages/address/list')"></list-cell>
				<list-cell icon="icon-shoucang_xuanzhongzhuangtai" iconColor="#54b4ef" title="我的收藏" @eventClick="navTo('/pages/product/favorite')"></list-cell>
				<list-cell icon="icon-huifu" iconColor="#e07472" title="在线客服" :openType="'contact'"></list-cell>
				<list-cell icon="icon-tuandui" iconColor="#EE82EE" title="个人资料" @eventClick="navTo('/pages/user/profile')"></list-cell>
				<list-cell icon="icon-iconfontweixin" iconColor="#EEEE00" title="我的优惠券" @eventClick="navTo('/pages/coupon/coupon')"></list-cell>
				<list-cell icon="icon-pinglun-copy" iconColor="#ee883b" title="关于" @eventClick="navTo('/pages/user/about')"></list-cell>
				<list-cell icon="icon-shanchu4" iconColor="#e07472" title="退出登录" border="" @eventClick="logout()"></list-cell>
			</view> 
		</view>
			
		
    </view>  
</template>  
<script>  
	import listCell from '@/components/mix-list-cell';
    import {  
        mapState 
    } from 'vuex';  
	let startY = 0, moveY = 0, pageAtTop = true;
    export default {
		components: {
			listCell
		},
		data(){
			return {
				titleNViewBackground: '',
				swiperCurrent: 0,
				swiperLength: 0,
				carouselList: [],
				coverTransform: 'translateY(0px)',
				coverTransition: '0s',
				moving: false,
				footprintList: [],
				isVip: false,
				currVipDegree: {},
				nextVipDegree: {}
			}
		},
		onShow() {
			this.isVip = this.$api.isVip()
			this.loadFootprint()
			this.loadData()
			if (this.hasLogin) {
				this.loadMemberLevel()
			}
		},
		onLoad(){
			this.loadData()
			if (this.hasLogin) {
				this.loadMemberLevel()
			}
		},
		// #ifndef MP
		onNavigationBarButtonTap(e) {
			const index = e.index;
			if (index === 0) {
				this.navTo('/pages/set/set');
			}else if(index === 1){
				// #ifdef APP-PLUS
				const pages = getCurrentPages();
				const page = pages[pages.length - 1];
				const currentWebview = page.$getAppWebview();
				currentWebview.hideTitleNViewButtonRedDot({
					index
				});
				// #endif
				uni.navigateTo({
					url: '/pages/notice/notice'
				})
			}
		},
		// #endif
        computed: {
			...mapState(['hasLogin','userInfo']),
			moneyToNext() {
				if (this.nextVipDegree.money > 0) {
					return (this.nextVipDegree.money - this.userInfo.points) / 100
				} else {
					return 0
				}
			},
			upgradePercent() {
				// 升级进度
				if (this.nextVipDegree.money > 0) {
					return (this.userInfo.points / this.nextVipDegree.money) * 100
				} else {
					return 0
				}
			}
		},
        methods: {
			async loadMemberLevel() {
				const that = this
				that.$api.request('member_level', 'getMemberLevel',{
					degree: that.userInfo.level,
					page: 1,
					limit: 1
				}, failres => {
					that.$api.msg(failres.errmsg)
				}).then(res => {
					let data = res.data
					console.log('curr level', data)
					if (data && data.length > 0) {
						this.currVipDegree = data[0]
					}
				})
				that.$api.request('member_level', 'getMemberLevel',{
					degree: that.userInfo.level + 1,
					page: 1,
					limit: 1
				}, failres => {
					that.$api.msg(failres.errmsg)
				}).then(res => {
					let data = res.data
					console.log('next level', data)
					if (data && data.length > 0) {
						this.nextVipDegree = data[0]
					}
				})
			},
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
			async loadFootprint() {
				const that = this
				that.$api.request('footprint', 'getAllFootprint').then(res => {
					that.footprintList = res.data
				})
			},
			
			deleteFootprint(item) {
				const that = this
				uni.showModal({
					title: '删除？',
					content: '您确定要删除此足迹吗？',
					success : (e) => {
						if (e.confirm) {
							that.$api.request('footprint', 'deleteFootprint', {
								footprintId: item.id
							}).then(res => {
								that.loadFootprint()
							})
						}
					}
				})
			},
			
			toLogin() {
				if (!this.hasLogin) {
					uni.navigateTo({
						url: '/pages/public/login'
					})
				}
			},
			
			logout() {
				const that = this
				uni.showModal({
					title: '询问',
					content: '您确定要退出吗？',
					cancelText: '取消',
					confirmText: '确定',
					success: (e) => {
						if (e.confirm) {
							that.$store.commit('logout')
							that.$api.logout()
						}
					}
				})
			},

			/**
			 * 统一跳转接口,拦截未登录路由
			 * navigator标签现在默认没有转场动画，所以用view
			 */
			navTo(url){
				if(!this.hasLogin){
					url = '/pages/public/login';
				}
				uni.navigateTo({  
					url
				})
			}, 
	
			//轮播图切换修改背景色
			swiperChange(e) {
				const index = e.detail.current;
				this.swiperCurrent = index;
				this.titleNViewBackground = this.carouselList[index].color;
			},
			/**
			 *  会员卡下拉和回弹
			 *  1.关闭bounce避免ios端下拉冲突
			 *  2.由于touchmove事件的缺陷（以前做小程序就遇到，比如20跳到40，h5反而好很多），下拉的时候会有掉帧的感觉
			 *    transition设置0.1秒延迟，让css来过渡这段空窗期
			 *  3.回弹效果可修改曲线值来调整效果，推荐一个好用的bezier生成工具 http://cubic-bezier.com/
			 */
			coverTouchstart(e){
				if(pageAtTop === false){
					return;
				}
				this.coverTransition = 'transform .1s linear';
				startY = e.touches[0].clientY;
			},
			coverTouchmove(e){
				moveY = e.touches[0].clientY;
				let moveDistance = moveY - startY;
				if(moveDistance < 0){
					this.moving = false;
					return;
				}
				this.moving = true;
				if(moveDistance >= 80 && moveDistance < 100){
					moveDistance = 80;
				}
		
				if(moveDistance > 0 && moveDistance <= 80){
					this.coverTransform = `translateY(${moveDistance}px)`;
				}
			},
			coverTouchend(){
				if(this.moving === false){
					return;
				}
				this.moving = false;
				this.coverTransition = 'transform 0.3s cubic-bezier(.21,1.93,.53,.64)';
				this.coverTransform = 'translateY(0px)';
			}
        }  
    }  
</script>  
<style lang='scss'>
	/* 头部轮播 */
	
	/* 头部 轮播图 */
	.carousel-section {
		position: relative;
		/* padding-top: 10px; */
	
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
			overflow: hidden;
		}
	
		image {
			width: 100%;
			height: 100%;
		}
	}
	
	
	%flex-center {
	 display:flex;
	 flex-direction: column;
	 justify-content: center;
	 align-items: center;
	}
	%section {
	  display:flex;
	  justify-content: space-around;
	  align-content: center;
	  background: #fff;
	  border-radius: 10upx;
	}

	.user-section{
		padding: 10upx 30upx 0;
		position:relative;
		margin: 30upx;
		box-shadow: 0 0 10upx black;
		border-radius: 20upx;
		.bg{
			position:absolute;
			left: 0;
			top: 0;
			width: 100%;
			height: 100%;
			filter: blur(1px);
			opacity: .7;
		}
		
		.user-info-box{
			height: 180upx;
			display:flex;
			align-items:center;
			justify-content: space-between;
			position:relative;
			z-index: 1;
			.portrait-box {
				width: 220upx;
				text-align: center;
				.portrait{
					width: 130upx;
					height: 130upx;
					border:5upx solid #fff;
					border-radius: 50%;
				}
			}
			.username{
				font-size: $font-lg + 6upx;
				color: $font-color-dark;
				margin-left: 20upx;
				margin-bottom: 20upx;
			}
			.level{
				display: block;
				font-size: $font-lg + 6upx;
				color: $font-color-dark;
				margin-left: 30upx;
				margin-top: 20upx;
			}
		}
		
		.vip-card-box{
			display:flex;
			flex-direction: column;
			/* height: 240upx; */
			background: linear-gradient(left, rgba(0,0,0,.7), rgba(0,0,0,.8));
			border-radius: 16upx 16upx 0 0;
			overflow: hidden;
			position: relative;
			padding: 20upx 24upx;
			.card-bg{
				position:absolute;
				top: 20upx;
				right: 0;
				width: 380upx;
				height: 260upx;
			}
			.b-btn{
				position: absolute;
				right: 20upx;
				top: 0upx;
				/* width: 132upx; */
				height: 40upx;
				text-align: center;
				line-height: 40upx;
				font-size: 22upx;
				color: #36343c;
				border-radius: 20px;
				background: linear-gradient(left, #f9e6af, #ffd465);
				z-index: 1;
			}
			.tit{
				font-size: $font-base+2upx;
				border: 6upx solid #f7d680;
				margin-bottom: 28upx;
				display: inline-block;
				width: 100upx;
				text-align: center;
			}
			.progress-box {
				height: 40upx;
			}
			.e-b{
				font-size: $font-sm;
				margin-top: 10upx;
				margin: 10upx auto;
			}
		}
	}
	.cover-container{
		background: $page-color-base;
		margin-top: 0upx;
		padding: 30upx;
		position:relative;
		background: #f5f5f5;
		padding-bottom: 20upx;
		.arc{
			position:absolute;
			left: 0;
			top: -34upx;
			width: 100%;
			height: 36upx;
		}
	}
	.tj-sction{
		@extend %section;
		.tj-item{
			@extend %flex-center;
			flex-direction: column;
			height: 140upx;
			font-size: $font-sm;
			color: #75787d;
		}
		.num{
			font-size: $font-lg;
			color: $font-color-dark;
			margin-bottom: 8upx;
		}
	}
	.order-section{
		@extend %section;
		padding: 28upx 0;
		.order-item{
			@extend %flex-center;
			width: 120upx;
			height: 120upx;
			border-radius: 10upx;
			font-size: $font-sm;
			color: $font-color-dark;
		}
		.yticon{
			font-size: 48upx;
			margin-bottom: 18upx;
			color: #fa436a;
		}
		.icon-shouhoutuikuan{
			font-size:44upx;
		}
	}
	.history-section{
		padding: 30upx 0 0;
		margin-top: 20upx;
		background: #fff;
		border-radius:10upx;
		.sec-header{
			display:flex;
			align-items: center;
			font-size: $font-base;
			color: $font-color-dark;
			line-height: 40upx;
			margin-left: 30upx;
			.yticon{
				font-size: 44upx;
				color: #5eba8f;
				margin-right: 16upx;
				line-height: 40upx;
			}
		}
		.h-list{
			white-space: nowrap;
			padding: 30upx 30upx 0;
			image{
				display:inline-block;
				width: 160upx;
				height: 160upx;
				margin-right: 20upx;
				border-radius: 10upx;
			}
		}
	}
	
</style>