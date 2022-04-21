package richmond.swe.dotsalary.data.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import richmond.swe.dotsalary.data.SortField;

/**
 * Pagination configuration for User records.
 */
public class OffsetPageRequest extends PageRequest {

    private final long offset;

    private OffsetPageRequest(final int page, final int offset, final int limit, final Sort sort) {
        super(page, limit, sort);
        this.offset = offset;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    /**
     * Get instance of builder.
     * @return OffsetPageRequestBuilder
     */
    public static OffsetPageRequestBuilder builder() {
        return new OffsetPageRequestBuilder();
    }

    /**
     * Builder class.
     */
    public static class OffsetPageRequestBuilder {
        private int page = 0;
        private int offset = 0;
        private int limit = Integer.MAX_VALUE;
        private Sort sort = Sort.unsorted();

        private OffsetPageRequestBuilder() {}

        public OffsetPageRequestBuilder offset(final int offset) {
            this.offset = offset;
            return this;
        }

        public OffsetPageRequestBuilder limit(final int limit) {
            this.limit = limit;
            return this;
        }

        public OffsetPageRequestBuilder sort(final SortField sort) {
            this.sort = sort.getField() == null ? Sort.unsorted() : Sort.by(sort.getField()).ascending();
            return this;
        }

        public OffsetPageRequest build() {
            return new OffsetPageRequest(page, offset, limit, sort);
        }
    }
}

