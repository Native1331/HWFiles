import com.codeborne.pdftest.PDF;
import com.codeborne.pdftest.matchers.ContainsText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


import static org.hamcrest.MatcherAssert.assertThat;

public class FilesHW {
    private ClassLoader cl = FilesHW.class.getClassLoader();



    @Test
    void zipParsingTest() throws Exception {

        ZipFile zf = new ZipFile(new File(Files.ZIP_FILE));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream(""));
        ZipEntry entry;
        try (InputStream inputStream = cl.getResourceAsStream("Files.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            while ((entry = is.getNextEntry()) != null) {
                {
                    checkEntry(entry, zis);
                }
            }
        }

    }

    private void checkEntry(ZipEntry entry, ZipInputStream zis) throws Exception{
        if(Files.PDF_FILE.equals(entry.getName())) {
            PDF pdf = new PDF(zis);
            Assertions.assertEquals(1, pdf.numberOfPages);
            assertThat(pdf, new ContainsText("Сценарии преддемо 15.03.2022"));
            System.out.println("Passed");
        } else if(Files.XLSX_FILE.equals(entry.getName())) {
            XLS xls = new XLS(zis);
            String stringCellValue = xls.excel.getSheetAt(1).getRow(1).getCell(2).getStringCellValue();
            org.assertj.core.api.Assertions.assertThat(stringCellValue).contains("A01");
            System.out.println("Passed");
        } else if(Files.CSV_FILE.equals(entry.getName())) {
            CSVReader reader = new CSVReader(new InputStreamReader(zis));
            List<String[]> content = reader.readAll();
            org.assertj.core.api.Assertions.assertThat(content).contains(new String[]{"Мой csv файл!"});
            System.out.println("Passed");
        }
    }
}