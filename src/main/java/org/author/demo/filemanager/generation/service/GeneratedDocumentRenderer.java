package org.author.demo.filemanager.generation.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openpdf.text.Document;
import org.openpdf.text.Font;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class GeneratedDocumentRenderer {
    protected static final int TITLE_FONT_SIZE = 14;
    protected static final boolean BOLD = true;
    protected static final String FAILED_TO_RENDER_DOCX_MESSAGE = "Failed to render DOCX";
    protected static final int BODY_FONT_SIZE = 12;

    public byte[] renderDocx(String text, String title) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            if (isValidString(title)) {
                XWPFParagraph titleParagraph = document.createParagraph();
                XWPFRun titleRun = titleParagraph.createRun();

                titleRun.setBold(BOLD);
                titleRun.setFontSize(TITLE_FONT_SIZE);
                titleRun.setText(title);
            }

            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();

            run.setText(getValidText(text));
            document.write(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(FAILED_TO_RENDER_DOCX_MESSAGE, e);
        }

    }

    public byte[] renderPdf(String text, String title) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            int family = Font.HELVETICA;

            PdfWriter.getInstance(document, outputStream);
            document.open();

            if (isValidString(title)) {
                Font titleFont = new Font(family, TITLE_FONT_SIZE, Font.BOLD);
                document.add(new Paragraph(title, titleFont));
            }

            Font bodyFont = new Font(family, BODY_FONT_SIZE, Font.NORMAL);
            Paragraph paragraph = new Paragraph(getValidText(text), bodyFont);

            document.add(paragraph);
            document.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidString(String text) {
        return text != null && !text.isBlank();
    }

    private String getValidText(String text) {
        return isValidString(text) ? text : "";
    }
}
