package net.dreamlu.easy.commons.utils;

import java.io.OutputStream;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码、条码生成处理类
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * @date 2014-3-28 5:11:43
 */
public class QrcodeUtils {
	
	/**
	 * 生成二维码
	 */
	public static void getQRcode(OutputStream out, String code, Integer width) {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.MARGIN, 1); // 二维码白边，默认是4 “1,2,3,4”
		getCode(out, code, BarcodeFormat.QR_CODE, width, width, hints);
	}
	
	/** 
	 * 生成一维码（code_128） 
	 */  
	public static void getBarcode(OutputStream out, String code, Integer width, Integer height) {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		getCode(out, code, BarcodeFormat.CODE_128, width, height, hints);
	}
	
	/**
	 * zxing生成条码
	 */
	private static void getCode(OutputStream out, String code, BarcodeFormat codeFormat,
			Integer width, Integer height, Hashtable<EncodeHintType, Object> hints) {
		if (width == null) width = 100;
		if (height == null) height = 50;
		// 文字编码  
		hints.put(EncodeHintType.CHARACTER_SET, Charsets.UTF_8);
		try {
			BitMatrix bmx = new MultiFormatWriter()
					.encode(code, codeFormat, width, height, hints);  
  
			MatrixToImageWriter.writeToStream(bmx, "JPEG", out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
