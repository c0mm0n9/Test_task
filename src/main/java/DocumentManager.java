import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentManager {

    // Collection to store documents
    private final Map<String, Document> documentStore = new HashMap<>();

    /**
     * Save or update a document. If the document doesn't have an ID, a new one is generated.
     * The created field is not changed if the document already exists.
     *
     * @param document - document content and author data
     * @return the saved document
     */
    public Document save(Document document) {
        if (document.getId() == null || !documentStore.containsKey(document.getId())) {
            // Generate a unique ID
            document.setId(UUID.randomUUID().toString());
        }

        // Add the document into the memory
        documentStore.put(document.getId(), document);
        return document;
    }

    /**
     * Search for documents that match the search criteria.
     *
     * @param request - search request with multiple filters
     * @return a list of matching documents
     */
    public List<Document> search(SearchRequest request) {
        return documentStore.values().stream()
                .filter(doc -> matchesSearchRequest(doc, request))
                .collect(Collectors.toList());
    }

    /**
     * Find a document by its ID.
     *
     * @param id - document id
     * @return optional of the document, if found
     */
    public Optional<Document> findById(String id) {
        return Optional.ofNullable(documentStore.get(id));
    }

    // Method to check whether the document matches search request
    private boolean matchesSearchRequest(Document document, SearchRequest request) {
        if (request.getTitlePrefixes() != null) {
            boolean matchesTitle = request.getTitlePrefixes().stream()
                    .anyMatch(prefix -> document.getTitle()!=null && document.getTitle().startsWith(prefix));
            if (!matchesTitle) return false;
        }

        if (request.getContainsContents() != null) {
            boolean matchesContent = request.getContainsContents().stream()
                    .anyMatch(content -> document.getContent() != null && document.getContent().contains(content));
            if (!matchesContent) return false;
        }

        if (request.getAuthorIds() != null) {
            boolean matchesAuthor = request.getAuthorIds().contains(document.getAuthor().getId());
            if (!matchesAuthor) return false;
        }

        if (request.getCreatedFrom() != null && document.getCreated().isBefore(request.getCreatedFrom())) {
            return false;
        }

        if (request.getCreatedTo() != null && document.getCreated().isAfter(request.getCreatedTo())) {
            return false;
        }

        return true;
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}
