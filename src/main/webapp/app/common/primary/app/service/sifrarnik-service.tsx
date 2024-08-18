import axios from 'axios';
import { an } from 'vitest/dist/types-2b1c412e';
import { TableDefinitionModel } from '@/common/primary/app/components/newsifrarnik/table-definition-model';

const baseUrl = 'http://localhost:8080/api'

export const fetchTypeOptions = (): Promise<any> => {
  return new Promise((resolve, reject) => {
    axios.get(`${baseUrl}/options/db-types`).then(res => resolve(res.data))
  })
}

export const fetchTables = (): Promise<TableDefinitionModel[]> => {
  return new Promise((resolve, reject) => {
    axios.get(`${baseUrl}/database/tables`).then(res => resolve(res.data))
  })
}
