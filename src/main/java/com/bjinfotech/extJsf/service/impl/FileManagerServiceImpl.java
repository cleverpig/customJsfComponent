package com.bjinfotech.extJsf.service.impl;

import com.bjinfotech.extJsf.service.FileManagerService;
import com.bjinfotech.extJsf.util.JsonTool;
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
 * Time: 下午1:42
 * To change this template use File | Settings | File Templates.
 */
public class FileManagerServiceImpl implements FileManagerService {
  private Log log= LogFactory.getLog(FileManagerServiceImpl.class);

  /**
   *
   * @param request
   * @param fileBaseDir
   * @param fileDir
   * @param allowedFileTypes
   * @return
   */
  @Override
  public String fileManagerHandler(
      HttpServletRequest request,
      String fileBaseDir,
      String fileDir,
      String[] allowedFileTypes){
    //根目录路径，可以指定绝对路径，比如 /var/www/attached/
    String rootPath = fileBaseDir + fileDir+ "/";
    //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
    String rootUrl  = request.getContextPath() + "/"+fileDir+"/";

    //根据path参数，设置各路径和URL
    String path = request.getParameter("path") != null ? request.getParameter("path") : "";
    String currentPath = rootPath + path;
    String currentUrl = rootUrl + path;
    String currentDirPath = path;
    String moveupDirPath = "";
    if (!"".equals(path)) {
      String str = currentDirPath.substring(0, currentDirPath.length() - 1);
      moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
    }

    //排序形式，name or size or type
    String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

    //不允许使用..移动到上一级目录
    if (path.indexOf("..") >= 0) {
      return "Access is not allowed.";
    }
    //最后一个字符不是/
    if (!"".equals(path) && !path.endsWith("/")) {
      return "Parameter is not valid.";
    }
    //目录不存在或不是目录
    File currentPathFile = new File(currentPath);
    if(!currentPathFile.isDirectory()){
      return "Directory does not exist.";
    }

    //遍历目录取的文件信息
    List<Hashtable> fileList = new ArrayList<Hashtable>();
    if(currentPathFile.listFiles() != null) {
      for (File file : currentPathFile.listFiles()) {
        Hashtable<String, Object> hash = new Hashtable<String, Object>();
        String fileName = file.getName();
        if(file.isDirectory()) {
          hash.put("is_dir", true);
          hash.put("has_file", (file.listFiles() != null));
          hash.put("filesize", 0L);
          hash.put("is_photo", false);
          hash.put("filetype", "");
        } else if(file.isFile()){
          String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
          hash.put("is_dir", false);
          hash.put("has_file", false);
          hash.put("filesize", file.length());
          hash.put("is_photo", Arrays.<String>asList(allowedFileTypes).contains(fileExt));
          hash.put("filetype", fileExt);
        }
        hash.put("filename", fileName);
        hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
        fileList.add(hash);
      }
    }

    if ("size".equals(order)) {
      Collections.sort(fileList, new SizeComparator());
    } else if ("type".equals(order)) {
      Collections.sort(fileList, new TypeComparator());
    } else {
      Collections.sort(fileList, new NameComparator());
    }
    return JsonTool.getFileInfoMessage(moveupDirPath,currentDirPath,currentUrl,fileList);

  }
}
class NameComparator implements Comparator {
	public int compare(Object a, Object b) {
		Hashtable hashA = (Hashtable)a;
		Hashtable hashB = (Hashtable)b;
		if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
			return -1;
		} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
			return 1;
		} else {
			return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
		}
	}
}
class SizeComparator implements Comparator {
	public int compare(Object a, Object b) {
		Hashtable hashA = (Hashtable)a;
		Hashtable hashB = (Hashtable)b;
		if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
			return -1;
		} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
			return 1;
		} else {
			if (((Long)hashA.get("filesize")) > ((Long)hashB.get("filesize"))) {
				return 1;
			} else if (((Long)hashA.get("filesize")) < ((Long)hashB.get("filesize"))) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
class TypeComparator implements Comparator {
	public int compare(Object a, Object b) {
		Hashtable hashA = (Hashtable)a;
		Hashtable hashB = (Hashtable)b;
		if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
			return -1;
		} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
			return 1;
		} else {
			return ((String)hashA.get("filetype")).compareTo((String)hashB.get("filetype"));
		}
	}
}
