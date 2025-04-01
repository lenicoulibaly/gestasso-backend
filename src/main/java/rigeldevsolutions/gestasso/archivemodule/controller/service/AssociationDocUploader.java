package rigeldevsolutions.gestasso.archivemodule.controller.service;

import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.archivemodule.controller.repositories.DocumentRepository;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.DocMapper;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.entities.Document;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;

@Component(value = "association")
public class AssociationDocUploader extends AbstractDocumentService
{
	public AssociationDocUploader(TypeRepo typeRepo, DocMapper docMapper, DocumentRepository docRepo) {
		super(typeRepo, docMapper, docRepo);
	}
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToAssociationDoc(dto);
	}
}