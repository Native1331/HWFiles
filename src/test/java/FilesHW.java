import com.codeborne.pdftest.PDF;
import com.codeborne.pdftest.matchers.ContainsText;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.hamcrest.MatcherAssert.assertThat;

public class HWFiles {
    ClassLoader cl = HWFiles.class.getClassLoader();


    @Test
    void zipParsingTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/zip/Files.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("zipFileName"));
        ZipEntry entry;
        try (InputStream inputStream = cl.getResourceAsStream("zipFileName");
             ZipInputStream iz = new ZipInputStream(is)) {
               while ((entry = is.getNextEntry()) != null) {
                {
                    if (entry.getName().equals("pdfFileName")) {
                        PDF pdf = new PDF(iz);
                        Assertions.assertEquals(1, pdf.numberOfPages);
                        assertThat(pdf, new ContainsText("Сценарии преддемо 15.03.2022"));

                    } else if (entry.getName().equals("xlsxFileName")) {
                        XLS xls = new XLS(iz);
                        String stringCellValue = xls.excel.getSheetAt(1).getRow(1).getCell(2).getStringCellValue();
                        org.assertj.core.api.Assertions.assertThat(stringCellValue).contains("A01");
                    } else if (entry.getName().equals("csvFileName")) {
                        CSVReader reader = new CSVReader(new InputStreamReader(iz));
                        List<String[]> content = reader.readAll();
                        org.assertj.core.api.Assertions.assertThat(content).contains(new String[]{"Мой csv файл!"});
                    }
                }
            }
        }

    }
}




