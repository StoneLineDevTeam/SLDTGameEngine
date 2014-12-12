package net.sldt_team.gameEngine.format.datafile;

import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.exception.code.ErrorCode001;

import java.io.*;
import java.util.*;

public class GameDataFile {

    private Map<String, String> data;

    /**
     * Is the file ready to be used ?
     */
    public boolean isReady;

    private boolean write;
    private File dataFile;

    /**
     * Opens a GDF (Game Data File)
     *
     * @param file The file to be loaded
     * @param read Mode (true = readOnly, false = write)
     */
    public GameDataFile(File file, boolean read) {

        File f1 = new File(file + ".gdf");

        if (read) {
            isReady = loadData(f1);
            return;
        }
        data = new HashMap<String, String>();
        isReady = false;
        write = true;
        dataFile = f1;
    }

    private boolean loadData(File dataFile) {
        if (dataFile.exists()) {
            data = new HashMap<String, String>();
            GameDataFileEncoder system = new GameDataFileEncoder(false);

            BufferedReader stream = null;
            try {
                stream = new BufferedReader(new FileReader(dataFile));
                String[] rawLines = new String[256];
                String currentLine;
                int lines = 0;
                String line;

                while ((line = stream.readLine()) != null) {
                    if (line.contains("-")) {
                        system.loadEncodingKeyFromString(line);
                        continue;
                    } else if (line.equals("file")) {
                        rawLines[lines] = line;
                        lines++;
                        continue;
                    } else if (line.equals("end")) {
                        rawLines[lines] = line;
                        lines++;
                        continue;
                    }
                    String toDecode = convertFromIntStringToString(line);
                    rawLines[lines] = system.decodeString(toDecode);
                    lines++;
                }

                if (rawLines[0] != null && !rawLines[0].equals("file")) {
                    throw new GameException(new ErrorCode001());
                }

                for (int i = 0; i < lines; i++) {
                    currentLine = rawLines[i];
                    if (currentLine.equals("file")) {
                        continue;
                    }
                    if (currentLine.equals("end")) {
                        return true;
                    }
                    List<String> l = splitFile("=>", currentLine);
                    data.put(l.get(0), l.get(1));
                }
                stream.close();
                return true;
            } catch (FileNotFoundException e) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                return false;
            } catch (IOException e) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return false;
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private String convertFromStringToIntString(String toConvert) {
        String result = "";
        for (int i = 0; i < toConvert.length(); i++) {
            int j = toConvert.charAt(i);
            result += j + "+";
        }
        return result;
    }

    private String convertFromIntStringToString(String toConvert) {
        String result = "";
        String[] strings = toConvert.split("\\+");
        for (String s : strings) {
            int i = Integer.parseInt(s);
            char c = (char) i;
            result += c;
        }
        return result;
    }

    /**
     * Call this method when you're ready to write (make sure mode is write and isReady is set to true)
     */
    public boolean saveData() {
        if (isReady && write) {
            GameDataFileEncoder system = new GameDataFileEncoder(true);
            try {
                BufferedWriter stream = new BufferedWriter(new FileWriter(dataFile));
                stream.write("file");
                stream.newLine();
                stream.write(system.getCurrentEncodingKey());
                stream.newLine();
                for (Map.Entry<String, String> entry : data.entrySet()) {

                    stream.append(convertFromStringToIntString(system.encodeString(entry.getKey() + "=>" + entry.getValue())));
                    stream.append('\n');
                }
                stream.append("end");
                stream.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Add a key/value to be saved in the file
     */
    public boolean addKeyValue(String key, String value) {
        if (write) {
            data.put(key, value);
            return true;
        }
        return false;
    }

    /**
     * Returns a list of words by character(charToReconize) from a char sequence(line)
     */
    private List<String> splitFile(String charToReconize, String line) {
        List<String> l = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(line, charToReconize);
        while (st.hasMoreTokens()) {
            l.add(st.nextToken());
        }
        return l;
    }

    /**
     * Returns a map who contains the variable name and the value
     */
    public Map<String, String> getData() {
        return data;
    }
}