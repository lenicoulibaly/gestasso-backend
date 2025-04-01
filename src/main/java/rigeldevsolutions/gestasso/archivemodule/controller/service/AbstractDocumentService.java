package rigeldevsolutions.gestasso.archivemodule.controller.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rigeldevsolutions.gestasso.archivemodule.controller.repositories.DocumentRepository;
import rigeldevsolutions.gestasso.archivemodule.model.constants.DocumentsConstants;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.DocMapper;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UpdateDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.response.ReadDocDTO;
import rigeldevsolutions.gestasso.archivemodule.model.entities.Document;
import rigeldevsolutions.gestasso.sharedmodule.exceptions.AppException;
import rigeldevsolutions.gestasso.sharedmodule.utilities.Base64ToFileConverter;
import rigeldevsolutions.gestasso.sharedmodule.utilities.StringUtils;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import rigeldevsolutions.gestasso.typemodule.model.entities.Type;
import rigeldevsolutions.gestasso.typemodule.model.enums.TypeGroup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Component @RequiredArgsConstructor
public abstract class AbstractDocumentService implements IServiceDocument
{
	protected final TypeRepo typeRepo;
	protected final DocMapper docMapper;
	protected final DocumentRepository docRepo;

	@Override
	public byte[] downloadFile(String filePAth)
	{
		File file = new File(filePAth);
		Path path = Paths.get(file.toURI());
		try
		{
			return Files.readAllBytes(path);
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new AppException("Erreur de téléchargement");
		}
	}

	@Override
	public ResponseEntity<Resource> downloadFile(Long docId) {
		Document doc = docRepo.findById(docId).orElse(null);
		String filename = doc.getDocType().getName();
		if(doc == null) return null;
		String docPath = doc.getDocPath();
		docPath = Optional.ofNullable(docPath).orElse("");
		//docPath = docPath.replace("\\", "\\\\");
		try {
			// Le chemin où se trouve le fichier
			Path filePath = Paths.get(docPath);
			//Path filePath = Paths.get("C:\\Users\\DGMP\\workspace\\gestasso\\docs\\uploads\\RECU-PAIE_Recu_de_reglementbbb4ba47-.pdf").normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}

			// Définir les headers pour le téléchargement
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM) // Type générique
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.body(resource);
		} catch (Exception ex) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	public MultipartFile downloadMultipartFile(String filePAth)
	{
		File file = new File(filePAth);
		return new CustomMultipartFile(file);
	}

	@Override
	public boolean deleteFile(String filePath)
	{
		File file = new File(filePath);
		return file == null ? false : file.delete();
	}

	protected abstract Document mapToDocument(UploadDocReq dto);


	@Override
	public String generatePath(MultipartFile file, String typeCode, String objectName)
	{
		if(!typeRepo.existsByUniqueCode(typeCode)) return "";
		String uuid = UUID.randomUUID().toString().substring(0, 9);

		return DocumentsConstants.UPLOADS_DIR +  File.separator  +typeCode + "_" +
				StringUtils.stripAccents(objectName).replace(" ", "_") + uuid + "." + FilenameUtils.getExtension(file.getOriginalFilename());
	}

	@Override
	public void renameFile(String oldPath, String newPath)
	{
		if(new File(oldPath).exists())
		{
			try {
				Files.move(Paths.get(oldPath), Paths.get(newPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException
	{
		try
		{
			Files.write(Paths.get(destinationPath), file.getBytes());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Transactional @Override
	public boolean uploadDocument(UploadDocReq dto)
	{
		if(dto.getDocUniqueCode() == null ) throw new AppException("Le type de document ne peut être null");
		MultipartFile file = dto.getFile();
		if(file == null) throw new AppException("Aucun fichier sélectionné");
		Type docType = typeRepo.findById(dto.getDocUniqueCode().toUpperCase(Locale.ROOT)).orElseThrow(()->new AppException("Type de document inconnu"));
		if(docType == null || docType.getTypeGroup() != TypeGroup.DOCUMENT)  throw new AppException("Ce type de document n'est pris en charge par le système");;
		Document doc = mapToDocument(dto);
		String path = generatePath(dto.getFile(), dto.getDocUniqueCode(), docType.getName());
		doc.setDocPath(path);
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		String mimeType = file.getContentType();
		doc.setDocExtension(extension);
		doc.setDocMimeType(mimeType);
		doc.setDocName(docType.getName());
		uploadFile(dto.getFile(), doc.getDocPath());
		doc = docRepo.save(doc);
		//logService.logg(ArchiveActions.UPLOAD_DOCUMENT, null, doc, ArchiveTable.DOCUMENT, );
		return true;//01 03 70 79 72
	}

	@Transactional @Override
	public boolean deleteDocument(Long docId) {
		Document doc = docRepo.findById(docId).orElseThrow(()->new AppException("Document inexistant"));
		docRepo.deleteById(docId);
		//logService.logg(ArchiveActions.DELETE_DOCUMENT, doc, new Document(), ArchiveTable.DOCUMENT);
		this.deleteFile(doc.getDocPath());
		return true;
	}

	@Transactional @Override
	public boolean updateDocument(UpdateDocReq dto) throws IOException {
		Document doc = docRepo.findById(dto.getDocId()).orElseThrow(()->new AppException("Document inexistant"));
		//if(dto.getFile() == null && dto.getBase64UrlFile() == null) throw new AppException("Veuillez charger le fichier!");
		MultipartFile file = dto.getFile() == null ? Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), "." + dto.getExtension()) : dto.getFile();
		String oldDocPath = doc.getDocPath();
		doc.setDocNum(Optional.ofNullable(dto.getDocNum()).orElse(doc.getDocNum()));
		doc.setDocDescription(Optional.ofNullable(dto.getDocDescription()).orElse(doc.getDocDescription()));
		doc.setDocName(Optional.ofNullable(dto.getDocName()).orElse(doc.getDocName()));
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		String mimeType = file.getContentType();
		doc.setDocExtension(extension);
		doc.setDocMimeType(mimeType);

		Type newType = dto.getDocUniqueCode() == null ? null : typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new AppException("Type de document inconnu"));

		if(dto.getDocUniqueCode() != null && !doc.getDocType().getUniqueCode().equals(dto.getDocUniqueCode())&& file != null)
		{
			doc.setDocType(newType);
			String path = generatePath(file, dto.getDocUniqueCode(), doc.getDocDescription());
			doc.setDocPath(path);
			uploadFile(file, path);
			this.deleteFile(oldDocPath);
		}
		else if(!areFilesIdentical(file, oldDocPath)&& file != null)
		{
			String path = generatePath(file, dto.getDocUniqueCode(), newType.getName());
			doc.setDocPath(path);
			uploadFile(file, path);
			this.deleteFile(oldDocPath);
		}

		//logService.logg(ArchiveActions.UPLOAD_DOCUMENT, doc, new Document(), ArchiveTable.DOCUMENT);
		return true;
	}

	@Override
	public void displayPdf(HttpServletResponse response, byte[] fileBytes, String displayName)  throws Exception
	{
		// Configurez l'en-tête de la réponse HTTP
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=" + displayName +".pdf");
		response.setContentLength(fileBytes.length);

		// Écrivez le rapport Jasper dans le flux de sortie de la réponse HTTP
		OutputStream outStream = response.getOutputStream();
		outStream.write(fileBytes);
		outStream.flush();
		outStream.close();
	}

	@Override
	public Page<ReadDocDTO> getAllDocsForObject(Long userId, Long assoId, Long sectionId, String key, Pageable pageable)
	{
		key = key == null || key.trim().equals("") ? "" : StringUtils.stripAccentsToUpperCase(key);
		return docRepo.getAllDocsForObject(userId, assoId, sectionId, key, pageable);
	}

	public boolean areFilesIdentical(MultipartFile file1, MultipartFile file2) throws Exception {
		// Vérifiez les métadonnées
		if (!file1.getOriginalFilename().equals(file2.getOriginalFilename())) {
			return false;
		}

		if (!file1.getContentType().equals(file2.getContentType())) {
			return false;
		}

		// Vérifiez les tailles
		if (file1.getSize() != file2.getSize()) {
			return false;
		}

		// Vérifiez le contenu ou le hash
		String hash1 = calculateFileHash(file1);
		String hash2 = calculateFileHash(file2);
		return hash1.equals(hash2);
	}

	public static boolean areFilesIdentical(MultipartFile multipartFile, String localFilePath) throws IOException {
		File localFile = new File(localFilePath);

		// Étape 1: Vérifiez les tailles
		if (multipartFile.getSize() != localFile.length()) {
			return false; // Les tailles sont différentes
		}

		// Étape 2: Comparez le contenu byte à byte
		byte[] multipartFileBytes = multipartFile.getBytes();
		byte[] localFileBytes = Files.readAllBytes(localFile.toPath());

		return Arrays.equals(multipartFileBytes, localFileBytes);
	}

	// Optionnel : Comparaison via hash (SHA-256)
	public  boolean areFilesIdenticalByHash(MultipartFile multipartFile, String localFilePath) throws Exception {
		String multipartFileHash = calculateFileHash(multipartFile);
		String localFileHash = calculateFileHash(Files.readAllBytes(new File(localFilePath).toPath()));

		return multipartFileHash.equals(localFileHash);
	}

	private String calculateFileHash(MultipartFile file) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		try (InputStream is = file.getInputStream()) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				digest.update(buffer, 0, bytesRead);
			}
		}
		StringBuilder hexString = new StringBuilder();
		for (byte b : digest.digest()) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private static String calculateFileHash(byte[] fileBytes) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(fileBytes);

		StringBuilder hexString = new StringBuilder();
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}