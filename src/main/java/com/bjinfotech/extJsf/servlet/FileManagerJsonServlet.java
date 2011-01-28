package com.bjinfotech.extJsf.servlet;

import com.bjinfotech.extJsf.service.FileManagerService;
import com.bjinfotech.extJsf.service.impl.FileManagerServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.bjinfotech.extJsf.servlet.Constants.*;
/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-28
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
public class FileManagerJsonServlet extends HttpServlet{
  private FileManagerService fileManagerService=new FileManagerServiceImpl();
  private String fileDir;

  @Override
  public void init(ServletConfig config) throws ServletException {
    fileDir =config.getInitParameter("fileDir");
    if (fileDir ==null)
      fileDir =DEFAULT_UPLOAD_DIR;

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
    jsonString=fileManagerService.fileManagerHandler(
        req,
        saveBaseDir,
        fileDir,
        DEFAULT_ALLOWED_FILE_TYPES
        );
    resp.getWriter().write(jsonString);
  }
}
