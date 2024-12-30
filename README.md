# FileManagerV2

[preview version hosted in Azure](https://gentle-plant-08ad8f103.4.azurestaticapps.net)

A webapp to manage Microsoft Azure Blob Storage containers and blobs

> What does it do?

Filemanager allows you to create, delete and use existing Blob containers in a storage account.
Inside a Blob container you can upload and download files from/to your device.

>[!NOTE]
>This is a more polished version of my [Java only project](https://github.com/andmikael/FileManager) with the same name that is also being hosted in Azure.

# Running the project

This project has a few prerequisites

1. You have an existing Azure Blob Storage account
2. You've set your storage accounts endpoint to an env variable
   - Trial account also requires a connectionstring as an env variable

After this run the the following commands in separate terminals

In the project root:
```
mvn clean package
java -jar ./target/filemanagerapi.jar
```
In the root of the src/main/frontend folder
```
ng serve
```
