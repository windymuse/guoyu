(global["webpackJsonp"]=global["webpackJsonp"]||[]).push([["pages/product/search"],{"01c0":function(e,t,o){"use strict";var n;o.d(t,"b",(function(){return r})),o.d(t,"c",(function(){return i})),o.d(t,"a",(function(){return n}));var r=function(){var e=this,t=e.$createElement;e._self._c},i=[]},"0908":function(e,t,o){"use strict";(function(e){o("f494");n(o("66fd"));var t=n(o("e634"));function n(e){return e&&e.__esModule?e:{default:e}}e(t.default)}).call(this,o("543d")["createPage"])},4380:function(e,t,o){"use strict";var n=o("d28c"),r=o.n(n);r.a},8973:function(e,t,o){"use strict";o.r(t);var n=o("93b4"),r=o.n(n);for(var i in n)"default"!==i&&function(e){o.d(t,e,(function(){return n[e]}))}(i);t["default"]=r.a},"93b4":function(e,t,o){"use strict";(function(e){Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var n=function(){o.e("components/mehaotian-search-revision").then(function(){return resolve(o("aa59"))}.bind(null,o)).catch(o.oe)},r={data:function(){return{defaultKeyword:"",keyword:"",oldKeywordList:[],hotKeywordList:[],keywordList:[],forbid:"",isShowKeywordList:!1}},onLoad:function(){this.init()},components:{mSearch:n},onShareAppMessage:function(){return{title:"国渔鲜生小程序",desc:"全球鲜生供应商",path:"/pages/index/index"}},onShareTimeline:function(){return{}},methods:{init:function(){this.loadDefaultKeyword(),this.loadOldKeyword()},blur:function(){e.hideKeyboard()},loadDefaultKeyword:function(){this.defaultKeyword="默认关键字"},loadOldKeyword:function(){var t=this;e.getStorage({key:"OldKeys",success:function(e){var o=JSON.parse(e.data);t.oldKeywordList=o}})},inputChange:function(e){e.detail&&e.detail.value},drawCorrelativeKeyword:function(e,t){for(var o=e.length,n=[],r=0;r<o;r++){var i=e[r],a=i[0].replace(t,"<span style='color: #9f9f9f;'>"+t+"</span>");a="<div>"+a+"</div>";var d={keyword:i[0],htmlStr:a};n.push(d)}return n},setkeyword:function(e){this.keyword=e.keyword},oldDelete:function(){var t=this;e.showModal({content:"确定清除历史搜索记录？",success:function(o){o.confirm?(console.log("用户点击确定"),t.oldKeywordList=[],e.removeStorage({key:"OldKeys"})):o.cancel&&console.log("用户点击取消")}})},hotToggle:function(){this.forbid=this.forbid?"":"_forbid"},doSearch:function(t){t=t||(this.keyword?this.keyword:this.defaultKeyword),this.keyword=t,this.saveKeyword(t),e.navigateTo({url:"/pages/product/list?keywords=".concat(t)})},saveKeyword:function(t){var o=this;e.getStorage({key:"OldKeys",success:function(n){console.log(n.data);var r=JSON.parse(n.data),i=r.indexOf(t);-1==i||r.splice(i,1),r.unshift(t),r.length>10&&r.pop(),e.setStorage({key:"OldKeys",data:JSON.stringify(r)}),o.oldKeywordList=r},fail:function(n){var r=[t];e.setStorage({key:"OldKeys",data:JSON.stringify(r)}),o.oldKeywordList=r}})}}};t.default=r}).call(this,o("543d")["default"])},d28c:function(e,t,o){},e634:function(e,t,o){"use strict";o.r(t);var n=o("01c0"),r=o("8973");for(var i in r)"default"!==i&&function(e){o.d(t,e,(function(){return r[e]}))}(i);o("4380");var a,d=o("f0c5"),s=Object(d["a"])(r["default"],n["b"],n["c"],!1,null,null,null,!1,n["a"],a);t["default"]=s.exports}},[["0908","common/runtime","common/vendor"]]]);