package com.bjinfotech.extJsf.kindeditor;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import static com.bjinfotech.extJsf.kindeditor.KindEditor.*;
/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-2-14
 * Time: 上午10:09
 * To change this template use File | Settings | File Templates.
 */
public class KindEditorTag  extends UIComponentELTag {
  private ValueExpression width;
  private ValueExpression height;
  private ValueExpression skin;
  private ValueExpression items;
  private ValueExpression usingJQueryExt;
  private ValueExpression extConfigPath;
  private ValueExpression disabled;
  private ValueExpression configProfile;
  private String styleClass;

  @Override
  public String getComponentType() {
    return KIND_EDITOR_COMPONENT_TYPE;
  }

  @Override
  public String getRendererType() {
    return KIND_EDITOR_COMPONENT_RENDER_TYPE;
  }

  public ValueExpression getWidth() {
    return width;
  }

  public void setWidth(ValueExpression width) {
    this.width = width;
  }

  public ValueExpression getHeight() {
    return height;
  }

  public void setHeight(ValueExpression height) {
    this.height = height;
  }

  public ValueExpression getSkin() {
    return skin;
  }

  public void setSkin(ValueExpression skin) {
    this.skin = skin;
  }

  public ValueExpression getItems() {
    return items;
  }

  public void setItems(ValueExpression items) {
    this.items = items;
  }

  public ValueExpression getUsingJQueryExt() {
    return usingJQueryExt;
  }

  public void setUsingJQueryExt(ValueExpression usingJQueryExt) {
    this.usingJQueryExt = usingJQueryExt;
  }

  public ValueExpression getExtConfigPath() {
    return extConfigPath;
  }

  public void setExtConfigPath(ValueExpression extConfigPath) {
    this.extConfigPath = extConfigPath;
  }

  public ValueExpression getDisabled() {
    return disabled;
  }

  public void setDisabled(ValueExpression disabled) {
    this.disabled = disabled;
  }

  public ValueExpression getConfigProfile() {
    return configProfile;
  }

  public void setConfigProfile(ValueExpression configProfile) {
    this.configProfile = configProfile;
  }

  public String getStyleClass() {
    return styleClass;
  }

  public void setStyleClass(String styleClass) {
    this.styleClass = styleClass;
  }

  @Override
  public void release() {
    super.release();
    this.styleClass=null;
    this.configProfile=null;
    this.disabled=null;
    this.extConfigPath=null;
    this.height=null;
    this.width=null;
    this.items=null;
    this.skin=null;
    this.usingJQueryExt=null;
  }

  private void setProperty(
      UIComponent uiComponent,
      String elName,
      ValueExpression elValue){
    if (elValue!=null){
      //如果是表达式
      if (elValue.isLiteralText()==false){
        uiComponent.setValueExpression(elName, elValue);
      }
      else{
        uiComponent.getAttributes().put(elName,elValue.getExpressionString());
      }
    }
  }
  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    if (styleClass!=null)
      component.getAttributes().put("styleClass",styleClass);

    setProperty(component,"configProfile",configProfile);
    setProperty(component,"disabled",disabled);
    setProperty(component,"extConfigPath",extConfigPath);
    setProperty(component,"height",height);
    setProperty(component,"width",width);
    setProperty(component,"items",items);
    setProperty(component,"skin",skin);
    setProperty(component,"usingJQueryExt",usingJQueryExt);
  }
}
