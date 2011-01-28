package com.bjinfotech.extJsf.service;

import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-28
 * Time: 上午9:29
 * To change this template use File | Settings | File Templates.
 */
public interface UploadService {
  public String servletUploadHandler(
      HttpServletRequest request,
      String saveBaseDir,
      String saveDir,
      String[] allowedFileTypes,
      long allowedMaxFileSize) throws FileUploadException;

}
