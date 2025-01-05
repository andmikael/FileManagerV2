import { AlertTypeEnum } from "../shared/alert/alert.type.enum";


export interface ApiError<T = void> {
    code: number;
    message?: string;
    isError: boolean;
  }

  export interface ApiUser {
    role: string
  }

  export interface BlobsModel {
    blobs: String[];
    containerName: String;
  }

  interface ResError extends Error {
    status?: number;
}

export interface AlertInterface {
    type: AlertTypeEnum
    text: string
}