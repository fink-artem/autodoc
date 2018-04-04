package ru.fink.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.fink.service.DownLoadService;

import java.io.IOException;

@Slf4j
@Component
public class DownloadJob {

    private final DownLoadService downLoadService;

    @Autowired
    public DownloadJob(DownLoadService downLoadService) {
        this.downLoadService = downLoadService;
    }

    @Scheduled(fixedDelay = 60_000)
    public void download() throws IOException {
        log.info("Start download");
        downLoadService.download();
        log.info("End download");
    }
}
