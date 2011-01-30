package com.bjinfotech.extJsf;

import com.icesoft.faces.component.ext.renderkit.TextareaRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-30
 * Time: 上午9:35
 * To change this template use File | Settings | File Templates.
 */
public class MyInputTextareaRenderer extends TextareaRenderer {
  @Override
  public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    super.encodeBegin(context,component);
  }
}
