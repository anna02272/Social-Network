package com.ftn.socialNetwork.indexservice.implementation;

import com.ftn.socialNetwork.exceptionhandling.exception.LoadingException;
import com.ftn.socialNetwork.exceptionhandling.exception.NotFoundException;
import com.ftn.socialNetwork.exceptionhandling.exception.StorageException;
import com.ftn.socialNetwork.indexmodel.PostIndex;
import com.ftn.socialNetwork.indexrepository.PostIndexRepository;
import com.ftn.socialNetwork.indexservice.interfaces.FileService;
import com.ftn.socialNetwork.indexservice.interfaces.GroupIndexingService;
import com.ftn.socialNetwork.indexservice.interfaces.PostIndexingService;
import com.ftn.socialNetwork.model.entity.Post;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostIndexingServiceImpl implements PostIndexingService {
    private final LanguageDetector languageDetector;
    private final FileService fileService;
    private final PostIndexRepository postIndexingRepository;


    @Override
    public PostIndex save(PostIndex postIndex) {
        return postIndexingRepository.save(postIndex);
    }

    @Override
    public void deleteById(String id) {
        postIndexingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void indexPost(MultipartFile documentFile, Post post) {
        var postIndex = new PostIndex();

        var pdfName = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        postIndex.setPdfFileUrl(pdfName);

        var documentContent = extractDocumentContent(documentFile);
        if (detectLanguage(documentContent).equals("SR")) {
            postIndex.setContentSr(documentContent);
        } else {
            postIndex.setContentEn(documentContent);
        }

        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());
        postIndex.setServerFilename(serverFilename);
        post.setPdfFile(serverFilename);

        postIndex.setId(post.getId().toString());
        postIndex.setTitle(post.getTitle());
        postIndex.setContent(post.getContent());
        postIndex.setCreationDate(post.getCreationDate());
        postIndex.setLikeCount(0);
        postIndex.setCommentCount(0);

        if (post.getGroup() != null) {
            var groupId = post.getGroup().getId().toString();
            postIndex.setGroupId(groupId);
        }

        postIndexingRepository.save(postIndex);
    }

    @Override
    @Transactional
    public void updatePostIndex(Post post) {
        var postIndex = postIndexingRepository.findById(post.getId().toString())
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndex.setTitle(post.getTitle());
        postIndex.setContent(post.getContent());
        postIndex.setCreationDate(post.getCreationDate());
        postIndex.setGroupId(post.getGroup().getId().toString());

        postIndexingRepository.save(postIndex);
    }

    @Override
    @Transactional
    public void deletePostIndex(Post post) {
        var postIndex = postIndexingRepository.findById(post.getId().toString())
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndexingRepository.delete(postIndex);
    }

    @Override
    @Transactional
    public void updateLikeCount(String postId) {
        var postIndex = postIndexingRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndex.setLikeCount(postIndex.getLikeCount() + 1);

        postIndexingRepository.save(postIndex);
    }

    @Override
    @Transactional
    public void deleteLikeCount(String postId) {
        var postIndex = postIndexingRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndex.setLikeCount(postIndex.getLikeCount() - 1);

        postIndexingRepository.save(postIndex);
    }

    @Override
    @Transactional
    public void updateCommentCount(String postId) {
        var postIndex = postIndexingRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndex.setCommentCount(postIndex.getCommentCount() + 1);

        postIndexingRepository.save(postIndex);
    }

    @Override
    @Transactional
    public void deleteCommentCount(String postId) {
        var postIndex = postIndexingRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post index not found"));

        postIndex.setCommentCount(postIndex.getCommentCount() - 1);

        postIndexingRepository.save(postIndex);
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
}
