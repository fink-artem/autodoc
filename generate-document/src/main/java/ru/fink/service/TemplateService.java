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
import ru.fink.utils.ZipUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TemplateService {

    private static final String REGEX_SEPARATOR = "__";
    private static final String SEPARATOR = "__";
    private static final Pattern regex = Pattern.compile("\\$\\{(.*?)}");

    private final ClientOntologyService clientOntologyService;

    @SneakyThrows
    public byte[] generate(byte[] bytes) {
        Set<String> keys;
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            XWPFDocument document = new XWPFDocument(OPCPackage.open(inputStream));
            keys = getAllKeys(document);
        }
        Pair<Set<String>, Set<String>> separatedKeys = separateKeys(keys);
        Map<String, List<String>> objects = clientOntologyService.getObjectsByClasses(separatedKeys.getFirst());


        String key = null;
        List<String> value = null;
        Map<String, String> singleObject = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : objects.entrySet()) {
            if (entry.getValue().size() == 1) {
                singleObject.put(entry.getKey(), entry.getValue().get(0));
            } else if (entry.getValue().size() > 1) {

                if (key != null || value != null) {
                    throw new UnsupportedOperationException("Не поддерживается");
                }

                key = entry.getKey();
                value = entry.getValue();
            }
        }

        if (key == null) {
            Map<String, String> values = new HashMap<>(singleObject);

            resolveTriplets(values, separatedKeys.getSecond());

            try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
                return ZipUtils.zipFile(Collections.singletonList(fillDocument(values, inputStream)));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        String finalKey = key;
        return ZipUtils.zipFile(value.stream()
                .map(el -> {
                    Map<String, String> values = new HashMap<>();
                    values.put(finalKey, el);
                    values.putAll(singleObject);

                    resolveTriplets(values, separatedKeys.getSecond());

                    try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
                        return fillDocument(values, inputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

    }

    private void resolveTriplets(Map<String, String> map, Set<String> second) {

        Set<String> secondAttempt = new HashSet<>();
        second.forEach(el -> {
            String[] split = el.split(REGEX_SEPARATOR);
            if (map.containsKey(split[0].trim())) {
                String s = clientOntologyService.resolveTriplet(map.get(split[0].trim()), split[1].trim()).trim();
                map.put(el, s);
                if (split.length > 2) {
                    map.put(split[2], s);
                }
            } else {
                secondAttempt.add(el);
            }
        });

        if (secondAttempt.size() < second.size()) {
            resolveTriplets(map, secondAttempt);
        }

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

    private byte[] fillDocument(Map<String, String> values, InputStream inputStream) throws Exception {
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(inputStream, TemplateEngineKind.Freemarker);

        IContext context = report.createContext();
        context.putMap(new HashMap<>(values));

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
