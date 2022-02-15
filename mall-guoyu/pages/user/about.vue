<template>
	<view class="content">
		<view class="row b-b">
			<text class="tit">版本号</text>
			<text class="tiv">v1.0.0</text>
		</view>
		<view class="row b-b">
			<text class="tit">贡献</text>
			<text class="tiv">Mike</text>
		</view>
		<view class="row b-b">
			<text class="tit">邮箱</text>
			<text class="tiv">cengwei@windyvs.com</text>
		</view>
		<text class="tcp">由北京风也科技有限公司开发</text>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
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
		methods: {
			
		}
	}
</script>

<style lang="scss">
	page {
		background: $page-color-base;
		padding-top: 16upx;
	}

	.row{
		display: flex;
		align-items: center;
		position: relative;
		padding:0 30upx;
		height: 110upx;
		background: #fff;
		
		.tit{
			flex-shrink: 0;
			width: 120upx;
			font-size: 30upx;
			color: $font-color-dark;
		}
		.tiv{
			flex: 1;
			font-size: 30upx;
			color: #aaaaaa;
			text-align: right;
		}
		
	}
	
	.tcp{
		width: 100%;
		font-size: 28upx;
		color: #aaaaaa;
		text-align: center;
		position: absolute;
		bottom: 200upx;
	}
	
	
	
</style>
