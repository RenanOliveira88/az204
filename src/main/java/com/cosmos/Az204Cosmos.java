/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.cosmos;


import java.util.concurrent.CompletableFuture;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.CosmosException;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
/**
 *
 * @author crena
 */
public class Az204Cosmos {


    private static CosmosClient cosmosClient;
    private static CosmosDatabase database;
    private static CosmosContainer container; 

    private String databaseId = "az204Database";
    private String containerId = "az204Container";

    public static void main(String[] args) {
        try{
            System.out.println("Beggining operation...");
            Az204Cosmos mainApp =  new Az204Cosmos();
            mainApp.CosmosAsync();
            
        }catch( CosmosException de){
            Throwable baseException = de.getCause();
            System.out.println(String.format("%s exception occurred: %s", de.getStatusCode(), de));
        }catch( Exception e){
            System.out.println("Error: " + e.getMessage());
        }finally{
            System.out.println("End of program press any key to exit");
            try{
                System.in.read();
            }catch(Exception e){
                System.out.println("Error reading key press");
            }
        }
        
    }
    public void CosmosAsync(){
        cosmosClient = new CosmosClientBuilder().endpoint(endpointUri)
                                                .key(primaryKey)
                                                .buildClient();
        try{
            createDatabaseAsync().join();

            createContainerAsync().join();

            System.out.println("Database e container criados com sucesso.");

        }catch (CosmosException de){
            System.out.println("Erro na criação da Database ou do container \n ERRO: " + de.getMessage());
        }
    }
    public CompletableFuture<Void> createDatabaseAsync(){
        CosmosDatabaseResponse databaseResponse = cosmosClient.createDatabaseIfNotExists(databaseId);
        database = cosmosClient.getDatabase(databaseResponse.getProperties().getId());

        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> createContainerAsync(){
        CosmosContainerResponse containerResponse = database.createContainerIfNotExists(containerId, "/partitionKey");
        container = database.getContainer(containerResponse.getProperties().getId());
        return CompletableFuture.completedFuture(null);    
    }
}
