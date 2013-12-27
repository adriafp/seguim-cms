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
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBAsyncClient;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.*;
import com.amazonaws.util.StringUtils;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.seguim.aws.pojo.S3ObjectPojo;

import java.util.*;

/**
 * User: adria
 * Date: 22/12/13
 * Time: 13:39
 */
public class Functions {

    private static AmazonS3 s3Client = null;

    private static AmazonS3 getS3Client() throws SAWSException {
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

    private static AmazonSimpleDB simpleDBClient = null;
    private static AmazonSimpleDB simpleDBAsyncClient = null;

    private static AmazonSimpleDB getSimpleDBAsyncClient() throws SAWSException {
        try {
            if(simpleDBAsyncClient==null) {
                System.out.println("Creating new AmazonSimpleDBAsyncClient...");
                simpleDBAsyncClient = new AmazonSimpleDBAsyncClient(new PropertiesCredentials(Functions.class.getResourceAsStream("/AwsCredentials.properties")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SAWSException("Error creating new AmazonS3Client: " + ex.getMessage());
        }
        return simpleDBAsyncClient;
    }

    private static AmazonSimpleDB getSimpleDBClient() throws SAWSException {
        try {
            if(simpleDBClient==null) {
                System.out.println("Creating new AmazonSimpleDBClient...");
                simpleDBClient = new AmazonSimpleDBClient(new PropertiesCredentials(Functions.class.getResourceAsStream("/AwsCredentials.properties")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SAWSException("Error creating new AmazonS3Client: " + ex.getMessage());
        }
        return simpleDBClient;
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
            ObjectListing objectListing = getS3Client().listObjects(listObjectsRequest);

            for(String folder : objectListing.getCommonPrefixes()) {
                S3ObjectPojo s3Object = new S3ObjectPojo();
                s3Object.setBucketName(bucketName);
                s3Object.setKey(folder);
                s3Object.setFolder(true);
                try {
                    ObjectMetadata objectMetadata = getS3Client().getObjectMetadata(bucketName, folder + "$dir$");
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

    public static void _saveOrUpdate(String domain, String itemName, Map map) {
        try {
            validateDomain(domain);

            PutAttributesRequest putAttributesRequest = new PutAttributesRequest();
            putAttributesRequest.setDomainName(domain);
            putAttributesRequest.setItemName(itemName);

            Collection<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>();
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                attributes.add(new ReplaceableAttribute(key, map.get(key).toString(), true));
            }

            putAttributesRequest.setAttributes(attributes);

            getSimpleDBClient().putAttributes(putAttributesRequest);
        } catch (SAWSException sawse) {
            sawse.printStackTrace();
        }
    }

    public static Map _get(String domain, String itemName) {
        Map<String,String> map = null;
        try {
            validateDomain(domain);
            GetAttributesRequest getAttributesRequest = new GetAttributesRequest();
            getAttributesRequest.setDomainName(domain);
            getAttributesRequest.setItemName(itemName);
            GetAttributesResult attributesResult = getSimpleDBClient().getAttributes(getAttributesRequest);
            if(attributesResult!=null) {
                map = new LinkedHashMap<String,String>();
                for(Attribute attribute : attributesResult.getAttributes()) {
                    map.put(attribute.getName(),attribute.getValue());
                }
            }
        } catch (SAWSException sawse) {
            sawse.printStackTrace();
        }
        return map;
    }

    public static void _delete(String domain, String itemName) {
        Map<String,String> map = null;
        try {
            validateDomain(domain);
            DeleteAttributesRequest deleteAttributesRequest = new DeleteAttributesRequest(domain,itemName);
            getSimpleDBClient().deleteAttributes(deleteAttributesRequest);
        } catch (SAWSException sawse) {
            sawse.printStackTrace();
        }
    }

    private static void validateDomain(String domainName) throws SAWSException {
        ListDomainsResult domainsResult = getSimpleDBClient().listDomains();
        if(!domainsResult.getDomainNames().contains(domainName)) {
            getSimpleDBClient().createDomain(new CreateDomainRequest(domainName));
        }
    }

    public static Map<String,String> _new() {
        return new LinkedHashMap<String, String>();
    }

    public static Map<String,String> _new(String json) {
        Map<String,String> _new = new LinkedHashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = (String) keys.next();
                _new.put(key, (String) jsonObject.get(key));
            }
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
        return _new;
    }

    public static Map<String,Map> _search(String domain, String where, String sort, int limit) {
        LinkedHashMap<String, Map> result = null;
        try {
            validateDomain(domain);
            String select = "select * from " + domain;
            if(where!=null && !"".equals(where))
                select += " where " + where;
            if(sort!=null && !"".equals(sort))
                select += " order by " + sort;
            if(limit>0)
                select += " limit " + limit;

            SelectResult selectResult = getSimpleDBClient().select(new SelectRequest(select));
            if(selectResult!=null) {
                result = new LinkedHashMap<String,Map>();
                for(Item item : selectResult.getItems()) {
                    Map<String, String> attributes = new LinkedHashMap<String,String>();
                    for(Attribute attribute : item.getAttributes()) {
                        attributes.put(attribute.getName(), attribute.getValue());
                    }
                    result.put(item.getName(),attributes);
                }
            }
        } catch (SAWSException sawse) {
            sawse.printStackTrace();
        }
        return result;
    }
}
