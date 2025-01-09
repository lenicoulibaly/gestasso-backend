package rigeldevsolutions.gestasso.archivemodule.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component @RequiredArgsConstructor
public class ResourceLoaderService implements IResourceLoader
{

    private final ResourceLoader resourceLoader;
    @Override
    public InputStream getStaticImages(String path) throws IOException {
        String resourcePath = "classpath:"+path ;
        Resource resource = resourceLoader.getResource(resourcePath);
        return resource.getInputStream();
    }

    @Override
    public InputStream getLocalImages(String path) throws IOException
    {
        return new FileInputStream(new File(path));
    }
}
