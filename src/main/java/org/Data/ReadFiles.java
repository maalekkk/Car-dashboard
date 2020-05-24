package org.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.image.Image;
import org.Presentation.Gui;

/**
 * The ReadFiles class that allows to load all files in the start of the app that we will use for the lifetime of the program.
 */
public class ReadFiles {
    /**
     * Load files from disk.
     *
     * @param directoryPath the directory path to directory with files
     * @param extension     the files extension
     * @return the list of all paths in the string
     * @throws IOException the io exception
     */
    public List<String> loadFiles(String directoryPath, String extension) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(directoryPath));
        return walk.map(Path::toString).filter(f -> f.endsWith("." + extension)).collect(Collectors.toList());
    }

    /**
     * Load mp3 songs to music player in the app.
     *
     * @param directoryPath the directory path to the music playlist
     * @return the list of all paths in the string
     * @throws IOException the io exception
     */
    public List<String> loadMp3Files(String directoryPath) throws IOException {
        return loadFiles(directoryPath, "mp3");
    }

    /**
     * Method allows read all lights image in the start of the app.
     *
     * @return the HashMap with pair: the names of light and two Image to this light, first with the light is turn off and second when the light is turn on..
     * @throws IOException the io exception
     */
//    public HashMap<String, Image[]> loadLights() throws IOException {
//        String[] lightList = {"pLights", "ltsLights", "rtsLights", "lbLights",
//                "hbLights", "ffLights", "rfLights", "ccLights"};
//        HashMap<String, Image[]> result = new HashMap<>();
//        String absolutePath = (new File("src\\main\\resources\\org\\Presentation\\img\\lights").getAbsolutePath()) + "\\";
//        for (String name : lightList) {
//            Image[] lights = {
//                    new Image(new FileInputStream(absolutePath + name + "D.png")),
//                    new Image(new FileInputStream(absolutePath + name + "E.png"))
//            };
//            result.put(name, lights);
//        }
//        return result;
//    }

    public HashMap<String, Image[]> loadLights() {
        String[] lightList = {"pLights", "ltsLights", "rtsLights", "lbLights",
                "hbLights", "ffLights", "rfLights", "ccLights"};
        HashMap<String, Image[]> result = new HashMap<>();

        for (String name : lightList) {
            Image[] lights = {
                    new Image(Gui.class.getResourceAsStream("img/lights/"+ name + "D.png")),
                    new Image(Gui.class.getResourceAsStream("img/lights/"+ name + "E.png"))
            };
            result.put(name, lights);
        }
        return result;
    }
}