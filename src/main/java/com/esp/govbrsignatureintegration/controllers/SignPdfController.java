package com.esp.govbrsignatureintegration.controllers;

import com.esp.govbrsignatureintegration.services.AssinarPKCS7Service;
import com.esp.govbrsignatureintegration.services.GetCertificateService;
import com.esp.govbrsignatureintegration.services.GetTokenService;
import com.esp.govbrsignatureintegration.signature.CertificataManager;
import com.esp.govbrsignatureintegration.signature.SignatureManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
@RequestMapping("/assinar")
public class SignPdfController {
    private static final Logger logger = LoggerFactory.getLogger(SignPdfController.class);

    @Autowired
    private GetTokenService getTokenService;

    @Autowired
    private AssinarPKCS7Service assinarPKCS7Service;

    @Autowired
    private GetCertificateService getCertificateService;

    @Value("${govbr.imgESPLogo}")
    private String imgESPLogo;

    @Value("${govbr.imgQRCodeSource}")
    private String imgQRCodeSource;

    /**
     * Rota para assinar um documento PDF.
     *
     * @param code {@link String} que é passada na rota como variável
     * @param pdf  {@link MultipartFile} do arquivo
     * @return um arquivo pdf assinado.
     */
    @PostMapping(value = "/{code}", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> uploadFilesToSign(@PathVariable String code, @RequestParam MultipartFile pdf) {
        logger.info("uploadFilesToSign | Starting PDF signing process for code: {}", code);

        try {
            // Validação do arquivo
            if (pdf == null || pdf.isEmpty()) {
                logger.error("uploadFilesToSign | PDF file is null or empty");
                return ResponseEntity.badRequest().build();
            }

            String contentType = pdf.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                logger.error("uploadFilesToSign | Invalid file type: {}", contentType);
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
            }

            // Processamento
            String token = this.getTokenService.getToken(code);
            CertificataManager certificataManager = new CertificataManager(getCertificateService, token);
            String certificateCreatorName = certificataManager.getCertificateCreatorName();
            SignatureManager signatureManager = new SignatureManager(
                    token, this.assinarPKCS7Service, this.getCertificateService, this.imgESPLogo, this.imgQRCodeSource, certificateCreatorName
            );

            byte[] outputBytes = signatureManager.getBytesPdfSigned(pdf.getInputStream());
            String fileName = Objects.requireNonNullElse(pdf.getOriginalFilename(), "document").replace(".pdf", "");

            // Retorno
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s_assinado.pdf\"", fileName))
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(new ByteArrayInputStream(outputBytes)));
        } catch (Exception e) {
            logger.error("uploadFilesToSign | Error signing PDF: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Rota para assinar um documentos PDF em Lote.
     *
     * @param code {@link String} que é passada na rota como variável.
     * @param pdfs Array de {@link MultipartFile} dos arquivos.
     * @return Retorna um arquivo zip com os documentos assinados.
     */
    @PostMapping(value = "/lote/{code}", produces = MediaType.MULTIPART_MIXED_VALUE)
    public ResponseEntity<?> uploadFilesToSignInLote(@PathVariable String code, @RequestParam MultipartFile[] pdfs) {
        logger.info("uploadFilesToSignInLote | code: {}", code);

        try {
            // Validação dos arquivos
            if (pdfs == null || pdfs.length == 0) {
                logger.error("uploadFilesToSignInLote | No PDF files received");
                return ResponseEntity.badRequest().body("Nenhum arquivo PDF foi enviado.");
            }

            for (MultipartFile pdf : pdfs) {
                if (pdf == null || pdf.isEmpty()) {
                    logger.error("uploadFilesToSignInLote | One of the PDF files is null or empty");
                    return ResponseEntity.badRequest().body("Um dos arquivos PDF está vazio.");
                }

                String contentType = pdf.getContentType();
                if (contentType == null || !contentType.equals("application/pdf")) {
                    logger.error("uploadFilesToSignInLote | Invalid file type: {}", contentType);
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Arquivo inválido enviado: " + contentType);
                }
            }

            // Processamento dos PDFs
            String token = this.getTokenService.getToken(code);
            CertificataManager certificataManager = new CertificataManager(getCertificateService, token);
            String certificateCreatorName = certificataManager.getCertificateCreatorName();
            SignatureManager signatureManager = new SignatureManager(
                    token, this.assinarPKCS7Service, this.getCertificateService, this.imgESPLogo, this.imgQRCodeSource, certificateCreatorName
            );

            // Construindo a resposta multipart
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            for (MultipartFile pdf : pdfs) {
                byte[] outputBytes = signatureManager.getBytesPdfSigned(pdf.getInputStream());
                String fileName = Objects.requireNonNullElse(pdf.getOriginalFilename(), "document").replace(".pdf", "") + "_assinado.pdf";

                bodyBuilder.part(fileName, new ByteArrayResource(outputBytes))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .contentType(MediaType.APPLICATION_PDF);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.MULTIPART_MIXED)
                    .body(bodyBuilder.build());
        } catch (Exception e) {
            logger.error("uploadFilesToSignInLote | Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar os PDFs.");
        }
    }


}
