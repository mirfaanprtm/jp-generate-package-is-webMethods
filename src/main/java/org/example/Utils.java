package org.example;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {
    public static String toCamelCase(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        String[] parts = s.replaceAll("[^a-zA-Z0-9\\s]", " ").split("\\s+");
        String firstPart = parts[0].toLowerCase();

        String remainingParts = Arrays.stream(parts, 1, parts.length)
                .map(p -> p.substring(0, 1).toUpperCase() + p.substring(1).toLowerCase())
                .collect(Collectors.joining());
        return firstPart + remainingParts;
    }

    // MEMBUAT STUKTUR FOLDER
    public static File createFolders(String rootFolderName) {
        File rootDir = new File(rootFolderName);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
        new File(rootDir, "code").mkdir();
        new File(rootDir, "config").mkdir();
        new File(rootDir, "lib").mkdir();

        File nsDir = new File(rootDir, "ns");
        nsDir.mkdir();
        new File(rootDir, "pub").mkdir();
        new File(rootDir, "resources").mkdir();

        File nestedMdrDir = new File(nsDir, rootFolderName);
        nestedMdrDir.mkdir();
        new File(nestedMdrDir, "adapters").mkdir();
        new File(nestedMdrDir, "documents").mkdir();
        new File(nestedMdrDir, "os").mkdir();
        new File(nestedMdrDir, "pr").mkdir();
        new File(nestedMdrDir, "restful").mkdir();
        new File(nestedMdrDir, "services").mkdir();
        new File(nestedMdrDir, "utils").mkdir();
        new File(nestedMdrDir, "wrapper").mkdir();
        new File(nestedMdrDir, "ws").mkdir();

        return rootDir;
    }

    // MEMBUAT FILE manifest.v3 DAN MENAMBAHKAN KE FOLDER ROOT
    public static void addManifestFile(File rootDir) throws IOException {
        File manifestFile = new File(rootDir, "manifest.v3");
        try (PrintWriter writer = new PrintWriter(manifestFile)) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("");
            writer.println("<Values version=\"2.0\">");
            writer.println("  <value name=\"enabled\">yes</value>");
            writer.println("  <value name=\"system_package\">no</value>");
            writer.println("  <value name=\"version\">1.0</value>");
            writer.println("  <null name=\"startup_services\"/>");
            writer.println("  <null name=\"shutdown_services\"/>");
            writer.println("  <null name=\"replication_services\"/>");
            writer.println("  <null name=\"requires\"/>");
            writer.println("  <null name=\"listACL\"/>");
            writer.println("  <value name=\"webappLoad\">yes</value>");
            writer.println("</Values>");
        }
    }

    // MEMBUAT FILE node.idf DAN MENAMBAHKAN KE FOLDER YANG DITENTUKAN
    public static void addNodeIdfFile(File targetDir) throws IOException {
        File nodeIdfFile = new File(targetDir, "node.idf");
        String nodeRootName = targetDir.getName();
        try (PrintWriter writer = new PrintWriter(nodeIdfFile)) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("");
            writer.println("<Values version=\"2.0\">");
            writer.println("  <value name=\"node_type\">interface</value>");
            writer.println("  <value name=\"node_subtype\">unknown</value>");
            writer.println("  <value name=\"node_nsName\">"+nodeRootName+"</value>");
            writer.println("  <value name=\"is_public\">false</value>");
            writer.println("</Values>");
        }
    }

    // MEMBUAT FOLDER REQUEST DAN RESPONSE DALAM FOLDER DOCUMENTS
    public static void createReqAndResFiles(File documentsDir, String docNameReq, String docNameRes, String reqXml, String resXml) throws IOException {
        // FOLDER UNTUK REQUEST
        File reqDir = new File(documentsDir, docNameReq);
        if (!reqDir.exists()) {
            reqDir.mkdir();
        }
        File reqFile = new File(reqDir, "node.ndf");
        try (PrintWriter writer = new PrintWriter(reqFile)) {
            writer.println(reqXml);
        }
        // FOLDER UNTUK RESPONSE
        File resDir = new File(documentsDir, docNameRes);
        if (!resDir.exists()) {
            resDir.mkdir();
        }
        File resFile = new File(resDir, "node.ndf");
        try (PrintWriter writer = new PrintWriter(resFile)) {
            writer.println(resXml);
        }
    }

    public static void addTemplateService(File targetDir) throws IOException {
        // DIREKTORI SUMBER (LOKASI FILE templateService)
        File sourceDir = new File("target/templateService");

        // MENENTUKAN DIREKTORI TUJUAN
        File destDir = new File(targetDir, sourceDir.getName());

        // MELAKUKAN PENYALINAN DIREKTORI
        if (sourceDir.exists() && sourceDir.isDirectory()) {
            System.out.println("Menyalin dari: " + sourceDir.getAbsolutePath());
            System.out.println("Menyalin ke: " + destDir.getAbsolutePath());

            FileUtils.copyDirectory(sourceDir, destDir);
            System.out.println("Penyalinan berhasil.");
        } else {
            System.err.println("Direktori sumber tidak ditemukan: " + sourceDir.getAbsolutePath());
        }
    }

    // MEN-COPY FOLDER DANN ISINYA
    static void copyFolder(File sourceFolder, File destinationFolder) throws IOException {
        if (sourceFolder.isDirectory()) {
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }
            String[] files = sourceFolder.list();
            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(sourceFolder, file);
                    File destFile = new File(destinationFolder, file);
                    copyFolder(srcFile, destFile);
                }
            }
        } else {
            try (FileInputStream fis = new FileInputStream(sourceFolder);
                 FileOutputStream fos = new FileOutputStream(destinationFolder)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }
        }
    }

    // MEN-COMPRESS DIREKTORI DAN SEMUA ISINYA MENJADI FILE ZIP
    public static void zipDirectory(File directoryToZip, String zipFilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zip(directoryToZip, directoryToZip.getName(), zos);
        zos.close();
        fos.close();
    }

    public static void zip(File fileToZip, String fileName, ZipOutputStream zos) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            if (!fileName.endsWith("/")) {
                fileName += "/";
            }
            zos.putNextEntry(new ZipEntry(fileName));
            zos.closeEntry();

            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zip(childFile, fileName + childFile.getName(), zos);
            }
            return;
        }

        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        fis.close();
        zos.closeEntry();
    }

    // MENGHAPUS FOLDER DAN SEMUA ISINYA (OPSIONAL)
//    public static void deleteFolder(File folder) {
//        File[] files = folder.listFiles();
//        if(files != null) {
//            for(File f: files) {
//                if(f.isDirectory()) {
//                    deleteFolder(f);
//                } else {
//                    f.delete();
//                }
//            }
//        }
//        folder.delete();
//    }
}
