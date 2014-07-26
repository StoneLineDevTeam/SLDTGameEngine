package fr.sldt.gameEngine.ext.gameSettings;

public class Setting {

    private final Object obj;

    protected Setting(Object b){
        if (b == null){
            obj = (byte)0;
        } else {
            obj = b;
        }
    }

    public int asInteger(){
        return (Integer) obj;
    }

    public boolean asBoolean(){
        return (Boolean) obj;
    }

    public byte asByte(){
        return (Byte) obj;
    }

    public String asString(){
        return String.valueOf(obj);
    }

    public float asFloat(){
        return (Float) obj;
    }

    public double asDouble(){
        return (Double) obj;
    }
}
