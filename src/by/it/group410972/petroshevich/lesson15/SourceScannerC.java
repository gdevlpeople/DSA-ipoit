package by.it.group410972.petroshevich.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Map<String, String> filesMap = new HashMap<>();
        File root = new File(src);

        scanFiles(root, src, filesMap);

        // Пример вывода относительных путей
        filesMap.forEach((path, content) -> System.out.println(path));
    }

    private static void scanFiles(File dir, String src, Map<String, String> filesMap) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanFiles(file, src, filesMap);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                try {
                    String content = Files.readString(file.toPath());

                    // Пропускаем тестовые файлы
                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        continue;
                    }

                    // Формируем относительный путь
                    String relativePath = file.getAbsolutePath().substring(src.length());
                    relativePath = relativePath.replace(File.separatorChar, '\\');

                    // Очистка текста: package, import, комментарии, <33 → пробел, trim
                    content = cleanContent(content);

                    if (!content.isEmpty()) {
                        filesMap.put(relativePath, content);
                    }

                } catch (MalformedInputException e) {
                    // Игнорируем файлы с некорректной кодировкой
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String cleanContent(String text) {
        StringBuilder sb = new StringBuilder();

        // Разбиваем на строки
        String[] lines = text.split("\r?\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("package") || line.startsWith("import")) continue; // пропускаем package/import
            sb.append(line).append('\n');
        }

        // Удаляем все комментарии
        String noComments = sb.toString().replaceAll("//.*|/\\*(?:.|\\R)*?\\*/", "");

        // Заменяем символы <33 на пробел и trim
        noComments = noComments.replaceAll("[\\x00-\\x20]+", " ").trim();

        return noComments;
    }
}
