/**
 * @Description:
 * @Author:zls
 * @Date:2021年10月25日下午5:27:58
 **/
package nc.pubitf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import nc.bs.logging.Logger;

/**
 * @author zls
 * 
 */
public class RequestUtil {

	public static String readAsChars(HttpServletRequest request) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Logger.error(e.getMessage());
		} finally {
			try {
				if (null != br) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Logger.error("readAsChars 读取后的参数：" + sb.toString());
		return sb.toString();
	}

	public static void response(PrintWriter out, Result result) {
		out.println(JSONObject.toJSON(result).toString());
		out.flush();
		out.close();
	}
}
