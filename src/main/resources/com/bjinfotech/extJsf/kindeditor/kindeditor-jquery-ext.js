/**
 * Created by .
 * User: cleverpig
 * Date: 11-1-25
 * Time: 上午5:19
 * To change this template use File | Settings | File Templates.
 */
jQuery.noConflict();//jQuery can working with other library:http://docs.jquery.com/Using_jQuery_with_Other_Libraries

KindEditorConfig=Class.create();
KindEditorConfig.prototype={
  initialize:function(editorId,baseURL,height,width,skinType,contextPath,items){
    this.editorId=editorId;
    this.baseURL=baseURL;
    this.height=height;
    this.width=width;
    this.skinType=skinType;
    this.contextPath=contextPath;
    this.items=items;
  }
};

KindEditorExt=Class.create();
KindEditorExt.prototype={
  initialize:function(config){
    this.config=config;
  },
  setupWhenDocumentIsReady:function(){
    var _self=this;
    var undefined;
    jQuery(document).ready(function() {
      //TODO:we must re-create kindeidtor everytime when document DOM was ready?
      //TODO:How to created kindeditor once and change display content when form submitted?
      //skip re-create when icefaces had shown error messages.
      if (jQuery('span.iceMsgError').length==0){
        try{
          KE.remove(_self.config.editorId);
        }
        catch(e){
          ;
        }
        KE.init({
          id:_self.config.editorId,
          skinsPath:_self.config.baseURL+"skins/",
          pluginsPath:_self.config.baseURL+"plugins/",
          imageUploadJson:_self.config.contextPath+"/uploadJson",
          fileManagerJson:_self.config.contextPath+"/fileManagerJson",
          allowFileManager:true,
          allowUpload:true,
          height:_self.config.height,
          width:_self.config.width,
          skinType:_self.config.skinType,
          items:_self.config.items
        });
        KE.create(_self.config.editorId);
      }
    });
  }
}

