import { DropdownModel } from '@/common/primary/app/components/valuelist/value-list.model';

export interface TableDefinitionModel {
  tableName: string;
  columns: ColumnDefinitionModel[]
}

export interface ColumnDefinitionModel {
  columnName: string;
  columnType: string
  mandatory: boolean;
  unique: boolean;
  defaultValue?: any
  foreignKey?: boolean
  referencedTableName?: string
  fkDisplayValueColumn?: string
  multiselectColumn?: boolean
  foreignKeyOptions?: DropdownModel[]
  identity: boolean
}

export enum NewSifrarnikColumnType {
  BOOLEAN = "BOOLEAN",
  STRING = "STRING",
  DATUM = "DATUM",
  NUMBER = "NUMBER"
}
