package com.example.TanKhoaLearningCenterBE.service;

import com.example.TanKhoaLearningCenterBE.dto.ParentDTO;
import com.example.TanKhoaLearningCenterBE.web.rest.request.CreateParentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.request.UpdateParentRequest;
import com.example.TanKhoaLearningCenterBE.web.rest.response.FileUploadResponse;
import com.example.TanKhoaLearningCenterBE.web.rest.response.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ParentService {
    ResponseEntity<ParentDTO> create(CreateParentRequest request);

    ResponseEntity<ParentDTO> put(UUID id, UpdateParentRequest request);

    ResponseEntity<PageResponse<ParentDTO>> getAll(Integer page, Integer size);

    ResponseEntity<ParentDTO> get(UUID id);

    ResponseEntity<?> delete(UUID id);

    ResponseEntity<List<ParentDTO>> search(String name);

    ResponseEntity<FileUploadResponse> uploadParentsFromExcel(MultipartFile file);

    List<CreateParentRequest> processExcelFile(MultipartFile file) throws IOException;

    void saveParents(List<CreateParentRequest> parentRequests);
}
