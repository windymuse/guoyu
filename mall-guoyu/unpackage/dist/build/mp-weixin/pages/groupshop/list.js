(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/groupshop/list"],{"0e0b":function(e,n,o){"use strict";o.r(n);var t=o("82b3"),a=o("a8f4");for(var r in a)"default"!==r&&function(e){o.d(n,e,(function(){return a[e]}))}(r);o("ad38");var u,i=o("f0c5"),c=Object(i["a"])(a["default"],t["b"],t["c"],!1,null,null,null,!1,t["a"],u);n["default"]=c.exports},"51ef":function(e,n,o){"use strict";(function(e){o("f494");t(o("66fd"));var n=t(o("0e0b"));function t(e){return e&&e.__esModule?e:{default:e}}wx.__webpack_require_UNI_MP_PLUGIN__=o,e(n.default)}).call(this,o("543d")["createPage"])},"82b3":function(e,n,o){"use strict";var t;o.d(n,"b",(function(){return a})),o.d(n,"c",(function(){return r})),o.d(n,"a",(function(){return t}));var a=function(){var e=this,n=e.$createElement;e._self._c},r=[]},a4ef:function(e,n,o){"use strict";(function(e){Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var t=function(){o.e("components/uni-load-more/uni-load-more").then(function(){return resolve(o("a58e"))}.bind(null,o)).catch(o.oe)},a=function(){o.e("components/empty").then(function(){return resolve(o("4551"))}.bind(null,o)).catch(o.oe)},r={components:{uniLoadMore:t,empty:a},data:function(){return{groupShopList:[],pageNo:1,loadingType:"more"}},onLoad:function(e){this.loadData()},onPullDownRefresh:function(){this.loadData("refresh")},onReachBottom:function(){this.loadData()},onShareAppMessage:function(){return{title:"国渔鲜生小程序",desc:"全球鲜生供应商",path:"/pages/index/index"}},onShareTimeline:function(){return{}},methods:{loadData:function(n){var o=this;"refresh"===n&&(o.pageNo=1,o.groupShopList=[],o.loadingType="more"),"more"==o.loadingType&&(o.loadingType="loading",o.$api.request("groupshop","getGroupShopPage",{pageNo:o.pageNo}).then((function(t){o.pageNo=t.data.pageNo+1,o.loadingType=t.data.pageNo<t.data.totalPageNo?"more":"nomore",t.data.items.forEach((function(e){o.groupShopList.push(e)})),"refresh"===n&&e.stopPullDownRefresh()})))},onImageLoad:function(e,n){this.$set(this[e][n],"loaded","loaded")},onImageError:function(e,n){this[e][n].image="/static/errorImage.jpg"}}};n.default=r}).call(this,o("543d")["default"])},a8f4:function(e,n,o){"use strict";o.r(n);var t=o("a4ef"),a=o.n(t);for(var r in t)"default"!==r&&function(e){o.d(n,e,(function(){return t[e]}))}(r);n["default"]=a.a},aafd:function(e,n,o){},ad38:function(e,n,o){"use strict";var t=o("aafd"),a=o.n(t);a.a}},[["51ef","common/runtime","common/vendor"]]]);