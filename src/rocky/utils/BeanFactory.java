package rocky.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BeanFactory {

	public static Object getBean(String id) {
		
		try {
			// 生产对象 将每个bean对象生产的细节配置到配置文件中
			SAXReader reader = new SAXReader();
			String path = BeanFactory.class.getClassLoader().getResource("bean.xml").getPath();
			Document doc = reader.read(path);
			Element element = (Element) doc.selectSingleNode("//bean[@id='"+id+"']");
			String className = element.attributeValue("class");
			// 利用反射创建对象
			Class clzz = Class.forName(className);
			// 创建对象
			Object object = clzz.newInstance();
			return object;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
