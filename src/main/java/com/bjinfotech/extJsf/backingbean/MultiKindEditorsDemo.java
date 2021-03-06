package com.bjinfotech.extJsf.backingbean;

import org.apache.commons.lang.StringUtils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-28
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public class MultiKindEditorsDemo {
  private String editContent01;
  private String editContent02;
  private String htmlContent;

  public String getEditContent01() {
    return editContent01;
  }

  public void setEditContent01(String editContent01) {
    this.editContent01 = editContent01;
  }

  public String getEditContent02() {
    return editContent02;
  }

  public void setEditContent02(String editContent02) {
    this.editContent02 = editContent02;
  }

  public String getHtmlContent() {
    return htmlContent;
  }

  public void setHtmlContent(String htmlContent) {
    this.htmlContent = htmlContent;
  }

  public void submitActionListener(ActionEvent event){
    FacesContext context=FacesContext.getCurrentInstance();
    if (StringUtils.isEmpty(editContent01)){
      FacesMessage message=new FacesMessage();
      message.setSummary("editor01 is blank");
      message.setDetail("editor01 is blank");
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      context.addMessage("form:editor01",message);
    }
    if (StringUtils.isEmpty(editContent02)){
      FacesMessage message=new FacesMessage();
      message.setSummary("editor02 is blank");
      message.setDetail("editor02 is blank");
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      context.addMessage("form:editor02",message);
    }
    if (context.getMessages("form:editor01").hasNext()==false &&
        context.getMessages("form:editor02").hasNext()==false)
      htmlContent=editContent01+"<br/>"+editContent02;
  }
}
