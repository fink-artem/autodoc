package ru.fink.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static byte[] zipFile(List<byte[]> files) throws IOException {

        if (CollectionUtils.isEmpty(files)) {
            return null;
        }

        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (int i = 0; i < files.size(); i++) {
            byte[] file = files.get(i);
            InputStream fis = new ByteArrayInputStream(file);
            ZipEntry zipEntry = new ZipEntry("document" + i + ".docx");
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
        return fos.toByteArray();
    }
}
