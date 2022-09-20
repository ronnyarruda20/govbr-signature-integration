package com.esp.govbrsignatureintegration.signature;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.signatures.IExternalSignatureContainer;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;

import java.io.*;
import java.security.GeneralSecurityException;

public class SignatureContainer implements IExternalSignatureContainer {

    private byte[] pkcs7;

    public SignatureContainer(byte[] pkcs7) {
        this.pkcs7 = pkcs7;
    }

    @Override
    public byte[] sign(InputStream data) {
        try {
            data.read(this.pkcs7);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this.pkcs7;
    }

    @Override
    public void modifySigningDictionary(PdfDictionary pdfDictionary) {
        pdfDictionary.put(PdfName.Filter, PdfName.Adobe_PPKLite);
        pdfDictionary.put(PdfName.SubFilter, PdfName.Adbe_pkcs7_detached);
    }
}
