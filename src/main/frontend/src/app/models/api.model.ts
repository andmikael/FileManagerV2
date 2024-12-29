export interface ApiError<T = void> {
    code: string;
    message?: string;
    isError: boolean;
    data?: T;
  }

  export interface ApiUser {
    accountType: string
  }

  export interface BlobsModel {
    blobs: String[];
    containerName: String;
  }