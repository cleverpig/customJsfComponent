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
  initialize:function(editorId,baseURL,height,width,skinType,items,htmlContent){
    this.editorId=editorId;
    this.baseURL=baseURL;
    this.height=height;
    this.width=width;
    this.skinType=skinType;
    this.items=items;
    this.htmlContent=htmlContent;
  }
};

KindEditorExt=Class.create();
KindEditorExt.prototype={
  initialize:function(config){
    this.editorId=config.editorId;
    this.baseURL=config.baseURL;
    this.height=config.height;
    this.width=config.width;
    this.skinType=config.skinType;
    this.items=config.items;
    this.htmlContent=config.htmlContent;
  },
  setupWhenDocumentIsReady:function(){
    var _self=this;
    jQuery(document).ready(function() {
      //TODO:we must re-create kindeidtor everytime when document DOM was ready?
      //TODO:How to created kindeditor once and change display content when form submitted?
      try{
        KE.remove(_self.editorId);
      }
      catch(e){
        ;
      }
      KE.init({
        id:_self.editorId,
        skinsPath:_self.baseURL+"skins/",
        pluginsPath:_self.baseURL+"plugins/",
        height:_self.height,
        width:_self.width,
        skinType:_self.skinType,
        items:_self.items
      });
      KE.create(_self.editorId);
//      alert(_self.htmlContent);
//      alert(Encoder.htmlDecode(_self.htmlContent));
      /**
       *  note:
       *    we must call Encoder to convert escaped HTML to normal HTML.
       *    since KindEditorRenderer would escape HTML when generated Serverside DOM.
       *    as you kown,normal HTML code will break or disrupt javascript when it was taken in as param.
       */
      KE.html(_self.editorId,Encoder.htmlDecode(_self.htmlContent));//take submitted data back to kindeditor
    });
  }
}

