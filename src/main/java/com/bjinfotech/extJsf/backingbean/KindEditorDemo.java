package com.bjinfotech.extJsf.backingbean;

import com.bjinfotech.extJsf.kindeditor.KindEditor;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-25
 * Time: 下午2:54
 * To change this template use File | Settings | File Templates.
 */
public class KindEditorDemo {
  private String editContent;
  private String htmlContent;
  private Log log= LogFactory.getLog(KindEditorDemo.class);

  public String getHtmlContent() {
    return htmlContent;
  }

  public String getEditContent() {
    return editContent;
  }

  public void setEditContent(String editContent) {
    this.editContent = editContent;
  }

  public void setHtmlContent(String htmlContent) {
    this.htmlContent = htmlContent;
  }

  public void submitActionListener(ActionEvent event){
    FacesContext context = FacesContext.getCurrentInstance();
//    editContent=context.getExternalContext().getRequestParameterMap().get("editor");
    log.debug("editContent:"+editContent);
    htmlContent=editContent;
  }


}
