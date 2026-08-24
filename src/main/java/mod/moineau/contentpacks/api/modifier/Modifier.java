package mod.moineau.contentpacks.api.modifier;

public interface Modifier<T> {
    void apply(T target);

    default Modifier<T> accumulate(Modifier<T> other) {
        return this;
    }
}
