package com.seguim.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.seguim.aws.pojo.S3ObjectPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * User: adria
 * Date: 22/12/13
 * Time: 13:39
 */
public class Functions {

    private static AmazonS3 s3Client = null;

    private static AmazonS3 getClient() throws SAWSException {
        try {
            if(s3Client==null) {
                System.out.println("Creating new AmazonS3Client...");
                s3Client = new AmazonS3Client(new PropertiesCredentials(Functions.class.getResourceAsStream("/AwsCredentials.properties")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SAWSException("Error creating new AmazonS3Client: " + ex.getMessage());
        }
        return s3Client;
    }

    public static ObjectListing listObjects(String bucketName, String dirName, int max) throws SAWSException {

        //Fixing dirName if doesn't end with a slash
        if(dirName!=null && !dirName.endsWith("/")) {
            dirName += "/";
        }

        List<S3ObjectPojo> result = new ArrayList<S3ObjectPojo>();

        try {
            System.out.println("Downloading an object");
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName, dirName,"","/",max);
            ObjectListing objectListing = getClient().listObjects(listObjectsRequest);

            for(String folder : objectListing.getCommonPrefixes()) {
                S3ObjectPojo s3Object = new S3ObjectPojo();
                s3Object.setBucketName(bucketName);
                s3Object.setKey(folder);
                s3Object.setFolder(true);
                try {
                    ObjectMetadata objectMetadata = getClient().getObjectMetadata(bucketName, folder+"$dir$");
                    String realName = (String) objectMetadata.getRawMetadata().get("realname");
                    s3Object.setRealName(realName);
                } catch (AmazonServiceException ase) {
                }
                result.add(s3Object);
            }

            for(S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                if(mustListObject(objectSummary)) {
                    S3ObjectPojo s3Object = new S3ObjectPojo();
                    s3Object.setBucketName(bucketName);
                    s3Object.setKey(objectSummary.getKey());
                    s3Object.setFolder(false);
                    result.add(s3Object);
                }
            }

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            ase.printStackTrace();
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            ace.printStackTrace();
        }
        return null;
    }

    private static boolean mustListObject(S3ObjectSummary objectSummary) {
        // $dir$ is our directory placeholder
        // _$folder$ is the S3Fox placeholder
        String key = objectSummary.getKey();
        return !( (key.endsWith("/$dir$") || key.endsWith("_$folder$") || key.indexOf("placeholder.ns3")>-1 ) && objectSummary.getSize()==0);

    }

}
