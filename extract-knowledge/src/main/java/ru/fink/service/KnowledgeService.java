package ru.fink.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import ru.fink.converter.SentToSyntaxTreeConverter;
import ru.fink.model.SyntaxTree;
import ru.fink.parser.seman.SemanParser;
import ru.fink.parser.seman.Sent;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class KnowledgeService {

    private final TemplateService templateService;

    public void extractKnowledge(byte[] byteArray) throws IOException {
        List<SyntaxTree> syntaxTrees = buildSyntaxTrees(byteArray);

    }

    private List<SyntaxTree> buildSyntaxTrees(byte[] byteArray) throws IOException {
        File tempFile = File.createTempFile("document", "");

        FileUtils.writeByteArrayToFile(tempFile, convertUtf8ToWindowsEncoding(byteArray));
        List<Sent> parse = SemanParser.parse(tempFile.getAbsolutePath());
        return CollectionUtils.emptyIfNull(parse).stream()
                .map(SentToSyntaxTreeConverter::convert)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private byte[] convertUtf8ToWindowsEncoding(byte[] byteArray) throws UnsupportedEncodingException {
        return new String(byteArray, StandardCharsets.UTF_8).getBytes("windows-1251");
    }

}
