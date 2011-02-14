package com.bjinfotech.extJsf.kindeditor;

import com.icesoft.faces.component.AttributeConstants;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.BaseInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;
import com.icesoft.faces.util.DOMUtils;
import com.icesoft.util.pooling.ClientIdPool;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-24
 * Time: 下午2:54
 * To change this template use File | Settings | File Templates.
 */
public class KindEditorRenderer extends BaseInputRenderer {
  private Log log= LogFactory.getLog(KindEditorRenderer.class);

  protected static final String ONMOUSEDOWN_FOCUS = "this.focus();";

  private static final String[] passThruExcludes = new String[] {
      HTML.ROWS_ATTR, HTML.COLS_ATTR, HTML.ONMOUSEDOWN_ATTR
  };
  private static final String[] passThruAttributes = AttributeConstants.getAttributes(
      AttributeConstants.H_INPUTTEXTAREA, passThruExcludes
  );

  public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    super.encodeBegin(facesContext, uiComponent);
    DomBasicRenderer.validateParameters(facesContext, uiComponent, UIInput.class);

    KindEditor ke= (KindEditor) uiComponent;

    ResponseWriter writer = facesContext.getResponseWriter();
//    String clientId =ke.getId();
    String clientId=uiComponent.getClientId(facesContext);

    writer.startElement(HTML.TEXTAREA_ELEM, uiComponent);
    writer.writeAttribute(HTML.ID_ATTR, clientId, null);
    writer.writeAttribute(HTML.NAME_ATTR, clientId, null);

    renderHtmlAttributes(facesContext, writer, uiComponent);
    PassThruAttributeWriter.renderBooleanAttributes(
        writer,
        uiComponent,
        PassThruAttributeWriter.EMPTY_STRING_ARRAY);

    Object styleClass = uiComponent.getAttributes().get("styleClass");
    if (styleClass != null) {
      writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
    }

    renderNumericAttributeOrDefault(writer, uiComponent, HTML.ROWS_ATTR, "2");
    renderNumericAttributeOrDefault(writer, uiComponent, HTML.COLS_ATTR, "20");

    writer.writeAttribute(HTML.STYLE_ATTR, "display:none;", null);
  }

  public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
      throws IOException {
    super.encodeChildren(facesContext, uiComponent);
    DomBasicRenderer.validateParameters(facesContext, uiComponent, UIInput.class);
  }

  public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
      throws IOException {
    //it must call the super.encode to support effects and facesMessage recovery
    super.encodeEnd(facesContext, uiComponent);
    ResponseWriter writer = facesContext.getResponseWriter();

    String currentValue = getValue(facesContext, uiComponent);
    if (currentValue != null && currentValue.length() > 0) {
      writer.write(DOMUtils.escapeAnsi(currentValue));
    }

    writer.endElement(HTML.TEXTAREA_ELEM);

    renderKindEditorJavascipt(facesContext,uiComponent);
  }

  private void renderKindEditorJavascipt(FacesContext facesContext,UIComponent uiComponent){
    KindEditor ke= (KindEditor) uiComponent;

    ke.loadJavaScript();

//    String editorId=ke.getId();
    String clientId=uiComponent.getClientId(facesContext);

    String htmlContent="";
    String htmlContentEscaped="";
    if (ke.getValue()!=null){
      htmlContent= (String) ke.getValue();
      htmlContentEscaped= StringEscapeUtils.escapeHtml(htmlContent);
    }
    String contextPath=facesContext.getExternalContext().getRequestContextPath();
    log.debug("contextPath:"+contextPath);

    //三种方式调用kindeditor脚本：直接调用、使用外部配置文件、使用组件属性配置
    //如果不使用JQuery扩展，则直接调用kindeditor的脚本
    StringBuffer scriptCall=new StringBuffer();
    String scriptPath=ke.getBaseURI().getPath();//调用getBaseURI()方法会自动装载javascript相关的资源

    if (ke.getUsingJQueryExt()==false){
      scriptCall.append(
          "try{\n" +
              "KE.remove('"+clientId+"');\n" +
              "}" +
              "catch(e){;}\n"+
              "KE.init({\n" +
              " id:'"+clientId+"',\n" +
              " imageUploadJson:'"+contextPath+"/uploadJson',\n" +
              " fileManagerJson:'"+contextPath+"/fileManagerJson',\n" +
              " allowFileManager:true,\n"+
              " allowUpload:true,\n"+
              " skinsPath:'"+scriptPath+"skins/',\n"+
              " pluginsPath:'"+scriptPath+"plugins/',\n"+
              " height:'"+ke.getHeight()+"',\n"+
              " width:'"+ke.getWidth()+"',\n"+
              " skinType:'"+ke.getSkin()+"',\n"+
              " items:"+ke.getItems()+"\n"+
              "});\n" +
              "KE.create('"+clientId+"');"
      );
    }
    //优先使用外部配置文件
    else if (ke.getExtConfigPath()!=null && ke.getExtConfigPath().length()>0){
      JavascriptContext.includeLib(ke.getExtConfigPath(),facesContext);
      String configProfile=ke.getConfigProfile();
      JavascriptContext.addJavascriptCall(
          facesContext,
          configProfile+".editorId='"+clientId+"';\n" +
              configProfile+".baseURL='"+scriptPath+"';\n" +
              configProfile+".contextPath='"+contextPath+"',\n" +
              "new KindEditorExt("+configProfile+").setupWhenDocumentIsReady();");
    }
    //使用页面中的组件属性值构造配置
    else{
      scriptCall.append(
          "new KindEditorExt(\n"+
              "new KindEditorConfig(\n" +
              "'"+clientId+"',\n" +
              "'"+ke.getBaseURI().getPath()+"',\n"+
              "'"+ke.getHeight()+"',\n"+
              "'"+ke.getWidth()+"',\n"+
              "'"+ke.getSkin()+"',\n" +
              "'"+contextPath+"',\n" +
              ke.getItems()+"\n"+
              ")\n"+
              ").setupWhenDocumentIsReady();"
      );
    }
    if (scriptCall.length()>0){
      DOMContext domContext = DOMContext.attachDOMContext(facesContext, uiComponent);
      if (!domContext.isInitialized()) {
        Element script=domContext.createRootElement(HTML.SCRIPT_ELEM);
        script.setAttribute(HTML.TYPE_ATTR,"text/javascript");
        script.setAttribute(HTML.ID_ATTR,ClientIdPool.get(clientId+"script"));
        script.appendChild(domContext.createTextNode(scriptCall.toString()));
      }
    }
  }
  protected void renderNumericAttributeOrDefault(
      ResponseWriter writer, UIComponent uiComponent,
      String attribName, String defaultValue)
      throws IOException {
    Object val = uiComponent.getAttributes().get(attribName);
    if (val == null || ((Integer)val).intValue() <= -1) {
      val = defaultValue;
    }
    writer.writeAttribute(attribName, val, attribName);
  }

  protected void renderHtmlAttributes(
      FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
      throws IOException {
    PassThruAttributeWriter.renderHtmlAttributes(
        writer, uiComponent, passThruAttributes);

    //fix for ICE-2514
    String app = (String)uiComponent.getAttributes().get(HTML.ONMOUSEDOWN_ATTR);
    String rend = ONMOUSEDOWN_FOCUS;
    String combined = DomBasicRenderer.combinedPassThru(app, rend);
    writer.writeAttribute(HTML.ONMOUSEDOWN_ATTR, combined, HTML.ONMOUSEDOWN_ATTR);
  }

}
