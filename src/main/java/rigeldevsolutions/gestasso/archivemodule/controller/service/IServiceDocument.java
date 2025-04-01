package rigeldevsolutions.gestasso.archivemodule.controller.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UpdateDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;

import java.io.IOException;
import java.net.UnknownHostException;

public interface IServiceDocument
{
	void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException;
	byte[] downloadFile(String filePAth);
	ResponseEntity<Resource> downloadFile(Long docI);

    @Transactional
	boolean uploadDocument(UploadDocReq dto) throws UnknownHostException;

	boolean deleteDocument(Long docId) throws UnknownHostException;

	@Transactional
    boolean updateDocument(UpdateDocReq dto) throws IOException;

    void displayPdf(HttpServletResponse response, byte[] reportBytes, String displayName)  throws Exception;

    MultipartFile downloadMultipartFile(String filePAth);

    boolean deleteFile(String filePath);
	String generatePath(MultipartFile file, String typeCode, String objectName);
	void renameFile(String oldPath, String newPath);

	Page<ReadDocDTO> getAllDocsForObject(Long userId, Long assoId, Long sectionId, String key, Pageable pageable);
}
