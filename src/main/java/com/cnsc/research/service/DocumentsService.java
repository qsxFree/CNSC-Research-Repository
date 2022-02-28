package com.cnsc.research.service;

import com.cnsc.research.domain.model.Documents;
import com.cnsc.research.domain.repository.DocumentsRepository;
import com.cnsc.research.misc.storage.LocalStorageUtility;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
public class DocumentsService {

    private final DocumentsRepository repository;
    private final LocalStorageUtility storageUtility;
    private final Logger logger;

    @Autowired
    public DocumentsService(DocumentsRepository repository, LocalStorageUtility storageUtility, Logger logger) {
        this.repository = repository;
        this.storageUtility = storageUtility;
        this.logger = logger;
    }

    public ResponseEntity addDocuments(Documents documents) {
        try {
            repository.save(documents);
            return new ResponseEntity<String>("Document has been added", OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on adding documents", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity editDocuments(Documents documents) {
        try {
            repository.save(documents);
            return new ResponseEntity<String>("Document has been updated", OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on updating documents", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getDocument(Integer documentId) {
        try {
            Optional<Documents> documentsOptional = repository.findById(documentId);
            if (documentsOptional.isPresent()) {
                return new ResponseEntity<Documents>(documentsOptional.get(), OK);
            } else {
                return new ResponseEntity<String>("Cannot find document.", BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on retrieving document.", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getAllDocuments() {
        try {
            List<Documents> documentsList = repository.findAll();
            return new ResponseEntity<List<Documents>>(documentsList, OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on retrieving documents.", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deleteDocument(Integer documentId) {
        try {
            repository.deleteById(documentId);
            return new ResponseEntity("Document has successfully deleted", OK);
        } catch (Exception e) {
            return new ResponseEntity("Error on deleting document", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity uploadFile(MultipartFile file) {
        String fileName = "Document" + System.currentTimeMillis();
        try {
            storageUtility.inContainer(LocalStorageUtility.DOCUMENT_CONTAINER)
                    .upload(file.getBytes(), fileName + ".pdf");
            return new ResponseEntity<String>(fileName, CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on writing file : " + file.getName(), INTERNAL_SERVER_ERROR);
        }
    }
}
