package com.github.ryoasai.springmvc.jxls;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import com.github.ryoasai.springmvc.Controllers;

public class JxlsView extends AbstractUrlBasedView {

	/** The content type for an Excel response */
	private static final String CONTENT_TYPE = "application/vnd.ms-excel";

	/** The extension to look for existing templates */
	private static final String EXTENSION = ".xls";

	private DownloadFilenameEncoder downloadFilenameEncoder = new DefaultDownloadFilenameEncoder();
	
	/**
	 * Default Constructor. Sets the content type of the view to
	 * "application/vnd.ms-excel".
	 */
	public JxlsView() {
		setContentType(CONTENT_TYPE);
	}

	public void setDownloadFilenameEncoder(
			DownloadFilenameEncoder downloadFilenameEncoder) {
		this.downloadFilenameEncoder = downloadFilenameEncoder;
	}

	
	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	/**
	 * Renders the Excel view, given the specified model.
	 */
	@Override
	protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HSSFWorkbook workbook = createWorkbook(request);

		buildExcelDocument(model, workbook, request, response);

		setupHeader(model, request, response);
		
		doRender(response, workbook);
	}

	protected void setupHeader(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {

		// Set the content type.
		response.setContentType(getContentType());

		setupHeaderForDownloadFilename(model, request, response);
	}

	protected void setupHeaderForDownloadFilename(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		Jxls jxls = getJxslAnnotation();
		assert jxls != null; // This view is only applied when @Jxls is present.
		
		String filename = jxls.filename();
		if (model.containsKey(filename)) {
			filename = model.get(filename).toString();
		}
		
		response.setHeader("Content-Disposition", "attachment; filename=" + downloadFilenameEncoder.encode(request, filename));
	}

	private Jxls getJxslAnnotation() {
		Method currentHandlerMethod = Controllers.getCurrentHandlerMethod();
		assert currentHandlerMethod != null;
		
		return currentHandlerMethod.getAnnotation(Jxls.class);
	}
	
	private HSSFWorkbook createWorkbook(HttpServletRequest request) throws Exception {
		HSSFWorkbook workbook;
		if (getUrl() != null) {
			workbook = getTemplateSource(getUrl(), request);
		} else {
			workbook = new HSSFWorkbook();
			logger.debug("Created Excel Workbook from scratch");
		}
		return workbook;
	}

	private void doRender(HttpServletResponse response, HSSFWorkbook workbook) throws IOException {

		// Flush byte array to servlet output stream.
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}

	/**
	 * Creates the workbook from an existing XLS document.
	 * 
	 * @param url
	 *            the URL of the Excel template without localization part nor
	 *            extension
	 * @param request
	 *            current HTTP request
	 * @return the HSSFWorkbook
	 * @throws Exception
	 *             in case of failure
	 */
	protected HSSFWorkbook getTemplateSource(String url, HttpServletRequest request) throws Exception {

		LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
		Locale userLocale = RequestContextUtils.getLocale(request);
		Resource inputFile = helper.findLocalizedResource(url, url.endsWith(EXTENSION) ? "" : EXTENSION, userLocale);

		// Create the Excel document from the source.
		if (logger.isDebugEnabled()) {
			logger.debug("Loading Excel workbook from " + inputFile);
		}

		POIFSFileSystem fs = new POIFSFileSystem(inputFile.getInputStream());
		return new HSSFWorkbook(fs);
	}

	/**
	 * Build excel document using jXML template and the given model.
	 * 
	 * @param model
	 *            the model Map
	 * @param workbook
	 *            the Excel workbook to complete
	 * @param request
	 *            in case we need locale etc. Shouldn't look at attributes.
	 * @param response
	 *            in case we need to set cookies. Shouldn't write to it.
	 */
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		XLSTransformer transformer = new XLSTransformer();
		transformer.transformWorkbook(workbook, model);
	}

}
