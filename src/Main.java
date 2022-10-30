import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static final String SAVE_PATH = "D:\\__Games\\savegames\\";
    public static void main(String[] args) {
        Map<String, GameProgress> saves = new HashMap<>();
        saves.put(SAVE_PATH + "save1.dat", new GameProgress(100, 1, 10, 1000.0));
        saves.put(SAVE_PATH + "save2.dat", new GameProgress(80, 4, 12, 19531.2));
        saves.put(SAVE_PATH + "save3.dat", new GameProgress(50, 8, 99, 65465465.2));

        for (String path : saves.keySet()) {
            saveGame(path, saves.get(path));
        }
        if (zipFiles(SAVE_PATH + "save.zip", new ArrayList<>(saves.keySet()))) {
            deleteFiles(new ArrayList<>(saves.keySet()));
        }
    }

    private static void deleteFiles(List<String> paths) {
        for (String path : paths) {
            File f = new File(path);
            f.delete();
        }
    }

    public static void saveGame(String path, GameProgress gp) {
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileOutputStream fs = new FileOutputStream(file); ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(gp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean zipFiles(String file, List<String> paths) {
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
            for (String path : paths) {
                File f = new File(path);
                FileInputStream fis = new FileInputStream(f);
                ZipEntry zipEntry = new ZipEntry(f.getName());
                zos.putNextEntry(zipEntry);

                byte[] buff = new byte[fis.available()];
                fis.read(buff);
                zos.write(buff);
                zos.closeEntry();
                fis.close();
            }
            zos.finish();
            return true;
        } catch (IOException e) {
            return  false;
        }
    }
}