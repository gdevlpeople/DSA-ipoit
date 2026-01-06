package by.it.group410972.petroshevich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SourceScannerA {

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

                // Игнорируем файлы с тестами
                if (content.contains("@Test") || content.contains("org.junit.Test")) return;

                // Удаляем package и все импорты
                StringBuilder sb = new StringBuilder();
                for (String line : content.split("\\R")) {
                    line = line.trim();
                    if (line.startsWith("package ") || line.startsWith("import ")) continue;
                    sb.append(line).append("\n");
                }
                // Убираем символы <33 в начале и конце
                String cleaned = sb.toString().replaceAll("^[\\x00-\\x20]+|[\\x00-\\x20]+$", "");

                byte[] bytes = cleaned.getBytes(StandardCharsets.UTF_8);
                String relativePath = baseDir.toURI().relativize(current.toURI()).getPath();
                files.add(new FileData(relativePath.replace("/", "\\"), bytes.length));

            } catch (IOException ignored) {
                // Игнорируем ошибки чтения
            }
        }
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
