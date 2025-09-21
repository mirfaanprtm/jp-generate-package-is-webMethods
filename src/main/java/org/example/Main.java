package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static org.example.Utils.*;
import static org.example.Utils.addNodeIdfFile;
import static org.example.Utils.zipDirectory;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
//        Scanner sc = new Scanner(System.in);
//        System.out.print("Insert service name: ");
//        String serviceName = sc.nextLine();
//        System.out.print("Insert doc req name: ");
//        String docNameReq = sc.nextLine();
//        System.out.print("Insert doc res name: ");
//        String docNameRes = sc.nextLine();
//        System.out.print("Insert bits req to convert: ");
//        String bitNumberInputReq = sc.nextLine();
//        System.out.print("Insert bits res to convert: ");
//        String bitNumberInputRes = sc.nextLine();

        // CHECK INPUT FROM JENKINS
        String serviceName;
        String docNameReq;
        String docNameRes;
        String bitNumberInputReq;
        String bitNumberInputRes;
        if (args.length > 0) {
            serviceName = args[0];
            docNameReq = args[1];
            docNameRes = args[2];
            bitNumberInputReq = args[3];
            bitNumberInputRes = args[4];
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.print("Insert service name: ");
            serviceName = sc.nextLine();
            System.out.print("Insert doc req name: ");
            docNameReq = sc.nextLine();
            System.out.print("Insert doc res name: ");
            docNameRes = sc.nextLine();
            System.out.print("Insert bits req to convert: ");
            bitNumberInputReq = sc.nextLine();
            System.out.print("Insert bits res to convert: ");
            bitNumberInputRes = sc.nextLine();
            sc.close();
        }

        // VALIDASI INPUT
        if (serviceName.isEmpty() || docNameReq.isEmpty() || docNameRes.isEmpty() || bitNumberInputReq.isEmpty() || bitNumberInputRes.isEmpty()) {
            throw new IllegalArgumentException("Input can't be empty");
        }

        // VALIDASI INPUT SERVICE NAME HARUS DIAWALI "MDR"
        if (!serviceName.startsWith("Mdr")) {
            throw new IllegalArgumentException("❌ Service name harus diawali dengan 'Mdr'");
        }

        // VALIDASI TIDAK BOLEH ADA KARAKTER SPECIAL
        if (!serviceName.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("❌ Service name tidak boleh mengandung karakter spesial (hanya huruf dan angka)");
        }


        // CONVERT TO JSON/REST
        List<String> rawBitNumbersReq = Arrays.asList(bitNumberInputReq.split(","));
        List<String> rawBitNumbersRes = Arrays.asList(bitNumberInputRes.split(","));

//        List<String> errorMessagesReq = new ArrayList<>();
//        List<String> errorMessagesRes = new ArrayList<>();

        // PATERN INPUT PARAMATER
        Pattern patternReq = Pattern.compile("^bit_\\d+$", Pattern.CASE_INSENSITIVE);
        Pattern patternRes = Pattern.compile("^bit_\\d+$", Pattern.CASE_INSENSITIVE);

        // VALIDASI BIT
        List<ISO8583> isoElements = Data.iso8583Element();
        for (String bitReq : rawBitNumbersReq) {
            String trimmedBitReq = bitReq.trim();
            if (!patternReq.matcher(trimmedBitReq).matches()) {
//                errorMessagesReq.add("Your pattern is incorrect: " + trimmedBitReq);
                throw new IllegalArgumentException("Your pattern is incorrect: " + trimmedBitReq);
            } else if (isoElements.stream().noneMatch(e -> e.getBitNumber().equalsIgnoreCase(trimmedBitReq))) {
//                errorMessagesReq.add(trimmedBitReq + " not registered");
                throw new IllegalArgumentException(trimmedBitReq + " not registered");
            }
        }

        for (String bitRes : rawBitNumbersRes) {
            String trimmedBitRes = bitRes.trim();
            if (!patternRes.matcher(trimmedBitRes).matches()) {
//                errorMessagesRes.add("Your pattern is incorrect: " + trimmedBitRes);
                throw  new IllegalArgumentException("Your pattern is incorrect: " + trimmedBitRes);
            } else if (isoElements.stream().noneMatch(e -> e.getBitNumber().equalsIgnoreCase(trimmedBitRes))) {
//                errorMessagesRes.add(trimmedBitRes + " not registered");
                throw  new IllegalArgumentException(trimmedBitRes + " not registered");
            }
        }

        // VALIDASI ERROR MESSAGE
//        if (!errorMessagesReq.isEmpty()) {
//            errorMessagesReq.forEach(System.out::println);
//            return;
//        }
//
//        if (!errorMessagesRes.isEmpty()) {
//            errorMessagesRes.forEach(System.out::println);
//            return;
//        }

        // FILTER JSON OUTPUT TO LOWER CASE DAN TRIM SPASI KOSONG
        Map<String, String> jsonOutputReq = new LinkedHashMap<>();
        Map<String, String> jsonOutputRes = new LinkedHashMap<>();

        List<String> lowercaseInputBitsReq = rawBitNumbersReq.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        List<String> lowercaseInputBitsRes = rawBitNumbersRes.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        // VALIDASI REGEX PARAMETER REQUEST DAN RESPONSE
        for (ISO8583 elementReq : isoElements) {
            if (lowercaseInputBitsReq.contains(elementReq.getBitNumber())) {
                String keyReq = elementReq.getDescription().replaceAll("[^a-zA-Z0-9]+", " ").trim();
                String camelCaseKeyReq = keyReq.substring(0, 1).toLowerCase() + keyReq.substring(1).replaceAll("\\s(.)", "$1".toUpperCase());
                jsonOutputReq.put(camelCaseKeyReq, "");
            }
        }

        for (ISO8583 elementRes : isoElements) {
            if (lowercaseInputBitsRes.contains(elementRes.getBitNumber())) {
                String keyRes = elementRes.getDescription().replaceAll("[^a-zA-Z0-9]+", " ").trim();
                String camelCaseKeyRes = keyRes.substring(0, 1).toLowerCase() + keyRes.substring(1).replaceAll("\\s(.)", "$1".toUpperCase());
                jsonOutputRes.put(camelCaseKeyRes, "");
            }
        }

        // MEMBUAT STRUKTUR MESSAGING JSON/REST
        StringBuilder jsonBuilderReq = new StringBuilder();
        jsonBuilderReq.append("{\n");

        List<String> entriesReq = new ArrayList<>();
        for (Map.Entry<String, String> entry : jsonOutputReq.entrySet()) {
            entriesReq.add("    \"" + entry.getKey() + "\": \"" + entry.getValue() + "\"");
        }

        jsonBuilderReq.append(String.join(",\n", entriesReq));
        jsonBuilderReq.append("\n}");

        StringBuilder jsonBuilderRes = new StringBuilder();
        jsonBuilderRes.append("{\n");

        List<String> entriesRes = new ArrayList<>();
        for (Map.Entry<String, String> entry : jsonOutputRes.entrySet()) {
            entriesRes.add("    \"" + entry.getKey() + "\": \"" + entry.getValue() + "\"");
        }

        jsonBuilderRes.append(String.join(",\n", entriesRes));
        jsonBuilderRes.append("\n}");

        System.out.println(jsonBuilderReq.toString());
        System.out.println(jsonBuilderRes.toString());


        // MAPPING OBJECT REQUEST dan response JSON ke XML
        ObjectMapper mapperReq = new ObjectMapper();
        ObjectMapper mapperRes = new ObjectMapper();
        Map<String, String> jsonMapReq = mapperReq.readValue(jsonBuilderReq.toString(), Map.class);
        Map<String, String> jsonMapRes = mapperRes.readValue(jsonBuilderRes.toString(), Map.class);

        String xmlTemplateHeaderReq = Template.templateHeaderReq(serviceName, docNameReq);
        StringBuilder xmlBuilderReq = new StringBuilder();
        xmlBuilderReq.append(xmlTemplateHeaderReq);

        String xmlTemplateHeaderRes = Template.templateHeaderRes(serviceName, docNameRes);
        StringBuilder xmlBuilderRes = new StringBuilder();
        xmlBuilderRes.append(xmlTemplateHeaderRes);

        String xmlDynamicTemplateReq = Template.dynamicTemplateReq();
        for (Map.Entry<String, String> entry : jsonMapReq.entrySet()) {
            String key = entry.getKey();
            String recordBlockReq = String.format(xmlDynamicTemplateReq, key);
            xmlBuilderReq.append(recordBlockReq);
        }

        String xmlDynamicTemplateRes = Template.dynamicTemplateRes();
        for (Map.Entry<String, String> entry : jsonMapRes.entrySet()) {
            String key = entry.getKey();
            String recordBlockRes = String.format(xmlDynamicTemplateRes, key);
            xmlBuilderRes.append(recordBlockRes);
        }

        String xmlTemplateFooterReq = Template.templateFooterReq();
        String xmlTemplateFooterRes = Template.templateFooterRes();
        xmlBuilderReq.append(xmlTemplateFooterReq);
        xmlBuilderRes.append(xmlTemplateFooterRes);

        System.out.println(xmlBuilderReq.toString());
        System.out.println(xmlBuilderRes.toString());

        String rootFolderName = serviceName;
        try {
            // MEMBUAT STRUKTUR FOLDER
            File rootDir = createFolders(rootFolderName);

            // MENAMBAHKAN FILE manifest.v3 DI FOLDER DOOR
            addManifestFile(rootDir);

            // MENAMBAHKAN FILE node.idf KE FOLDER YANG DITENTUKAN
            File nestedDir = new File(rootDir, "ns/" + rootFolderName);
            addNodeIdfFile(nestedDir);
            addNodeIdfFile(new File(nestedDir, "adapters"));
            addNodeIdfFile(new File(nestedDir, "documents"));
            addNodeIdfFile(new File(nestedDir, "os"));
            addNodeIdfFile(new File(nestedDir, "pr"));
            addNodeIdfFile(new File(nestedDir, "restful"));
            addNodeIdfFile(new File(nestedDir, "services"));
            addNodeIdfFile(new File(nestedDir, "utils"));
            addNodeIdfFile(new File(nestedDir, "wrapper"));
            addNodeIdfFile(new File(nestedDir, "ws"));

            // MENAMBAHKAN FILE DOCUMENTS
            File documentsDir= new File(nestedDir, "documents");
            createReqAndResFiles(documentsDir, docNameReq, docNameRes, xmlBuilderReq.toString(), xmlBuilderRes.toString());

            // MENAMBAHKAN TEMPLATE SERVICE
            File nestedDir2 = new File(rootDir, "ns/" + rootFolderName);
            addTemplateService(new File(nestedDir2, "os"));
            addTemplateService(new File(nestedDir2, "pr"));
            addTemplateService(new File(nestedDir2, "services"));
            addTemplateService(new File(nestedDir2, "wrapper"));

            // MENAMBAHKAN FOLDER convertToBitMap
            File sourceDir = new File("target/convertToBitMap");
            File destinationDir = new File(nestedDir, "utils");

            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }

            File finalDestination = new File(destinationDir, sourceDir.getName());
            copyFolder(sourceDir, finalDestination);


            // KOMPRES FOLDER MENJADI FILE ZIP
            zipDirectory(rootDir, rootFolderName + ".zip");

            System.out.println("Struktur folder berhasil dibuat dan dikompres menjadi " + rootFolderName + ".zip");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}