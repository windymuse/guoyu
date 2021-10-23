(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-62b7"],{"CC6/":function(t,e,n){},GeIi:function(t,e,n){"use strict";n.r(e);var o=n("P2sY"),i=n.n(o),r=n("rs3x"),a=n("mRed"),s=n("X4fA"),l=n("QbLZ"),d=n.n(l),u=n("t3Un"),c=n("Qyje"),p=n.n(c);var m={name:"GoodsEdit",components:{Editor:a.a},data:function(){var t=function(t,e,n){/^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,15})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,15}|180)$/.test(e)||n(new Error("经度整数部分为0-180,小数部分为0到15位!")),n()},e=function(t,e,n){/^(\-|\+)?([0-8]?\d{1}\.\d{0,15}|90\.0{0,15}|[0-8]?\d{1}|90)$/.test(e)||n(new Error("纬度整数部分为0-90,小数部分为0到15位!")),n()};return{uploadPath:r.b,list:[],dataForm:{title:void 0,logoUrl:void 0,description:void 0,address:void 0,longitude:void 0,latitude:void 0,showType:1},rules:{address:[{required:!0,message:"商铺地址不能为空",trigger:"blur"}],title:[{required:!0,message:"商铺标题不能为空",trigger:"blur"}],description:[{required:!0,message:"商铺描述不能为空",trigger:"blur"}],longitude:[{required:!0,validator:t,trigger:"blur"},{validator:t,trigger:"change"}],latitude:[{required:!0,validator:e,trigger:"blur"},{validator:e,trigger:"change"}]},listLoading:!1}},computed:{headers:function(){return{accessToken:Object(s.a)()}}},created:function(){this.getList()},methods:{getList:function(){var t=this;this.listLoading=!0,function(t){return Object(u.a)({method:"get",params:d()({_gp:"admin.merchant",_mt:"getMerchant"},t)})}().then(function(e){t.list=e.data.data,t.dataForm=i()({},t.list),t.listLoading=!1}).catch(function(){t.list=[],t.listLoading=!1})},handleEdit:function(){var t=this;this.$refs.dataForm.validate(function(e){e?function(t){return Object(u.a)({method:"post",data:p.a.stringify(d()({_gp:"admin.merchant",_mt:"updateMerchant"},t))})}(t.dataForm).then(function(e){t.$notify.success({title:"成功",message:"更改成功"})}).catch(function(e){t.$notify.error({title:"失败",message:"更改失败"})}):t.$notify.error({title:"失败",message:"请完善表单"})})},uploadSuccessHandle:function(t){this.list.logoUrl=t.url,this.dataForm.logoUrl=t.url,this.dialogFormVisible=!1,this.dialogFormVisible=!0},onBeforeUpload:function(t){var e="image/jpeg"===t.type||"image/gif",n=t.size/1024/1024<1;return e||this.$message.error("上传文件只能是图片格式!"),n||this.$message.error("上传文件大小不能超过 1MB!"),e&&n}}},f=(n("V+z7"),n("KHd+")),g=Object(f.a)(m,function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"app-container"},[n("el-card",{staticClass:"box-card"},[n("h3",[t._v("商铺信息")]),t._v(" "),n("el-form",{ref:"dataForm",attrs:{rules:t.rules,model:t.dataForm,"label-width":"150px"}},[n("el-form-item",{attrs:{label:"标题",prop:"title"}},[n("el-input",{model:{value:t.dataForm.title,callback:function(e){t.$set(t.dataForm,"title",e)},expression:"dataForm.title"}})],1),t._v(" "),n("el-form-item",{attrs:{label:"商铺logo",prop:"logoUrl"}},[n("el-upload",{staticClass:"avatar-uploader",attrs:{action:t.uploadPath,"show-file-list":!1,"on-success":t.uploadSuccessHandle,"before-upload":t.onBeforeUpload,accept:".jpg, .jpeg, .png, .gif"}},[t.list.logoUrl?n("img",{staticClass:"avatar",attrs:{src:t.dataForm.logoUrl}}):n("i",{staticClass:"el-icon-plus avatar-uploader-icon"})])],1),t._v(" "),n("el-form-item",{attrs:{label:"描述",prop:"description"}},[n("el-input",{model:{value:t.dataForm.description,callback:function(e){t.$set(t.dataForm,"description",e)},expression:"dataForm.description"}})],1),t._v(" "),n("el-form-item",{attrs:{label:"地址",prop:"address"}},[n("el-input",{model:{value:t.dataForm.address,callback:function(e){t.$set(t.dataForm,"address",e)},expression:"dataForm.address"}})],1),t._v(" "),n("el-form-item",{attrs:{label:"经度",prop:"longitude"}},[n("el-input",{model:{value:t.dataForm.longitude,callback:function(e){t.$set(t.dataForm,"longitude",e)},expression:"dataForm.longitude"}})],1),t._v(" "),n("el-form-item",{attrs:{label:"维度",prop:"latitude"}},[n("el-input",{model:{value:t.dataForm.latitude,callback:function(e){t.$set(t.dataForm,"latitude",e)},expression:"dataForm.latitude"}})],1)],1)],1),t._v(" "),n("div",{staticClass:"op-container"},[n("el-button",{directives:[{name:"permission",rawName:"v-permission",value:["promote:merchant:create"],expression:"['promote:merchant:create']"}],attrs:{type:"primary"},on:{click:t.handleEdit}},[t._v("保存更改")])],1)],1)},[],!1,null,null,null);g.options.__file="merchant.vue";e.default=g.exports},HziX:function(t,e,n){"use strict";(function(t){n.d(e,"a",function(){return i});var o=function(){return"undefined"!=typeof window?window:t},i=function(){var t=o();return t&&t.tinymce?t.tinymce:null}}).call(this,n("yLpj"))},"V+z7":function(t,e,n){"use strict";var o=n("CC6/");n.n(o).a},mRed:function(t,e,n){"use strict";var o=["onActivate","onAddUndo","onBeforeAddUndo","onBeforeExecCommand","onBeforeGetContent","onBeforeRenderUI","onBeforeSetContent","onBeforePaste","onBlur","onChange","onClearUndos","onClick","onContextMenu","onCopy","onCut","onDblclick","onDeactivate","onDirty","onDrag","onDragDrop","onDragEnd","onDragGesture","onDragOver","onDrop","onExecCommand","onFocus","onFocusIn","onFocusOut","onGetContent","onHide","onInit","onKeyDown","onKeyPress","onKeyUp","onLoadContent","onMouseDown","onMouseEnter","onMouseLeave","onMouseMove","onMouseOut","onMouseOver","onMouseUp","onNodeChange","onObjectResizeStart","onObjectResized","onObjectSelected","onPaste","onPostProcess","onPostRender","onPreProcess","onProgressState","onRedo","onRemove","onReset","onSaveContent","onSelectionChange","onSetAttrib","onSetContent","onShow","onSubmit","onUndo","onVisualAid"],i=function(t){return-1!==o.indexOf(t)},r=function(t,e,n){var o=e.$props.value?e.$props.value:"",r=e.$props.initialValue?e.$props.initialValue:"";n.setContent(o||r),e.$listeners.input&&function(t,e){var n,o=t.$props.modelEvents?t.$props.modelEvents:null,i=Array.isArray(o)?o.join(" "):o;t.$watch("value",function(t,o){e&&"string"==typeof t&&t!==n&&t!==o&&(e.setContent(t),n=t)}),e.on(i||"change keyup undo redo",function(){n=e.getContent(),t.$emit("input",n)})}(e,n),function(t,e,n){Object.keys(e).filter(i).forEach(function(o){var i=e[o];"function"==typeof i&&("onInit"===o?i(t,n):n.on(o.substring(2),function(t){return i(t,n)}))})}(t,e.$listeners,n)},a=0,s=function(t){var e=(new Date).getTime();return t+"_"+Math.floor(1e9*Math.random())+ ++a+String(e)},l=function(t){return void 0===t||""===t?[]:Array.isArray(t)?t:t.split(" ")},d=n("HziX"),u={apiKey:String,cloudChannel:String,id:String,init:Object,initialValue:String,inline:Boolean,modelEvents:[String,Array],plugins:[String,Array],tagName:String,toolbar:[String,Array],value:String,disabled:Boolean},c=Object.assign||function(t){for(var e,n=1,o=arguments.length;n<o;n++)for(var i in e=arguments[n])Object.prototype.hasOwnProperty.call(e,i)&&(t[i]=e[i]);return t},p={listeners:[],scriptId:s("tiny-script"),scriptLoaded:!1},m=function(t){return function(){var e=c({},t.$props.init,{readonly:t.$props.disabled,selector:"#"+t.elementId,plugins:function(t,e){return l(t).concat(l(e))}(t.$props.init&&t.$props.init.plugins,t.$props.plugins),toolbar:t.$props.toolbar||t.$props.init&&t.$props.init.toolbar,inline:t.inlineEditor,setup:function(e){t.editor=e,e.on("init",function(n){return r(n,t,e)}),t.$props.init&&"function"==typeof t.$props.init.setup&&t.$props.init.setup(e)}});(function(t){return null!==t&&"textarea"===t.tagName.toLowerCase()})(t.element)&&(t.element.style.visibility=""),Object(d.a)().init(e)}},f={props:u,created:function(){this.elementId=this.$props.id||s("tiny-vue"),this.inlineEditor=this.$props.init&&this.$props.init.inline||this.$props.inline},watch:{disabled:function(){this.editor.setMode(this.disabled?"readonly":"design")}},mounted:function(){if(this.element=this.$el,null!==Object(d.a)())m(this)();else if(this.element){var t=this.element.ownerDocument,e=this.$props.cloudChannel?this.$props.cloudChannel:"stable",n=this.$props.apiKey?this.$props.apiKey:"";!function(t,e,n,o){t.scriptLoaded?o():(t.listeners.push(o),e.getElementById(t.scriptId)||function(t,e,n,o){var i=e.createElement("script");i.type="application/javascript",i.id=t,i.addEventListener("load",o),i.src=n,e.head.appendChild(i)}(t.scriptId,e,n,function(){t.listeners.forEach(function(t){return t()}),t.scriptLoaded=!0}))}(p,t,"https://cloud.tinymce.com/"+e+"/tinymce.min.js?apiKey="+n,m(this))}},beforeDestroy:function(){null!==Object(d.a)()&&Object(d.a)().remove(this.editor)},render:function(t){return this.inlineEditor?function(t,e,n){return t(n||"div",{attrs:{id:e}})}(t,this.elementId,this.$props.tagName):function(t,e){return t("textarea",{attrs:{id:e},style:{visibility:"hidden"}})}(t,this.elementId)}};e.a=f},rs3x:function(t,e,n){"use strict";n.d(e,"b",function(){return i}),n.d(e,"a",function(){return r});var o=n("t3Un"),i="https://guoyu.windymuse.cn/upload/admin";function r(t){return Object(o.a)({url:i,method:"post",data:t})}}}]);