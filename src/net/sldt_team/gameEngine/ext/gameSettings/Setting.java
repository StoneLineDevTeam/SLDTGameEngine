package net.sldt_team.gameEngine.ext.gameSettings;

public class Setting {

    private final Object obj;

    /**
     * @exclude
     */
    protected Setting(Object b) {
        if (b == null) {
            obj = (byte) 0;
        } else {
            obj = b;
        }
    }

    /**
     * Returns this setting as Integer
     */
    public int asInteger() {
        return (Integer) obj;
    }

    /**
     * Returns this setting as Boolean
     */
    public boolean asBoolean() {
        return (Boolean) obj;
    }

    /**
     * Returns this setting as Byte
     */
    public byte asByte() {
        return (Byte) obj;
    }

    /**
     * Returns this setting as String
     */
    public String asString() {
        return String.valueOf(obj);
    }

    /**
     * Returns this setting as Float
     */
    public float asFloat() {
        return (Float) obj;
    }

    /**
     * Returns this setting as Double
     */
    public double asDouble() {
        return (Double) obj;
    }
}
