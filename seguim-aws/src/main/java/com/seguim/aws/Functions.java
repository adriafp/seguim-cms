package com.seguim.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

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

    public static List<String> getListFolders(String bucketName, String dirName)  throws SAWSException {
        if(dirName!=null && !dirName.endsWith("/")) {
            dirName += "/";
        }
        try {
            System.out.println("Downloading an object");
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName, dirName,"","/",10000);
            ObjectListing objectListing = getClient().listObjects(listObjectsRequest);
            return objectListing.getCommonPrefixes();
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

    public static List<String> getListFiles(String bucketName, String dirName)  throws SAWSException {
        if(dirName!=null && !dirName.endsWith("/")) {
            dirName += "/";
        }
        List<String> files = new ArrayList<String>();
        try {
            System.out.println("Downloading an object");
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName, dirName,"","/",10000);
            ObjectListing objectListing = getClient().listObjects(listObjectsRequest);
            for(S3ObjectSummary s3ObjectSummary : objectListing.getObjectSummaries()) {
                files.add(s3ObjectSummary.getKey());
            }
            return files;
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

}
