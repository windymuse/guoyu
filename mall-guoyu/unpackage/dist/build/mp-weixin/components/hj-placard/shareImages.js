(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["components/hj-placard/shareImages"],{"1c45":function(e,t,n){"use strict";(function(e){Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var a=r(n("a34a"));function r(e){return e&&e.__esModule?e:{default:e}}function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function s(e,t,n,a,r,i,s){try{var c=e[i](s),o=c.value}catch(l){return void n(l)}c.done?t(o):Promise.resolve(o).then(a,r)}function c(e){return function(){var t=this,n=arguments;return new Promise((function(a,r){var i=e.apply(t,n);function c(e){s(i,a,r,c,o,"next",e)}function o(e){s(i,a,r,c,o,"throw",e)}c(void 0)}))}}var o,l=function(){Promise.all([n.e("common/vendor"),n.e("components/qr_code/qrcode")]).then(function(){return resolve(n("9645"))}.bind(null,n)).catch(n.oe)},u=i({name:"canvas-images",props:{canvasID:{Type:String,default:"shareCanvas"},canvasWidth:{Type:"int",default:375},canvasHeight:{Type:"int",default:500},shareTitle:{Type:"String",default:"我是这张图片的标题"},goodsTitle:{Type:"String",default:"我是这张图片的标题我是这张图片的标题我是这张图片的标"},goodsPrice:{Type:"Number",default:0},goodsOriginalPrice:{Type:"Number",default:0},shareImage:{Type:"String",default:"../../static/bg.jpg"},qrSize:{Type:"int",default:100},qrUrl:{Type:"String",default:"https://ext.dcloud.net.cn/plugin?id=5747"}},components:{QRCode:l},data:function(){return{qrCode:""}},mounted:function(){o=this},methods:{canvasCreate:function(){return c(a.default.mark((function e(){return a.default.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return o.qrCode=o.qrUrl,e.next=3,o.onCanvas();case 3:case"end":return e.stop()}}),e)})))()},onCanvas:function(){var t=this;return c(a.default.mark((function n(){var r,i,s,c,l,u,f,d,h;return a.default.wrap((function(n){while(1)switch(n.prev=n.next){case 0:return e.showLoading({title:"分享图片生成中..."}),r=e.createCanvasContext(o.canvasID,o),r.setFillStyle("#FF4038"),r.fillRect(0,0,o.canvasWidth,o.canvasHeight),t.drawRoundRect(r,o.canvasWidth-20,o.canvasHeight-60,5,10,50),r.fillStyle="#fff",r.fill(),r.restore(),console.log("_this.shareImage",o.shareImage),n.next=11,e.getImageInfo({src:o.shareImage});case 11:return i=n.sent,console.log(i),s=i[1].path,r.drawImage(s,20,60,o.canvasWidth-40,o.canvasHeight-200),r.setFillStyle("#fff"),r.setFontSize(18),r.setTextAlign("center"),r.fillText(o.shareTitle,o.canvasWidth/2,30),r.setFillStyle("#000000"),r.setTextAlign("left"),r.setFontSize(16),o.writeTextOnCanvas(r,30,21,o.goodsTitle,20,o.canvasHeight-100),r.setFillStyle("#000000"),r.setTextAlign("left"),r.setFontSize(14),o.writeTextOnCanvas(r,30,21,"秒杀价",20,o.canvasHeight-60),c=(o.goodsPrice/100).toString(),o.writeTextOnCanvas(r,30,21,"￥",70,o.canvasHeight-60),r.setFillStyle("#ff0000"),r.setFontSize(32),o.writeTextOnCanvas(r,60,21,c,85,o.canvasHeight-60),r.setFillStyle("#000000"),r.setTextAlign("left"),r.setFontSize(14),o.writeTextOnCanvas(r,30,21,"日常价",20,o.canvasHeight-20),r.setFillStyle("#000000"),r.setTextAlign("left"),r.setFontSize(16),l="￥"+(o.goodsOriginalPrice/100).toString(),o.writeTextOnCanvas(r,30,21,l,70,o.canvasHeight-20),r.beginPath(),u=r.measureText(l).width,r.rect(70,o.canvasHeight-27.5,u,1),r.fillStyle="#000",r.fill(),r.closePath(),console.log(o.qrUrl),n.next=50,e.getImageInfo({src:o.qrUrl});case 50:return f=n.sent,console.log(f),d=f[1].path,r.drawImage(d,o.canvasWidth-120,o.canvasHeight-120,100,100),n.next=56,o.setTime(r);case 56:h=n.sent,o.$emit("success",h),o.qr_code="";case 59:case"end":return n.stop()}}),n)})))()},drawRoundRect:function(e,t,n,a,r,i){e.beginPath(0),e.arc(t-a+r,n-a+i,a,0,Math.PI/2),e.lineTo(a+r,n+i),e.arc(a+r,n-a+i,a,Math.PI/2,Math.PI),e.lineTo(0+r,a+i),e.arc(a+r,a+i,a,Math.PI,3*Math.PI/2),e.lineTo(t-a+r,0+i),e.arc(t-a+r,a+i,a,3*Math.PI/2,2*Math.PI),e.lineTo(t+r,n-a+i),e.closePath()},writeTextOnCanvas:function(e,t,n,a,r,i){function s(e){for(var t=e.length,n=0,a=0;a<t;a++)e.charCodeAt(a)>128?n+=2:n+=1;return n}function c(e,t){for(var n=e.length,a=n,r=0,i=0;i<n;i++)if(e.charCodeAt(i)>128){if(!(r+2<t)){a=i;break}r+=2}else{if(!(r+1<t)){a=i;break}r+=1}return a}for(var o=1;s(a)>0;o++){var l=c(a,n);e.fillText(a.substr(0,l).replace(/^\s+|\s+$/,""),r,(o-1)*t+i),a=a.substr(l)}},setTime:function(e){return new Promise((function(t,n){setTimeout((function(){e.draw(!1,c(a.default.mark((function e(){var n;return a.default.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,o.getNewPic();case 2:n=e.sent,t(n);case 4:case"end":return e.stop()}}),e)}))))}),600)}))},getNewPic:function(){return new Promise((function(t,n){setTimeout((function(){e.canvasToTempFilePath({canvasId:o.canvasID,quality:1,complete:function(n){e.hideLoading(),t(n.tempFilePath)}},o)}),200)}))}}},"mounted",(function(){o=this}));t.default=u}).call(this,n("543d")["default"])},a03a:function(e,t,n){"use strict";n.r(t);var a=n("fb36"),r=n("cedb");for(var i in r)"default"!==i&&function(e){n.d(t,e,(function(){return r[e]}))}(i);var s,c=n("f0c5"),o=Object(c["a"])(r["default"],a["b"],a["c"],!1,null,null,null,!1,a["a"],s);t["default"]=o.exports},cedb:function(e,t,n){"use strict";n.r(t);var a=n("1c45"),r=n.n(a);for(var i in a)"default"!==i&&function(e){n.d(t,e,(function(){return a[e]}))}(i);t["default"]=r.a},fb36:function(e,t,n){"use strict";var a;n.d(t,"b",(function(){return r})),n.d(t,"c",(function(){return i})),n.d(t,"a",(function(){return a}));var r=function(){var e=this,t=e.$createElement;e._self._c},i=[]}}]);
;(global["webpackJsonp"] = global["webpackJsonp"] || []).push([
    'components/hj-placard/shareImages-create-component',
    {
        'components/hj-placard/shareImages-create-component':(function(module, exports, __webpack_require__){
            __webpack_require__('543d')['createComponent'](__webpack_require__("a03a"))
        })
    },
    [['components/hj-placard/shareImages-create-component']]
]);
