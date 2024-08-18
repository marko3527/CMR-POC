import {
  ColumnDefinitionModel,
  NewSifrarnikColumnType,
} from '@/common/primary/app/components/newsifrarnik/table-definition-model';
import React from 'react';
import { Form, InputGroup } from 'react-bootstrap';
import { DropdownModel } from '@/common/primary/app/components/valuelist/value-list.model';
import Select from 'react-select';

export const renderInput = (columnDefinition: ColumnDefinitionModel, value: any, index: number, changeFunction: Function) => {
  switch (columnDefinition.columnType) {
    case NewSifrarnikColumnType.STRING:
      return stringInput(value, columnDefinition.columnName, index, changeFunction);
    case NewSifrarnikColumnType.NUMBER:
      if (columnDefinition.foreignKey) {
        return dropdownInput(value, columnDefinition, index, changeFunction);
      } else {
        return stringInput(value, columnDefinition.columnName, index, changeFunction);
      }
    case NewSifrarnikColumnType.BOOLEAN:
      return checkInput(value, columnDefinition.columnName, index, changeFunction);
  }
}

export const stringInput = (value: any, columnName: string, index: number, changeFunction: Function) => {
  return (
    <>
      <InputGroup className="mb-3">
        <Form.Control value={value} onChange={e => changeFunction(columnName, index, e.target.value)}/>
      </InputGroup>
    </>
  )
}

export const checkInput = (value: any, columnName: string, index: number, changeFunction: Function) => {
  return (
    <>
      <InputGroup className="mb-3">
        <Form.Check checked={value} onChange={e => changeFunction(columnName, index, e.target.checked)}/>
      </InputGroup>
    </>
  )
}

export const dropdownInput = (value: any, columnDefinition: ColumnDefinitionModel, index: number, changeFunction: Function) => {

  return (
    <>
      <Select value={value}
              onChange={(option: DropdownModel) => changeFunction(columnDefinition.columnName, index, option)}
              options={columnDefinition.foreignKeyOptions}
              getOptionLabel={(option: DropdownModel) => option.value}
              placeholder={"Odaberite vrijednost..."}
      />
    </>
  )
}
