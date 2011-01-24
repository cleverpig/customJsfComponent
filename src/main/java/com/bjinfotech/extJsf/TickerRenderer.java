package com.bjinfotech.extJsf;

import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-20
 * Time: 下午3:23
 * To change this template use File | Settings | File Templates.
 */
public class TickerRenderer extends DomBasicRenderer {
  private Log log= LogFactory.getLog(TickerRenderer.class);

  @Override
  public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    log.debug("encodeBegin...");

    ResponseWriter writer=context.getResponseWriter();
    writer.startElement("div",component);

    writer.writeAttribute("id",component.getClientId(context),null);

    String style= (String) component.getAttributes().get("style");
    if (style!=null)
      writer.writeAttribute("style",style,null);

    String styleClass= (String) component.getAttributes().get("styleClass");
    if (styleClass!=null)
      writer.writeAttribute("class",styleClass,null);

    //值表达式
    if (component.getValueExpression("value")!=null){
      writer.writeText(
          component.getValueExpression("value").getValue(context.getELContext()),
          component,
          "value"
      );
    }
    //属性值
    else if (component.getAttributes().get("value")!=null){
      writer.writeText(
          component.getAttributes().get("value"),
          component,
          "value"
      );
    }else{
      writer.writeText("默认输出：hello",null);
    }
    writer.endElement("div");
//    super.encodeBegin(context,component);
  }

  @Override
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    log.debug("encodeChildren...");
    super.encodeChildren(context, component);    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override
  public boolean getRendersChildren() {
    return true;    //To change body of overridden methods use File | Settings | File Templates.
  }

  @Override
  public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    log.debug("encodeChildren...");
    super.encodeEnd(context,component);
  }
}
