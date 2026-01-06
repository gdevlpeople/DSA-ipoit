package by.it.group410972.petroshevich.lesson15;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        File srcDir = new File(src);
        List<FileData> files = new ArrayList<>();
        scanFiles(srcDir, srcDir, files);

        // Сортировка: сначала по размеру, потом лексикографически по пути
        files.sort(Comparator
                .comparingLong((FileData f) -> f.size)
                .thenComparing(f -> f.relativePath));

        for (FileData f : files) {
            System.out.println(f.size + " " + f.relativePath);
        }
    }

    private static void scanFiles(File baseDir, File current, List<FileData> files) {
        if (current.isDirectory()) {
            for (File f : Objects.requireNonNull(current.listFiles())) {
                scanFiles(baseDir, f, files);
            }
        } else if (current.isFile() && current.getName().endsWith(".java")) {
            try {
                String content = Files.readString(current.toPath(), StandardCharsets.UTF_8);

                // Игнорируем тестовые файлы
                if (content.contains("@Test") || content.contains("org.junit.Test")) return;

                // Удаляем package и импорты
                StringBuilder sb = new StringBuilder();
                String[] lines = content.split("\\R");
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("package ") || line.startsWith("import ")) continue;
                    sb.append(line).append("\n");
                }
                String noPackageImports = sb.toString();

                // Удаляем все комментарии
                String noComments = removeComments(noPackageImports);

                // Убираем символы <33 в начале и конце
                String cleaned = noComments.replaceAll("^[\\x00-\\x20]+|[\\x00-\\x20]+$", "");

                // Убираем пустые строки
                StringBuilder finalText = new StringBuilder();
                for (String line : cleaned.split("\\R")) {
                    if (!line.trim().isEmpty()) {
                        finalText.append(line.trim()).append("\n");
                    }
                }

                byte[] bytes = finalText.toString().getBytes(StandardCharsets.UTF_8);
                String relativePath = baseDir.toURI().relativize(current.toURI()).getPath();
                files.add(new FileData(relativePath.replace("/", "\\"), bytes.length));

            } catch (IOException ignored) {
                // Игнорируем ошибки чтения
            }
        }
    }

    private static String removeComments(String code) {
        StringBuilder sb = new StringBuilder();
        boolean inBlockComment = false;
        boolean inLineComment = false;
        int len = code.length();
        for (int i = 0; i < len; i++) {
            if (inBlockComment) {
                if (i + 1 < len && code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++; // пропускаем '/'
                }
            } else if (inLineComment) {
                if (code.charAt(i) == '\n') {
                    inLineComment = false;
                    sb.append('\n');
                }
            } else {
                if (i + 1 < len && code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++;
                } else if (i + 1 < len && code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++;
                } else {
                    sb.append(code.charAt(i));
                }
            }
        }
        return sb.toString();
    }

    private static class FileData {
        String relativePath;
        long size;

        FileData(String relativePath, long size) {
            this.relativePath = relativePath;
            this.size = size;
        }
    }
}
