package com.bjinfotech.extJsf.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-28
 * Time: 下午1:55
 * To change this template use File | Settings | File Templates.
 */
public interface FileManagerService {
  String fileManagerHandler(
      HttpServletRequest request,
      String fileBaseDir,
      String fileDir,
      String[] allowedFileTypes);
}
