<template>
	<view style="position: fixed;z-index: -9999;">
		<canvas :canvas-id="canvasID" :style="{width:canvasWidth+'px',height:canvasHeight+'px'}"></canvas>
		<view v-if="qrCode == ''">
			<QRCode ref="qrcode" />
		</view>
	</view>
</template>

<script>
	import QRCode from "../qr_code/qrcode.vue"
	var _this;
	export default {
		name: 'canvas-images',
		props: {
			// canvasID 等同于 canvas-id
			canvasID: {
				Type: String,
				default: 'shareCanvas'
			},
			canvasWidth:{	// 画布宽度
				Type: 'int',
				default: 375
			},
			canvasHeight:{	// 画布高度
				Type: 'int',
				default: 500
			},
			shareTitle:{	// 分享标题
				Type: 'String',
				default: '我是这张图片的标题'
			},
			goodsTitle:{	// 商品宣传标题
				Type: 'String',
				default: '我是这张图片的标题我是这张图片的标题我是这张图片的标'
			},
			goodsPrice:{	// 商品秒杀价
				Type: 'Number',
				default: 0.0
			},
			goodsOriginalPrice:{	// 商品日常价
				Type: 'Number',
				default: 0.0
			},
			shareImage:{	// 分享图片
				Type: 'String',
				default: '../../static/bg.jpg'
			},
			qrSize:{	// 二维码大小
				Type: 'int',
				default: 100
			},
			qrUrl:{		// 生成二维码的链接
				Type: 'String',
				default: 'https://ext.dcloud.net.cn/plugin?id=5747'
			}
		},
		components:{
			QRCode
		},
		data() {
			return {
				qrCode:'',	// 二维码
			}
		},
		mounted(){
			_this = this;
		},
		methods: {
			// 创建二维码
			async canvasCreate(){
				// let res = await _this.$refs.qrcode.make({size: _this.qrSize,text: _this.qrUrl})
				// 	// .then(async res => {
				// 	// 	// 返回的res与uni.canvasToTempFilePath返回一致
				// 	// 	// console.log(res)
				// 	// 	_this.qrCode = res.tempFilePath;
				// 	// 	await _this.onCanvas();
				// 	// });
					
				// // 返回的res与uni.canvasToTempFilePath返回一致
				// // console.log(res)
				// _this.qrCode = res.tempFilePath;
				// await _this.onCanvas();
				
				// console.log('qrUrl', _this.qrUrl)
				// const img = await _this.getImage(_this.qrUrl)
				// // 给出下载到本地的图片的临时路径
				// _this.qrCode = img.path
				// console.log('qr code path', _this.qrCode)
				
				_this.qrCode = _this.qrUrl
				await _this.onCanvas();
			},
			// 画图
			async onCanvas() {
				uni.showLoading({
					title: "分享图片生成中..."
				});
				const ctx = uni.createCanvasContext(_this.canvasID, _this);
				// 设置 canvas 背景色
				ctx.setFillStyle('#FF4038');
				ctx.fillRect(0, 0, _this.canvasWidth, _this.canvasHeight);
				
				this.drawRoundRect(ctx, _this.canvasWidth - 20, _this.canvasHeight - 60, 5, 10, 50)
				
				// ctx.strokeStyle = "#fff";
				// ctx.stroke();
				ctx.fillStyle = '#fff';
				ctx.fill();
				// ctx.setFillStyle('#fff')
				ctx.restore();
				
				console.log('_this.shareImage', _this.shareImage)
				const res_shareImage = await uni.getImageInfo({src: _this.shareImage})
				console.log(res_shareImage)
				let imgPath = res_shareImage[1].path
				// 背景图片
				ctx.drawImage(imgPath, 20, 60,_this.canvasWidth - 40, _this.canvasHeight - 200);
				
				
				ctx.setFillStyle('#fff');
				ctx.setFontSize(18);
				ctx.setTextAlign('center');
				ctx.fillText(_this.shareTitle, _this.canvasWidth/2, 30);
				// 左边标题
				ctx.setFillStyle('#000000');
				ctx.setTextAlign('left')
				ctx.setFontSize(16)
				_this.writeTextOnCanvas(ctx,30,21,_this.goodsTitle,20, _this.canvasHeight - 100);
				// 秒杀价
				ctx.setFillStyle('#000000');
				ctx.setTextAlign('left')
				ctx.setFontSize(14)
				_this.writeTextOnCanvas(ctx,30,21,'秒杀价',20, _this.canvasHeight - 60);
				// 秒杀价价格
				const price = (_this.goodsPrice / 100).toString()
				_this.writeTextOnCanvas(ctx,30,21, '￥',70, _this.canvasHeight - 60);
				ctx.setFillStyle('#ff0000');
				ctx.setFontSize(32)
				_this.writeTextOnCanvas(ctx,60,21, price,85, _this.canvasHeight - 60);
				// 日常价
				ctx.setFillStyle('#000000');
				ctx.setTextAlign('left')
				ctx.setFontSize(14)
				_this.writeTextOnCanvas(ctx,30,21,'日常价',20, _this.canvasHeight - 20);
				// 日常价价格
				ctx.setFillStyle('#000000');
				ctx.setTextAlign('left')
				ctx.setFontSize(16)
				const oriPrice = '￥' + (_this.goodsOriginalPrice / 100).toString()
				_this.writeTextOnCanvas(ctx,30,21,oriPrice,70, _this.canvasHeight - 20);
				// 日常价价格删除线
				ctx.beginPath();
				const textWidth = ctx.measureText(oriPrice).width;
				ctx.rect(70, _this.canvasHeight - 27.5, textWidth, 1);
				ctx.fillStyle = '#000';
				ctx.fill();
				ctx.closePath()
				
				// 二维码
				console.log(_this.qrUrl)
				const res = await uni.getImageInfo({src: _this.qrUrl})
				console.log(res)
				let qrPath = res[1].path
				ctx.drawImage(qrPath,_this.canvasWidth - 120,_this.canvasHeight - 120,100,100);
				// ctx.draw();
				
				
				// 延迟后渲染至canvas上
				let pic = await _this.setTime(ctx)
				_this.$emit('success', pic);
				_this.qr_code = '';
			},
			drawRoundRect(ctx, width, height, radius, widthOffset, heightOffset) {
				ctx.beginPath(0);
				//从右下角顺时针绘制，弧度从0到1/2PI  
				ctx.arc(width - radius + widthOffset, height - radius + heightOffset, radius, 0, Math.PI / 2);
		
				//矩形下边线  
				ctx.lineTo(radius + widthOffset, height + heightOffset);
		
				//左下角圆弧，弧度从1/2PI到PI  
				ctx.arc(radius + widthOffset, height - radius + heightOffset, radius, Math.PI / 2, Math.PI);
		
				//矩形左边线  
				ctx.lineTo(0 + widthOffset, radius + heightOffset);
		
				//左上角圆弧，弧度从PI到3/2PI  
				ctx.arc(radius + widthOffset, radius + heightOffset, radius, Math.PI, Math.PI * 3 / 2);
		
				//上边线  
				ctx.lineTo(width - radius + widthOffset, 0 + heightOffset);
		
				//右上角圆弧  
				ctx.arc(width - radius + widthOffset, radius + heightOffset, radius, Math.PI * 3 / 2, Math.PI * 2);
		
				//右边线  
				ctx.lineTo(width + widthOffset, height - radius + heightOffset);
				ctx.closePath();
			},
			/**
			 * @param {Object} ctx_2d	getContext("2d") 对象
			 * @param {int} lineheight	段落文本行高
			 * @param {int} bytelength	设置单字节文字一行内的数量
			 * @param {string} text		写入画面的段落文本
			 * @param {int} startleft	开始绘制文本的 x 坐标位置（相对于画布）
			 * @param {int} starttop	开始绘制文本的 y 坐标位置（相对于画布）
			 */
			writeTextOnCanvas(ctx_2d, lineheight, bytelength, text ,startleft, starttop){
				// 获取字符串的真实长度（字节长度）
				function getTrueLength(str){
					var len = str.length, truelen = 0;
					for(var x = 0; x < len; x++){
						if(str.charCodeAt(x) > 128){
							truelen += 2;
						}else{
							truelen += 1;
						}
					}
					return truelen;
				}
				// 按字节长度截取字符串，返回substr截取位置
				function cutString(str, leng){
					var len = str.length, tlen = len, nlen = 0;
					for(var x = 0; x < len; x++){
						if(str.charCodeAt(x) > 128){
							if(nlen + 2 < leng){
								nlen += 2;
							}else{
								tlen = x;
								break;
							}
						}else{
							if(nlen + 1 < leng){
								nlen += 1;
							}else{
								tlen = x;
								break;
							}
						}
					}
					return tlen;
				}
				for(var i = 1; getTrueLength(text) > 0; i++){
					var tl = cutString(text, bytelength);
					ctx_2d.fillText(text.substr(0, tl).replace(/^\s+|\s+$/, ""), startleft, (i-1) * lineheight + starttop);
					text = text.substr(tl);
				}
			},
			// 彻底改成同步 防止拿到的图片地址为空
			setTime(ctx) {
				return new Promise((resole, err) => {
					setTimeout(() => {
						ctx.draw(false, async () => {
							let pic = await _this.getNewPic();
							resole(pic)
						});
					}, 600)
				})
			},
			// 获取新的图片地址
			getNewPic() {
				return new Promise((resolve, errs) => {
					setTimeout(()=>{
						uni.canvasToTempFilePath({
							canvasId: _this.canvasID,
							quality: 1,
							complete: (res) => {
								// 在H5平台下，tempFilePath 为 base64
								// 关闭showLoading
								uni.hideLoading();
								//  储存海报地址  也是分享的地址
								resolve(res.tempFilePath)
							}
						}, _this);
					},200)
				})
			},
		},
		mounted() {
			_this = this;
		}
	}
</script>