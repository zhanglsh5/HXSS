package nc.itf.psndoc.util;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import nc.bs.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParamUtils {

	private static Map<String, String> map = null;

	/**
	 * Object ת�� �ַ���
	 * 
	 * @param obj
	 * @return
	 */
	public static String toStr(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();

	}

	/**
	 * ��ȡ���������ļ���Ϣ
	 * 
	 * @return Properties
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> getParameters() {

		Properties props = new Properties();
		try {
			InputStream in = ParamUtils.class.getResourceAsStream("parameter.properties");
			props.load(in);
			map = new HashMap<String, String>((Map) props);

		} catch (FileNotFoundException e) {
			nc.bs.logging.Logger.error("�Ҳ������������ļ���" + e);
		} catch (IOException e) {
			nc.bs.logging.Logger.error("д�������Ϣʧ�ܣ�" + e);

		}
		return map;
	}

	public static String getParameter(String key) {
		if (map == null) {
			getParameters();
		}
		return map.get(key);
	}

	/**
	 * ����XML���õ����ڵ�
	 * 
	 * @param xml
	 * @return
	 */
	public static Element getRootElement(String xml) {
		SAXReader saxreader = new SAXReader();
		Document document = null;
		try {
			document = saxreader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			Element root = document.getRootElement();// ���ڵ�
			return root;
		} catch (UnsupportedEncodingException e) {
			nc.bs.logging.Logger.error("����XML�����쳣��" + e);
		} catch (DocumentException e) {
			nc.bs.logging.Logger.error("����XML�����쳣��" + e);
		}

		return null;

	}

	// yyyyMMddHHmmss ת���� yyyy-MM-dd HH:mm:ss
	public static String formatDate(String date) {
		String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
		date = date.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
		return date;
	}

	public static String getPubcerdirPath() {
		try {
			String path = URLDecoder.decode(new URL(ParamUtils.class.getClassLoader().getResource("").toString()).getPath(), "UTF-8");
			Logger.info("getPubcerdirPath��" + path);
			return path;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(ParamUtils.getPubcerdirPath());
	}

}
