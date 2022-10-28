import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        Map<String, GameProgress> saves = new HashMap<>();
        saves.put("D:\\Games\\savegames\\save1.dat", new GameProgress(100, 1, 10, 1000.0));
        saves.put("D:\\Games\\savegames\\save2.dat", new GameProgress(80, 4, 12, 19531.2));
        saves.put("D:\\Games\\savegames\\save3.dat", new GameProgress(50, 8, 99, 65465465.2));

        for (String path : saves.keySet()) {
            saveGame(path, saves.get(path));
        }
        zipFiles("D:\\Games\\savegames\\save.zip", saves.keySet().stream().toList());


    }

    public static void saveGame(String path, GameProgress gp) {
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileOutputStream fs = new FileOutputStream(file);
             ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(gp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFiles(String file, List<String> paths) {
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
                f.delete();
            }
            zos.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}