package rigeldevsolutions.gestasso.archivemodule.controller.service;

import rigeldevsolutions.gestasso.archivemodule.controller.repositories.DocumentRepository;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.DocMapper;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.entities.Document;
import rigeldevsolutions.gestasso.modulelog.controller.service.ILogService;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("photo") @Primary
public class PhotoDocUploader extends AbstractDocumentService
{
	public PhotoDocUploader(TypeRepo typeRepo, DocMapper docMapper, DocumentRepository docRepo, ILogService logService) {
		super(typeRepo, docMapper, docRepo, logService);
	}
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToPhotoDoc(dto);
	}
}