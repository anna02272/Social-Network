package com.ftn.socialNetwork.indexservice.implementation;

import com.ftn.socialNetwork.exceptionhandling.exception.LoadingException;
import com.ftn.socialNetwork.exceptionhandling.exception.StorageException;
import com.ftn.socialNetwork.indexmodel.GroupIndex;
import com.ftn.socialNetwork.indexmodel.Table;
import com.ftn.socialNetwork.indexrepository.GroupIndexRepository;
import com.ftn.socialNetwork.indexrepository.TableRepository;
import com.ftn.socialNetwork.indexservice.interfaces.FileService;
import com.ftn.socialNetwork.indexservice.interfaces.GroupIndexingService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupIndexingServiceImpl implements GroupIndexingService {
    private final LanguageDetector languageDetector;
    private final FileService fileService;
    private final GroupIndexRepository groupIndexingRepository;
    private final TableRepository tableRepository;


    @Override
    public GroupIndex save(GroupIndex groupIndex) {
        return groupIndexingRepository.save(groupIndex);
    }

    @Override
    public void deleteById(String id) {
        groupIndexingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public String indexDocument(MultipartFile documentFile) {
        var newEntity = new Table();
        var newIndex = new GroupIndex();

        var name = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newIndex.setName(name);
        newEntity.setTitle(name);

        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            newIndex.setContentSr(documentContent);
        } else {
            newIndex.setContentEn(documentContent);
        }
        newEntity.setTitle(name);

        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());
        newIndex.setServerFilename(serverFilename);
        newEntity.setServerFilename(serverFilename);

        newEntity.setMimeType(detectMimeType(documentFile));
        var savedEntity = tableRepository.save(newEntity);

        newIndex.setDatabaseId(savedEntity.getId());
        groupIndexingRepository.save(newIndex);

        return serverFilename;
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) {
        String documentContent;
        try (var pdfFile = multipartPdfFile.getInputStream()) {
            var pdDocument = PDDocument.load(pdfFile);
            var textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new LoadingException("Error while trying to load PDF file content.");
        }

        return documentContent;
    }

    private String detectLanguage(String text) {
        var detectedLanguage = languageDetector.detect(text).getLanguage().toUpperCase();
        if (detectedLanguage.equals("HR")) {
            detectedLanguage = "SR";
        }

        return detectedLanguage;
    }

    private String detectMimeType(MultipartFile file) {
        var contentAnalyzer = new Tika();

        String trueMimeType;
        String specifiedMimeType;
        try {
            trueMimeType = contentAnalyzer.detect(file.getBytes());
            specifiedMimeType =
                    Files.probeContentType(Path.of(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            throw new StorageException("Failed to detect mime type for file.");
        }

        if (!trueMimeType.equals(specifiedMimeType) &&
                !(trueMimeType.contains("zip") && specifiedMimeType.contains("zip"))) {
            throw new StorageException("True mime type is different from specified one, aborting.");
        }

        return trueMimeType;
    }
}
