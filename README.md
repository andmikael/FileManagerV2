# FileManagerV2

A webapp to manage Microsoft Azure Blob Storage containers and blobs

Allows you to create, delete and select existing Blob containers in the storage account.
Inside a Blob container you can upload and download files from/to your device

This will be an ongoing project with the intent of integrading Angular v19 and replacing thymeleaf with it.

The reason for this is because the original FileManager only supports SSR (Server-Side-Rendering) and the project 
could really use responsive functionalities to make it more usable.

The idea is to keep the project mostly SSR, but adding CSR where full page reloads wouldn't make sense.
