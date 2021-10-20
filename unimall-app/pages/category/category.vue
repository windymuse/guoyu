<template>
	<view class="content">
	
		<!-- 小程序头部兼容 -->
		<!-- #ifdef MP -->
		<view class="info-box">
			<view class="title">xxxxx店</view>
			<view class="info">距离您xxkm</view>
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
							<image class="img" :src="good.img + '?x-oss-process=style/200px'"></image>
							<text class="title">{{good.title}}</text>
							<text class="price">￥ {{good.originalPrice / 100}}</text>
							<view class="chooseBtn">选规格</view>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
	import neilModal from '@/components/neil-modal/neil-modal.vue';
	export default {
		data() {
			return {
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
			}
		},
		async onLoad(){
			await this.loadData()
			console.log('flist')
			console.log(this.flist)
		},
		methods: {
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
		z-index: 9999;
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
		    background: #6a74c9;
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
		}
		.price {
			position: absolute;
			right: 30upx;
			bottom: 50upx;
		}
		.chooseBtn {
			position: absolute;
			right: 0upx;
			bottom: 0;
			padding: 5upx 20upx;
			background-color: #28D7FE;
			border-radius: 20upx;
			color: white;
		}
	}
</style>
