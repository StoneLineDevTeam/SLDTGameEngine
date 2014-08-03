package fr.sldt.gameEngine.ext;

import fr.sldt.gameEngine.GameApplication;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Translator {

    private String langFileName;
    private Map<String, String> langValues;

    public static Translator instance;

    public Translator(String langName) {
        langFileName = langName;
        langValues = new HashMap<String, String>();
        loadLanguage();

        instance = this;
    }

    public void updateUserLanguage(String newLanguage) {
        langFileName = newLanguage;
        loadLanguage();
    }

    public String translate(String key) {
        if (langValues.containsKey(key))
            return langValues.get(key);
        else
            return key;
    }

    private void loadLanguage() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(GameApplication.getGameDir() + File.separator + "resources" + File.separator + "lang" + File.separator + langFileName + ".txt")));

            String current;
            while ((current = reader.readLine()) != null) {
                String[] vars = current.split("=");
                if (vars.length == 1) {
                    continue;
                }
                String var0 = vars[0];
                String var1 = vars[1];

                langValues.put(var0, var1);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
