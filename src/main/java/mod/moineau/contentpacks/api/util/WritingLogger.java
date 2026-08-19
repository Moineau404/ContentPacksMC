package mod.moineau.contentpacks.api.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WritingLogger {
    protected final File file;
    protected final Logger logger;
    protected final List<String> lines = new LinkedList<>();
    
    public WritingLogger(File file, Logger logger) {
        this.file = file;
        this.logger = logger;
    }
    
    public WritingLogger(File file) {
        this(file, null);
    }
    
    public void info(String message) {
        if (logger != null) logger.info(message);
        this.lines.add(message);
    }
    
    public void info(String message, Object... args) {
        String formatted = MessageFormatter.basicArrayFormat(message, args);
        if (logger != null) logger.info(formatted);
        this.lines.add(formatted);
    }
    
    public void warn(String message) {
        if (logger != null) logger.warn(message);
        this.lines.add(message);
    }

    public void warn(String message, Object... args) {
        String formatted = MessageFormatter.basicArrayFormat(message, args);
        if (logger != null) logger.warn(formatted);
        this.lines.add(formatted);
    }
    
    public void error(String message) {
        if (logger != null) logger.error(message);
        this.lines.add(message);
    }

    public void error(String message, Object... args) {
        String formatted = MessageFormatter.basicArrayFormat(message, args);
        if (logger != null) logger.error(formatted);
        this.lines.add(formatted);
    }

    public void flush() {
        try {
            FileUtils.writeLines(this.file, this.lines);
        } catch (IOException e) {
            if (logger != null) this.logger.error("Failed to write log file {}: {}", file, e);
        }

        this.clear();
    }

    public void clear() {
        this.lines.clear();
    }
}
