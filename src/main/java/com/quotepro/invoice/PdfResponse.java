package com.quotepro.invoice;

public class PdfResponse {
	private String tailPdfBase64;
	private String fabPdfBase64;
	private String tailFileName;
	private String fabFileName;

	public String getTailPdfBase64() {
		return tailPdfBase64;
	}

	public void setTailPdfBase64(String tailPdfBase64) {
		this.tailPdfBase64 = tailPdfBase64;
	}

	public String getFabPdfBase64() {
		return fabPdfBase64;
	}

	public void setFabPdfBase64(String fabPdfBase64) {
		this.fabPdfBase64 = fabPdfBase64;
	}

	public String getTailFileName() {
		return tailFileName;
	}

	public void setTailFileName(String tailFileName) {
		this.tailFileName = tailFileName;
	}

	public String getFabFileName() {
		return fabFileName;
	}

	public void setFabFileName(String fabFileName) {
		this.fabFileName = fabFileName;
	}
}
