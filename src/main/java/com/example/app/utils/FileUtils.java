package com.example.app.utils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static String GAME_PATH = ".myapp/jeuNerveux/";

    public static Path getUserGameDir() throws IOException {
        Path dir = Paths.get(
                System.getProperty("user.home"),
                GAME_PATH
        );
        Files.createDirectories(dir);
        return dir;
    }

    public static Path getUserGameDir(String directory) throws IOException {
        Path dirPath = Paths.get(
                System.getProperty("user.home"),
                GAME_PATH + directory
        );
        Files.createDirectories(dirPath);
        return dirPath;
    }

    public static InputStream loadFile(String directory, String fileName) throws IOException {
        // 1 Try filesystem first (runtime-created files)
        // The root folder is user/.myApp/jeuNerveux
        Path filePath = getUserGameDir(directory).resolve(fileName);
        if (Files.exists(filePath)) {
            return Files.newInputStream(filePath);
        }

        // 2 Try classpath (packaged main.java.main.resources)
        // The root folder is resources
        String path = directory + "/" + fileName;
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);

        if (is != null) {
            return is;
        }

        throw new FileNotFoundException("File not found: " + path);
    }

    public static InputStream loadFile(String fileName) throws IOException {
        return loadFile("", fileName);
    }

    public static Path writeNewFile(String fileName, List<String> lines) throws IOException {
        Path file = getUserGameDir().resolve(fileName);
        Files.write(file, lines);
        return file;
    }

    public static Path writeNewFile(String directory, String fileName, List<String> lines) throws IOException {
        Path file = getUserGameDir(directory).resolve(fileName);
        Files.write(file, lines);
        return file;
    }

    public static List<String> listResources(String directory) throws IOException, URISyntaxException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        assert !directory.isEmpty();
        URL url = cl.getResource(directory);

        if (url == null) {
            throw new IllegalArgumentException("Resource dir not found: " + directory);
        }

        String protocol = url.getProtocol();

        if (protocol.equals("file")) {
            // Running from IDE (filesystem)
            Path path = Paths.get(url.toURI());
            try (var stream = Files.walk(path)) {
                return stream
                        .filter(Files::isRegularFile)
                        .map(p -> path.relativize(p).toString())
                        .toList();
            }
        }

        if (protocol.equals("jar")) {
            // Running from JAR
            JarURLConnection jarConn = (JarURLConnection) url.openConnection();
            JarFile jar = jarConn.getJarFile();
            String prefix = jarConn.getEntryName();

            List<String> result = new ArrayList<>();
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.startsWith(prefix) && !entry.isDirectory()) {
                    result.add(name.substring(prefix.length() + 1));
                }
            }
            return result;
        }

        throw new UnsupportedOperationException("Unsupported protocol: " + protocol);
    }

    public static List<String> listUserFiles(String directory) throws IOException {
        Path userHome = Paths.get(System.getProperty("user.home"), GAME_PATH + directory);

        try (Stream<Path> paths = Files.list(userHome)) {
            return paths
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    public static List<String> listUserFiles() throws IOException {
        return listUserFiles("");
    }

    public static List<String> listAllResources(String directory) throws IOException, URISyntaxException {
        List<String> userResources = listUserFiles(directory);
        List<String> baseResources = listResources(directory);

        userResources.addAll(baseResources);

        return userResources;
    }

    public static void saveMap(int[][][] tileMapNum, String mapName){
        int maxCol = tileMapNum.length;
        int maxRow = tileMapNum[0].length;
        int layerCount = tileMapNum[0][0].length;

        List<String> lines = new ArrayList<>();
        for (int row = 0; row < maxRow; row++) {

            StringBuilder stringBuilder = new StringBuilder();
            for (int col = 0; col < maxCol; col++) {

                // TO ADD LAYER INFO
                for (int layer = 0; layer < layerCount; layer++) {
                    stringBuilder.append(tileMapNum[col][row][layer]);
                    if (layer != layerCount - 1) {
                        stringBuilder.append(":");
                    }
                }

                if (col != maxCol - 1) {
                    stringBuilder.append(" ");
                }
            }
            lines.add(stringBuilder.toString());
        }

        try {
            writeNewFile("maps", mapName, lines);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int[] getMapDimensions(String mapName){
        // COL, ROW, LAYER
        try (InputStream is = loadFile("maps", mapName)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            // GET THE LINE AND FORMAT IT
            while (line.contains("  ")) {
                line = line.replace("  ", " ");
            }
            while (line.startsWith(" ")){
                line = line.substring(1);
            }
            while (line.endsWith(" ")){
                line = line.substring(0, line.length() - 1);
            }

            // GET THE STRINGS FOR EVERY TILES
            String[] colList = line.split(" ");

            // GET THE NUMBERS FOR EVERY LAYER OF A TILE
            String[] layerList = colList[0].split(":");

            // GET THE NUMBER OF LINES
            int lineCount = 1;
            while (br.readLine() != null){
                lineCount++;
            }

            return new int[]{colList.length, lineCount, layerList.length};

        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public static void loadMap(int[][][] tileMapNum, String mapName){
        try (InputStream is = loadFile("maps", mapName)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int maxWorldCol = tileMapNum.length;
            int maxWorldRow = tileMapNum[0].length;
            int layerCount = tileMapNum[0][0].length;

            int col = 0;
            int row = 0;

            while (col < maxWorldCol && row < maxWorldRow){

                // GET THE LINE AND FORMAT IT
                String line = br.readLine();
                while (line.contains("  ")) {
                    line = line.replace("  ", " ");
                }
                while (line.startsWith(" ")){
                    line = line.substring(1);
                }

                // GET THE STRINGS FOR EVERY TILES
                String[] lineLayerNumbers = line.split(" ");

                while (col < maxWorldCol){

                    // GET THE NUMBERS FOR EVERY LAYER OF A TILE
                    String[] tileLayerNumbers = lineLayerNumbers[col].split(":");
                    for (int layer = 0; layer < layerCount; layer++) {

                        // LAYER VALUE
                        int num = Integer.parseInt(tileLayerNumbers[layer]);
                        tileMapNum[col][row][layer] = num;
                    }

                    col++;
                }

                if (col == maxWorldCol){
                    col = 0;
                    row++;
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
