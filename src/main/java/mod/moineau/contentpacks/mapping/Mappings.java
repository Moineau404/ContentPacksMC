package mod.moineau.contentpacks.mapping;

import com.google.common.base.Splitter;
import mod.moineau.contentpacks.ContentPacks;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@ApiStatus.Internal
@ApiStatus.Experimental
public final class Mappings {
    public static final Logger LOGGER = LoggerFactory.getLogger("ContentPacks/Mappings");

    public static Map<Key, String> YARN = new HashMap<>();

    public static void load() {
        Properties properties = new Properties();
        try {
            properties.load(ContentPacks.class.getResourceAsStream("/mappings/yarn.properties"));
        } catch (IOException e) {
            LOGGER.error("Failed to load mappings:", e);
            return;
        }
        properties.forEach((key, value) -> {
            YARN.put(Key.of(key.toString()), value.toString());
        });
        LOGGER.info(YARN.toString());
    }

    public record Key(String context, String name) {
        public static Key of(String key) {
            Splitter splitter = Splitter.on('.');
            Iterator<String> iterator = splitter.split(key).iterator();
            if (iterator.hasNext()) {
                String first = iterator.next();
                if (iterator.hasNext()) {
                    String second = iterator.next();
                    return new Key(first, second);
                }
                return new Key("", first);
            }
            throw new IllegalStateException("Trying to create empty mapping key!");
        }

        public <T> T get(Function<String, T> function) {
            return Objects.requireNonNullElseGet(function.apply(name), () -> function.apply(YARN.get(this)));
        }

        public Stream<String> names() {
            String yarn = YARN.get(this);
            return yarn != null ? Stream.of(name, yarn) : Stream.of(name);
        }

        @Override
        public @NotNull String toString() {
            return context + "." + name;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Key(String context1, String name1) && (Objects.equals(this.context, context1) && Objects.equals(this.name, name1)))
                    || (obj instanceof String string && this.toString().equals(string));
        }
    }
}
