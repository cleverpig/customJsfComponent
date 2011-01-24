package com.bjinfotech.extJsf;

import com.icesoft.faces.component.ext.HtmlOutputText;

import javax.faces.component.UIComponentBase;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-20
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class Ticker extends UIComponentBase {
  public static final String COMPONENT_TYPE="com.bjinfotech.extJsf.Ticker";
  public static final String COMPONENT_FAMILY="com.bjinfotech.extJsf.TickerFamily";
  public static final String COMPONENT_RENDER_TYPE ="com.bjinfotech.extJsf.TickerRenderer";
  private String value;

  public Ticker() {
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

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
