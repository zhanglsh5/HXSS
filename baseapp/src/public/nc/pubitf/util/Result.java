/**
 * @Description:
 * @Author:zls
 * @Date:2021年10月26日上午11:00:17
 **/
package nc.pubitf.util;

import java.io.Serializable;

/**
 * @author zls
 * 
 */
public class Result implements Serializable {

	private static final long serialVersionUID = -4765159947915798161L;

	// “ACK”代表操作成功，“NACK”代表操作失败
	private String code;
	// 显示操作失败的原因
	private String message;

	// 操作失败的数据
	private String data;

	public Result(String code, String message, String data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public Result(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public static Result ok(String message) {

		return new Result("ACK", message);
	}

	public static Result error(String message, String data) {

		return new Result("NACK", message, data);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
