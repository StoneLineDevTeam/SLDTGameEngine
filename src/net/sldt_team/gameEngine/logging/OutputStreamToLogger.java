package net.sldt_team.gameEngine.logging;

import com.sun.istack.internal.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OutputStreamToLogger extends ByteArrayOutputStream {

       private String lineSeparator;

        private Logger logger;
        private Level level;

        public OutputStreamToLogger(@NotNull Logger logger, @NotNull Level level) {
            super();
            this.logger = logger;
            this.level = level;
            lineSeparator = System.getProperty("line.separator");
        }

        public void flush() throws IOException {
            String record;
            synchronized(this) {
                super.flush();
                record = this.toString();
                super.reset();

                if (record.length() == 0 || record.equals(lineSeparator)) {
                    // avoid empty records
                    return;
                }

                logger.log(level, record);
            }
        }
}
