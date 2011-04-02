package com.github.ryoasai.springmvc.jxls;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.sf.jxls.reader.ReaderBuilder;
import net.sf.jxls.reader.XLSReadStatus;
import net.sf.jxls.reader.XLSReader;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.Resource;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

public class JxlsFileReader {

	public XLSReadStatus read(Resource templateFile, MultipartFile file,
			Model model) throws InvalidFormatException, IOException, SAXException {
		
		return read(templateFile, file, model.asMap());
	}

		
	public XLSReadStatus read(Resource templateFile, MultipartFile file,
			Map<?, ?> model) throws InvalidFormatException, IOException, SAXException {

		InputStream inputXML = null;
		InputStream inputXLS = null;

		try {
			inputXML = new BufferedInputStream(templateFile.getInputStream());
			XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);

			inputXLS = new BufferedInputStream(file.getInputStream());

			return mainReader.read(inputXLS, model);

		} finally {
			IOUtils.closeQuietly(inputXML);
			IOUtils.closeQuietly(inputXLS);
		}
	}

}
