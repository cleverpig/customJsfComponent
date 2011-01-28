package com.bjinfotech.extJsf.service.impl;

import com.bjinfotech.extJsf.service.UploadService;
import com.bjinfotech.extJsf.util.JsonTool;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-28
 * Time: 上午9:07
 * To change this template use File | Settings | File Templates.
 */
public class UploadServiceImpl implements UploadService {
  private Log log= LogFactory.getLog(UploadServiceImpl.class);

  /**
   * serverlet upload handler
   * @param request
   * @param saveDir
   * @param allowedFileTypes
   * @param allowedMaxFileSize
   * @return json structure result,including error code and mesage or url
   * @throws FileUploadException
   */
  @Override
  public String servletUploadHandler(
      HttpServletRequest request,
      String saveBaseDir,
      String saveDir,
      String[] allowedFileTypes,
      long allowedMaxFileSize) throws FileUploadException {

    //文件保存目录路径
    String savePath = saveBaseDir + saveDir+"/";
    //文件保存目录URL
    String saveUrl  = request.getContextPath() + "/"+saveDir+"/";

    if(!ServletFileUpload.isMultipartContent(request)){
      return JsonTool.getErrorMessage("请选择文件。");
    }

    //检查目录
    File uploadDir = new File(savePath);
    if(!uploadDir.isDirectory()){
      return JsonTool.getErrorMessage("上传目录不存在。");
    }

    //检查目录写权限
    if(!uploadDir.canWrite()){
      return JsonTool.getErrorMessage("上传目录没有写权限。");
    }

    FileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);
    upload.setHeaderEncoding("UTF-8");
    List items = upload.parseRequest(request);
    Iterator itr = items.iterator();
    while (itr.hasNext()) {
      FileItem item = (FileItem) itr.next();
      String fileName = item.getName();
      long fileSize = item.getSize();
      if (!item.isFormField()) {
        //检查文件大小
        if(item.getSize() > allowedMaxFileSize){
          return JsonTool.getErrorMessage("上传文件大小超过限制。");
        }
        //检查扩展名
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if(!Arrays.<String>asList(allowedFileTypes).contains(fileExt)){
          return JsonTool.getErrorMessage("上传文件扩展名是不允许的扩展名。");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
        try{
          File uploadedFile = new File(savePath, newFileName);
          item.write(uploadedFile);
        }catch(Exception e){
          log.error("we encount exception when processing upload:"+e.getMessage());
          return JsonTool.getErrorMessage("上传文件失败。");
        }

        return JsonTool.getSuccessMessage(saveUrl + newFileName);
      }
    }
    log.debug("we processed any fileItem,but didn't return avaliable json result!");
    return "";
  }


}
