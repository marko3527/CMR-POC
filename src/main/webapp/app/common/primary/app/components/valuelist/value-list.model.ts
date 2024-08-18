import {
  ColumnDefinitionModel,
  NewSifrarnikColumnType,
} from '@/common/primary/app/components/newsifrarnik/table-definition-model';

export interface RowModel {
  columns: ColumnModel[]
  rowChanged: boolean;
}

export interface ColumnModel {
  columnDefinition: ColumnDefinitionModel
  value: any
  columnChanged: boolean;
}

export interface DropdownModel {
  id: number;
  value: any;
}
