package rocky.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import rocky.domain.Category;
import rocky.domain.Product;
import rocky.service.AdminService;
import rocky.utils.BeanFactory;
import rocky.utils.CommonsUtils;

@SuppressWarnings("serial")
public class AdminAddProductServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 收集表单数据并封装
		Product product = new Product();
		// 收集数据
		Map<String, Object>map = new HashMap<String,Object>();
		try {
			// 创建磁盘文件工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 文件上上传核心对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解析request获得文件项集合
			List<FileItem> parseRequest = upload.parseRequest(request);
			for (FileItem fileItem : parseRequest) {
				// 判断是否是普通表单项
				boolean formField = fileItem.isFormField();
				if (formField) {
					// 普通表单项
					String fieldName = fileItem.getFieldName();
					String fieldValue = fileItem.getString("UTF-8");
					map.put(fieldName, fieldValue);

				}else {
					// 文件上传项
					String name = fileItem.getName();
					String path = this.getServletContext().getRealPath("upload");
					InputStream in = fileItem.getInputStream();
					OutputStream out = new FileOutputStream(path+"/"+name);
					IOUtils.copy(in, out);
					in.close();
					out.close();
					fileItem.delete();
					map.put("pimage", "upload/"+name);
				}
			}
			BeanUtils.populate(product, map);
			// 手动封装没有值得属性
			product.setPid(CommonsUtils.getUUID());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String ds = format.format(new Date());
			product.setPdate(ds);
			product.setPflag(0);
			AdminService service = (AdminService) BeanFactory.getBean("adminService");
			service.saveProduct(product);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 将图片存到服务器磁盘

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}