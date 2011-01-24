package com.bjinfotech.extJsf;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.faces.webapp.UIComponentTag;
import static com.bjinfotech.extJsf.Ticker.*;
/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-20
 * Time: 下午2:58
 * To change this template use File | Settings | File Templates.
 */
public class TickerTag extends UIComponentELTag {
  private String style;
  private String styleClass;
  private ValueExpression value;

  @Override
  public String getComponentType() {
    return COMPONENT_TYPE;
  }

  @Override
  public String getRendererType() {
    return COMPONENT_RENDER_TYPE;
  }

  public String getStyle() {
    return style;
  }

  public void setStyle(String style) {
    this.style = style;
  }

  public String getStyleClass() {
    return styleClass;
  }

  public void setStyleClass(String styleClass) {
    this.styleClass = styleClass;
  }

  public ValueExpression getValue() {
    return value;
  }

  public void setValue(ValueExpression value) {
    this.value = value;
  }

  @Override
  public void release() {
    super.release();
    style=null;
    styleClass=null;
    value=null;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    if (style!=null)
      component.getAttributes().put("style",style);
    if (styleClass!=null)
      component.getAttributes().put("styleClass",styleClass);
    if (value!=null){
      //如果是表达式
      if (value.isLiteralText()==false){
        component.setValueExpression("value", value);
      }
      else{
        component.getAttributes().put("value",value.getExpressionString());
      }
    }
  }

}
