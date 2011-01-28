package com.bjinfotech.extJsf.util;

import org.json.simple.JSONObject;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-28
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class JsonTool {
   public static String getErrorMessage(String message) {
    JSONObject obj = new JSONObject();
    obj.put("error", 1);
    obj.put("message", message);
    return obj.toJSONString();
  }

  public static String getSuccessMessage(String url) {
    JSONObject obj = new JSONObject();
    obj.put("error",0);
    obj.put("url", url);
    return obj.toJSONString();
  }

  public static String getFileInfoMessage(
      String moveupDirPath,String currentDirPath,String currentUrl,
      List<Hashtable> fileList){
    JSONObject result = new JSONObject();
    result.put("moveup_dir_path", moveupDirPath);
    result.put("current_dir_path", currentDirPath);
    result.put("current_url", currentUrl);
    result.put("total_count", fileList.size());
    result.put("file_list", fileList);
    return result.toJSONString();
  }
}
