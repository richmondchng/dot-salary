package richmond.swe.dotsalary.data;

/**
 * Enum for field.
 * @author richmondchng
 */
public enum SortField {
    NAME("name"),
    SALARY("salary"),
    NONE(null);

    private String field;

    SortField(final String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
