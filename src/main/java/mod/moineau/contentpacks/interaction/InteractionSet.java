package mod.moineau.contentpacks.interaction;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import java.util.AbstractSet;
import java.util.Iterator;

public class InteractionSet<T> extends AbstractSet<Interaction<T>> {
    private final Multimap<InteractionType<T, ?>, Interaction<T>> multimap = MultimapBuilder.hashKeys().arrayListValues().build();

    @Override
    public Iterator<Interaction<T>> iterator() {
        return multimap.values().iterator();
    }

    @Override
    public int size() {
        return multimap.size();
    }

    @Override
    public boolean add(Interaction<T> interaction) {
        if (interaction.override()) {
            multimap.removeAll(interaction.getType());
        }
        return multimap.put(interaction.getType(), interaction);
    }
}
