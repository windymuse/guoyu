(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/product/appraise"],{"04cb":function(e,n,t){"use strict";(function(e){t("f494");a(t("66fd"));var n=a(t("7cc1"));function a(e){return e&&e.__esModule?e:{default:e}}wx.__webpack_require_UNI_MP_PLUGIN__=t,e(n.default)}).call(this,t("543d")["createPage"])},"1b71":function(e,n,t){"use strict";t.d(n,"b",(function(){return o})),t.d(n,"c",(function(){return u})),t.d(n,"a",(function(){return a}));var a={uniLoadMore:function(){return t.e("components/uni-load-more/uni-load-more").then(t.bind(null,"a58e"))}},o=function(){var e=this,n=e.$createElement;e._self._c},u=[]},"3c27":function(e,n,t){},"4c65":function(e,n,t){"use strict";var a=t("3c27"),o=t.n(a);o.a},"6ffa":function(e,n,t){"use strict";t.r(n);var a=t("9b1c"),o=t.n(a);for(var u in a)"default"!==u&&function(e){t.d(n,e,(function(){return a[e]}))}(u);n["default"]=o.a},"7cc1":function(e,n,t){"use strict";t.r(n);var a=t("1b71"),o=t("6ffa");for(var u in o)"default"!==u&&function(e){t.d(n,e,(function(){return o[e]}))}(u);t("4c65");var i,r=t("f0c5"),c=Object(r["a"])(o["default"],a["b"],a["c"],!1,null,null,null,!1,a["a"],i);n["default"]=c.exports},"9b1c":function(e,n,t){"use strict";(function(e){Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var a=function(){t.e("components/uni-load-more/uni-load-more").then(function(){return resolve(t("a58e"))}.bind(null,t)).catch(t.oe)},o={components:{uniLoadMore:a},data:function(){return{page:{},spuId:void 0,loadingType:"more"}},onLoad:function(e){this.page=JSON.parse(e.firstpage),this.spuId=e.spuid,this.loadingType=this.page.pageNo<this.page.totalPageNo?"more":"nomore"},onReachBottom:function(e){var n=this;"more"===n.loadingType&&(n.loadingType="loading",n.$api.request("appraise","getSpuAllAppraise",{spuId:n.spuId,pageNo:n.page.pageNo+1,pageSize:10}).then((function(e){n.page.items=n.page.items.concat(e.data.items),n.page.pageNo=e.data.pageNo,n.loadingType=e.data.pageNo<e.data.totalPageNo?"more":"nomore"})))},onShareAppMessage:function(){return{title:"国渔鲜生小程序",desc:"全球鲜生供应商",path:"/pages/index/index"}},onShareTimeline:function(){return{}},methods:{previewImg:function(n,t){e.previewImage({current:t,urls:n})}}};n.default=o}).call(this,t("543d")["default"])}},[["04cb","common/runtime","common/vendor"]]]);