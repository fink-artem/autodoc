package ru.fink.parser.seman;

import org.apache.commons.lang3.EnumUtils;
import ru.fink.parser.syntax.SyntaxRel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SemanParser {

    private static final String RELATIONS = "Relations:";
    private static final String LANGUAGE = "Russian";
    private static final String ENCODING = "WINDOWS-1251";
    public static final String NODE = "Node";

    public static List<Sent> parse(String input) throws IOException {
        String rml = System.getenv("RML");
        if (rml == null) {
            return null;
        }

        File sentFile = File.createTempFile("sent", "");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(rml + "/Bin/GraphmatThick.exe", LANGUAGE,
                    input, "-sents", sentFile.getAbsolutePath());
            Process process = processBuilder.start();
            process.waitFor();
            List<Sent> sentList = new ArrayList<>();
            try (Scanner read = new Scanner(sentFile, ENCODING)) {
                int counter = 0;
                while (read.hasNext()) {
                    File temp = File.createTempFile("temp", "");
                    try (PrintWriter out = new PrintWriter(temp, ENCODING)) {
                        String text = read.nextLine();
                        out.println(text);
                        counter++;
                        System.out.println(counter + " " + text);
                    }
                    File semanFile = File.createTempFile("seman", "");
                    processBuilder = new ProcessBuilder(rml + "/Bin/TestSeman.exe");
                    processBuilder.redirectInput(temp);
                    processBuilder.redirectOutput(semanFile);
                    process = processBuilder.start();
                    process.waitFor();

                    Sent sent = new Sent();
                    try (Scanner reader = new Scanner(semanFile, ENCODING)) {
                        reader.nextLine();
                        reader.nextLine();
                        while (true) {
                            String line = reader.nextLine();
                            if (line.equals(RELATIONS)) {
                                break;
                            }
                            parseLineAndPut(line, sent.getNodes());
                        }
                        while (reader.hasNextLine()) {
                            String line = reader.nextLine();
                            Link link = parseLink(line);
                            if (link == null) {
                                break;
                            }
                            sent.getLinkList().add(link);
                        }

                    } catch (FileNotFoundException | NoSuchElementException ex) {
                        ex.printStackTrace();
                    }
                    //processingLink(linkList, nodeList);
                    sentList.add(sent);
                }
            }
            return sentList;
        } catch (InterruptedException | IOException ex) {
            return null;
        }
    }

    private static void parseLineAndPut(String line, Map<Integer, Node> nodes) {
        int search = line.lastIndexOf(":") + 2;
        int searchSpace = line.indexOf(" ", search);
        Node node = new Node();
        node.name = line.substring(search, searchSpace).trim();

        if (node.name.equals("")) {
            search -= 2;
            searchSpace = line.lastIndexOf(" ", search);
            node.name = line.substring(searchSpace, search).trim();
        }

        int index = extractNodeNumber(line);

        search = line.lastIndexOf("->") + 3;
        searchSpace = line.indexOf(" ", search);
        node.speechPart = EnumUtils.getEnum(SpeechPart.class, line.substring(search, searchSpace));
        if (node.speechPart == null) {
            node.speechPart = SpeechPart.UNDEFINED;
        }

        nodes.put(index, node);
    }

    private static int extractNodeNumber(String line) {
        int positionOfNode = line.indexOf(NODE);
        if (positionOfNode < 0) {
            System.out.println("Неверный формат");
            return -1;
        }
        int searchSpace = line.indexOf(" ", positionOfNode + NODE.length() + 1);
        return Integer.parseInt(line.substring(positionOfNode + NODE.length() + 1, searchSpace));
    }

    private static Link parseLink(String line) {
        int searchOpen = line.lastIndexOf("(");
        int searchClose = line.lastIndexOf(")");
        int searchComma = line.indexOf(",", searchOpen);
        int searchEquals = line.lastIndexOf("=", searchOpen);
        try {
            Link link = new Link();
            link.setSemanType(SemanRel.convert((new Scanner(line)).next()));
            link.setSynanType(SyntaxRel.convert(line.substring(searchEquals + 2, searchOpen - 1).trim().toUpperCase()));
            if (link.getSemanType() == SemanRel.BELNG || link.getSemanType() == SemanRel.TYPE_OF) {
                link.setFirstNodeNumber(Integer.parseInt(line.substring(searchComma + 2, searchClose)));
                link.setSecondNodeNumber(Integer.parseInt(line.substring(searchOpen + 1, searchComma)));
            } else {
                link.setFirstNodeNumber(Integer.parseInt(line.substring(searchOpen + 1, searchComma)));
                link.setSecondNodeNumber(Integer.parseInt(line.substring(searchComma + 2, searchClose)));
            }
            return link;
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }

    private static void processingLink(List<Link> linkList, List<Node> nodeList) {
        int size = nodeList.size();
        for (int i = 0; i < size; i++) {
            if (nodeList.get(i).name.equals("И")) {
                int size2 = linkList.size();
                for (int j = 0; j < size2; j++) {
                    if (linkList.get(j).getFirstNodeNumber() == i) {
                        for (Link link : linkList) {
                            if (link.getSecondNodeNumber() == i) {
                                link.setSecondNodeNumber(linkList.get(j).getSecondNodeNumber());
                                link.setSemanType(linkList.get(j).getSemanType());
                            }
                        }
                        linkList.remove(j);
                        j--;
                        size2--;
                    }
                }
            }
        }
    }
}
