package com.bjinfotech.extJsf.servlet;

import com.bjinfotech.extJsf.service.impl.UploadServiceImpl;
import com.bjinfotech.extJsf.service.UploadService;
import com.bjinfotech.extJsf.util.JsonTool;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.bjinfotech.extJsf.servlet.KindEditorUploadConstants.*;
/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-28
 * Time: 上午9:03
 * To change this template use File | Settings | File Templates.
 */
public class UploadJsonServlet extends HttpServlet{
  private UploadService uploadService=new UploadServiceImpl();
  private String saveDir;
  private long maxFileSize;

  @Override
  public void init(ServletConfig config) throws ServletException {
    saveDir=config.getInitParameter("saveDir");
    if (saveDir==null)
      saveDir=DEFAULT_UPLOAD_DIR;

    if (config.getInitParameter("maxFileSize")!=null)
      maxFileSize=Long.parseLong(config.getInitParameter("maxFileSize"));
    else
      maxFileSize=DEFAULT_ALLOWED_MAX_FILE_SIZE;

    super.init(config);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    service(req,resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    service(req,resp);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html; charset=UTF-8");
    String saveBaseDir=getServletContext().getRealPath("/");
    String jsonString="";
    try {
      jsonString=uploadService.servletUploadHandler(
          req,saveBaseDir,
          saveDir,
          DEFAULT_ALLOWED_FILE_TYPES,
          maxFileSize);
    } catch (FileUploadException e) {
      jsonString= JsonTool.getErrorMessage("文件上传失败。");
      e.printStackTrace();
    }
    resp.getWriter().write(jsonString);
  }
}
