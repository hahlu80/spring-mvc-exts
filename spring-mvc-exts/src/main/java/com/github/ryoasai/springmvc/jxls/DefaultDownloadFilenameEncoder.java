package com.github.ryoasai.springmvc.jxls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class DefaultDownloadFilenameEncoder implements DownloadFilenameEncoder {

	private String encoding = "UTF-8";
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String encode(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
		if (request.getHeader("User-Agent").indexOf("MSIE") == -1) {
			return MimeUtility.encodeWord(filename, getEncoding(), "B");
		} else {
			// for legacy IE6
		    return URLEncoder.encode(filename, getEncoding());
		}
	}

}
