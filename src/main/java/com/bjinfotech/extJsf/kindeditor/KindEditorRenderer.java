package com.bjinfotech.extJsf.kindeditor;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.util.pooling.ClientIdPool;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-24
 * Time: 下午2:54
 * To change this template use File | Settings | File Templates.
 */
public class KindEditorRenderer extends DomBasicInputRenderer {
  private Log log= LogFactory.getLog(KindEditorRenderer.class);

  @Override
  public void setSubmittedValue(UIComponent uiComponent, Object submittedValue) {
    super.setSubmittedValue(uiComponent, submittedValue);
  }

  @Override
  public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    log.debug("encodeBegin...");
    String clientId=component.getClientId(context);
    DOMContext domContext=DOMContext.attachDOMContext(context,component);

    if (domContext.isInitialized()==false){
      KindEditor ke= (KindEditor) component;
      ke.loadJavaScript();

      String editorId=ke.getId();
      Element root=domContext.createRootElement(HTML.DIV_ELEM);
      root.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId+"Container"));

      String htmlContent="";
      String htmlContentEscaped="";
      if (ke.getValue()!=null){
        htmlContent= (String) ke.getValue();
        htmlContentEscaped= StringEscapeUtils.escapeHtml(htmlContent);
      }
      Element textarea=domContext.createElement(HTML.TEXTAREA_ELEM);
//      textarea.setAttribute(HTML.COLS_ATTR,ke.getCols());
//      textarea.setAttribute(HTML.ROWS_ATTR,ke.getRows());
      textarea.setAttribute(HTML.NAME_ATTR,editorId);
      textarea.setAttribute(HTML.ID_ATTR,ClientIdPool.get(editorId));
      textarea.setAttribute(HTML.STYLE_ATTR,"display:none;");
      textarea.setAttribute(HTML.VALUE_ATTR, htmlContentEscaped);
      root.appendChild(textarea);

      String contextPath=context.getExternalContext().getRequestContextPath();
      log.debug("contextPath:"+contextPath);

      //三种方式调用kindeditor脚本：直接调用、使用外部配置文件、使用组件属性配置
      //如果不使用JQuery扩展，则直接调用kindeditor的脚本
      StringBuffer scriptCall=new StringBuffer();
      String scriptPath=ke.getBaseURI().getPath();//调用getBaseURI()方法会自动装载javascript相关的资源

      if (ke.getUsingJQueryExt()==false){
        scriptCall.append(
            "try{\n" +
                "KE.remove('"+editorId+"');\n" +
                "}" +
                "catch(e){;}\n"+
                "KE.init({\n" +
                " id:'"+editorId+"',\n" +
                " imageUploadJson:'"+contextPath+"/uploadJson',\n" +
                " fileManagerJson:'"+contextPath+"/fileManagerJson',\n" +
                " allowFileManager:true,\n"+
                " skinsPath:'"+scriptPath+"skins/',\n"+
                " pluginsPath:'"+scriptPath+"plugins/',\n"+
                " height:'"+ke.getHeight()+"',\n"+
                " width:'"+ke.getWidth()+"',\n"+
                " skinType:'"+ke.getSkin()+"',\n"+
                " items:"+ke.getItems()+"\n"+
                "});\n" +
                "KE.create('"+editorId+"');\n"+
                "KE.html('"+editorId+"',\""+htmlContentEscaped+"\");"
        );
      }
      //优先使用外部配置文件
      else if (ke.getExtConfigPath()!=null && ke.getExtConfigPath().length()>0){
        JavascriptContext.includeLib(ke.getExtConfigPath(),context);

        JavascriptContext.addJavascriptCall(
            context,
            "config.editorId='"+editorId+"';\n" +
                "config.baseURL='"+scriptPath+"';\n" +
                "config.contextPath='"+contextPath+"',\n" +
                "config.htmlContent=\""+htmlContentEscaped+"\";\n" +
                "new KindEditorExt(config).setupWhenDocumentIsReady();");
      }
      //使用页面中的组件属性值构造配置
      else{
        scriptCall.append(
            "new KindEditorExt(\n"+
                "new KindEditorConfig(\n" +
                "'"+editorId+"',\n" +
                "'"+ke.getBaseURI().getPath()+"',\n"+
                "'"+ke.getHeight()+"',\n"+
                "'"+ke.getWidth()+"',\n"+
                "'"+ke.getSkin()+"',\n" +
                "'"+contextPath+"',\n" +
                ke.getItems()+",\n"+
                "\""+htmlContentEscaped+"\"\n"+
                ")\n"+
              ").setupWhenDocumentIsReady();"
        );
      }
      if (scriptCall.length()>0){
        Element script=domContext.createRootElement(HTML.SCRIPT_ELEM);
        script.setAttribute(HTML.TYPE_ATTR,"text/javascript");
        script.setAttribute(HTML.ID_ATTR,ClientIdPool.get(clientId+"script"));
        script.appendChild(domContext.createTextNode(scriptCall.toString()));
      }

      domContext.stepOver();
    }
  }
}
