package com.bjinfotech.extJsf.kindeditor;

import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.context.JarResource;
import com.icesoft.faces.context.Resource;
import com.icesoft.faces.context.ResourceLinker;
import com.icesoft.faces.context.ResourceRegistry;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.util.CoreUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: cleverpig
 * Date: 11-1-24
 * Time: 下午1:31
 * TODO:kindeditor's required attribute doesn't work,please use application-level validation to work around.
 * look more about that,please looking:http://facestutorials.icefaces.org/tutorial/validators-tutorial.html#applevelvalidation
 */
public class KindEditor extends HtmlInputTextarea {
  private static Boolean instanceExist =false;
  private static URI baseURI;
  private static final Date lastModified=new Date();
  private static final Map zipEntryCache=new HashMap();
  private static final String KIND_EDITOR_ZIP= "com/bjinfotech/extJsf/kindeditor/kindeditor.zip";
  private static final String KIND_EDITOR_JS= "com/bjinfotech/extJsf/kindeditor/kindeditor.js";
  public static final String KIND_EDITOR_COMPONENT_TYPE ="com.bjinfotech.extJsf.kindeditor.KindEditor";
  public static final String KIND_EDITOR_COMPONENT_FAMILY ="com.bjinfotech.extJsf.kindeditor.KindeditorFamily";
  public static final String KIND_EDITOR_COMPONENT_RENDER_TYPE ="com.bjinfotech.extJsf.kindeditor.KindEditorRenderer";
  public static final String REQUIRED_MESSAGE_ID="com.bjinfotech.extJsf.kindeditor.required";

  private static final Log log= LogFactory.getLog(KindEditor.class);
  private static final String DEFAULT_WIDTH="100%";
  private static final String DEFAULT_HEIGHT="100%";
  private static final String DEFAULT_SKIN="default";
  private static final String DEFAULT_ITEMS=
      "['source', '|', 'fullscreen', 'undo', 'redo', 'print', 'cut', 'copy', 'paste',\n" +
      "'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',\n" +
      "'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',\n" +
      "'superscript', '|', 'selectall', '-',\n" +
      "'title', 'fontname', 'fontsize', '|', 'textcolor', 'bgcolor', 'bold',\n" +
      "'italic', 'underline', 'strikethrough', 'removeformat', '|', 'image',\n" +
      "'flash', 'media', 'advtable', 'hr', 'emoticons', 'link', 'unlink', '|', 'about']";
  private static final String JQUERY_FRAMEWORK_JS ="com/bjinfotech/extJsf/jquery/jquery.js" ;//jQuery framework
  private static final String ENCODER_TOOL_JS ="com/bjinfotech/extJsf/encoder/encoder.js" ; //this is a javascript encode/decode library:http://www.strictly-software.com/htmlencode
  private static final String KIND_EDITOR_JQUERY_EXT_JS ="com/bjinfotech/extJsf/kindeditor/kindeditor-jquery-ext.js" ;//kindeditor config & call function with jQuery and Encoder library's power
  private static final String DEFAULT_CONFIG_PROFILE="config";

  private String width;
  private String height;
  private String skin;
  private String items;
  private Boolean usingJQueryExt =false; //using jQuery javascript framework?
  private String extConfigPath;
  private Boolean disabled = null;
  private String configProfile;

  public KindEditor() {
    super();
    instanceExist =true;
    baseURI=null;
    setRendererType(KIND_EDITOR_COMPONENT_RENDER_TYPE);
  }

  /**
   * 从zip路径装载zip中的文件条目到cache中
   * @param zipPath
   */
  private static void loadZipEntryCache(String zipPath){
    try {
      InputStream in= KindEditor.class.getClassLoader().getResourceAsStream(zipPath);
      ZipInputStream zip=new ZipInputStream(in);
      ZipEntry entry;
      while((entry=zip.getNextEntry())!=null){
        if (entry.isDirectory()==false){
          zipEntryCache.put(entry.getName(),toByteArray(zip));
        }
      }
    } catch (IOException e) {
      log.error("装载zip文件条目时失败:"+e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * 将inputStream转换为byteArray
   * @param input
   * @return
   * @throws IOException
   */
  private static byte[] toByteArray(InputStream input) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] buf = new byte[4096];
    int len = 0;
    while ((len = input.read(buf)) > -1) output.write(buf, 0, len);
    return output.toByteArray();
  }

  /**
   * KIND_EDITOR_LINKED_BASE：kind editor的脚本或资源文件装载根路径
   */
  private static final ResourceLinker.Handler KIND_EDITOR_LINKED_BASE = new ResourceLinker.Handler() {
    public void linkWith(ResourceLinker linker) {
      synchronized(zipEntryCache) {
        if (zipEntryCache.isEmpty()) {
          loadZipEntryCache(KIND_EDITOR_ZIP);
        }
      }
      Iterator i = zipEntryCache.keySet().iterator();
      while (i.hasNext()) {
        final String entryName = (String) i.next();
        linker.registerRelativeResource(entryName, new Resource() {
          public String calculateDigest() {
            return String.valueOf(KIND_EDITOR_ZIP + entryName);
          }

          public Date lastModified() {
            return lastModified;
          }

          public InputStream open() throws IOException {
            return new ByteArrayInputStream((byte[]) zipEntryCache.get(entryName));
          }

          public void withOptions(Resource.Options options) {
            options.setFileName(entryName);
            options.setLastModified(lastModified);
          }
        });
      }
    }
  };

  private static final Resource KIND_EDITOR_RESOURCE=new KindEditorJarResource(KIND_EDITOR_JS);

  public void loadJavaScript(){
    if (extConfigPath!=null && extConfigPath.length()>0){
          usingJQueryExt=true;
    }
    if (FacesContext.getCurrentInstance()!=null && baseURI==null && instanceExist){
      ResourceRegistry registry= (ResourceRegistry) FacesContext.getCurrentInstance();

      if (registry!=null){
        baseURI=registry.loadJavascriptCode(KIND_EDITOR_RESOURCE,KIND_EDITOR_LINKED_BASE);
        log.debug("baseURI:"+baseURI);

        if (usingJQueryExt){
          registry.loadJavascriptCode(new KindEditorJarResource(JQUERY_FRAMEWORK_JS));
          registry.loadJavascriptCode(new KindEditorJarResource(ENCODER_TOOL_JS));
          registry.loadJavascriptCode(new KindEditorJarResource(KIND_EDITOR_JQUERY_EXT_JS));
        }
      }
      else
        log.fatal("转换ResourceRegistry失败");
    }
  }

  public URI getBaseURI(){
    if (baseURI==null){
      loadJavaScript();
    }
    return baseURI;
  }

  @Override
  public String getFamily(){
    return KIND_EDITOR_COMPONENT_FAMILY;
  }

  @Override
  public String getRendererType(){
    return KIND_EDITOR_COMPONENT_RENDER_TYPE;
  }

  private Object getValueBindingWithDefaultValue(String name, Object defaultValue){
    try {
      Field field=KindEditor.class.getDeclaredField(name);
      Object fieldValue= field.get(this);
      if (fieldValue!=null){
        return fieldValue;
      }
      else{
        ValueExpression ve=getValueExpression(name);
        if (ve==null){
          return defaultValue;
        }
        if (ve.isLiteralText()){
          return ve.getExpressionString();
        }
        else{
          return ve.getValue(getFacesContext().getELContext());
        }
      }
    } catch (Exception e) {
      log.fatal("获取绑定变量值时发生错误："+e.getMessage());
      e.printStackTrace();
      return defaultValue;
    }
  }

  public Object getMutableAttribute(String name,Object defaultValue){
    Object attributeValue=getAttributes().get(name);
    return attributeValue==null?defaultValue:attributeValue;
  }
  public String getWidth() {
    return (String) getValueBindingWithDefaultValue("width", DEFAULT_WIDTH);
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return (String) getValueBindingWithDefaultValue("height", DEFAULT_HEIGHT);
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getSkin() {
    return (String) getValueBindingWithDefaultValue("skin", DEFAULT_SKIN);
  }

  public void setSkin(String skin) {
    this.skin = skin;
  }

  public String getItems() {
    return (String) getValueBindingWithDefaultValue("items", DEFAULT_ITEMS);
  }

  public void setItems(String items) {
    this.items = items;
  }

  public Boolean getDisabled() {
    return (Boolean) getValueBindingWithDefaultValue("disabled",false);
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  private String getResourceURLFromValueBinding(String name){
    try {
      Field field= null;
      field = KindEditor.class.getDeclaredField(name);
      String fieldValue= (String) field.get(this);
      if (fieldValue!=null){
        return CoreUtils.resolveResourceURL(getFacesContext(),fieldValue);
      }
      else{
        ValueExpression ve=getValueExpression(fieldValue);
        if (ve==null){
          return null;
        }
        if (ve.isLiteralText()){
          return CoreUtils.resolveResourceURL(getFacesContext(),ve.getExpressionString());
        }
        else{
          return CoreUtils.resolveResourceURL(
              getFacesContext(),
              (String) ve.getValue(getFacesContext().getELContext())
          );
        }
      }
    } catch(Exception e) {
      log.fatal("fatal error when get resource URL for"+name+":"+e.getMessage());
      e.printStackTrace();
      return null;
    }

  }

  public Boolean getUsingJQueryExt() {
    return usingJQueryExt;
  }

  public void setUsingJQueryExt(Boolean usingJQueryExt) {
    this.usingJQueryExt = usingJQueryExt;
  }

  public String getExtConfigPath() {
    return getResourceURLFromValueBinding("extConfigPath");
  }

  public void setExtConfigPath(String extConfigPath) {
    this.extConfigPath = extConfigPath;
  }

  public String getConfigProfile() {
    return (String) getValueBindingWithDefaultValue("configProfile",DEFAULT_CONFIG_PROFILE);
  }

  public void setConfigProfile(String configProfile) {
    this.configProfile = configProfile;
  }

  @Override
  public void decode(FacesContext context) {
    Map map = context.getExternalContext().getRequestParameterMap();
    String clientId = getClientId(context);
    if (map.containsKey(getId())) {
      String newValue = map.get(getId()).toString().replace('\n', ' ');//将value中的回车替换为空格
      setSubmittedValue(newValue);
    }
    super.decode(context);
  }

  @Override
  public Object saveState(FacesContext context) {
    Object[] values=new Object[6];
    values[0]=super.saveState(context);
    values[1]=width;
    values[2]=height;
    values[3]=skin;
    values[4]=items;
    values[5]=disabled;
    return values;
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    Object[] values= (Object[]) state;
    super.restoreState(context, values[0]);
    width= (String) values[1];
    height= (String) values[2];
    skin= (String) values[3];
    items= (String) values[4];
    disabled= (Boolean) values[5];
  }

  @Override
  public void validate(FacesContext context) {
    //TODO:required value always is false.
    // there is a possible reason that kindeditor didn't apply onbur event(htmlTextarea could apply).
    // so required value couldn't be submitted with partial-submit way before form was submitted.
    log.debug("validate...isRequired="+isRequired());
    super.validate(context);
  }
}

class KindEditorJarResource extends JarResource{
  public KindEditorJarResource(String path) {
    super(path);
  }

  /**
   * 这很重要，因为JarResource默认会将path作为资源下载时option的文件名取值
   * @param options
   * @throws IOException
   */
  @Override
  public void withOptions(Options options) throws IOException {
  }
}
