package com.bjinfotech.extJsf.backingbean;

import com.bjinfotech.extJsf.MyInputTextarea;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-30
 * Time: 下午1:27
 * To change this template use File | Settings | File Templates.
 */
public class MyInputTextDemo {
  private String value;
  private Log log= LogFactory.getLog(MyInputTextarea.class);

  public MyInputTextDemo() {
    this.value = "";
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String submitActionHandler(){
    log.debug("submitActionHandler...value="+value);
    return "";
  }
}
