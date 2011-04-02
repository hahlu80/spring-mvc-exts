package com.github.ryoasai.springmvc.jxls;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

public interface DownloadFilenameEncoder {

	String encode(HttpServletRequest request, String filename) throws UnsupportedEncodingException;
}
