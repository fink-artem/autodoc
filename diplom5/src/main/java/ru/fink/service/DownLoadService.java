package ru.fink.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fink.config.DownloadConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Service
public class DownLoadService {

    private final DownloadConfig downloadConfig;

    @Autowired
    public DownLoadService(DownloadConfig downloadConfig) {
        this.downloadConfig = downloadConfig;
    }

    public void download() throws IOException {
        URL url = new URL(downloadConfig.getUrl());
        StringBuilder site = new StringBuilder();
        try (InputStream is = url.openStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                site.append(line);
            }
        }
        Pattern p = Pattern.compile("<div\\b[^>]*class=\"item-page\"[^>]*>([\\s\\S]*?)</div>");
        Matcher m = p.matcher(site);

        if (!m.find()) {
            log.warn("tag div not found");
            return;
        }
        String div = site.substring(m.start(), m.end());
        p = Pattern.compile("href=\"(.*?)\"");
        m = p.matcher(div);
        while (m.find()) {
            String fileUrl = div.substring(m.start(1), m.end(1));
            String fileUrlDecoded = URLDecoder.decode(fileUrl, UTF_8.toString());
            String fileName = fileUrlDecoded.substring(fileUrlDecoded.lastIndexOf("/") + 1);
            FileUtils.copyURLToFile(new URL("http://fit.nsu.ru" + fileUrl),
                    new File(downloadConfig.getDir() + "/" + fileName));
        }

    }

}
