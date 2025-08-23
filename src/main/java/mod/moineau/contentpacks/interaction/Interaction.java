package mod.moineau.contentpacks.interaction;

// TODO Registerable interaction type system
public interface Interaction<T> {
    void register(T target);

    InteractionType<T, ?> getType();

    default boolean override() {
        return true;
    }
}
