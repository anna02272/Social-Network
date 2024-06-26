package com.ftn.socialNetwork.indexcontroller;

import com.ftn.socialNetwork.dto.DocumentFileDTO;
import com.ftn.socialNetwork.dto.DocumentFileResponseDTO;
import com.ftn.socialNetwork.indexservice.interfaces.GroupIndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group_index")
@RequiredArgsConstructor
public class GroupIndexController {

    private final GroupIndexingService indexingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentFileResponseDTO addDocumentFile(
        @ModelAttribute DocumentFileDTO documentFile) {
        var serverFilename = indexingService.indexDocument(documentFile.file());
        return new DocumentFileResponseDTO(serverFilename);
    }
}
