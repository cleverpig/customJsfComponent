package com.bjinfotech.extJsf;

import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-30
 * Time: 上午9:01
 * To change this template use File | Settings | File Templates.
 */
public class MyInputTextarea extends HtmlInputTextarea {
  public static final String COMPONENT_TYPE="com.bjinfotech.extJsf.MyInputTextarea";
  public static final String COMPONENT_FAMILY="com.bjinfotech.extJsf.MyInputTextareaFamily";
  public static final String COMPONENT_RENDER_TYPE ="com.bjinfotech.extJsf.MyInputTextareaRenderer";
  private Log log= LogFactory.getLog(MyInputTextarea.class);

  public MyInputTextarea(){
    super();
    setRendererType(COMPONENT_RENDER_TYPE);
  }
  @Override
  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  @Override
  public String getRendererType() {
    return COMPONENT_RENDER_TYPE;
  }

  @Override
  protected void validateValue(FacesContext context, Object newValue) {
    log.debug("validate...isRequired="+isRequired());
    super.validateValue(context, newValue);
  }

  @Override
  public Object saveState(FacesContext context) {
    log.debug("saveState...isRequired="+isRequired());
    return super.saveState(context);
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    log.debug("restoreState...isRequired="+isRequired());
    super.restoreState(context, state);
  }

  @Override
  public void setRequired(boolean required) {
    log.debug("setRequired:"+required);
    super.setRequired(required);
  }
}
