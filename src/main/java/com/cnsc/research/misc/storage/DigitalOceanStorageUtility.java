package com.cnsc.research.misc.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;

@Component
public class DigitalOceanStorageUtility extends StorageUtility {
    public static final String PDF_CONTAINER = "pdf";
    public static final String DOCUMENT_CONTAINER = "documents";

    private String doSpaceKey = "LBUCUF7STEXJTQHU2WLH";

    private String doSpaceSecret = "EnX273/kca8eSs+B1rvKg7NKIRXWmLdy/OUNRMeqQx8";

    private String doSpaceEndpoint = "digitaloceanspaces.com";

    private String doSpaceRegion = "sgp1";

    private String doSpaceBucket = "research-repository";

    private AmazonS3 amazonS3;
    private String container;

    public DigitalOceanStorageUtility() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(doSpaceKey, doSpaceSecret);
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new EndpointConfiguration(doSpaceEndpoint, doSpaceRegion))
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    @Override
    public void upload(byte[] streamData, String fileName) throws IOException {
    }

    public void upload(MultipartFile file, String fileName) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getInputStream().available());
        if (file.getContentType() != null && !"".equals(file.getContentType())) {
            metadata.setContentType(file.getContentType());
        }
        amazonS3.putObject(new PutObjectRequest(doSpaceBucket + ".sgp1", container + fileName, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public void uploadOverwrite(byte[] streamData, String fileName) throws IOException {

    }

    @Override
    public void delete(String filename) {
        amazonS3.deleteObject(new DeleteObjectRequest(doSpaceBucket, container + filename));
    }

    @Override
    public DigitalOceanStorageUtility inContainer(String container) {
        this.container = container + "/";
        return this;
    }
}
