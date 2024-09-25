import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DocumentManagerTest {

    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();

        // Create an author
        DocumentManager.Author author = DocumentManager.Author.builder()
                .id("author1")
                .name("Author Name")
                .build();

        // Save a document
        DocumentManager.Document doc = DocumentManager.Document.builder()
                .title("Title")
                .content("This is the content of the document.")
                .author(author)
                .build();

        DocumentManager.Document savedDoc = manager.save(doc);
        System.out.println("Saved Document: " + savedDoc);

        System.out.println("-----");

        // Save another document with the same author
        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Another title")
                .content("More content here.")
                .author(author)
                .build();

        DocumentManager.Document savedDoc2 = manager.save(doc2);
        System.out.println("Saved Second Document: " + savedDoc2);

        System.out.println("-----");

        // Search for documents with a title prefix
        DocumentManager.SearchRequest searchByTitle = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Title"))
                .build();

        List<DocumentManager.Document> searchResults = manager.search(searchByTitle);
        System.out.println("Search Results for Title Prefixes:");
        searchResults.forEach(System.out::println);

        System.out.println("-----");

        // Find document by ID
        Optional<DocumentManager.Document> foundDoc = manager.findById(savedDoc.getId());
        if (foundDoc.isPresent()) {
            System.out.println("Document found by ID: " + foundDoc.get());
        } else {
            System.out.println("Document not found.");
        }

        System.out.println("-----");

        // Search for documents by content
        DocumentManager.SearchRequest searchByContent = DocumentManager.SearchRequest.builder()
                .containsContents(Collections.singletonList("content"))
                .build();

        List<DocumentManager.Document> contentSearchResults = manager.search(searchByContent);
        System.out.println("Search Results for Content Containing:");
        contentSearchResults.forEach(System.out::println);

        System.out.println("-----");

        // Search for documents by author ID
        DocumentManager.SearchRequest searchByAuthor = DocumentManager.SearchRequest.builder()
                .authorIds(List.of("author2"))
                .build();

        List<DocumentManager.Document> authorSearchResults = manager.search(searchByAuthor);
        System.out.println("Search Results for Author ID:");
        authorSearchResults.forEach(System.out::println);

        System.out.println("-----");
    }
}
