package com.cnsc.research.api;

import com.cnsc.research.domain.model.Documents;
import com.cnsc.research.service.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentsController {

    private final DocumentsService service;

    @Autowired
    public DocumentsController(DocumentsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity addDocument(@RequestBody Documents documents) {
        return service.addDocuments(documents);
    }

    @PutMapping
    public ResponseEntity editDocument(@RequestBody Documents documents) {
        return service.editDocuments(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDocumentById(@PathVariable(name = "id") Integer documentId) {
        return service.getDocument(documentId);
    }

    @GetMapping("/list")
    public ResponseEntity getAllDocuments() {
        return service.getAllDocuments();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDocument(@PathVariable(name = "id") Integer documentId) {
        return service.deleteDocument(documentId);
    }


    @PostMapping("/file")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        return service.uploadFile(file);
    }
}
