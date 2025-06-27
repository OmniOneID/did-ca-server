import { CasStatus } from '../constants/CasStatus';

export interface CasInfoResDto {
  id: number;
  did: string;
  name: string;
  status: CasStatus;
  serverUrl: string;
  certificateUrl: string;
  didDocument?: any;
  createdAt: string;
  updatedAt: string;
}
