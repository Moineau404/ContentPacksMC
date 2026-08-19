package mod.moineau.contentpacks.interaction;

public interface Interaction<T> {
    void register(T target);

    InteractionType<T, ?> getType();

    default boolean override() {
        return true;
    }
}
