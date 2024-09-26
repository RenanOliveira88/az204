package com.blob;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

public class blob {
    public static void main(String[] args) {
        System.out.println("Azure Blob Storage exercise\n");

        // Run the examples asynchronously, wait for the results before proceeding
        ProcessAsync().join(); // Wait for the async process to complete

        System.out.println("Press enter to exit the sample application.");
        new java.util.Scanner(System.in).nextLine(); // Wait for user input
    }

    private static CompletableFuture<Void> ProcessAsync() {
        //createContainer(); //Criar um container
        //azureBlobUpload(); //Subir um blob
        //listBlob(); //listar os blobs de um container
        downloadBlob();

        return CompletableFuture.completedFuture(null); // Replace with actual async code
    }

    private static void crateContainer(){
        // Copy the connection string from the portal in the variable below
        String storageConnectionString = "";

        // Create a client that can authenticate with a connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(storageConnectionString)
                .buildClient();

// Generate a unique container name
        String containerName = "wtblob" + UUID.randomUUID().toString();

        // Create the container and return a container client object
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        containerClient.create(); // Create the container

        System.out.println("A container named '" + containerName + "' has been created." +
                "\nTake a minute and verify in the portal." +
                "\nNext, a file will be created and uploaded to the container.");

        // Wait for user input to continue
        System.out.println("Press 'Enter' to continue.");
        
        new Scanner(System.in).nextLine();

    }

    private static void azureBlobUpload(){
        try {
            // Create local file path
            String localPath = "./data/";
            String fileName = "wtfile" + UUID.randomUUID().toString() + ".txt";
            String localFilePath = Paths.get(localPath, fileName).toString();

            // Create data directory if it does not exist
            Files.createDirectories(Paths.get(localPath));

            // Write text to the file
            Files.write(Paths.get(localFilePath), "Hello, World!".getBytes());

            // Create a BlobServiceClient
            String connectionString = ""; // replace with your connection string
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            // Get a container client
            String containerName = "az204-blob"; // replace with your container name
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Get a reference to the blob
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            System.out.println("Uploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());

            // Upload the file
            blobClient.uploadFromFile(localFilePath, true);
            System.out.println("\nThe file was uploaded. We'll verify by listing" +
                    " the blobs next.");
            System.out.println("Press 'Enter' to continue.");
            System.in.read(); // Wait for user input

            

        } catch (IOException e) {
            System.err.println("Error creating or uploading the file: " + e.getMessage());
        }
    }
    private static void listBlob(){
                try {
            // Create a BlobServiceClient
            String connectionString = ""; // replace with your connection string
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            // Get a container client
            String containerName = "az204-blob"; // replace with your container name
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Listing blobs
            System.out.println("Listing blobs...");
            for (BlobItem blobItem : containerClient.listBlobs()) {
                System.out.println("\t" + blobItem.getName());
            }

            System.out.println("\nYou can also verify by looking inside the " +
                    "container in the portal." +
                    "\nNext, the blob will be downloaded with an altered file name.");
            System.out.println("Press 'Enter' to continue.");
            System.in.read(); // Wait for user input

        } catch (Exception e) {
            System.err.println("Error listing blobs: " + e.getMessage());
        }
    }

    private static void downloadBlob(){
        try {
            // Create local file path
            String localPath = "./data/";
            String originalFileName = "wtfile9c8efacd-57a1-4f1f-b7d5-21938863ad0a.txt"; // replace with your original filename
            String downloadFilePath = Paths.get(localPath, originalFileName.replace(".txt", "DOWNLOADED.txt")).toString();

            // Create a BlobServiceClient
            String connectionString = ""; // replace with your connection string
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            // Get a container client
            String containerName = "az204-blob"; // replace with your container name
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Get a reference to the blob
            BlobClient blobClient = containerClient.getBlobClient(originalFileName);
            System.out.println("\nDownloading blob to\n\t" + downloadFilePath + "\n");

            blobClient.downloadToFile(downloadFilePath);

            System.out.println("\nLocate the local file in the data directory created earlier to verify it was downloaded.");
            System.out.println("The next step is to delete the container and local files.");
            System.out.println("Press 'Enter' to continue.");
            System.in.read(); // Wait for user input

        } catch (Exception e) {
            System.err.println("Error downloading the blob: " + e.getMessage());
        }
    }
    
}
