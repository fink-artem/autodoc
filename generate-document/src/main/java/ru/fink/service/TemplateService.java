package ru.fink.service;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TemplateService {

    private static final String SEPARATOR = "->";
    private static final Pattern regex = Pattern.compile("\\$\\{(.*?)\\}");

    private final OntologyService ontologyService;

    @SneakyThrows
    public List<byte[]> generate(byte[] bytes) {
        Set<String> keys;
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            XWPFDocument document = new XWPFDocument(OPCPackage.open(inputStream));
            keys = getAllKeys(document);
        }
        Pair<Set<String>, Set<String>> separatedKeys = separateKeys(keys);
        Map<String, Object> objects = ontologyService.getObjectsByClasses(separatedKeys.getFirst());


        Map.Entry<String, Object> next = objects.entrySet().iterator().next();
        String key = next.getKey();
        List<String> value = (List<String>)next.getValue();

        return value.stream().map(el -> {
            Map<String, Object> values = resolveTriplets(key, el, separatedKeys.getSecond());

            try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
                return fillDocument(values, inputStream);
            }
        }).collect(Collectors.toList());

    }

    private Map<String, Object> resolveTriplets(String key, String value, Set<String> second) {
        final Map<String, Object> map = Collections.singletonMap(key, value);
        second.forEach(el -> {
            String[] split = el.split(SEPARATOR);
            String s = ontologyService.resolveTriplet(map.get(split[0]).toString(), split[1]);
            map.put(el, s);
            if (split.length > 2) {
                map.put(split[2], s);
            }
        });
        return map;
    }

    private Pair<Set<String>, Set<String>> separateKeys(Set<String> keys) {
        Set<String> firstSet = new HashSet<>();
        Set<String> secondSet = new HashSet<>();
        keys.forEach(key -> {
            if (key.contains(SEPARATOR)) {
                secondSet.add(key);
            } else {
                firstSet.add(key);
            }
        });
        return new Pair<>(firstSet, secondSet);
    }

    private byte[] fillDocument(Map<String, Object> values, InputStream inputStream) throws Exception {
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputStream, TemplateEngineKind.Freemarker);

        IContext context = report.createContext();
        context.putMap(values);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        report.process(context, out);
        return out.toByteArray();
    }

    private Set<String> getAllKeys(XWPFDocument document) {
        Set<String> result = new HashSet<>();
        document.getParagraphs().stream()
                .map(XWPFParagraph::getText)
                .map(regex::matcher)
                .forEach(matcher -> {
                    while (matcher.find()) {
                        result.add(matcher.group(1).trim());
                    }
                });
        return result;
    }


}
