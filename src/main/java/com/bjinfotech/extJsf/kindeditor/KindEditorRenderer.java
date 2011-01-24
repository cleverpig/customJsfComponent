package com.bjinfotech.extJsf.kindeditor;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.util.pooling.ClientIdPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
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
    KindEditor ke= (KindEditor) component;

    if (domContext.isInitialized()==false){
      Element root=domContext.createRootElement(HTML.DIV_ELEM);
      root.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId+"Container"));

      Element textarea=domContext.createElement(HTML.TEXTAREA_ELEM);
      textarea.setAttribute(HTML.COLS_ATTR,ke.getCols());
      textarea.setAttribute(HTML.ROWS_ATTR,ke.getRows());
      textarea.setAttribute(HTML.ID_ATTR,ClientIdPool.get(clientId+"editor"));
      root.appendChild(textarea);

      StringBuffer scriptCall=new StringBuffer();
      String scriptPath=ke.getBaseURI().getPath();//调用getBaseURI()方法会自动装载javascript相关的资源

      if (ke.getCustomConfigPath()==null){//这里可以使用外部的自定义配置文件，但因为kindEditor目前不支持外部配置文件，因此这里仅留作判断，今后有机会能用得到
        scriptCall.append(
            "KE.show({\n" +
            " id:'"+ClientIdPool.get(clientId+"editor")+"',\n" +
            " skinsPath:'"+scriptPath+"skins/',\n"+
            " pluginsPath:'"+scriptPath+"plugins/',\n"+
            " height:'"+ke.getHeight()+"',\n"+
            " width:'"+ke.getWidth()+"',\n"+
            " skinType:'"+ke.getSkin()+"',\n"+
            " items:"+ke.getItems()+"\n"+
            "});");
        Element script=domContext.createElement(HTML.SCRIPT_ELEM);
        script.setAttribute(HTML.TYPE_ATTR,"text/javascript");
        script.setAttribute(HTML.ID_ATTR,ClientIdPool.get(clientId+"script"));
        script.appendChild(domContext.createTextNode(scriptCall.toString()));
        root.appendChild(script);
      }
      domContext.stepOver();
    }
//    ResponseWriter writer=context.getResponseWriter();
//    writer.startElement("div",component);
//
//    KindEditor ke= (KindEditor) component;
//    writer.writeAttribute("baseURI",ke.getBaseURI().getPath(),null);

//    writer.endElement("div");
  }
}
