(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-2489"],{LROu:function(e,t,n){"use strict";var i=n("Qvsb");n.n(i).a},Mz3J:function(e,t,n){"use strict";Math.easeInOutQuad=function(e,t,n,i){return(e/=i/2)<1?n/2*e*e+t:-n/2*(--e*(e-2)-1)+t};var i=window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(e){window.setTimeout(e,1e3/60)};function o(e,t,n){var o=document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop,a=e-o,r=0;t=void 0===t?500:t;!function e(){r+=20,function(e){document.documentElement.scrollTop=e,document.body.parentNode.scrollTop=e,document.body.scrollTop=e}(Math.easeInOutQuad(r,o,a,t)),r<t?i(e):n&&"function"==typeof n&&n()}()}var a={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[10,20,30,50]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(e){this.$emit("update:page",e)}},pageSize:{get:function(){return this.limit},set:function(e){this.$emit("update:limit",e)}}},methods:{handleSizeChange:function(e){this.$emit("pagination",{page:this.currentPage,limit:e}),this.autoScroll&&o(0,800)},handleCurrentChange:function(e){this.$emit("pagination",{page:e,limit:this.pageSize}),this.autoScroll&&o(0,800)}}},r=(n("LROu"),n("KHd+")),s=Object(r.a)(a,function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"pagination-container",class:{hidden:e.hidden}},[n("el-pagination",e._b({attrs:{background:e.background,"current-page":e.currentPage,"page-size":e.pageSize,layout:e.layout,total:e.total},on:{"update:currentPage":function(t){e.currentPage=t},"update:pageSize":function(t){e.pageSize=t},"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}},"el-pagination",e.$attrs,!1))],1)},[],!1,null,"2fc659d3",null);s.options.__file="index.vue";t.a=s.exports},Qvsb:function(e,t,n){},"XdW+":function(e,t,n){"use strict";n.r(t);var i=n("QbLZ"),o=n.n(i),a=n("t3Un"),r=n("Qyje"),s=n.n(r);var l=n("xA6U"),u=n("rs3x"),d=n("X4fA"),c=n("7Qib"),m=n("Mz3J"),p=[{value:1,unionType:3,name:"橱窗推荐"},{value:"",unionType:3,name:"全部"}],f={name:"Recommend",components:{ElOption:n("KFLd").a,Pagination:m.a},filters:{recommendTypeFilter:function(e){return p[e-1].name}},data:function(){return{uploadPath:u.b,linkunio:void 0,options:[],value:[],list:[],total:0,listLoading:!0,recommendTypeMap:p,listQuery:{pageNo:1,limit:20,recommendType:void 0},dataForm:{id:void 0,recommendType:void 0,spuId:void 0},dialogFormVisible:!1,dialogStatus:"",textMap:{update:"编辑",create:"创建"},rules:{recommendType:[{required:!0,message:"推荐类型",trigger:"blur"}],spuId:[{required:!0,message:"推荐商品",trigger:"blur"}]},downloadLoading:!1}},computed:{headers:function(){return{accessToken:Object(d.a)()}}},created:function(){this.getList(),this.refreshOptions()},methods:{getList:function(){var e=this;this.listLoading=!0,function(e){return Object(a.a)({method:"get",params:o()({_gp:"admin.recommend",_mt:"queryRecommendByType"},e)})}(this.listQuery).then(function(t){e.list=t.data.data.items,e.total=t.data.data.total,e.listLoading=!1}).catch(function(){e.list=[],e.total=0,e.listLoading=!1})},handleLink:function(e){if(void 0!==e){var t=e[e.length-1];this.dataForm.spuId=t.substring(2)}},queryRecommendBtn:function(){this.listQuery.page=1,this.getList()},resetForm:function(){this.dataForm={id:void 0,recommendType:void 0,spuId:void 0}},refreshOptions:function(){var e=this;0===this.options.length&&Object(l.h)().then(function(t){e.options=Object(c.a)(t.data.data)})},createDialogBtn:function(){var e=this;this.resetForm(),this.refreshOptions(),this.dialogStatus="create",this.linkunio=void 0,this.dialogFormVisible=!0,this.$nextTick(function(){e.$refs.dataForm.clearValidate()})},createRecommendBtn:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.checkGoods()&&function(e){return Object(a.a)({method:"post",data:s.a.stringify(o()({_gp:"admin.recommend",_mt:"addRecommend"},e))})}(e.dataForm).then(function(t){e.getList(),e.dialogFormVisible=!1,e.$notify.success({title:"成功",message:"创建成功"})}).catch(function(t){e.$notify.error({title:"失败",message:t.data.errmsg})})})},checkGoods:function(){return!(void 0===this.linkunio||null===this.linkunio||this.linkunio.length<=3)||(this.$notify.error({title:"提示",message:"请选择商品"}),!1)},deleteRecommendBtn:function(e){var t=this;this.$confirm("此操作将永久删除该商品推荐--"+e.spuTitle+"--, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){(function(e,t){return Object(a.a)({method:"post",data:s.a.stringify({_gp:"admin.recommend",_mt:"deleteRecommend",id:e,recommendType:t})})})(e.id,e.recommendType).then(function(n){t.$notify.success({title:"成功",message:"删除成功"});var i=t.list.indexOf(e);t.list.splice(i,1)}).catch(function(e){t.$notify.error({title:"失败",message:e.data.errmsg})})}).catch(function(){return!1})}}},g=(n("iJ/F"),n("KHd+")),h=Object(g.a)(f,function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-select",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{placeholder:"请选择推荐类型"},model:{value:e.listQuery.recommendType,callback:function(t){e.$set(e.listQuery,"recommendType",t)},expression:"listQuery.recommendType"}},e._l(e.recommendTypeMap,function(e,t){return n("el-option",{key:t,attrs:{label:e.name,value:e.value}})})),e._v(" "),n("el-button",{directives:[{name:"permission",rawName:"v-permission",value:["promote:recommend:query"],expression:"['promote:recommend:query']"}],staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search"},on:{click:e.queryRecommendBtn}},[e._v("查找")]),e._v(" "),n("el-button",{directives:[{name:"permission",rawName:"v-permission",value:["promote:recommend:create"],expression:"['promote:recommend:create']"}],staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-edit"},on:{click:e.createDialogBtn}},[e._v("添加")])],1),e._v(" "),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],attrs:{data:e.list,size:"small","element-loading-text":"正在查询中。。。",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{align:"center",label:"推荐ID",prop:"id",sortable:""}}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"推荐商品",prop:"spuTitle"}}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"推荐类型",prop:"recommendType"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-tag",[e._v(e._s(e._f("recommendTypeFilter")(t.row.recommendType)))])]}}])}),e._v(" "),n("el-table-column",{attrs:{align:"center",label:"操作",width:"200","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{directives:[{name:"permission",rawName:"v-permission",value:["promote:advertisement:delete"],expression:"['promote:advertisement:delete']"}],attrs:{type:"danger",size:"mini"},on:{click:function(n){e.deleteRecommendBtn(t.row)}}},[e._v("删除")])]}}])})],1),e._v(" "),n("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.listQuery.pageNo,limit:e.listQuery.limit},on:{"update:page":function(t){e.$set(e.listQuery,"pageNo",t)},"update:limit":function(t){e.$set(e.listQuery,"limit",t)},pagination:e.getList}}),e._v(" "),n("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],visible:e.dialogFormVisible},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[n("el-form",{ref:"dataForm",staticStyle:{width:"400px","margin-left":"50px"},attrs:{rules:e.rules,model:e.dataForm,"status-icon":"","label-position":"left","label-width":"100px"}},[n("el-form-item",{attrs:{label:"隐藏的推荐id",prop:"id",hidden:""}},[n("el-input",{model:{value:e.dataForm.id,callback:function(t){e.$set(e.dataForm,"id",t)},expression:"dataForm.id"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"推荐类型",prop:"recommendType"}},[n("el-select",{attrs:{placeholder:"请选择"},model:{value:e.dataForm.recommendType,callback:function(t){e.$set(e.dataForm,"recommendType",t)},expression:"dataForm.recommendType"}},e._l(e.recommendTypeMap,function(e,t){return n("el-option",{key:t,attrs:{label:e.name,value:e.value}})}))],1),e._v(" "),n("el-form-item",{attrs:{label:"推荐商品",prop:"spuId"}},[n("el-cascader",{attrs:{options:e.options,placeholder:"关联商品",filterable:""},on:{change:e.handleLink},model:{value:e.linkunio,callback:function(t){e.linkunio=t},expression:"linkunio"}})],1)],1),e._v(" "),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取消")]),e._v(" "),"create"==e.dialogStatus?n("el-button",{attrs:{type:"primary"},on:{click:e.createRecommendBtn}},[e._v("确定")]):e._e()],1)],1)],1)},[],!1,null,null,null);h.options.__file="recommend.vue";t.default=h.exports},"gDS+":function(e,t,n){e.exports={default:n("oh+g"),__esModule:!0}},"iJ/F":function(e,t,n){"use strict";var i=n("vYs1");n.n(i).a},"oh+g":function(e,t,n){var i=n("WEpk"),o=i.JSON||(i.JSON={stringify:JSON.stringify});e.exports=function(e){return o.stringify.apply(o,arguments)}},rs3x:function(e,t,n){"use strict";n.d(t,"b",function(){return o}),n.d(t,"a",function(){return a});var i=n("t3Un"),o="https://guoyu.windymuse.cn/upload/admin";function a(e){return Object(i.a)({url:o,method:"post",data:e})}},vYs1:function(e,t,n){},xA6U:function(e,t,n){"use strict";n.d(t,"h",function(){return d}),n.d(t,"g",function(){return c}),n.d(t,"e",function(){return m}),n.d(t,"c",function(){return p}),n.d(t,"a",function(){return f}),n.d(t,"b",function(){return g}),n.d(t,"d",function(){return h}),n.d(t,"f",function(){return v});var i=n("gDS+"),o=n.n(i),a=n("QbLZ"),r=n.n(a),s=n("t3Un"),l=n("Qyje"),u=n.n(l);function d(){return Object(s.a)({method:"get",params:{_gp:"admin.goods",_mt:"getSpuBigTree"}})}function c(e){return Object(s.a)({method:"get",params:r()({_gp:"admin.goods",_mt:"list"},e)})}function m(e){return Object(s.a)({method:"post",data:u.a.stringify({_gp:"admin.goods",_mt:"edit",spuDTO:o()(e)})})}function p(e){return Object(s.a)({method:"post",params:{_gp:"admin.goods",_mt:"delete",spuId:e}})}function f(e){return Object(s.a)({method:"post",data:u.a.stringify({_gp:"admin.goods",_mt:"batchDelete",ids:o()(e)})})}function g(e){return Object(s.a)({method:"post",data:u.a.stringify({_gp:"admin.goods",_mt:"create",spuDTO:o()(e)})})}function h(e){return Object(s.a)({method:"get",params:{_gp:"admin.goods",_mt:"detail",spuId:e}})}function v(e,t){return Object(s.a)({method:"post",params:{_gp:"admin.goods",_mt:"freezeOrActivation",spuId:e,status:t}})}}}]);