package rigeldevsolutions.gestasso.archivemodule.controller.service;

import org.springframework.stereotype.Component;
import rigeldevsolutions.gestasso.archivemodule.controller.repositories.DocumentRepository;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.DocMapper;
import rigeldevsolutions.gestasso.archivemodule.model.dtos.request.UploadDocReq;
import rigeldevsolutions.gestasso.archivemodule.model.entities.Document;
import rigeldevsolutions.gestasso.typemodule.controller.repositories.TypeRepo;

@Component("versement")
public class VersementsDocUploader extends AbstractDocumentService {
    public VersementsDocUploader(TypeRepo typeRepo, DocMapper docMapper, DocumentRepository docRepo) {
        super(typeRepo, docMapper, docRepo);
    }

    @Override
    protected Document mapToDocument(UploadDocReq dto) {
        return docMapper.mapToVersementDoc(dto);
    }
}
