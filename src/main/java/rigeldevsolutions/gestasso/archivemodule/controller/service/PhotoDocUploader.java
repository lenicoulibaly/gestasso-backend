package rigeldevsolutions.gestasso.archivemodule.controller.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.archivemodule.controller.repositories.DocumentRepository;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.DocMapper;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.entities.Document;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;

@Component("photo") @Primary
public class PhotoDocUploader extends AbstractDocumentService
{
	public PhotoDocUploader(TypeRepo typeRepo, DocMapper docMapper, DocumentRepository docRepo) {
		super(typeRepo, docMapper, docRepo);
	}
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToPhotoDoc(dto);
	}
}